package io.joggr.aaa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                     .antMatchers(
                             "/users/sign-up/**",
                             "/",
                             "/index.html", "/*.js", "/*.js.map",
                             "/*.svg", "/*.css", "/*.woff", "/*.woff2", "/*.eot", "/*.ttf"
                     ).permitAll()
                .and().authorizeRequests()
                    .antMatchers("/manage/**").access("hasIpAddress('127.0.0.1') or hasIpAddress('[::1]')")
                .and().authorizeRequests()
                    .anyRequest().authenticated()
                .and()
                    .httpBasic()
                .and()
                    .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        ;
    }

};