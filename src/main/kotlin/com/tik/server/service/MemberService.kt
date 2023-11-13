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
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Transactional
@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val memberRoleRepository: MemberRoleRepository,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider,
) {
    fun signUp(memberRequestDto: MemberRequestDto): String {
        var member: Member? = memberRepository.findByUid(memberRequestDto.uid)
        if(member != null) {
            throw InvalidInputException("uid", "이미 존재하는 아이디입니다.")
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
    fun signIn(signInDto: SignInDto): TokenInfo {   // signindto로 정보 받아 tokeninfo로 발행한 토큰  정보 넘겨줌



        val authenticationToken = UsernamePasswordAuthenticationToken(signInDto.uid, signInDto.password)
        // 이메일과 패스워드를 사용하여 usernamepasswordauthenticationtoken을 발행
        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)
        // managerbuilder에 토큰을 전달, authenticate가 실행되면서 CustomUserDetailsService의 loadUserByUsername가 호출되면서
        // DB에 존재하는 멤버정보와 비교
        return jwtTokenProvider.createToken(authentication) // 정상적으로 실행시 해당 정보로 토큰을 발행하고 사용자에게 돌려줌
    }

}