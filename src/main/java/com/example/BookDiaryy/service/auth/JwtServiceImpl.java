package com.example.BookDiaryy.service.auth;

import com.example.BookDiaryy.model.entity.User;
import com.example.BookDiaryy.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService{

    private final UserRepository userRepository;

    String secret = "0ea38897925118e4ab1d18f5a13b6cd50094b326df9655222a9cb7dcb2c1e9ff";

    private final SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

    public JwtServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String generateToken(UserDetails user) {
        Map<String, Object> claims = new HashMap<>();
        Optional<User> currentUser = userRepository.findByEmail(user.getUsername());
        if (currentUser.isPresent()){
            claims.put("id", currentUser.get().getId());
            claims.put("role",currentUser.get().getRole());
        }

        return /*"Bearer-" +*/ generateTokenValue(claims, user.getUsername());
    }

    @Override
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    @Override
    public String generateTokenFromUsername(String username) {
        return /*"Bearer-" +*/ generateTokenValue(new HashMap<>(), username);
    }

    private String generateTokenValue(Map<String, Object> claims, String username){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Date extractExpiration(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private <E> E getClaimFromToken(String token, Function<Claims, E> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
}
