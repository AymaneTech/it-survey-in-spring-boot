package com.wora.state_of_dev.config.filter;

import io.swagger.v3.oas.models.OpenAPI;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final Optional<String> token = parseJwt(request);
        System.out.println("my custom filter worked");

        if (token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("found a token ");
        System.out.println(token);

    }

    private Optional<String> parseJwt(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer "))
            return Optional.of(authHeader.substring(7));

        return Optional.empty();
    }

}
