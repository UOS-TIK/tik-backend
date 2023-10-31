package com.tik.server.service

import com.tik.server.client.LlmClient
import com.tik.server.dto.*
import com.tik.server.entity.InterviewHistory
import com.tik.server.entity.Question
import com.tik.server.repository.InterviewHistoryRepository
import com.tik.server.repository.ResumeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

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

    suspend fun finishInterview(request: FinishInterviewRequest): FinishInterviewResponse {
        val interviewHistory = withContext(Dispatchers.IO) {
            interviewHistoryRepository.findById(request.interviewId)
        }.filter {
            // TODO: it.resume.member.id == userId 조건
            it.comment == null
        }.orElseThrow {
            throw Exception("invalid interviewId.")
        }

        val res = llmClient.finishInterview(
            body = LlmClient.FinishInterview.Body(
                interviewId = request.interviewId
            )
        )

        if (res.data == null || res.error !== null) {
            // TODO: 예외처리
            when (res.error) {
                LlmClient.FinishInterview.Exception.INTERVIEW_NOT_INITED -> throw Exception("invalid interviewId.")
                LlmClient.FinishInterview.Exception.INTERVIEW_NOT_FINISHED -> throw Exception("invalid interviewId.")
                LlmClient.FinishInterview.Exception.INVALID_ID -> throw Exception("invalid interviewId.")
                LlmClient.FinishInterview.Exception.INTERVIEW_LOCKED -> throw Exception("invalid interviewId.")
                else -> throw Exception("invalid interviewId.")
            }
        }

        interviewHistory.endTime = LocalDateTime.now()
        interviewHistory.script = res.data.interviewHistory.joinToString("__")
        interviewHistory.comment = res.data.interviewPaper.finalOneLineReview
        interviewHistory.score = res.data.interviewPaper.finalScore
        interviewHistory.questions = res.data.interviewPaper.items.map {
            Question(
                id = 0,
                question = it.question,
                answer = it.answer,
                score = it.evaluation.score,
                feedback = it.evaluation.comment,
                interviewHistory = interviewHistory,
            ).apply {
                tailQuestions = it.tailQuestions.map { each ->
                    Question(
                        id = 0,
                        question = each.question,
                        answer = each.answer,
                        score = each.evaluation.score,
                        feedback = each.evaluation.comment,
                        interviewHistory = interviewHistory,
                        parent = this,
                    )
                }.toMutableList()
            }
        }.toMutableList()

        withContext(Dispatchers.IO) {
            interviewHistoryRepository.save(interviewHistory)
        }

        return FinishInterviewResponse(interviewId = request.interviewId)
    }
}