package com.openbanking.config;

import com.openbanking.model.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/auth/login", "/api/auth/register", "/api/reconciliation-manage/gw-iconnect").permitAll()

//                .antMatchers("/api/account/list").hasAuthority("ADMIN") // Chỉ cho phép ADMIN
//                .antMatchers("/api/account/get").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/account/create").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/account/reset-password").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/account/update").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/account/delete").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/account-type/list").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/account-type/get").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/account-type/update").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/account-type/create").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/account-type/delete").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/customer/list").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/customer/create").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/customer/update").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/customer/get-by-id").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/customer/delete").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/partner/list").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/partner/create").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/partner/update").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/partner/delete").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/partner/get").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/reconciliation-manage/list").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/transaction-manage/list").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/transaction-manage/get").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/reconciliation/create").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/reconciliation/update").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/reconciliation/get").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/reconciliation/delete").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/reconciliation/list").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/source/create").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/source/update").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/source/delete").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/source/get").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/source/list").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/transaction/list").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/transaction/create").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/transaction/update").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/transaction/get").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền
//                .antMatchers("/api/transaction/list").hasAnyAuthority("USER", "ADMIN") // USER hoặc ADMIN đều có quyền

                .anyRequest().authenticated();

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
