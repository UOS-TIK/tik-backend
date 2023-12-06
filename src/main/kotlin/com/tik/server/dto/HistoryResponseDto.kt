package com.tik.server.dto

import com.tik.server.entity.InterviewHistory
import com.tik.server.entity.QuestionView
import java.time.LocalDateTime

data class HistoryResponseList (
    val interviewHistoryId: Int,
    val interviewName: String?,
    val company: String?,
    val beginTime: LocalDateTime?
) {
    companion object {
        fun from(interviewHistory: InterviewHistory): HistoryResponseList {
            return HistoryResponseList(
                interviewHistoryId = interviewHistory.id,
                interviewName = interviewHistory.interviewName,
                company = interviewHistory.company,
                beginTime = interviewHistory.beginTime
            )
        }
    }
}
/*
    data class HistoryResponse(
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
            fun from(interviewHistory: InterviewHistory): HistoryResponse {
                return HistoryResponse(
                    interviewHistoryId = interviewHistory.id,
                    resume = interviewHistory.resume.name,
                    jobDescription = interviewHistory.jobDescription,
                    company = interviewHistory.company,
                    script = interviewHistory.script,
                    comment = interviewHistory.comment,
                    beginTime = interviewHistory.beginTime,
                    endTime = interviewHistory.endTime,
                )
            }
        }
    }
*/
    data class HistoryResponseView(
        val interviewHistoryId: Int,
        val resume: Int?,
        val jobDescription: String?,
        val resumeQuestion: Int?,
        val jdQuestion: Int?,
        val csQuestion: Int?,
        val score: Int?,
        val interviewName: String?,
        val company: String?,
        val script: String?,
        val comment: String?,
        val beginTime: LocalDateTime?,
        val endTime: LocalDateTime?,
        val question : List<List<QuestionView>>?
    )
