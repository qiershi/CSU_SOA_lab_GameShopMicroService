package com.csu.gatewayservice.config;

import com.csu.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

/**
* @ProjectName: GameShopMicroService
* @Title: JwtAuthFilter
* @Package: com.csu.gatewayservice.config
* @Description: 
* @author qiershi
* @date 2025/11/21 16:37
* @version V1.0
* Copyright (c) 2025, qiershi2006@h163.com All Rights Reserved.
*/
class JwtAuthWebFilter implements WebFilter {

    private final JwtUtil jwtUtil;

    @Value("${jwt.static-token}")
    private String staticToken;

    public JwtAuthWebFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String header = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        System.out.println("Authorization header: " + header);

        String subject = null;
        String token = null;
        boolean isStaticToken = false;

        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            if (token.equals(staticToken)) {
                isStaticToken = true;
                try {
                    subject = jwtUtil.extractSubject(token);
                    System.out.println("Static token detected, extracted subject: " + subject);
                } catch (RuntimeException e) {
                    System.err.println("Failed to extract subject from static token: " + e.getMessage());
                }
            } else {
                try {
                    subject = jwtUtil.extractSubject(token);
                    System.out.println("Extracted subject: " + subject);
                } catch (RuntimeException e) {
                    System.err.println("Failed to extract subject: " + e.getMessage());
                }
            }
        } else {
            System.out.println("No valid Authorization header found");
        }

        if (subject != null) {
            boolean isValid;
            if (isStaticToken) {
                isValid = true;
            } else {
                isValid = jwtUtil.validateToken(token, subject);
            }

            if (isValid) {
                List<SimpleGrantedAuthority> authorities = subject.equals("test")
                        ? List.of(new SimpleGrantedAuthority("ROLE_TEST"))
                        : List.of(new SimpleGrantedAuthority("ROLE_USER"));

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(subject, null, authorities);

                SecurityContextImpl context = new SecurityContextImpl(auth);
                return chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)));
            } else {
                System.err.println("JWT validation failed for subject: " + subject);
            }
        }

        return chain.filter(exchange); // 未认证时直接放行，由 SecurityWebFilterChain 拦截返回 401
    }

}