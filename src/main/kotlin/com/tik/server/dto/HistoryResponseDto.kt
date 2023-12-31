package com.tik.server.dto

import com.tik.server.entity.InterviewHistory
import com.tik.server.entity.QuestionView
import java.time.LocalDateTime

data class HistoryResponseList (
    val interviewHistoryId: Int,
    val interviewName: String?,
    val company: String?,
    val beginTime: LocalDateTime?,
    val endTime: LocalDateTime?,
    val score: Int?,
    val comment: String?,
    val resumeId: Int?
) {
    companion object {
        fun from(interviewHistory: InterviewHistory): HistoryResponseList {
            return HistoryResponseList(
                interviewHistoryId = interviewHistory.id,
                interviewName = interviewHistory.interviewName,
                company = interviewHistory.company,
                beginTime = interviewHistory.beginTime,
                endTime = interviewHistory.endTime,
                score = interviewHistory.score,
                comment = interviewHistory.comment,
                resumeId = interviewHistory.resume.id
            )
        }
    }
}

    data class HistoryResponseView(
        val interviewHistoryId: Int,
        val occupation: String?,
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
