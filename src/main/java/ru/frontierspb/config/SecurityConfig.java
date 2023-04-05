package ru.frontierspb.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public SecurityFilterChain configure(HttpSecurity http)
            throws Exception {
        http.csrf().disable()// TODO потом убрать, это для работы postman
            .authorizeHttpRequests()
            .requestMatchers("/api/docs/**").permitAll()
            .requestMatchers("/api/sign-in/**").permitAll()
            .requestMatchers("/api/sign-up/**").permitAll()
            .requestMatchers("/api/data/**").permitAll()
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            .requestMatchers("/api/customer/**").hasAnyRole("ADMIN", "USER")
            .and()
            /* Хоть я и настроил sign-out здесь, все равно не понимаю как лучше его реализовать. Почему не в
            отдельном контроллере? */
            .logout(logout -> logout.logoutUrl("/api/sign-out")
                                    .invalidateHttpSession(true)
                                    .deleteCookies("JSESSIONID")
                                    .logoutRequestMatcher(new AntPathRequestMatcher("/api/sign-out", "POST"))
                                    .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                                    .permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // TODO Поменять
    }
}
        /*
        .formLogin(form -> form.defaultSuccessUrl("/api", true).failureUrl("/login?error=true"))
        .usernameParameter("username")
        .passwordParameter("password")
        .loginProcessingUrl("/process-login")
        */
