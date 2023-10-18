package com.tik.server.service

import com.tik.server.dto.ResumeCreateRequest
import com.tik.server.dto.ResumeDetail
import com.tik.server.entity.Project
import com.tik.server.entity.ProjectTechStack
import com.tik.server.entity.Resume
import com.tik.server.repository.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class ResumeService(
    private val resumeRepository: ResumeRepository,
    private val projectRepository: ProjectRepository,
    private val projectTechStackRepository: ProjectTechStackRepository,
    private val techStackRepository: TechStackRepository,
    private val userRepository: UserRepository
) {
    companion object {
        private const val SAVE_SUCCESS_MESSAGE = "이력서 저장 성공"
        private const val NOT_FOUND_TECH_STACK = "존재하지 않는 기술스택입니다."
    }

    @Transactional
    fun saveResume(request: ResumeCreateRequest) {
        val user = userRepository.findById(request.userId.toInt()) ?: throw IllegalStateException("유저가 존재하지 않습니다.")
        val resume = resumeRepository.save(Resume(user = user, name = request.name, introduction = request.introduction))
        request.projects.forEach {
            val project = projectRepository.save(Project(name = it.name, summary = it.summary, description = it.description, resume = resume))
            val projectTechStackList: MutableList<ProjectTechStack> = ArrayList()
            it.techStack.forEach{
                val techStack = techStackRepository.findById(it).get()
                projectTechStackList.add(ProjectTechStack(project = project, techStack = techStack))
            }
            projectTechStackRepository.saveAll(projectTechStackList)
        }
    }

    fun findAllResume(userId: Int): List<ResumeDetail> {
        return resumeRepository.findAllByUserId(userId).map {
            ResumeDetail.from(it)
        }
    }

    fun deleteResume(resumeId: Long) {
        return resumeRepository.deleteById(resumeId)
    }
}