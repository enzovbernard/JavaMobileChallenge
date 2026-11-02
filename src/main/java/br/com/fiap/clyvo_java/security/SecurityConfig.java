package br.com.fiap.clyvo_java.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
        http.securityMatcher("/api/**")
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(req -> req
                .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/animais", "/api/responsaveis").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/animais/**", "/api/responsaveis/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/animais/**", "/api/responsaveis/**").hasRole("ADMIN")
                .anyRequest().denyAll())
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain filtrar(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(req -> req
                .requestMatchers("/css/**", "/js/**", "/images/**", "/acesso_negado").permitAll()
                .requestMatchers("/veterinarios/**", "/doencas/**", "/vacinas/**").hasRole("ADMIN")
                .anyRequest().authenticated())
            .formLogin(login -> login
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
                .failureUrl("/login?falha=true")
                .permitAll())
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll())
            .exceptionHandling(exception -> exception
                .accessDeniedHandler((request, response, accessDeniedException)
                    -> response.sendRedirect("/acesso_negado")));
        return http.build();
    }
}