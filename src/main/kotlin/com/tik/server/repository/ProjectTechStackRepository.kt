package com.tik.server.repository

import com.tik.server.entity.ProjectTechStack
import org.springframework.data.jpa.repository.JpaRepository

interface ProjectTechStackRepository : JpaRepository<ProjectTechStack, Int> {
}