package DomainLayer.Market.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;

@Service
public class JwtService {

    private final String SECRET_KEY = "b29b4484ec5f0074943c9d687d6db1d4bed7bee4eb90512209ecbd110bf08941";

    public String generateToken(String username, String role) {
        return Jwts
                .builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .claim("role", role)
                .expiration(new Date(System.currentTimeMillis() + 24*60*60*1000 ))
                .signWith(getSignInKey())
                .compact();

    }

    private SecretKey getSignInKey(){
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver){
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token){
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public boolean isValid(String token, UserDetails user){
        String username = extractUsername(token);
        return (username.equals(user.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
