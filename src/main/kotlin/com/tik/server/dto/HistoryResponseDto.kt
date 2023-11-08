package com.tik.server.dto

import com.tik.server.entity.InterviewHistory
import com.tik.server.entity.Resume
import java.time.LocalDateTime

data class HistoryResponseList (
    val interviewHistoryId: Int,
    val company: String,
    val beginTime: LocalDateTime?
) {
    companion object {
        fun from(interviewHistory: InterviewHistory): HistoryResponseList {
            return HistoryResponseList(
                interviewHistoryId = interviewHistory.id,
                company = interviewHistory.company,
                beginTime = interviewHistory.beginTime
            )
        }
    }
}
    data class HistoryResponseView(
        val interviewHistoryId: Int,
        val resume: String,
        val jobDescription: String,
        val company: String,
        val script: String?,
        val comment: String?,
        val beginTime: LocalDateTime?,
        val endTime: LocalDateTime?
    ) {
        companion object {
            fun from(interviewHistory: InterviewHistory): HistoryResponseView {
                return HistoryResponseView(
                    interviewHistoryId = interviewHistory.id,
                    resume = interviewHistory.resume.name,
                    jobDescription = interviewHistory.jobDescription,
                    company = interviewHistory.company,
                    script = interviewHistory.script,
                    comment = interviewHistory.comment,
                    beginTime = interviewHistory.beginTime,
                    endTime = interviewHistory.endTime
                )
            }
        }
    }
