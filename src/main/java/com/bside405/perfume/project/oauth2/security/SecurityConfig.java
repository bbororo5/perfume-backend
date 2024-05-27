package com.bside405.perfume.project.oauth2.security;

import com.bside405.perfume.project.oauth2.CustomOAuth2Service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2Service customOAuth2Service;

    public SecurityConfig(CustomOAuth2Service customOAuth2UserService) {
        this.customOAuth2Service = customOAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Collections.singletonList("https://www.perfume-bside.site"));
                    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowCredentials(true);
                    config.setAllowedHeaders(Collections.singletonList("*"));
                    config.setMaxAge(3600L);
                    return config;
                }))

                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers("/", "/error", "/oauth2/**", "/api/health").permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .loginPage("/api/oauth2/authorization/naver")
                                .defaultSuccessUrl("https://www.perfume-bside.site/", true)
                                .userInfoEndpoint(userInfo -> userInfo
                                        .userService(customOAuth2Service)
                                )
                )
                .logout(logout ->
                        logout
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                                .logoutUrl("/api/logout")
                                .logoutSuccessUrl("https://www.perfume-bside.site/")
                )

                .csrf(AbstractHttpConfigurer::disable)
                .headers((headers) -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        return http.build();
    }
}
