package com.tik.server.service

import com.tik.server.security.JwtTokenProvider
import com.tik.server.common.ROLE
import com.tik.server.dto.MemberRequestDto
import com.tik.server.dto.SignInDto
import com.tik.server.entity.Member
import com.tik.server.entity.MemberRole
import com.tik.server.exception.InvalidInputException
import com.tik.server.repository.MemberRepository
import com.tik.server.repository.MemberRoleRepository
import com.tik.server.security.TokenInfo
import jakarta.transaction.Transactional
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.stereotype.Service

@Transactional
@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val memberRoleRepository: MemberRoleRepository,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider
) {
    fun signUp(memberRequestDto: MemberRequestDto): String {
        var member: Member? = memberRepository.findByEmail(memberRequestDto.email)
        if(member != null) {
            throw InvalidInputException("email", "이미 등록된 email입니다")
        }

        member = memberRequestDto.toEntity()
        memberRepository.save(member)

        val memberRole: MemberRole = MemberRole(null, ROLE.MEMBER, member)
        memberRoleRepository.save(memberRole)

        return "회원가입이 완료되었습니다."
    }

    /**
     *  로그인 -> 토큰 발행
     */
    fun signIn(signInDto: SignInDto): TokenInfo {
        val authenticationToken = UsernamePasswordAuthenticationToken(signInDto.email, signInDto.password)
        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)

        return jwtTokenProvider.createToken(authentication)
    }

}