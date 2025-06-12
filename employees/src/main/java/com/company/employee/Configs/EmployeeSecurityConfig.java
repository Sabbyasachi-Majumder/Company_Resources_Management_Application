package com.company.employee.configs;

import com.company.employee.security.JwtRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;


@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = {"dto", "repository", "service", "Controllers", "entity"})
public class EmployeeSecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeSecurityConfig.class);

    private final JwtRequestFilter jwtRequestFilter;

    public EmployeeSecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring SecurityFilterChain");
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/v1/employees/testConnection", "/api/v1/employees/testDataBaseConnection").permitAll()
                        .requestMatchers("/actuator/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/employees/fetchEmployees", "/api/v1/employees/searchEmployee/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/employees/addEmployees", "/api/v1/employees/updateEmployees", "/api/v1/employees/deleteEmployees").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            logger.debug("Authentication exception: {}", authException.getMessage());
                            throw authException; // Propagate to @RestControllerAdvice
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            logger.debug("Access denied: {}", accessDeniedException.getMessage());
                            throw accessDeniedException; // Propagate to @RestControllerAdvice
                        })
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Used for manually generating user details . Replaced by database driven user profile storage
    //@Bean
    // public UserDetailsService userDetailsService() {
    //     logger.info("Creating access roles to access endpoints");
    //     InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
    //     logger.info("user can access fetch and search operations");
    //     manager.createUser(User.withUsername("user")
    //             .password(passwordEncoder().encode("pass")) // {noop} for plain text; use BCrypt in Step 3
    //             .roles("USER")
    //             .build());
    //     logger.info("admin can access add , update and delete operations");
    //     manager.createUser(User.withUsername("admin")
    //             .password(passwordEncoder().encode("pass"))
    //             .roles("ADMIN")
    //             .build());
    //     logger.info("Access roles are created and enforced");
    //     return manager;
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
