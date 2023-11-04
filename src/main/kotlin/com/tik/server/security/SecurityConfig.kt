package com.tik.server.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher
import org.springframework.web.servlet.handler.HandlerMappingIntrospector

@Configuration
@EnableWebSecurity
class SecurityConfig(private val jwtTokenProvider: JwtTokenProvider) {

    // 필터 설정
    @Bean
    fun filterChain(http: HttpSecurity, introspector: HandlerMappingIntrospector): SecurityFilterChain {
        http
    // httpBasic, csrf 사용안함 처리
            .httpBasic { it.disable() }
            .csrf{ it.disable() }
    // jwt를 사용하기 때문에 session 사용안함 처리
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
    // 권한 관리
//            .authorizeHttpRequests {
//    // 해당 url로 접근하는 사용자는 인증되지 않은 사용자 여아함
//                it.requestMatchers(MvcRequestMatcher(introspector, "/user/signUp")).anonymous()
//                    .requestMatchers(MvcRequestMatcher(introspector, "/user/signIn")).anonymous()
//    // 그외 /resume로 시작하는 모든 요청은 MEMBER 권한이 있어야 접근 가능
//                    .requestMatchers(MvcRequestMatcher(introspector, "/resume/**")).hasRole("MEMBER")
//    // 그외 요청은 권한 없이 모두 접근 가능
//                    .anyRequest().permitAll()
//            }

    // JWTAuthenticationFilter가 UsernamePasswordAuthenticationFilter보다 먼저 실행
    // 앞의 필터가 성공하면 뒤 필터는 시행 x
            .addFilterBefore(
                JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter::class.java
            )

        return http.build()
    }


// 비밀번호 암호화

    @Bean
    fun passwordEncoder(): PasswordEncoder =
        PasswordEncoderFactories.createDelegatingPasswordEncoder()
}