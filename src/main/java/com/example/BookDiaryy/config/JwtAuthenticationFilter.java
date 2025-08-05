package com.example.BookDiaryy.config;

import com.example.BookDiaryy.service.auth.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private UserDetails userDetails;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        // Skip filter for login/register endpoints
        if (path.equals("/user/login") || path.equals("/user/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (path.startsWith("/user/login") ||
                path.startsWith("/user/register") ||
                path.startsWith("/book/")) {

            filterChain.doFilter(request, response);
            return;
        }

        if ( request.getRequestURI().equals("/book/**") || request.getRequestURI().equals("/book/allBooks") ) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = null;
        String authHeader = request.getHeader("Authorization");

        // 1. First check Authorization header
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
        }
        // 2. Fallback to cookie if header not present
        else if (request.getCookies() != null) {
            Cookie jwtCookie = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals("jwt"))
                    .findFirst()
                    .orElse(null);
            jwt = jwtCookie != null ? jwtCookie.getValue() : null;
        }

        // If no token found, continue chain
        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String username = jwtService.getUsernameFromToken(jwt);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            // Handle token expiration/refresh logic if needed
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT");
            return;
        }

        filterChain.doFilter(request, response);

        /*
        if (request.getRequestURI().equals("/login") || request.getRequestURI().equals("/register")){
            filterChain.doFilter(request,response);
            return;
        }

        if (request.getCookies() == null){
            filterChain.doFilter(request,response);
            return;
        }

        Cookie jwtCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("jwt")).findFirst().orElse(null);
        String authorizationHeader = jwtCookie != null ? jwtCookie.getValue() : null;
        String jwt;
        String username;
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authorizationHeader.substring(7);
        try {
            username = jwtService.getUsernameFromToken(jwt);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtService.isTokenValid(jwt, userDetails)){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        }catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e){
            boolean rememberme = Arrays.stream(request.getCookies()).anyMatch(cookie -> cookie.getName().equals("rememberme"));
            if (rememberme){
                String newJwt = jwtService.generateTokenFromUsername(userDetails.getUsername());
                Cookie newJwtCookie = new Cookie("jwt", newJwt);
                newJwtCookie.setMaxAge(60*60*24);
                newJwtCookie.setPath("/home");
                response.addCookie(newJwtCookie);
            }
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
        }
         */
    }
}
