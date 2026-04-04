package org.buglaban.travelapi.config;

import lombok.RequiredArgsConstructor;
import org.buglaban.travelapi.filter.JwtTokenFilter;
import org.buglaban.travelapi.util.UserType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableWebMvc
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    private final AuthenticationProvider authenticationProvider;

    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                .cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
                    @Override
                    public void customize(CorsConfigurer<HttpSecurity> c) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(List.of("*"));
                        config.setAllowedMethods(Arrays.asList(
                                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                        config.setAllowedHeaders(Arrays.asList(
                                "authorization", "content-type", "x-auth-token"));
                        config.setExposedHeaders(List.of("x-auth-token"));
                        UrlBasedCorsConfigurationSource source =
                                new UrlBasedCorsConfigurationSource();
                        source.registerCorsConfiguration("/**", config);
                        c.configurationSource(source);
                    }
                })

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authenticationProvider(authenticationProvider)

                .addFilterBefore(jwtTokenFilter,
                        UsernamePasswordAuthenticationFilter.class)

                .authorizeHttpRequests(req -> req

                        .requestMatchers("/error").permitAll()

                        // ──────────────────────────────────────────────────
                        // USER
                        // ──────────────────────────────────────────────────
                        .requestMatchers(POST,
                                p("/user/register"),
                                p("/user/login")
                        ).permitAll()

                        .requestMatchers(GET,
                                p("/user/{id}")
                        ).authenticated()

                        .requestMatchers(PATCH,
                                p("/user/update/{id}")
                        ).authenticated()

                        .requestMatchers(PUT,
                                p("/user/reset-password/{id}")
                        ).authenticated()

                        .requestMatchers(GET,
                                p("/user/")
                        ).hasAnyRole(UserType.ADMIN.name(), UserType.STAFF.name())

                        .requestMatchers(DELETE,
                                p("/user/delete/{id}")
                        ).hasAnyRole(UserType.ADMIN.name(), UserType.STAFF.name())

                        .requestMatchers(PUT,
                                p("/user/{id}/status")
                        ).hasAnyRole(UserType.ADMIN.name(), UserType.STAFF.name())

                        // ──────────────────────────────────────────────────
                        // TOUR — public
                        // ──────────────────────────────────────────────────
                        .requestMatchers(GET,
                                p("/tours"),
                                p("/tours/{slug}"),
                                p("/tours/search"),
                                p("/tours/related"),
                                p("/tours/*/detail"),
                                p("/tours/slug/*/detail")
                        ).permitAll()

                        .requestMatchers(POST,
                                p("/tours/calculate-price")
                        ).permitAll()

                        // ──────────────────────────────────────────────────
                        // ADMIN TOUR — ADMIN only
                        // ──────────────────────────────────────────────────
                        .requestMatchers(p("/admin/tours/**"))
                        .hasRole(UserType.ADMIN.name())

                        // ──────────────────────────────────────────────────
                        // CATEGORY
                        // ──────────────────────────────────────────────────
                        .requestMatchers(GET,  p("/categories/**")).permitAll()
                        .requestMatchers(POST, p("/categories/**")).hasRole(UserType.ADMIN.name())
                        .requestMatchers(PUT,  p("/categories/**")).hasRole(UserType.ADMIN.name())
                        .requestMatchers(PATCH,p("/categories/**")).hasRole(UserType.ADMIN.name())
                        .requestMatchers(DELETE,p("/categories/**")).hasRole(UserType.ADMIN.name())

                        // ──────────────────────────────────────────────────
                        // COUPON
                        // ──────────────────────────────────────────────────
                        .requestMatchers(GET,  p("/coupons/**")).permitAll()
                        .requestMatchers(POST,
                                p("/coupons/validate"),
                                p("/coupons/check")
                        ).permitAll()
                        .requestMatchers(POST,  p("/coupons")).hasRole(UserType.ADMIN.name())
                        .requestMatchers(PUT,   p("/coupons/**")).hasRole(UserType.ADMIN.name())
                        .requestMatchers(PATCH, p("/coupons/**")).hasRole(UserType.ADMIN.name())
                        .requestMatchers(DELETE,p("/coupons/**")).hasRole(UserType.ADMIN.name())

                        // ──────────────────────────────────────────────────
                        // TOUR SCHEDULE
                        // ──────────────────────────────────────────────────
                        .requestMatchers(GET,   p("/tour-schedules/**")).permitAll()
                        .requestMatchers(POST,  p("/tour-schedules/**")).hasRole(UserType.ADMIN.name())
                        .requestMatchers(PUT,   p("/tour-schedules/**")).hasRole(UserType.ADMIN.name())
                        .requestMatchers(PATCH, p("/tour-schedules/**")).hasRole(UserType.ADMIN.name())
                        .requestMatchers(DELETE,p("/tour-schedules/**")).hasRole(UserType.ADMIN.name())

                        // ──────────────────────────────────────────────────
                        // ORDER
                        // ──────────────────────────────────────────────────
                        .requestMatchers(POST, p("/order/"))
                        .hasRole(UserType.USER.name())

                        .requestMatchers(GET, p("/order/my-orders"))
                        .hasRole(UserType.USER.name())

                        // /order/admin phải khai báo TRƯỚC /order/{id}
                        .requestMatchers(GET, p("/order/admin"))
                        .hasAnyRole(UserType.ADMIN.name(), UserType.STAFF.name())

                        .requestMatchers(GET, p("/order/{id}"))
                        .authenticated()

                        .requestMatchers(PUT, p("/order/{id}/cancel"))
                        .hasRole(UserType.USER.name())

                        .requestMatchers(PUT, p("/admin/tours/{id}/preview"))
                        .hasRole(UserType.ADMIN.name())

                        .requestMatchers(PUT, p("/order/{id}/status"))
                        .hasAnyRole(UserType.ADMIN.name(), UserType.STAFF.name())

                        // ──────────────────────────────────────────────────
                        // REVIEW
                        // ──────────────────────────────────────────────────
                        .requestMatchers(GET,
                                p("/review/tour/{tourId}"),
                                p("/review/tour/{tourId}/statistics"),
                                p("/review/{id}")
                        ).permitAll()

                        .requestMatchers(GET, p("/review/user/{userId}"))
                        .authenticated()

                        // /review/admin/** phải khai báo TRƯỚC /review/{id}
                        .requestMatchers(GET,   p("/review/admin/pending"))
                        .hasRole(UserType.ADMIN.name())

                        .requestMatchers(PATCH, p("/review/admin/{id}/status"))
                        .hasRole(UserType.ADMIN.name())

                        .requestMatchers(POST,   p("/review/"))
                        .hasRole(UserType.USER.name())

                        .requestMatchers(PUT,    p("/review/{id}"))
                        .hasRole(UserType.USER.name())

                        .requestMatchers(DELETE, p("/review/{id}"))
                        .hasAnyRole(UserType.USER.name(), UserType.ADMIN.name())

                        // ──────────────────────────────────────────────────
                        // Mọi request còn lại phải đăng nhập
                        // ──────────────────────────────────────────────────
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    private String p(String path) {
        return apiPrefix + path;
    }
}