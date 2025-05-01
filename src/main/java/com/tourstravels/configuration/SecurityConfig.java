package com.tourstravels.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import com.tourstravels.entity.User;
import com.tourstravels.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Configuration
public class SecurityConfig {
    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) 
            .csrf(csrf -> csrf.disable()) 
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/tours_travel/register", "/tours_travel/login", "/tours_travel/bookings/new", 
                                 "/tours_travel/bookings/all", "/tours_travel/send-otp", "/tours_travel/verify-otp",
                                 "/tours_travel/contacts","/tours_travel/forgot-password/send-otp",
                                 "/tours_travel/forgot-password/verify-otp","/tours_travel/forgot-password/reset",
                                 "/login/oauth2/**", "/tours_travel/user","/", "/login", "/oauth2/**")
                .permitAll()  
                .anyRequest().authenticated()  
            )
            .oauth2Login(oauth2 -> oauth2
                .defaultSuccessUrl("http://localhost:5500/index.html", true) 
                .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService()))
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));  

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://127.0.0.1:5500", "http://localhost:5500", "http://localhost:3000"));
        config.setAllowedHeaders(List.of("*"));  
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));  

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        return userRequest -> {
            OAuth2User oauthUser = new DefaultOAuth2UserService().loadUser(userRequest);
            String email = oauthUser.getAttribute("email");
            String name = oauthUser.getAttribute("name");

            Optional<User> existingUser = userRepository.findByEmail(email);
            if (existingUser.isEmpty()) {
                // ✅ Automatically register user instead of throwing an error
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setName(name);
                newUser.setEnabled(true);
                userRepository.save(newUser);
            }

            return oauthUser;
        };
    }
}
