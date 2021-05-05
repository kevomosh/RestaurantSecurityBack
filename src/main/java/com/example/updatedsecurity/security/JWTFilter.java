package com.example.updatedsecurity.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    private JWTUtility jwtUtility;

    private UserPrincipalDetailsService userPrincipalDetailsService;

    public JWTFilter(JWTUtility jwtUtility, UserPrincipalDetailsService userPrincipalDetailsService) {
        this.jwtUtility = jwtUtility;
        this.userPrincipalDetailsService = userPrincipalDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");
        String token = null;
        String id = null;

        if (authorization != null && authorization.startsWith("Bearer ")){
            token = authorization.substring(7);
            id = jwtUtility.getIdFromToken(token);
        }

        if (id != null
                && SecurityContextHolder.getContext().getAuthentication() == null
        ) {
            UserDetails userDetails = userPrincipalDetailsService.loadUserById(id);

            if (jwtUtility.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request,response);
    }
}
