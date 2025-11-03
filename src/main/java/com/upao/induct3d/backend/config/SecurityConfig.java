package com.upao.induct3d.backend.config;

import com.upao.induct3d.backend.jwt.JwtEntryPoint;
import com.upao.induct3d.backend.jwt.JwtFilter;
import com.upao.induct3d.backend.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtEntryPoint jwtEntryPoint;
    private final JwtFilter jwtFilter;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    public SecurityConfig(UserDetailsServiceImpl userDetailsService, PasswordEncoder passwordEncoder, JwtEntryPoint jwtEntryPoint, JwtFilter jwtFilter, AuthenticationConfiguration authenticationConfiguration) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtEntryPoint = jwtEntryPoint;
        this.jwtFilter = jwtFilter;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "https://induct3d.netlify.app/",
                "https://induct3d.tech/"
        ));
        configuration.addAllowedMethod("*"); // GET, POST, etc.
        configuration.addAllowedHeader("*"); // Authorization, Content-Type, etc.
        configuration.setAllowCredentials(true); // permite cookies/autenticación

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(authenticationProvider())
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/templates").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/templates/{templateId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/templates/glb/{templateId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/tours/{tourId}").permitAll()
                        //.requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/tours/**").hasAnyRole("CREATOR", "ADMIN")
                        .requestMatchers("/api/templates/**").hasAnyRole("CREATOR", "ADMIN")
                        .requestMatchers(
                                "/swagger-ui.html", 
                                "/swagger-ui/**",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exc -> exc.authenticationEntryPoint(jwtEntryPoint))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
