package com.openbanking.config;

import com.openbanking.model.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

//    @Autowired
//    @Qualifier("customAuthenticationEntryPoint")
//    private AuthenticationEntryPoint authEntryPoint;

//    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, AuthorizationEntryPoint customAuthenticationEntryPoint) {
//        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
//        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
//    }

    private static final Map<String, String> endpointPermissions = new HashMap<>();

    static {
        endpointPermissions.put("/api/auth/login", null);
        endpointPermissions.put("/api/auth/register", null);
        endpointPermissions.put("/api/reconciliation-manage/gw-iconnect", null);

        // Account endpoints
        endpointPermissions.put("/api/account/list", "account:system:account:view");
        endpointPermissions.put("/api/account/get", "account:system:account:view");
        endpointPermissions.put("/api/account/create", "account:system:account:create");
        endpointPermissions.put("/api/account/reset-password", "account:system:password");
        endpointPermissions.put("/api/account/update", "account:system:account:edit");
        endpointPermissions.put("/api/account/delete", "account:system:account:delete");

        // Account Type endpoints
        endpointPermissions.put("/api/account-type/list", "account:system:permission:view");
        endpointPermissions.put("/api/account-type/get", "account:system:permission:view");
        endpointPermissions.put("/api/account-type/update", "account:system:permission:edit");
        endpointPermissions.put("/api/account-type/create", "account:system:permission:create");
        endpointPermissions.put("/api/account-type/delete", "account:system:permission:delete");

        // Customer endpoints
        endpointPermissions.put("/api/customer/list", "account:customer:view");
        endpointPermissions.put("/api/customer/create", "account:customer:create");
        endpointPermissions.put("/api/customer/update", "account:customer:edit");
        endpointPermissions.put("/api/customer/get-by-id", "account:customer:view");
        endpointPermissions.put("/api/customer/delete", "account:customer:delete");

        // Partner endpoints
        endpointPermissions.put("/api/partner/list", "account:partner:view");
        endpointPermissions.put("/api/partner/create", "account:partner:create");
        endpointPermissions.put("/api/partner/update", "account:partner:edit");
        endpointPermissions.put("/api/partner/delete", "account:partner:delete");
        endpointPermissions.put("/api/partner/get", "account:partner:view");

        // Reconciliation endpoints
        endpointPermissions.put("/api/reconciliation/create", "account:configuration:auto:create");
        endpointPermissions.put("/api/reconciliation/update", "account:configuration:auto:edit");
        endpointPermissions.put("/api/reconciliation/get", "account:configuration:auto:view");
        endpointPermissions.put("/api/reconciliation/delete", "account:configuration:auto:delete");
        endpointPermissions.put("/api/reconciliation/list", "account:configuration:auto:view");

        // Source endpoints
        endpointPermissions.put("/api/source/create", "account:configuration:source:create");
        endpointPermissions.put("/api/source/update", "account:configuration:source:update");
        endpointPermissions.put("/api/source/delete", "account:configuration:source:delete");
        endpointPermissions.put("/api/source/get", "account:configuration:source:view");
        endpointPermissions.put("/api/source/list", "account:configuration:source:view");

        // Transaction endpoints
        endpointPermissions.put("/api/transaction/create", "account:configuration:content:create");
        endpointPermissions.put("/api/transaction/update", "account:configuration:content:edit");
        endpointPermissions.put("/api/transaction/delete", "account:configuration:content:delete");
        endpointPermissions.put("/api/transaction/get", "account:configuration:content:view");
        endpointPermissions.put("/api/transaction/list", "account:configuration:content:view");
        endpointPermissions.put("/api/transaction-manage/list", "account:transaction:view");
        endpointPermissions.put("/api/transaction-manage/get", "account:transaction:view");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests(auth -> {
                    endpointPermissions.forEach((endpoint, permission) -> {
                        if (permission == null) {
                            auth.antMatchers(endpoint).permitAll();
                        } else {
                            auth.antMatchers(endpoint).hasAuthority(permission);
                        }
                    });
                    auth.anyRequest().authenticated();
                });

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
