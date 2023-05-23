//package DKUDCoding20231Team3.VISTA.FilesUsedBefore;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import java.security.Key;
//import java.util.*;
//import java.util.stream.Collectors;
//
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class JwtProvider {
//    private final Key secretKey;
//
//    @Autowired
//    public JwtProvider(@Value("${spring.jwt.secret}") String secretKey) {
//        byte[] keyBytes = Decoders.BASE64.decode((secretKey));
//        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
//    }
//    public Jwt generateToken(Authentication authentication) {
//        // 권한 가져오기
//        String authorities = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(","));
//
//        Claims claims = Jwts.claims();
//        claims.setSubject(authentication.getName());
//        claims.put("auth", authorities);
//        claims.put("roles", authorities.replaceAll("ROLE_", ""));
//
//        long now = (new Date()).getTime();
//        // Access Token 생성
//        // 숫자 86400000은 토큰의 유효기간으로 1일을 나타냅니다. 보통 토큰은 30분 정도로 생성하는데 테스트를 위해 1일로 설정했습니다.
//        // 1일: 24*60*60*1000 = 86400000
//        Date accessTokenExpiresIn = new Date(now + 60*1000);
//        String accessToken = Jwts.builder()
////                .setSubject(authentication.getName())
//                .setClaims(claims)
//                .setExpiration(accessTokenExpiresIn)
//                .signWith(secretKey, SignatureAlgorithm.HS256)
//                .compact();
//
//        // Refresh Token 생성
//        String refreshToken = Jwts.builder()
//                .setExpiration(new Date(now + 86400000))
//                .signWith(secretKey, SignatureAlgorithm.HS256)
//                .compact();
//
//        return Jwt.builder()
//                .grantType("Bearer")
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .build();
//    }
//
//    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
//    public Authentication getAuthentication(String accessToken) {
//        // 토큰 복호화
//        Claims claims = parseClaims(accessToken);
//
//        if (claims.get("auth") == null) {
//            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
//        }
//
//        // 클레임에서 권한 정보 가져오기
//        Collection<? extends GrantedAuthority> authorities =
//                Arrays.stream(claims.get("auth").toString().split(","))
//                        .map(SimpleGrantedAuthority::new)
//                        .collect(Collectors.toList());
//
//        // UserDetails 객체를 만들어서 Authentication 리턴
//        UserDetails principal = new User(claims.getSubject(), "", authorities);
//
//        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
//    }
//
//    public String getMemberMail(String accessToken) {
//        return Jwts.parserBuilder()
//                .setSigningKey(secretKey)
//                .build()
//                .parseClaimsJws(accessToken)
//                .getBody()
//                .getSubject();
//    }
//
//    // 토큰 정보를 검증하는 메서드
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
//            System.out.println("JwtProvider method validateToken - Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token)" + Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token));
//            return true;
//        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
//            log.info("Invalid JWT Token", e);
//        } catch (ExpiredJwtException e) {
//            log.info("Expired JWT Token", e);
//        } catch (UnsupportedJwtException e) {
//            log.info("Unsupported JWT Token", e);
//        } catch (IllegalArgumentException e) {
//            log.info("JWT claims string is empty.", e);
//        }
//        return false;
//    }
//
//    private Claims parseClaims(String accessToken) {
//        System.out.println("JwtProvider method parseClaims - accessToken: " + accessToken);
//
//        try {
//            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken).getBody();
//        } catch (ExpiredJwtException e) {
//            return e.getClaims();
//        }
//    }
//
//}
