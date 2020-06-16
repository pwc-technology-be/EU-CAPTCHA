package com.sii.EuCaptcha.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.springframework.stereotype.Component;
import java.security.Key;

@Component
public class JwtToken {

    /**
     *
     * @return jwtToken
     */
    public String generateJwtToken(Key key) {
        String jwtString = Jwts.builder().setSubject("euCaptcha").signWith(SignatureAlgorithm.HS512, key).compact();
        return jwtString;
    }

    /**
     *
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
