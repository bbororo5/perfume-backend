package com.bside405.perfume.project.oauth2.security;

import com.bside405.perfume.project.oauth2.CustomOAuth2Service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2Service customUserService;

    public SecurityConfig(CustomOAuth2Service customOAuth2UserService) {
        this.customUserService = customOAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers("/", "/login", "/login/oauth2/code/callback", "/h2-console/**", "/error").permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .defaultSuccessUrl("https://perfume-client.vercel.app/", true)
                                .userInfoEndpoint(userInfo -> userInfo
                                        .userService(customUserService)
                                )
                )
                .logout(logout ->
                        logout
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("https://perfume-client.vercel.app/")
                )

                //테스트용1
                .csrf(AbstractHttpConfigurer::disable
                )
                //테스트용2
                .headers((headers) ->
	   			    headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
	   		    );
        return http.build();
    }
}
