package com.tik.server.security

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.filter.GenericFilterBean
// genericfilterbean을 상속받아 토큰 정보를 검사
// security contextholder에 정보를 기록
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider
) : GenericFilterBean() { // 생성자로 jwtTokenProvider를 받는 GenericFilterBean 상속받아

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val token = resolveToken(request as HttpServletRequest)  // 토큰 정보 추출

        if (token != null && jwtTokenProvider.validateToken(token)) {   // 정상적인 토큰일 경우
            val authentication = jwtTokenProvider.getAuthentication(token)     // 정보를 뽑아와
            SecurityContextHolder.getContext().authentication = authentication  //   securityContextHolder에 기록 후 사용
        }

        chain?.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? { //
        val bearerToken = request.getHeader("Authorization")    // request로 부터 헤더의 'authorization'으로 존재하는
                                                                // 문자를 가져와  bearer와 일치하는지 확인 후
        return if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            bearerToken.substring(7)                // 일치하면 키값만 추출
        } else {
            null
        }
    }
}