package com.tik.server.service

import com.tik.server.dto.HistoryResponseList
import com.tik.server.dto.HistoryResponseView
import com.tik.server.entity.Resume
import com.tik.server.repository.InterviewHistoryRepository
import com.tik.server.repository.QuestionRepository
import com.tik.server.repository.ResumeRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class HistoryService (
    private val resumeRepository: ResumeRepository,
    private val interviewHistoryRepository: InterviewHistoryRepository,
    private val questionRepository: QuestionRepository
    ) {
    fun searchHistoryList(memberId: Int): List<HistoryResponseList> {
        val resume: List<Resume> = resumeRepository.findAllByMemberId(memberId)
        return resume.flatMap {
            interviewHistoryRepository.findAllByResumeId(it.id!!).map() {
                HistoryResponseList.from(it)
            }
        }
    }

    fun searchHistoryView(interviewHistoryId: Int): HistoryResponseView {
        val interviewHistory = interviewHistoryRepository.findByIdOrNull(interviewHistoryId)!!
        val questions = questionRepository.findViewByInterviewHistoryId(interviewHistoryId)

        return HistoryResponseView(
            interviewHistoryId = interviewHistory.id,
            resume = interviewHistory.resume.name,
            jobDescription = interviewHistory.jobDescription,
            company = interviewHistory.company,
            script = interviewHistory.script,
            comment = interviewHistory.comment,
            beginTime = interviewHistory.beginTime,
            endTime = interviewHistory.endTime,
            question = listOf(questions)

        )


    }

    fun deleteHistory(interviewHistoryId: Int) {
        return interviewHistoryRepository.deleteById(interviewHistoryId)
    }

}