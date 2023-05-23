package DKUDCoding20231Team3.VISTA.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    public static final long MINUTE = 1000 * 60;
    public static final long HOUR = 60 * MINUTE;
    public static final long DAY = 24 * HOUR;
    public static final long MONTH = 30 * DAY;

    public static final long AT_EXP_TIME =  1 * MINUTE;
    public static final long RT_EXP_TIME =  10 * MINUTE;

    // Header
    public static final String AT_HEADER = "access_token";
    public static final String RT_HEADER = "refresh_token";
    public static final String TOKEN_HEADER_PREFIX = "Bearer ";
    private final Key secretKey;
    private final String encodedKey;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    // Expiration Time

    @Autowired
    public JwtUtil(@Value("${spring.jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode((secretKey));
        this.encodedKey = secretKey;
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

//    public String generateAccessToken(Authentication authentication) {
//        Date date = new Date();
//
//        return Jwts.builder()
//                .setSubject(authentication.getName())
//                .setExpiration(new Date(date.getTime() + ACCESS_TIME))
//                .setIssuedAt(date)
//                .signWith(secretKey, signatureAlgorithm)
//                .compact();
//    }
//    public String generateRefreshToken(Authentication authentication) {
//        Date date = new Date();
//
//        return Jwts.builder()
//                .setSubject(authentication.getName())
//                .setExpiration(new Date(date.getTime() + REFRESH_TIME))
//                .signWith(secretKey, SignatureAlgorithm.HS256)
//                .compact();
//    }

    public String generateAccessToken(String memberMail) {
        Date date = new Date();

        return Jwts.builder()
                .setSubject(memberMail)
                .setExpiration(new Date(date.getTime() + AT_EXP_TIME))
                .setIssuedAt(date)
                .signWith(secretKey, signatureAlgorithm)
                .compact();
    }
    public String generateRefreshToken(String memberMail) {
        Date date = new Date();

        return Jwts.builder()
                .setSubject(memberMail)
                .setExpiration(new Date(date.getTime() + AT_EXP_TIME))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 인증 객체 생성
//    public Authentication generateAuthentication(String mail) {
//        UserDetails userDetails = memberService.loadUserByUsername(mail);
//        // spring security 내에서 가지고 있는 객체입니다. (UsernamePasswordAuthenticationToken)
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }

    private Claims parseClaims(String Token) {
        System.out.println("JwtProvider method parseClaims - Token: " + Token);

        try {
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(Token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

//    public Authentication generateAuthentication(String accessToken) {
    public UsernamePasswordAuthenticationToken generateAuthentication(String accessToken) {

        Claims claims = parseClaims(accessToken);

//        if (claims.get("auth") == null) {
//            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
//        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

//        // UserDetails 객체를 만들어서 Authentication 리턴
//        UserDetails userDetails = new User(claims.getSubject(), "", authorities);

//        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);

        return new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);

//        UserDetails userDetails = memberService.loadUserByUsername(mail);
//        // spring security 내에서 가지고 있는 객체입니다. (UsernamePasswordAuthenticationToken)
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public Boolean validateToken(String Token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(Token);
            return true;
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT AccessToken", e);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }



//    // refreshToken 토큰 검증
//    // db에 저장되어 있는 token과 비교
//    // db에 저장한다는 것이 jwt token을 사용한다는 강점을 상쇄시킨다.
//    // db 보다는 redis를 사용하는 것이 더욱 좋다. (in-memory db기 때문에 조회속도가 빠르고 주기적으로 삭제하는 기능이 기본적으로 존재합니다.)
//    public Boolean refreshTokenValidation(String token) {
//
//        // 1차 토큰 검증
//        if(!tokenValidation(token)) return false;
//
//        // DB에 저장한 토큰 비교
//        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccountNickname(getEmailFromToken(token));
//
//        return refreshToken.isPresent() && token.equals(refreshToken.get().getRefreshToken());
//    }

    public String getMailFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    // header 토큰을 가져오는 기능
    public String getAccessTokenFromHeader(HttpServletRequest request) {
        System.out.println("JwtAuthenticationFilter method getAccessTokenFromHeader - request.getHeader(ACEESS_TOKEN): " + request.getHeader(AT_HEADER));
        return request.getHeader(AT_HEADER);
    }

    public String getRefreshTokenFromHeader(HttpServletRequest request) {
        System.out.println("JwtAuthenticationFilter method getRefreshTokenFromHeader - request.getHeader(REFRESH_TOKEN): " + request.getHeader(RT_HEADER));
        return request.getHeader(RT_HEADER);
    }


    // 어세스 토큰 헤더 설정
    public void setAccessTokenToHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(AT_HEADER, accessToken);
    }

    // 리프레시 토큰 헤더 설정
    public void setRefreshTokenToHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(RT_HEADER, refreshToken);
    }

}
