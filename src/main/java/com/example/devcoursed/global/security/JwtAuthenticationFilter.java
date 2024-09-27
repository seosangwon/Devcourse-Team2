package com.example.devcoursed.global.security;

import com.example.devcoursed.global.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null) {
            try {
                String token = bearerToken.substring("Bearer ".length());

                //위변조 체크 및 디코드
                Claims claims = JwtUtil.decode(token);

                //caims의 정보들 파싱
                Map<String, Object> data = (Map<String, Object>) claims.get("data");
                long id = Long.parseLong((String) data.get("id"));
                String loginId = (String) data.get("loginId");

                //security GrantedAuthority 변환
                List<? extends GrantedAuthority> authorities = ((List<String>) data.get("authorities"))
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                //SecurityUser 생성 (id , loginId , pw , authorities)
                SecurityUser user = new SecurityUser(id, loginId, "", authorities);

                //SecurityContext에 넣을  Authenticiation 생성
                Authentication auth = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());


                //SecurityContext에 auth 넣기
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (JwtException e) {
                log.warn("유효하지 않는 JWT 토큰 :  {}", e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
                return;
            }


        }


        filterChain.doFilter(request, response);
    }
}
