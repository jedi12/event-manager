package ru.project.my.eventmanager.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.project.my.eventmanager.security.CustomAuthenticationEntryPoint;
import ru.project.my.eventmanager.security.CustomUserDetails;
import ru.project.my.eventmanager.services.UserService;
import ru.project.my.eventmanager.services.model.User;

import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    static final Logger log = LoggerFactory.getLogger(JwtTokenFilter.class.getName());

    private final JwtTokenManager jwtTokenManager;
    private final UserService userService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public JwtTokenFilter(JwtTokenManager jwtTokenManager, UserService userService, CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.jwtTokenManager = jwtTokenManager;
        this.userService = userService;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request,response);
            return;
        }

        String login;
        try {
            String jwt = authHeader.substring(7);
            login = jwtTokenManager.getLogin(jwt);
        } catch (Exception e) {
            log.warn("Jwt токен не валидный", e);
            customAuthenticationEntryPoint.commence(request, response, new InsufficientAuthenticationException(e.getMessage(), e));
            return;
        }

        if (login != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userService.findUserByLogin(login);
            CustomUserDetails userDetails = new CustomUserDetails(user);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}
