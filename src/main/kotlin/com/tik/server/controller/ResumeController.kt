package com.tik.server.controller

import com.tik.server.dto.ResumeCreateRequest
import com.tik.server.dto.ResumeDetail
import com.tik.server.service.ResumeService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/resume")
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
    fun findAllResume(@RequestParam(name = "userId") userId: Int): List<ResumeDetail> {
        return resumeService.findAllResume(userId)
    }
}