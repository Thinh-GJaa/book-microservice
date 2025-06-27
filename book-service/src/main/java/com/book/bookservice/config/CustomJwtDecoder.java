package com.book.bookservice.config;

import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;

@Component
@Slf4j
public class CustomJwtDecoder implements JwtDecoder {
    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            // Ensure roles are included in the claims
            return new Jwt(token,
                    signedJWT.getJWTClaimsSet().getIssueTime().toInstant(),
                    signedJWT.getJWTClaimsSet().getExpirationTime().toInstant(),
                    signedJWT.getHeader().toJSONObject(),
                    signedJWT.getJWTClaimsSet().getClaims());

        } catch (ParseException e) {
            log.warn("[SECURITY][JWT] Invalid token parse attempt. Error: {} | Token: {}", e.getMessage(),
                    token != null && token.length() > 20 ? token.substring(0, 20) + "..." : token);
            throw new JwtException("Invalid token");
        }
    }
}