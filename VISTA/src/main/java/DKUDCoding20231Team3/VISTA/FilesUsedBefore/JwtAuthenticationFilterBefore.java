////package DKUDCoding20231Team3.VISTA.util.JwtUtil;
////
////import jakarta.servlet.FilterChain;
////import jakarta.servlet.ServletException;
////import jakarta.servlet.ServletRequest;
////import jakarta.servlet.ServletResponse;
////import jakarta.servlet.http.HttpServletRequest;
////import lombok.RequiredArgsConstructor;
////import org.springframework.security.core.Authentication;
////import org.springframework.security.core.context.SecurityContextHolder;
////import org.springframework.util.StringUtils;
////import org.springframework.web.filter.GenericFilterBean;
////
////import java.io.IOException;
////
////@RequiredArgsConstructor
////public class JwtAuthenticationFilter extends GenericFilterBean {
////
////    private final JwtProvider jwtProvider;
////
////    @Override
////    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
////
////        String token = resolveToken((HttpServletRequest) request);
////
////        if (token != null && jwtProvider.validateToken(token)) {
////            Authentication authentication = jwtProvider.getAuthentication(token);
////            SecurityContextHolder.getContext().setAuthentication(authentication);
////        }
////        chain.doFilter(request, response);
////    }
////
////    // Request Header 에서 토큰 정보 추출
////    public String resolveToken(HttpServletRequest request) {
////        String bearerToken = request.getHeader("Authorization");
////        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
////            return bearerToken.substring(7);
////        }
////        return null;
////    }
////}
//
//package DKUDCoding20231Team3.VISTA.FilesUsedBefore;
//
//import DKUDCoding20231Team3.VISTA.util.JwtUtil;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//
//@Slf4j
//@RequiredArgsConstructor
//public class JwtAuthenticationFilterBefore extends OncePerRequestFilter {
//
//    private final JwtUtil jwtUtil;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        // Access / Refresh 헤더에서 토큰을 가져옴.
//        String accessToken = jwtUtil.getAccessTokenFromHeader(request);
//        String refreshToken = jwtUtil.getRefreshTokenFromHeader(request);
//
//        if(accessToken != null) {
//            // 엑세스 토큰값이 유효하다면 setAuthentication를 통해 security context에 인증 정보저장
//            if(jwtUtil.validateAccessToken(accessToken)){
//                setAuthentication(jwtUtil.getMailFromToken(accessToken));
//            }
//            // 어세스 토큰이 만료된 상황 && 리프레시 토큰 또한 존재하는 상황
//            else if (refreshToken != null) {
//                // 리프레시 토큰 검증 && 리프레시 토큰 DB에서  토큰 존재유무 확인
//                boolean isRefreshToken = jwtUtil.validateRefreshToken(refreshToken);
//                // 리프레시 토큰이 유효하고 리프레시 토큰이 DB와 비교했을때 똑같다면
//                if (isRefreshToken) {
//                    // 리프레시 토큰으로 아이디 정보 가져오기
//                    String loginId = jwtUtil.getMailFromToken(refreshToken);
//                    // 새로운 어세스 토큰 발급
//                    String newAccessToken = jwtUtil.generateAccessToken(loginId);
//                    // 헤더에 어세스 토큰 추가
//                    jwtUtil.setHeaderAccessToken(response, newAccessToken);
//                    // Security context에 인증 정보 넣기
////                    setAuthentication(jwtUtil.getMailFromToken(newAccessToken));
//                    setAuthentication(newAccessToken);
//                }
//                // 리프레시 토큰이 만료 || 리프레시 토큰이 DB와 비교했을때 똑같지 않다면
////                else {
////                    jwtExceptionHandler(response, "RefreshToken Expired", HttpStatus.BAD_REQUEST);
////                    return;
////                }
//            }
//        }
//
//        filterChain.doFilter(request,response);
//    }
//
//    // SecurityContext 에 Authentication 객체를 저장합니다.
////    public void setAuthentication(String mail) {
//    public void setAuthentication(String accessToken) {
////        Authentication authentication = jwtUtil.generateAuthentication(mail);
//        Authentication authentication = jwtUtil.generateAuthentication(accessToken);
//        // security가 만들어주는 securityContextHolder 그 안에 authentication을 넣어줍니다.
//        // security가 securitycontextholder에서 인증 객체를 확인하는데
//        // jwtAuthfilter에서 authentication을 넣어주면 UsernamePasswordAuthenticationFilter 내부에서 인증이 된 것을 확인하고 추가적인 작업을 진행하지 않습니다.
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//    }
//}