package com.tik.server.service

import com.tik.server.dto.*
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
    private val memberRepository: MemberRepository
) {
    companion object {
        private const val SAVE_SUCCESS_MESSAGE = "이력서 저장 성공"
        private const val NOT_FOUND_TECH_STACK = "존재하지 않는 기술스택입니다."
    }

    @Transactional
    fun saveResume(request: ResumeCreateRequest, memberId: Int): List<ResumeResult> {
        val member = memberRepository.findById(memberId) ?: throw IllegalStateException("유저가 존재하지 않습니다.")
        val resume = resumeRepository.save(Resume(member = member, name = request.name, introduction = request.introduction, enabled = true))
        request.projects.forEach {
            val project = projectRepository.save(Project(name = it.name, summary = it.summary, description = it.description, resume = resume))
            val projectTechStackList: MutableList<ProjectTechStack> = ArrayList()
            it.techStack.forEach{
                val techStack = techStackRepository.findById(it).get()
                projectTechStackList.add(ProjectTechStack(project = project, techStack = techStack))
            }
            projectTechStackRepository.saveAll(projectTechStackList)
        }
        return resumeRepository.findAllByMemberIdAndEnabledOrderByIdDesc(memberId, true).map {
            ResumeResult.from(it)
        }
    }

    @Transactional
    fun modifyResume(memberId: Int, request: ResumeModifyRequest): List<ResumeResult> {
        val resume = resumeRepository.findByIdAndEnabled(request.resumeId.toInt(), true).orElse(null)
            ?: throw IllegalStateException("이력서가 존재하지 않습니다.")
        resume.updateResume(request.name, request.introduction)
        val projectList = projectRepository.findAllByResumeId(request.resumeId.toInt()).map { it.id }
        projectRepository.deleteAllByIds(projectList)
        request.projects.forEach {
            val project = projectRepository.save(Project(name = it.name, summary = it.summary, description = it.description, resume = resume))
            val projectTechStackList: MutableList<ProjectTechStack> = ArrayList()
            it.techStack.forEach{
                val techStack = techStackRepository.findById(it).get()
                projectTechStackList.add(ProjectTechStack(project = project, techStack = techStack))
            }
            projectTechStackRepository.saveAll(projectTechStackList)
        }
        resumeRepository.save(resume)
        return resumeRepository.findAllByMemberIdAndEnabledOrderByIdDesc(memberId, true).map {
            ResumeResult.from(it)
        }
    }

    fun findAllResume(memberId: Int): List<ResumeResult> {
        return resumeRepository.findAllByMemberIdAndEnabledOrderByIdDesc(memberId, true).map {
            ResumeResult.from(it)
        }
    }

    fun findResume(resumeId: Int): ResumeDetailResult {
        val resume = resumeRepository.findById(resumeId).orElse(null)
            ?: throw IllegalStateException("이력서가 존재하지 않습니다.")
        return ResumeDetailResult.from(resume)
    }

    fun findAllTechStack(): List<TechStackDTO> {
        return techStackRepository.findAll().map {
            TechStackDTO.from(it)
        }
    }

    fun findTechStacks(text: String): List<TechStackDTO> {
        return techStackRepository.findByNameContaining(text).map {
            TechStackDTO.from(it)
        }
    }

    @Transactional
    fun disableResume(memberId: Int, resumeId: Int): List<ResumeResult> {
        val resume = resumeRepository.findById(resumeId).orElse(null)
            ?: throw IllegalStateException("이력서가 존재하지 않습니다.")
        resume.softDeleteResume()
        resumeRepository.save(resume)
        return resumeRepository.findAllByMemberIdAndEnabledOrderByIdDesc(memberId, true).map {
            ResumeResult.from(it)
        }
    }

    @Transactional
    fun deleteResume(resumeId: Int) {
        return resumeRepository.deleteById(resumeId)
    }
}