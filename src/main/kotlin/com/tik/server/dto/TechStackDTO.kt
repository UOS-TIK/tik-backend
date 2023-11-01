package com.tik.server.dto

import com.tik.server.entity.TechStack

data class TechStackDTO(
    val id: Int,
    val name: String
) {
    companion object {
        fun from(techStack: TechStack): TechStackDTO {
            return TechStackDTO(
                id = techStack.id,
                name = techStack.name
            )
        }
    }
}
