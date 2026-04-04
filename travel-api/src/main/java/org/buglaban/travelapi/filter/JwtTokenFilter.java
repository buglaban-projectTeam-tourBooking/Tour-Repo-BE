package org.buglaban.travelapi.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.buglaban.travelapi.components.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    @Value("${api.prefix}")
    private String apiPrefix;

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if (isBypassToken(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            final String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            final String token = authHeader.substring(7);
            final String email = jwtTokenUtils.extractEmail(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                if (jwtTokenUtils.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()  // ✅ Role từ UserDetailsService
                            );
                    authenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            logger.error("JWT Filter error: " + ex.getMessage(), ex);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Authentication failed: " + ex.getMessage());
        }
    }

    private boolean isBypassToken(@NonNull HttpServletRequest request) {
        String path = request.getServletPath();
        String method = request.getMethod();


        List<String> publicGetPaths = Arrays.asList(
                "/error",

                apiPrefix + "/tours",
                apiPrefix + "/tours/search",
                apiPrefix + "/tours/related",
                apiPrefix + "/tours/calculate-price",

                apiPrefix + "/categories",

                apiPrefix + "/review/tour",

                apiPrefix + "/tour-schedules",

                apiPrefix + "/coupons/validate",
                apiPrefix + "/coupons/check",
                apiPrefix + "/coupons/active"
        );

        List<String> publicPostPaths = Arrays.asList(
                apiPrefix + "/user/register",
                apiPrefix + "/user/login",

                apiPrefix + "/tours/calculate-price",

                apiPrefix + "/coupons/validate",
                apiPrefix + "/coupons/check"
        );

        if ("GET".equals(method)) {
            for (String publicPath : publicGetPaths) {
                if (path.startsWith(publicPath)) {
                    return true;
                }
            }
            if (path.matches(apiPrefix + "/tours/[^/]+")) {
                return true;
            }
        }

        if ("POST".equals(method)) {
            for (String publicPath : publicPostPaths) {
                if (path.startsWith(publicPath)) {
                    return true;
                }
            }
        }

        return false;
    }
}