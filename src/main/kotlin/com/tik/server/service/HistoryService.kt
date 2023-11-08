package com.tik.server.service

import com.tik.server.dto.HistoryResponseList
import com.tik.server.dto.HistoryResponseView
import com.tik.server.entity.InterviewHistory
import com.tik.server.entity.Resume
import com.tik.server.repository.InterviewHistoryRepository
import com.tik.server.repository.ResumeRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class HistoryService (
    private val resumeRepository: ResumeRepository,
    private val interviewHistoryRepository: InterviewHistoryRepository
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
        return HistoryResponseView.from(interviewHistoryRepository.findByIdOrNull(interviewHistoryId)!!)
    }

    fun deleteHistory(interviewHistoryId: Int) {
        return interviewHistoryRepository.deleteById(interviewHistoryId)
    }

}