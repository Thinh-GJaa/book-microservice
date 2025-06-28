package com.book.orderservice.config;

import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    private static final Logger log = LoggerFactory.getLogger(CustomJwtDecoder.class);

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            log.info("[SECURITY][JWT] Decoding JWT token");
            SignedJWT signedJWT = SignedJWT.parse(token);
            log.info("[SECURITY][JWT] JWT claims: [{}]", signedJWT.getJWTClaimsSet().getClaims());
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