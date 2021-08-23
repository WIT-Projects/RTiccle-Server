package com.rticcle.server.config

import com.rticcle.server.config.auth.CustomOauth2UserService
import com.rticcle.server.config.auth.OAuthSuccessHandler
import com.rticcle.server.domain.user.Role
import com.rticcle.server.security.JWTTokenProvider
import com.rticcle.server.security.JwtAuthenticationFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableWebSecurity
class SecurityConfig() : WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var customOauth2UserService: CustomOauth2UserService

    @Autowired
    private lateinit var oAuthSuccessHandler: OAuthSuccessHandler

    @Autowired
    private lateinit var jwtTokenProvider: JWTTokenProvider

    // Configure Http Security
    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .headers().frameOptions().disable()
            .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT 인증을 할 거라서 세션은 사용 x
            .and()
                .authorizeRequests()
                .antMatchers("/api/v1/**", "/token/**").hasRole(Role.USER.toString())
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
            .and()
                .logout().logoutSuccessUrl("/")
            .and()
                .oauth2Login()
                    .successHandler(oAuthSuccessHandler)
                    .userInfoEndpoint().userService(customOauth2UserService)

        http.
            addFilterBefore(
                JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter::class.java
            )
    }
}