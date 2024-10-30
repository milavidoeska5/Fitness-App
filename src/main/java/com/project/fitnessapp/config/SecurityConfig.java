package com.project.fitnessapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Autowired
    private final CustomAuthenticationSuccessHandler successHandler;

    public SecurityConfig(CustomAuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF if not needed
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register", "/h2-console/**").permitAll() // Public access
                        .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                        .requestMatchers("/programs/instructor-programs/**",
                                "/programs/*/addProgram",
                                "/programs/client-info/**").hasRole("INSTRUCTOR") // Instructor only
                        .requestMatchers("/programs/client-programs/**", "/programs/**").hasRole("CLIENT") // Client only
                        .anyRequest().authenticated() // All other requests require authentication
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(successHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                )
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        List<UserDetails> users = new ArrayList<>();

        UserDetails instructor1 = User.withUsername("mv@gmail.com")
                .password(passwordEncoder.encode("admin123"))
                .roles("INSTRUCTOR")
                .build();

        UserDetails instructor2 = User.withUsername("mt@gmail.com")
                .password(passwordEncoder.encode("admin123"))
                .roles("INSTRUCTOR")
                .build();

        UserDetails client1 = User.withUsername("klient1@gmail.com")
                .password(passwordEncoder.encode("user123"))
                .roles("CLIENT")
                .build();

        UserDetails client2 = User.withUsername("klient2@gmail.com")
                .password(passwordEncoder.encode("user123"))
                .roles("CLIENT")
                .build();

        users.add(instructor1);
        users.add(instructor2);
        users.add(client1);
        users.add(client2);

        return new InMemoryUserDetailsManager(users);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
