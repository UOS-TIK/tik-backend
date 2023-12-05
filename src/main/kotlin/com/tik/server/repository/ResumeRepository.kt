package com.tik.server.repository

import com.tik.server.entity.Resume
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ResumeRepository : JpaRepository<Resume, Int> {
    fun findByIdAndEnabled(resumeId: Int, enabled: Boolean): Optional<Resume>

    fun findAllByMemberId(memberId: Int): List<Resume>

    fun findAllByMemberIdAndEnabledOrderByIdDesc(memberId: Int, enabled: Boolean): List<Resume>
}