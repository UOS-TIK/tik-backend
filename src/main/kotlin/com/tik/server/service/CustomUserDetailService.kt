package com.tik.server.service

import com.tik.server.dto.CustomUser
import com.tik.server.dto.MemberRequestDto
import com.tik.server.dto.SignInDto
import com.tik.server.entity.Member
import com.tik.server.repository.MemberRepository
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

// userdetailservice를 구현
@Service
class CustomUserDetailService (
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails =
        memberRepository.findByEmail(username)
            // 불러온 암호화된 객체와 입력한 비암호화 객체(signInDto.password) 비교..
            ?.let { createUserDetails(it) }
            ?: throw UsernameNotFoundException("해당유저는 존재하지 않습니다.") // ExceptionHanlder의 BadCredentialsException해당
    // 사용자의 이메일로 사용자가 존재하는지 확인 후 존재하면 createUserDetails함수 실행하여 CustomUser 인스턴스를 userDetails로 반환
    private fun createUserDetails(member: Member): UserDetails =
        CustomUser(
            member.id!!,
            member.email,
            passwordEncoder.encode(member.password),
            member.memberRole!!.map {SimpleGrantedAuthority("ROLE_${it.role}")}
        )

}