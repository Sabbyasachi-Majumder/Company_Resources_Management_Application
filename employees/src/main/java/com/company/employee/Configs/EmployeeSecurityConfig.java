package com.company.employee.Configs;

import com.company.employee.Security.JwtRequestFilter;
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
@ComponentScan(basePackages = {"DTOs", "Repositories", "Services", "Controllers", "Entity"})
public class EmployeeSecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeSecurityConfig.class);

    private final JwtRequestFilter jwtRequestFilter;

    public EmployeeSecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Creating rules to access endpoints");
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/testConnection", "/testDataBaseConnection").permitAll()
                        .requestMatchers("/fetchEmployees", "/searchEmployee/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/addEmployees", "/updateEmployees", "/deleteEmployees").hasRole("ADMIN")
                        .anyRequest().authenticated()
                ).addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        logger.info("Rules to access endpoints created and enforced");
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

    // Encrypts the given password
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*Added AuthenticationManager bean for /login.*/
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
