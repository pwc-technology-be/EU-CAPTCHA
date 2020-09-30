package com.sii.eucaptcha.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.springframework.stereotype.Component;
import java.security.Key;

/**
 * @author mousab.aidoud
 * @version 1.0
 * Jwt token
 */
@Component
public class JwtToken {

    /**
     * Generating Token
     * @param key
     * @return jwtToken
     */
    public String generateJwtToken(Key key) {
        String jwtString = Jwts.builder().setSubject("euCaptcha").signWith(SignatureAlgorithm.HS512, key).compact();
        return jwtString;
    }

    /**
     * Verify token
     * @param jwtToken
     * @return true|false
     */
    public Boolean verifyToken(String jwtToken , Key key) throws SignatureException {
        if (Jwts.parser().setSigningKey(key).parseClaimsJws(jwtToken).getBody().getSubject().equals("euCaptcha")) {
            return true;
        } else {
            return false;
        }
    }
}
