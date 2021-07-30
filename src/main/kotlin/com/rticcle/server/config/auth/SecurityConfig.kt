package com.rticcle.server.config.auth

import com.rticcle.server.domain.user.Role
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@EnableWebSecurity
class SecurityConfig() : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var customOauth2UserService: CustomOauth2UserService

    // Configure Http Security
    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .headers().frameOptions().disable()
            .and()
            .authorizeRequests()
            .antMatchers("/api/v1/**").hasRole(Role.ROLE_USER.toString())
            .antMatchers("/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .logout().logoutSuccessUrl("/")
            .and()
            .oauth2Login().userInfoEndpoint().userService(customOauth2UserService)

    }
}