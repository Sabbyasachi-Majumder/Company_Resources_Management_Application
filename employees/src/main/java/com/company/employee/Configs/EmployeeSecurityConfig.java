package com.company.employee.configs;

import com.company.employee.security.CustomAuthenticationEntryPoint;
import com.company.employee.security.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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
public class EmployeeSecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    public EmployeeSecurityConfig(JwtRequestFilter jwtRequestFilter,
                                  CustomAuthenticationEntryPoint authenticationEntryPoint) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    // Web-specific security filter chain (handles /web/** endpoints with form-based authentication)
    @Bean
    @Order(1)
    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/web/**")
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/web/employees/login",
                                "/web/employees/logout",
                                "/error",
                                "/error/**",
                                "/web/employees/testConnection",
                                "/web/employees/testDataBaseConnection",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/favicon.ico"
                        ).permitAll()
                        .requestMatchers(
                                "/web/employees/fetchEmployees",
                                "/web/employees/searchEmployees"
                        ).hasRole("USER")
                        .requestMatchers("/web/employees/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/web/employees/login")
                        .defaultSuccessUrl("/web/employees/fetchEmployees", true)
                        .failureUrl("/web/employees/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/web/employees/logout")
                        .logoutSuccessUrl("/web/employees/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint));

        return http.build();
    }

    // API-specific security filter chain (handles /api/** endpoints with JWT authentication)
    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**") // Restrict to API endpoints
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/favicon.ico",
                                "/error",
                                "/error/**",
                                "/api/v1/employees/authenticate",
                                "/api/v1/employees/register"
                        ).permitAll()
                        .requestMatchers(
                                "/api/v1/employees/testConnection",
                                "/api/v1/employees/testDataBaseConnection"
                        ).permitAll()
                        .requestMatchers("/api/v1/employees/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/actuator/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Shared password encoder for API and web authentication
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Shared authentication manager for API and web authentication
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
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
}
