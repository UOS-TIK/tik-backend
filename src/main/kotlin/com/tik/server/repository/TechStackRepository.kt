package com.tik.server.repository

import com.tik.server.entity.TechStack
import org.springframework.data.jpa.repository.JpaRepository

interface TechStackRepository : JpaRepository<TechStack, Long> {
    fun findByNameContaining(text: String): List<TechStack>
}