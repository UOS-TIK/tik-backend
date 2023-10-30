package com.tik.server.service

import com.tik.server.client.LlmClient
import com.tik.server.dto.InterviewAnswerRequest
import com.tik.server.dto.InterviewCreateRequest
import com.tik.server.dto.InterviewCreateResponse
import com.tik.server.dto.InterviewQuestion
import com.tik.server.entity.InterviewHistory
import com.tik.server.repository.InterviewHistoryRepository
import com.tik.server.repository.ResumeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InterviewService(
    private val llmClient: LlmClient,
    private val resumeRepository: ResumeRepository,
    private val interviewHistoryRepository: InterviewHistoryRepository,
) {
    @Transactional
    suspend fun initInterview(request: InterviewCreateRequest): InterviewCreateResponse {
        val resume = resumeRepository.findById(request.resumeId.toInt()).orElse(null)
            ?: throw IllegalStateException("이력서가 존재하지 않습니다.")

        var techStack: String = resume.name + "\n" + resume.introduction + "\n"
        resume.project.forEach {
            techStack += it.name + "\n" + it.summary + "\n" + it.description + "\n"
        }

        val interviewHistory = interviewHistoryRepository.save(
            InterviewHistory(
                resume = resume,
                jobDescription = request.jobDescription,
                company = request.company
            )
        )

        val response = llmClient.initInterview(
            body = LlmClient.InitInterview.Body(
                interviewId = interviewHistory.id,
                techStack = arrayListOf(techStack),
                jobDescription = arrayListOf(request.jobDescription),
                options = LlmClient.InitInterview.Options(
                    questionCount = request.options.questionCount
                )
            )
        )
        return if (response.data != null) {
            InterviewCreateResponse(
                interviewId = response.data.interviewId
            )
        } else {
            throw IllegalStateException(response.error?.message)
        }
    }

    suspend fun speakToInterviewer(request: InterviewAnswerRequest): InterviewQuestion {
        val response = llmClient.speakToInterviewer(
            body = LlmClient.SpeakToInterviewer.Body(
                interviewId = request.interviewId.toInt(),
                message = request.message
            )
        )
        return if (response.data != null) {
            InterviewQuestion(
                reply = response.data.reply
            )
        } else {
            // todo: error=interview finished 상태일 때 처리
            throw IllegalStateException(response.error?.message)
        }
    }
}