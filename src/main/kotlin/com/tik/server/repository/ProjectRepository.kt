package com.tik.server.repository

import com.tik.server.entity.Project
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ProjectRepository : JpaRepository<Project, Int> {
    fun findAllByResumeId(resumeId: Int): List<Project>

    @Modifying
    @Query("delete from Project p where p in :projects")
    fun deleteAllByIds(@Param("projects") projects: List<Int?>)
}