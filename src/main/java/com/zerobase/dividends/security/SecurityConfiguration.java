package com.zerobase.dividends.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 권한 체크 annotation
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Http Security
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity)
        throws Exception {
        httpSecurity
            .httpBasic().disable()
            .csrf().disable()
            .cors().disable()
            .sessionManagement().sessionCreationPolicy(
                SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/**/singup", "/**/signin")
            .permitAll()
            .and()
            .addFilterBefore(this.jwtAuthenticationFilter
                , UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    /**
     * Web Security 해당 uri 요청에 대한 인증정보는 제외
     * 개발 관련된 uri 를 선언해서 구분을 둔다.
     * filterChain에 구현된 antMatchers 를 넣어도 된다.
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/h2-console/**");
    }
}
