package com.tik.server.service

import com.tik.server.client.LlmClient
import com.tik.server.dto.UserRequestDto
import com.tik.server.entity.User
import com.tik.server.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Transactional
@Service
class UserService(
    private val userRepository: UserRepository,
    private val llmClient: LlmClient
) {
    fun signUp(userRequestDto: UserRequestDto): String {
        var user: User? = userRepository.findByEmail(userRequestDto.email)
        if (user != null) return "이미 존재하는 이메일 입니다."
        user = User(
            null,
            userRequestDto.email,
            userRequestDto.password,
            userRequestDto.userName
        )
        userRepository.save(user)
        return "회원가입이 완료되었습니다."
    }

    suspend fun example(): Int {
        val res1 = llmClient.speakToInterviewer(
            body = LlmClient.SpeakToInterviewer.Body(
                interviewId = 3782,
                message = "wqe"
            )
        )
        if (res1.error == LlmClient.SpeakToInterviewer.Exception.INTERVIEW_NOT_INITED) {
            println(res1.error.message)
        }

        val res2 = llmClient.finishInterview(
            body = LlmClient.FinishInterview.Body(
                interviewId = 0
            )
        )
        if (res2.data != null) {
            println(res2.data.interviewHistory)
        }

        return 1
    }
}