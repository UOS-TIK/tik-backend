package com.tik.server.service

import com.tik.server.dto.ResumeCreateRequest
import com.tik.server.dto.ResumeDetail
import com.tik.server.entity.Project
import com.tik.server.entity.ProjectTechStack
import com.tik.server.entity.Resume
import com.tik.server.repository.ProjectRepository
import com.tik.server.repository.ProjectTechStackRepository
import com.tik.server.repository.ResumeRepository
import com.tik.server.repository.TechStackRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class ResumeService(
    private val resumeRepository: ResumeRepository,
    private val projectRepository: ProjectRepository,
    private val projectTechStackRepository: ProjectTechStackRepository,
    private val techStackRepository: TechStackRepository
) {
    companion object {
        private const val SAVE_SUCCESS_MESSAGE = "이력서 저장 성공"
        private const val NOT_FOUND_TECH_STACK = "존재하지 않는 기술스택입니다."
    }

    @Transactional
    fun saveResume(request: ResumeCreateRequest) {
        val resume = resumeRepository.saveAndFlush(Resume(name = request.name))
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
        return resumeRepository.findAllByUserId(1).map {
            ResumeDetail.from(it)
        }
        // todo: 추후 userId로 수정
    }
}