package com.tik.server.repository

import com.tik.server.entity.Resume
import jdk.jfr.Enabled
import org.springframework.data.jpa.repository.JpaRepository

interface ResumeRepository : JpaRepository<Resume, Int> {
    fun findByIdAndEnabled(resumeId: Int, enabled: Boolean): Resume

    fun findAllByMemberId(memberId: Int): List<Resume>

    fun findAllByMemberIdAndEnabled(memberId: Int, enabled: Boolean): List<Resume>
}