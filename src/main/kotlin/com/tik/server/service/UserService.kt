package com.tik.server.service

import com.tik.server.dto.UserRequestDto
import com.tik.server.entity.User
import com.tik.server.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Transactional
@Service
class UserService(private val userRepository: UserRepository) {
    fun signUp(userRequestDto: UserRequestDto): String {
        var user: User? = userRepository.findByEmail(userRequestDto.email)
        if(user != null) return "이미 존재하는 이메일 입니다."
        user = User(
            null,
            userRequestDto.email,
            userRequestDto.password,
            userRequestDto.userName
        )
        userRepository.save(user)
        return "회원가입이 완료되었습니다."
    }
}