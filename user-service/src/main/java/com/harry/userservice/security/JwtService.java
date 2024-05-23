package com.harry.userservice.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cglib.core.internal.Function;
import org.springframework.stereotype.Component;

import com.harry.userservice.entity.UserX;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtService {

    // @Value("${application.security.jwt.secret-key}")
    // private String secretKey = "jwtwebtoken";
    private String secretKey = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437"; // 64 digits

    // @Value("${application.security.jwt.expiration}")
    private long expiration = 864800000; // 30 days

    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_SCOPE = "scope";

    // @PostConstruct
    // protected void init() {
    //     secretKey = Encoders.BASE64.encode(secretKey.getBytes());
    // }
    public String generateToken(UserX user, AuthScope scope) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(KEY_SCOPE, scope.value);
        claims.put(KEY_NAME, user.getName());
        claims.put(KEY_EMAIL, user.getEmail());
        return createToken(claims, String.valueOf(user.getUserId()));
    }

    private String createToken(Map<String, Object> claims, String userId) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    // ---------------------Validating token here---------------

    public Boolean validateTokenId(String token, UserX user) {
        if (isTokenExpired(token)) {
            return false;
        }
        String userId = String.valueOf(user.getUserId());
        return isUserIdMatched(token, userId) && !isTokenExpired(token);
    }

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Boolean isUserIdMatched(String token, String userId) {
        String tokenUserId = extractUserId(token);
        return tokenUserId.equals(userId);
    }
    // ----------------Resolving token here-----------------

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        return resolveToken(bearerToken);
    }

    public String resolveToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean resolveAndValidate(String bearerToken, AuthScope scopeToVerify) {
        String token = resolveToken(bearerToken);

        if (token == null || token.isEmpty()) {
            return false;
        }

        return validateToken(token, scopeToVerify);
    }

    public boolean validateToken(String token, AuthScope scopeToVeify) {
        try {
            Claims claims = extractAllClaims(token);
            String claimScope = claims.get(KEY_SCOPE).toString();

            if (scopeToVeify.value.equals(claimScope)) {
                return true;
            }
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
