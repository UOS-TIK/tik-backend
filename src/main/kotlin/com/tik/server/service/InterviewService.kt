package com.tik.server.service

import com.tik.server.client.LlmClient
import com.tik.server.dto.*
import com.tik.server.entity.InterviewHistory
import com.tik.server.entity.Question
import com.tik.server.repository.InterviewHistoryRepository
import com.tik.server.repository.ResumeRepository
import kotlinx.coroutines.Dispatchers.IO
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
        val resume = withContext(IO) {
            resumeRepository.findById(request.resumeId.toInt()).orElse(null)
                ?: throw IllegalStateException("이력서가 존재하지 않습니다.")
        }

        var techStack: String = resume.name + "\n" + resume.introduction + "\n"
        resume.project.forEach {
            techStack += it.name + "\n" + it.summary + "\n" + it.description + "\n"
        }

        val interviewHistory = withContext(IO) {
            interviewHistoryRepository.save(
                InterviewHistory(
                    resume = resume,
                    jobDescription = request.jobDescription,
                    interviewName = request.interviewName,
                    company = request.company,
                    resumeQuestion = request.options.resumeQuestion,
                    jdQuestion = request.options.jdQuestion,
                    csQuestion = request.options.csQuestion
                )
            )
        }

        val response = llmClient.initInterview(
            body = LlmClient.InitInterview.Body(
                interviewId = interviewHistory.id,
                techStack = arrayListOf(techStack),
                jobDescription = arrayListOf(request.jobDescription),
                options = LlmClient.InitInterview.Options(
                    resumeQuestion = request.options.resumeQuestion,
                    jdQuestion = request.options.jdQuestion,
                    csQuestion = request.options.csQuestion
                )
            )
        )
        return if (response.data != null) {
            InterviewCreateResponse(
                interviewId = response.data.interviewId
            )
        } else {
            // todo: 시나리오 별 에러 처리
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
                reply = response.data.reply,
                isFinished = response.data.isFinished
            )
        } else {
            throw IllegalStateException(response.error?.message)
        }
    }

    @Transactional
    fun abortInterview(interviewId: Int): FinishInterviewResponse {
        interviewHistoryRepository.findById(interviewId)
            .map {
                if (it.endTime != null)
                    throw IllegalStateException("이미 종료된 면접입니다.")
                interviewHistoryRepository.delete(it)
            }
            .orElseThrow { throw IllegalStateException("유효한 면접이 아닙니다.") }
        return FinishInterviewResponse(status = "중단")
    }

    @Transactional
    suspend fun finishInterview(request: FinishInterviewRequest): FinishInterviewResponse {
        val interviewHistory = withContext(IO) {
            interviewHistoryRepository.findById(request.interviewId)
        }.filter {
            it.comment == null
        }.orElseThrow {
            throw Exception("invalid interviewId.")
        }

        if (interviewHistory.endTime == null) {
            interviewHistory.endTime = LocalDateTime.now()
            withContext(IO) { interviewHistoryRepository.save(interviewHistory) }
        }

        val res = llmClient.finishInterview(
            body = LlmClient.FinishInterview.Body(
                interviewId = request.interviewId
            )
        )

        if (res.data == null || res.error != null) {
            when (res.error) {
                LlmClient.FinishInterview.Exception.INTERVIEW_LOCKED -> return FinishInterviewResponse(status = "처리중")
                else -> throw Exception("invalid interviewId.")
            }
        }

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

        withContext(IO) { interviewHistoryRepository.save(interviewHistory) }

        return FinishInterviewResponse(status = "완료")
    }
}