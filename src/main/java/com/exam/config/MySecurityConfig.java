// package com.exam.config;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.AuthenticationProvider;
// import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.core.userdetails.UserDetailsService;

// import org.springframework.security.crypto.password.NoOpPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// import com.exam.service.impl.UserDetailsServiceImpl;

// @Configuration
// @EnableMethodSecurity(prePostEnabled = true)
// public class MySecurityConfig extends WebSecurityConfigurerAdapter {
//     @Autowired
//     private JwtAuthenticationEntryPoint unauthorizedHandler;

//     @Autowired
//     private JwtAuthenticationFilter jwtAuthenticationFilter;

//     @Autowired
//     private UserDetailsServiceImpl userDetailsServiceImpl;
    

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         // return new BCryptPasswordEncoder();
//         return NoOpPasswordEncoder.getInstance();
//     }

//     @Bean
//     public AuthenticationProvider authenticationProvider() {
//         DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//         provider.setUserDetailsService(userDetailsServiceImpl);
//         provider.setPasswordEncoder(passwordEncoder());
//         return provider;
//     }

//     @Bean
//     public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//         return config.getAuthenticationManager();
//     }

//     // @Bean
//     // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//     //     return http
//     //         .csrf(csrf -> csrf.disable())
//     //         .authorizeHttpRequests(authz -> authz
//     //             .requestMatchers("/generate-token", "/user/**").permitAll()
//     //             .requestMatchers(HttpMethod.OPTIONS).permitAll()
//     //             .anyRequest().authenticated()
//     //         )
//     //         .exceptionHandling(e -> e.authenticationEntryPoint(unauthorizedHandler))
//     //         .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//     //         .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//     //         .build();
//     // }

//     // @Override
//     // protected void configue(HttpSecurity http) throws Exception {
//     //     http
//     //         .csrf()
//     //         .disable()
//     //         .cors()
//     //         .disable()
//     //         .authorizeRequests()
//     //         .antMatchers("/generate-token", "/user/").permitAll()
//     //         .antMatchers(HttpMethod.OPTIONS).permitAll()
//     //         .anyRequest().authenticated()
//     //         .and()
//     //         .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
//     //         .and()
//     //         .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

//     //         http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//     // }

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

//         http
//                 .csrf(csrf -> csrf.disable())
//                 .cors(cors -> cors.disable())
//                 .authorizeHttpRequests(auth -> auth
//                         .requestMatchers("/generate-token", "/user/").permitAll()
//                         .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                         .anyRequest().authenticated())
//                 .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))
//                 .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

//         return http.build();
//     }

// }

package com.exam.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.exam.service.impl.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class MySecurityConfig {

    @Autowired
    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    @Autowired
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    public MySecurityConfig(
            JwtAuthenticationEntryPoint unauthorizedHandler,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            UserDetailsServiceImpl userDetailsServiceImpl) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
        // Recommended:
        // return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsServiceImpl);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("JWT Filter Executed");
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/generate-token", "/user/").permitAll()
                        //.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
