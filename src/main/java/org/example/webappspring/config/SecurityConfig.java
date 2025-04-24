package org.example.webappspring.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationSuccessHandler customSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // Отключаем CSRF для всего приложения (НЕ рекомендуется на продакшн-сервере)
                .authorizeRequests()  // Обновлённый способ настройки авторизации
                .requestMatchers("/auth/register", "/auth/login").permitAll()  // Разрешаем доступ без аутентификации
                .requestMatchers("/admin/**").hasRole("ADMIN")  // Доступ для администраторов
                .requestMatchers("/user/**").hasRole("USER")  // Доступ для обычных пользователей
                .requestMatchers("/api/auth/**").permitAll()  // Разрешаем доступ для API
                .requestMatchers("/api/admin/**").hasRole("ADMIN")  // Доступ для админов в API
                .requestMatchers("/api/user/**").hasRole("USER")  // Доступ для пользователей в API
                .anyRequest().authenticated()  // Все остальные запросы требуют аутентификации
                .and()
                .formLogin()  // Настройка формы входа
                .loginPage("/auth/login")
                .successHandler(customSuccessHandler)  // Перенаправление после успешного входа
                .and()
                .logout()  // Настройка выхода
                .logoutUrl("/logout")
                .logoutSuccessUrl("/auth/login")
                .and()
                .exceptionHandling()  // Обработка ошибок
                .accessDeniedPage("/error");

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();  // Лучше заменить на BCrypt позже
    }
}
