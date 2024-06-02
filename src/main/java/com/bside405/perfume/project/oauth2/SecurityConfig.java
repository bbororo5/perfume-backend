package com.bside405.perfume.project.oauth2;

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
                    config.setAllowedOrigins(Arrays.asList("https://perfume-bside.site", "https://frontend.perfume-bside.site","http://perfume-bside.site"));
                    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowCredentials(true);
                    config.setAllowedHeaders(Collections.singletonList("*"));
                    config.setMaxAge(3600L);
                    return config;
                }))

                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers("/", "/oauth2/authorization/**", "/login/oauth2/code/**").permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .defaultSuccessUrl("https://frontend.perfume-bside.site/main", true)
                                .userInfoEndpoint(userInfo -> userInfo
                                        .userService(customOAuth2Service)
                                )
                )
                .logout(logout ->
                        logout
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                                .logoutUrl("/api/logout")
                                .logoutSuccessHandler(new CustomLogoutSuccessHandler())
                )

                .csrf(AbstractHttpConfigurer::disable)
                .headers((headers) -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        return http.build();
    }
}
