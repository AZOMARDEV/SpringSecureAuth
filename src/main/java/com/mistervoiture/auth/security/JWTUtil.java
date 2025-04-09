package com.mistervoiture.auth.security;

import com.mistervoiture.auth.entity.Auth;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTUtil {

    @Value("${jwt.secret.accessToken}")
    private String secretKeyAccessToken;

    @Value("${jwt.secret.refreshToken}")
    private String secretKeyRefreshToken;


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractUsernameRefreshToken(String token) {
        return extractClaimRefreshToken(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public <T> T extractClaimRefreshToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaimsRefreshToken(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(Auth userDetails) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());
        claims.put("bearer" , userDetails.getBearer());
        claims.put("nickname", userDetails.getSecretNickname());
        claims.put("nicknameCode", userDetails.getCodeNickname());

        return generateToken(claims, userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return buildToken(extraClaims, userDetails);
    }

    public String generateRefreshToken(
            UserDetails userDetails
    ) {
        return buildRefreshToken(new HashMap<>(), userDetails);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(
                        Date.from(
                                Instant.now().plus(5, ChronoUnit.DAYS)
                        )
                )
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private String buildRefreshToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(
                        Date.from(
                                Instant.now().plus(20, ChronoUnit.SECONDS)
                        )
                )
                .signWith(getSignInKeyRefreshToken(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenValidRefreshToken(String token, UserDetails userDetails) {
        final String username = extractUsernameRefreshToken(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpiredRefreshToken(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private boolean isTokenExpiredRefreshToken(String token) {
        return extractExpirationRefreshToken(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Date extractExpirationRefreshToken(String token) {
        return extractClaimRefreshToken(token, Claims::getExpiration);
    }


    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Claims extractAllClaimsRefreshToken(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKeyRefreshToken())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKeyAccessToken);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Key getSignInKeyRefreshToken() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKeyRefreshToken);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}