package com.rticcle.server.config

import com.rticcle.server.config.auth.CustomOAuth2UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter


@Configuration
@EnableWebSecurity
class SecurityConfig: WebSecurityConfigurerAdapter() {
    @Autowired
    private val customOAuth2UserService: CustomOAuth2UserService? = null

    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .antMatcher("/**")
            .authorizeRequests()
            .antMatchers("/", "/h2-console/**", "/favicon.ico", "/login**").permitAll() //권한 허용
            .anyRequest().authenticated()
            .and().logout().logoutSuccessUrl("/").permitAll()
            .and().headers().frameOptions().sameOrigin()
            .and().oauth2Login().userInfoEndpoint().userService(customOAuth2UserService);
    }
}