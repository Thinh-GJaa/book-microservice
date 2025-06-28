package com.book.inventory.config;

import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;

@Slf4j
@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            log.info("[SECURITY][JWT] Decoding JWT token");
            SignedJWT signedJWT = SignedJWT.parse(token);
            log.info("[SECURITY][JWT] JWT claims: [{}]", signedJWT.getJWTClaimsSet().getClaims());
            // Ensure roles are included in the claims
            return new Jwt(token,
                    signedJWT.getJWTClaimsSet().getIssueTime().toInstant(),
                    signedJWT.getJWTClaimsSet().getExpirationTime().toInstant(),
                    signedJWT.getHeader().toJSONObject(),
                    signedJWT.getJWTClaimsSet().getClaims());

        } catch (ParseException e) {
            log.error("[SECURITY][JWT][ERROR] Invalid JWT token: [{}]", e.getMessage());
            throw new JwtException("Invalid token");
        }
    }
}