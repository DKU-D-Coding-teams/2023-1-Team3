package DKUDCoding20231Team3.VISTA.util;

import DKUDCoding20231Team3.VISTA.domain.entity.Member;
import DKUDCoding20231Team3.VISTA.domain.repository.MemberRepository;
import DKUDCoding20231Team3.VISTA.dto.response.SignInResponse;
import DKUDCoding20231Team3.VISTA.exception.VistaException;
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
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static DKUDCoding20231Team3.VISTA.exception.ErrorCode.NOT_FOUND_MEMBER;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    public static final long SECOND = 1000;
    public static final long MINUTE = SECOND * 60;
    public static final long HOUR = 60 * MINUTE;
    public static final long DAY = 24 * HOUR;
    public static final long MONTH = 30 * DAY;

    public static final long AT_EXP_TIME =  1 * MINUTE;
    public static final long RT_EXP_TIME =  30 * MINUTE;

    // Header
    public static final String AT_HEADER = "access_token";
    public static final String RT_HEADER = "refresh_token";
    public static final String TOKEN_HEADER_PREFIX = "Bearer ";
    private final Key secretKey;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    public JwtUtil(@Value("${spring.jwt.secret}") String secretKey, MemberRepository memberRepository, AuthenticationManagerBuilder authenticationManagerBuilder) {
        byte[] keyBytes = Decoders.BASE64.decode((secretKey));
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.memberRepository = memberRepository;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    public String generateAccessToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setExpiration(new Date(System.currentTimeMillis() + AT_EXP_TIME))
                .claim("roles", authorities)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(secretKey, signatureAlgorithm)
                .compact();
    }
    public String generateRefreshToken(Authentication authentication) {
        return Jwts.builder()
                .setSubject(authentication.getName())
                .setExpiration(new Date(System.currentTimeMillis() + RT_EXP_TIME))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String regenerateAccessToken(String refreshToken) {
        String mail = getMailFromToken(refreshToken);
        final Member member = memberRepository.findMemberByMail(mail)
                .orElseThrow(() -> new VistaException(NOT_FOUND_MEMBER));

        Authentication authentication = generateAuthentication(member.getMail(), member.getPassword());

        return generateAccessToken(authentication);
    }

    public String regenerateRefreshToken(String refreshToken) {
        String mail = getMailFromToken(refreshToken);
        final Member member = memberRepository.findMemberByMail(mail)
                .orElseThrow(() -> new VistaException(NOT_FOUND_MEMBER));

        Authentication authentication = generateAuthentication(member.getMail(), member.getPassword());

        return generateRefreshToken(authentication);
    }

    public SignInResponse regenerateTokens(String refreshToken) {
        String mail = getMailFromToken(refreshToken);
        final Member member = memberRepository.findMemberByMail(mail)
                .orElseThrow(() -> new VistaException(NOT_FOUND_MEMBER));

        Authentication authentication = generateAuthentication(member.getMail(), member.getPassword());
        String newAccessToken = generateAccessToken(authentication);
        String newRefreshToken = generateRefreshToken(authentication);

        return SignInResponse.of(newAccessToken, newRefreshToken);
    }

    private Claims parseClaims(String Token) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(Token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {

        Claims claims = parseClaims(token);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("roles").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
    }

    public Authentication generateAuthentication(String mail, String password) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(mail, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);

        return authentication;
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

    public Boolean validateAccessToken(String accessToken) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken);
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

    public Boolean validateRefreshToken(String refreshToken) {
        try {
            memberRepository.findRefreshTokenByMail(Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(refreshToken).getBody().getSubject());
            return true;
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT RefreshToken", e);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }

        return false;
    }

    public String getMailFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    public String getTokenFromHeader(HttpServletRequest request) {
        return request.getHeader("Authorization").substring(TOKEN_HEADER_PREFIX.length());
    }

    public String getAccessTokenFromHeader(HttpServletRequest request) {
        return request.getHeader(AT_HEADER);
    }

    public String getRefreshTokenFromHeader(HttpServletRequest request) {
        return request.getHeader(RT_HEADER);
    }

    public void setAccessTokenToHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(AT_HEADER, accessToken);
    }

    public void setRefreshTokenToHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(RT_HEADER, refreshToken);
    }

    public void setAccessTokenToBody(HttpServletResponse response, String accessToken) {
        response.setHeader(AT_HEADER, accessToken);
    }

    public void setRefreshTokenToBody(HttpServletResponse response, String refreshToken) {
        response.setHeader(RT_HEADER, refreshToken);
    }

}
