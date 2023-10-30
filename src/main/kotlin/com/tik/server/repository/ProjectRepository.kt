package com.tik.server.repository

import com.tik.server.entity.Project
import org.springframework.data.jpa.repository.JpaRepository

interface ProjectRepository : JpaRepository<Project, Int> {
}