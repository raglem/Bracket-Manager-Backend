package com.theodenmelgar.bracketmanager.config;

import com.theodenmelgar.bracketmanager.dto.auth.AuthDTO;
import com.theodenmelgar.bracketmanager.enums.LoginMethodEnum;
import com.theodenmelgar.bracketmanager.service.AuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthService authService;
    private final JwtConfig jwtConfig;
    private final JwtFilter jwtFilter;

    public SecurityConfig(AuthService authService,
                          JwtConfig jwtConfig, JwtFilter jwtFilter){
        this.authService = authService;
        this.jwtConfig = jwtConfig;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    // Configure OAuth and redirect to login/logout api routes
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // enable later with frontend integration
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .exceptionHandling(exception -> exception
                    // This is the key: Stop the redirect, just return 401
                    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            )
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(
                        "/auth/register", "/auth/login",
                        "/auth/logout", "/error"
                    ).permitAll()
                    .anyRequest().authenticated()
            )
            .oauth2Login(oauth -> oauth
                    .successHandler(oauth2SuccessHandler(authService))
            )
            .logout(logout -> logout
                    .logoutSuccessUrl("/")
                    .deleteCookies("AUTH-TOKEN")
            );

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    // Enable CORS with frontend
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "X-Requested-With"
        ));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationSuccessHandler oauth2SuccessHandler(AuthService authService) {
        return (request, response, authentication) -> {
            // Extract user info
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String oauthId = oAuth2User.getName();
            String name = oAuth2User.getAttribute("given_name");
            String email = oAuth2User.getAttribute("email");
            // for now, only Google OAuth is supported
            LoginMethodEnum provider = LoginMethodEnum.GOOGLE;

            AuthDTO authDTO = authService.oAuthLoginOrRegister(oauthId, name, email, provider);

            // Build the cookie and attach it to the response
            String token = authService.generateTokenForUser(authDTO.getUser().getId());
            ResponseCookie cookie = authService.createAuthCookie(token, jwtConfig.getExpiration());

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            response.sendRedirect("http://localhost:5173/brackets");
        };
    }
}
