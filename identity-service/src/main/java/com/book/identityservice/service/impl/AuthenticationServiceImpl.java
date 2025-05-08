package com.book.identityservice.service.impl;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import com.book.identityservice.dto.request.LoginRequest;
import com.book.identityservice.dto.request.IntrospectRequest;
import com.book.identityservice.dto.response.LoginResponse;
import com.book.identityservice.dto.response.IntrospectResponse;
import com.book.identityservice.dto.response.RefreshResponse;
import com.book.identityservice.entity.InvalidatedToken;
import com.book.identityservice.entity.User;
import com.book.identityservice.exception.CustomException;
import com.book.identityservice.exception.ErrorCode;
import com.book.identityservice.repository.InvalidatedTokenRepository;
import com.book.identityservice.repository.UserRepository;

import com.book.identityservice.service.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {

    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    PasswordEncoder passwordEncoder; // Inject PasswordEncoder

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    @NonFinal
    @Value("${jwt.issuer}")
    protected String ISSUER;

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) {
        var token = request.getAccessToken();
        boolean isValid = true;
        try {
            verifyToken(token, false);
        } catch (CustomException | JOSEException | ParseException e) {
            isValid = false;
            log.error("Xác thực token thất bại: {}", e.getMessage(), e);
        }

        return IntrospectResponse.builder().valid(isValid).build();
    }

    @Override
    public LoginResponse login(LoginRequest request, HttpServletResponse response) {
        // Kiểm tra người dùng và mật khẩu
        User user = userRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USERNAME_NOT_FOUND, request.getUsername()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Invalid password for user: {}", request.getUsername());
            throw new CustomException(ErrorCode.PASSWORD_INCORRECT);
        }

        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);

        setRefreshTokenInHttpOnlyCookie(refreshToken, response);

        return LoginResponse.builder().accessToken(accessToken).build();
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) throws ParseException, JOSEException {
        // Lấy refresh token từ cookie
        String refreshToken = getRefreshTokenFromCookie(request);

        if (refreshToken == null || refreshToken.isEmpty()) {
            log.warn("Refresh token is missing from cookie during logout");
            clearRefreshTokenFromCookie(response); // Xóa cookie nếu có
            return; // Không ném lỗi, chỉ đơn giản là không làm gì thêm
        }

        try {
            SignedJWT signedJWT = verifyToken(refreshToken, true);

            String jit = signedJWT.getJWTClaimsSet().getJWTID();
            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            invalidateToken(jit, expiryTime);

            log.info("Logout successful, token invalidated: {}", jit);
        } catch (CustomException e) {
            log.warn("Logout: Token đã hết hạn hoặc không hợp lệ: {}", e.getMessage());
            throw e; // Ném lại lỗi để client biết lý do logout thất bại
        } finally {
            clearRefreshTokenFromCookie(response); // Luôn xóa cookie ở client
        }
    }

    @Override
    public RefreshResponse refreshToken(HttpServletRequest request, HttpServletResponse response)
            throws ParseException, JOSEException {
        String refreshToken = getRefreshTokenFromCookie(request);

        if (refreshToken == null || refreshToken.isEmpty()) {
            log.warn("Refresh token is missing from cookie");
            throw new CustomException(ErrorCode.COOKIE_NOT_FOUND, "refresh_token");
        }

        try {
            // Xác minh Refresh Token
            SignedJWT signedJWT = verifyToken(refreshToken, true);
            String tokenId = signedJWT.getJWTClaimsSet().getJWTID();
            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            invalidateToken(tokenId, expiryTime);

            String username = signedJWT.getJWTClaimsSet().getSubject();
            User user = userRepository.findById(username)
                    .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHENTICATED));

            String newAccessToken = generateAccessToken(user);
            String newRefreshToken = generateRefreshToken(user);

            setRefreshTokenInHttpOnlyCookie(newRefreshToken, response);

            log.info("Refresh token successful for user: {}", username);
            return RefreshResponse.builder().accessToken(newAccessToken).build();
        } catch (CustomException e) {
            log.error("Failed to refresh token: {}", e.getMessage(), e);
            throw e;
        }
    }

    private String getRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null)
            return null;

        return Arrays.stream(request.getCookies())
                .filter(cookie -> "refresh_token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    private void setRefreshTokenInHttpOnlyCookie(String refreshToken, HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge((int) REFRESHABLE_DURATION);

        response.addCookie(refreshTokenCookie);
    }

    // Phương thức xóa Refresh Token khỏi cookie
    private void clearRefreshTokenFromCookie(HttpServletResponse response) {
        // Tạo một cookie mới với tên "refresh_token" nhưng không có giá trị và đặt thời
        // gian sống bằng 0
        Cookie refreshTokenCookie = new Cookie("refresh_token", null);
        refreshTokenCookie.setHttpOnly(true); // Đảm bảo cookie không thể truy cập qua JavaScript
        refreshTokenCookie.setSecure(true); // Chỉ gửi cookie qua HTTPS
        refreshTokenCookie.setPath("/"); // Cookie có thể được truy cập từ tất cả các endpoint
        refreshTokenCookie.setMaxAge(0); // Đặt thời gian sống cookie bằng 0 để xóa nó

        // Thêm cookie vào response để xóa nó ở client
        response.addCookie(refreshTokenCookie);
        log.info("Đã xóa refresh token khỏi cookie");
    }

    private void invalidateToken(String tokenId, Date expiryTime) {
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(tokenId)
                .expiryTime(expiryTime)
                .build();
        invalidatedTokenRepository.save(invalidatedToken);
        log.info("Token invalidated: {}", tokenId);
    }

    private String generateAccessToken(User user) {
        try {
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
            String tokenId = UUID.randomUUID().toString();
            Date expiryTime = new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli());

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getUserId())
                    .issuer(ISSUER)
                    .issueTime(new Date())
                    .expirationTime(expiryTime)
                    .jwtID(tokenId)
                    .build();

            JWSObject jwsObject = new JWSObject(header, new Payload(claimsSet.toJSONObject()));
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));

            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Failed to generate token: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.JWT_SIGN_ERROR, "Token generation failed");
        }
    }

    private String generateRefreshToken(User user) {
        try {
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
            String tokenId = UUID.randomUUID().toString();
            Date expiryTime = new Date(Instant.now().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli());

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getUserId())
                    .issuer(ISSUER)
                    .issueTime(new Date())
                    .expirationTime(expiryTime)
                    .jwtID(tokenId)
                    .build();

            JWSObject jwsObject = new JWSObject(header, new Payload(claimsSet.toJSONObject()));
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));

            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Failed to generate refresh token: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.JWT_SIGN_ERROR, "Refresh token generation failed");
        }
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            if (!signedJWT.verify(verifier)) {
                log.warn("Token signature invalid");
                throw new CustomException(ErrorCode.TOKEN_SIGNATURE_INVALID);
            }

            if (expiryTime.before(new Date())) {
                log.warn("Token expired");
                throw new CustomException(ErrorCode.TOKEN_EXPIRED);
            }

            if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
                log.warn("Token has been invalidated: {}", signedJWT.getJWTClaimsSet().getJWTID());
                throw new CustomException(ErrorCode.TOKEN_ALREADY_INVALIDATED);
            }

            return signedJWT;
        } catch (ParseException e) {
            log.error("JWT parse error: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.JWT_PARSE_ERROR);
        } catch (JOSEException e) {
            log.error("JWT verify error: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.JWT_VERIFY_ERROR);
        }
    }
}