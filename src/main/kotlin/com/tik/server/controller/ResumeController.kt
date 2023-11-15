package com.tik.server.controller

import com.tik.server.dto.ResumeCreateRequest
import com.tik.server.dto.ResumeDetail
import com.tik.server.service.ResumeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/resume")
@CrossOrigin
@Tag(name = "Resume", description = "이력서 관련 API입니다.")
class ResumeController(
    private val resumeService: ResumeService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "이력서 저장",
        description = "이력서 저장하는 API입니다."
    )
    fun saveResume(@RequestBody request: ResumeCreateRequest) {
        resumeService.saveResume(request)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
        summary = "회원 이력서 조회",
        description = "활성화 된 이력서 전체를 조회하는 API입니다."
    )
    fun findAllResume(@RequestParam(name = "memberId") memberId: Int): List<ResumeDetail> {
        return resumeService.findAllResume(memberId)
    }

    @PatchMapping("/disable/{resumeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
        summary = "이력서 비활성화",
        description = "이력서를 추후 사용 및 조회할 수 없도록 비활성화 하는 API입니다."
    )
    fun disableResume(@PathVariable resumeId: Int) {
        resumeService.disableResume(resumeId)
    }

}