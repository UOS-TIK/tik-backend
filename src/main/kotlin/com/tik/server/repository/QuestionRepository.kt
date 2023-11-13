package com.tik.server.repository

import com.tik.server.entity.Question
import com.tik.server.entity.QuestionView
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface QuestionRepository : JpaRepository<Question, Int> {

    fun findAllByInterviewHistoryId(interviewHistoryId: Int): List<Question>
    fun findViewByInterviewHistoryId(interviewHistoryId: Int): List<QuestionView>
}