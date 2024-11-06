package com.project.fitnessapp.config;

import com.project.fitnessapp.models.AppUser;
import com.project.fitnessapp.repositories.AppUserRepository;
import com.project.fitnessapp.services.LoginAttemptService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;


@Configuration
public class SecurityConfig {
    @Autowired
    private final AppUserRepository appUserRepository;

    @Autowired
    private final LoginAttemptService loginAttemptService;

    public SecurityConfig(AppUserRepository appUserRepository, LoginAttemptService loginAttemptService) {
        this.appUserRepository = appUserRepository;
        this.loginAttemptService = loginAttemptService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))) // Disable CSRF if not needed
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login/**", "/register").permitAll() // Public access
                        .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                        .requestMatchers("/h2-console/**","/programs/instructor-programs/**",
                                "/programs/*/addProgram",
                                "/programs/client-info/**").hasRole("INSTRUCTOR") // Instructor only
                        .requestMatchers("/programs/client-programs/**", "/programs/**").hasRole("CLIENT") // Client only
                        .anyRequest().authenticated() // All other requests require authentication
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .failureUrl("/login?error")
                        .failureHandler(customAuthenticationFailureHandler())
                        .successHandler(customAuthenticationSuccessHandler())
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
                        .addHeaderWriter(new StaticHeadersWriter("Content-Security-Policy", "script-src 'self'"))
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String sessionId = request.getSession().getId();

            if (loginAttemptService.isSessionLockedOut(sessionId)) {
                throw new UsernameNotFoundException("User is locked out due to too many failed attempts.");
            }

            AppUser user = appUserRepository.findByEmail(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }

            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().toString()))
            );
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            AppUser user = appUserRepository.findByEmail(userDetails.getUsername());

            String redirectUrl;
            if ("INSTRUCTOR".equals(user.getRole().toString())) {
                redirectUrl = "/programs/instructor-programs/" + user.getId();
            } else {
                redirectUrl = "/programs/" + user.getId();
            }
            response.sendRedirect(redirectUrl);
        };
    }

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return (HttpServletRequest request, HttpServletResponse response,
                org.springframework.security.core.AuthenticationException exception) -> {

            String sessionId = request.getSession().getId();

            if (loginAttemptService.isSessionLockedOut(sessionId)) {
                response.sendRedirect("/login?locked=true");
            } else {
                loginAttemptService.recordFailedAttempt(request);
                loginAttemptService.checkAndLockSession(request);
                response.sendRedirect("/login?error=true");
            }
        };
    }
}
