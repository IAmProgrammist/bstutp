package ru.ultrabasic.bstutp.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import ru.ultrabasic.bstutp.models.users.Admin;
import ru.ultrabasic.bstutp.models.users.User;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {
    @Autowired
    private DataSource dataSource;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeRequests(request ->
                        request
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/**").permitAll())
                .formLogin(form ->
                        form
                                .usernameParameter("username")
                                .passwordParameter("password")
                                .loginProcessingUrl("/api/login")
                                .successHandler((request, response, authentication) -> {
                                    response.setStatus(HttpServletResponse.SC_OK);
                                })
                                .failureHandler((request, response, exception) -> {
                                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                })
                                .permitAll())
                .rememberMe(remember ->
                        remember
                                .rememberMeParameter("rememberMe")
                                .authenticationSuccessHandler((request, response, authentication) -> {
                                    response.setStatus(HttpServletResponse.SC_OK);
                                }))
                .logout(form ->
                        form.permitAll());
        return http.build();
    }
}
