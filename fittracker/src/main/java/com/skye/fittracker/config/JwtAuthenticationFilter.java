package com.skye.fittracker.config;

import com.skye.fittracker.repository.UserRepository;
import com.skye.fittracker.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.skye.fittracker.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;


    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);

            if (jwtUtil.validateToken(token)) {
                String email = jwtUtil.extractEmail(token);
                User user = userRepository.findByEmail(email)
                        .orElse(null);

                if (user != null &&
                        SecurityContextHolder.getContext()
                                .getAuthentication() == null) {
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    List.of()
                            );

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (Exception e){
            SecurityContextHolder.clearContext();
        }


//        if (jwtUtil.validateToken(token)) {
//
//            String email = jwtUtil.extractEmail(token);
//
//            UserDetails userDetails = new User(email, "", Collections.emptyList());
//
//            UsernamePasswordAuthenticationToken auth =
//                    new UsernamePasswordAuthenticationToken(
//                            userDetails,
//                            null,
//                            userDetails.getAuthorities()
//                    );
//
//            SecurityContextHolder.getContext().setAuthentication(auth);
//        }


        filterChain.doFilter(request, response);
    }
}