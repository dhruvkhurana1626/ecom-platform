package com.example.demo.security.jwt;

import com.example.demo.security.basicAuth.CustomUserDetailsManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsManager customUserDetailsManager;

    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // STEP 1 → READ HEADER
        String authHeader =
                request.getHeader("Authorization");

        // NO TOKEN
        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        // STEP 2 → EXTRACT TOKEN
        String token =
                authHeader.substring(7);

        // STEP 3 → EXTRACT EMAIL
        String email =
                jwtUtil.extractEmail(token);

        // STEP 4 → CHECK USER NOT ALREADY AUTHENTICATED
        if (email != null &&
                SecurityContextHolder.getContext()
                        .getAuthentication() == null) {

            // LOAD USER
            var userDetails =
                    customUserDetailsManager
                            .loadUserByUsername(email);

            // VALIDATE TOKEN
            if (jwtUtil.isValid(token)) {

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                // AUTHENTICATE USER
                SecurityContextHolder.getContext()
                        .setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
