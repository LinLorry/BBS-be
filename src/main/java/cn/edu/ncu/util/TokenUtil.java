package cn.edu.ncu.util;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Token Until
 * use JWT generate token and get authentication from token
 * @author lorry
 * @author lin864464995@163.com
 * @see io.jsonwebtoken.Claims
 * @see io.jsonwebtoken.Jwts
 * @see org.springframework.security.core.Authentication
 */
@Component
public class TokenUtil {
    private static final long TOKEN_VALIDITY = 5 * 60 * 60;

    @Value("${bbs.secret.token}")
    private String secret;

    @Value("${bbs.authentication.passwordField}")
    private String passwordField;

    /**
     * Generate Token by user
     * @param userDetails the user.
     * @return token string.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(passwordField, userDetails.getPassword());
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * Get Authentication From Token
     * @param token the token string.
     * @return the user Authentication get from token but haven't been authenticated.
     * @throws ExpiredJwtException if token expired.
     * @throws SignatureException if token invalid.
     */
    public Authentication getAuthenticationFromToken(String token)
            throws ExpiredJwtException, SignatureException {
        Claims claims = getAllClaimsFromToken(token);

        String username = claims.getSubject();
        String password = (String) claims.get(passwordField);

        return new UsernamePasswordAuthenticationToken(
                username, password
        );
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
}