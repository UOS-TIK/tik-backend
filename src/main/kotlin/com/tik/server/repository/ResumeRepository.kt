package com.tik.server.repository

import com.tik.server.entity.Resume
import org.springframework.data.jpa.repository.JpaRepository

interface ResumeRepository : JpaRepository<Resume, Int> {
    fun findAllByMemberId(memberId: Int): List<Resume>
}