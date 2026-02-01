package com.message.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    public JWTFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        try {

            // 🔥 Verifica se existe header e começa com Bearer
            if (header != null && header.startsWith(JWTProperties.PREFIX + " ")) {

                // Evita autenticar duas vezes
                if (SecurityContextHolder.getContext().getAuthentication() == null) {

                    JWTObject tokenObject = JWTCreator.parse(
                            header,
                            JWTProperties.PREFIX,
                            JWTProperties.KEY
                    );

                    String username = tokenObject.getSubject();

                    UserDetails userDetails =
                            userDetailsService.loadUserByUsername(username);
                    
                    System.out.println("Authorities do usuário: " + userDetails.getAuthorities());

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

            filterChain.doFilter(request, response);
            

        } catch (ExpiredJwtException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } catch (MalformedJwtException | UnsupportedJwtException | SignatureException e) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
        }
    }
}
