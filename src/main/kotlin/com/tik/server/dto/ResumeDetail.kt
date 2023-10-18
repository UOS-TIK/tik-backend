package com.tik.server.dto

import com.tik.server.entity.Project
import com.tik.server.entity.Resume
import com.tik.server.entity.TechStack

data class ResumeDetail(
    val id: Int?, // todo: memberId 추가
    val name: String,
    val introduction: String,
    val projects: List<ProjectDetail>
) {
    companion object {
        fun from(resume: Resume): ResumeDetail {
            return ResumeDetail(
                id = resume.id,
                name = resume.name,
                introduction = resume.introduction,
                projects = resume.project.map {
                    ProjectDetail.from(it)
                }
            )
        }
    }

    data class ProjectDetail(
        val id: Int?,
        val name: String,
        val summary: String,
        val description: String,
        val techStack: List<TechStackDetail>
    ) {
        companion object {
            fun from(project: Project): ProjectDetail {
                return ProjectDetail(
                    id = project.id,
                    name = project.name,
                    summary = project.summary,
                    description = project.description,
                    techStack = project.projectTechStack.map {
                        TechStackDetail.from(it.techStack)
                    }
                )
            }
        }
    }

    data class TechStackDetail(
        val id: Int?,
        val name: String
    ) {
        companion object {
            fun from(techStack: TechStack): TechStackDetail {
                return TechStackDetail(
                    id = techStack.id,
                    name = techStack.name
                )
            }
        }
    }
}