package com.tik.server.repository

import com.tik.server.entity.InterviewHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InterviewHistoryRepository: JpaRepository<InterviewHistory, Int> {
}