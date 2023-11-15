package com.tik.server.security

import org.springframework.boot.web.servlet.FilterRegistrationBean
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
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import org.springframework.web.servlet.handler.HandlerMappingIntrospector


// 인증 인가 관리 config
@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider)
{
/*
    @Bean
    fun corsFilterRegistrationBean(): FilterRegistrationBean<CorsFilter> {
        // CORS 설정을 위한 객체 생성
        val config = CorsConfiguration()

        // CORS 설정 값 지정
        config.allowCredentials = true
        config.addAllowedOrigin("http://localhost:3000")
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")
        config.maxAge = 6000L

        // CORS 설정을 등록할 소스 생성
        val source = UrlBasedCorsConfigurationSource()

        // 소스에 CORS 설정 등록
        source.registerCorsConfiguration("/**", config)

        // CORS 필터를 가지는 FilterRegistrationBean 객체 생성
        val filterBean: FilterRegistrationBean<CorsFilter> = FilterRegistrationBean<CorsFilter>(CorsFilter(source))

        // 필터의 순서 설정
        filterBean.setOrder(0)

        // FilterRegistrationBean 객체 반환
        return filterBean
    }
**/

 */
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
            .authorizeHttpRequests {
    // 해당 url로 접근하는 사용자는 인증되지 않은 사용자 여아함
                it.requestMatchers(MvcRequestMatcher(introspector, "/user/signUp")).anonymous()
                    .requestMatchers(MvcRequestMatcher(introspector, "/user/signIn")).anonymous()
    // 그외 /resume로 시작하는 모든 요청은 MEMBER 권한이 있어야 접근 가능
                    .requestMatchers(MvcRequestMatcher(introspector, "/resume/**")).hasRole("MEMBER")
                    .requestMatchers(MvcRequestMatcher(introspector, "/interview/**")).hasRole("MEMBER")
                    .requestMatchers(MvcRequestMatcher(introspector, "/history/**")).hasRole("MEMBER")

                    // 그외 요청은 권한 없이 모두 접근 가능
                    .anyRequest().permitAll()
            }
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