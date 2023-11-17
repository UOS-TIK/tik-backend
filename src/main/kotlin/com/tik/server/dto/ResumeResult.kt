package com.tik.server.dto

import com.tik.server.entity.Resume

data class ResumeResult(
    val id: Int?,
    val name: String,
    val introduction: String
) {
    companion object {
        fun from(resume: Resume): ResumeResult {
            return ResumeResult(
                id = resume.id,
                name = resume.name,
                introduction = resume.introduction
            )
        }
    }
}