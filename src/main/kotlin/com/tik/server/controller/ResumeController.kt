package com.tik.server.controller

import com.tik.server.dto.ResumeCreateRequest
import com.tik.server.dto.ResumeDetail
import com.tik.server.service.ResumeService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/resume")
@Tag(name = "Resume", description = "이력서 관련 API입니다.")
class ResumeController(
    private val resumeService: ResumeService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun saveResume(@RequestBody request: ResumeCreateRequest) {
        resumeService.saveResume(request)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun findAllResume(@RequestParam(name = "memberId") memberId: Int): List<ResumeDetail> {
        return resumeService.findAllResume(memberId)
    }

    @DeleteMapping("/delete/{resumeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteResume(@PathVariable resumeId: Long) {
        resumeService.deleteResume(resumeId)
    }

}