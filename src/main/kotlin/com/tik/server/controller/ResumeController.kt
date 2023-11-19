package com.tik.server.controller

import com.tik.server.common.BaseResponse
import com.tik.server.dto.*
import com.tik.server.service.ResumeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/resume")
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
    fun saveResume(@AuthenticationPrincipal user: CustomUser, @RequestBody request: ResumeCreateRequest): BaseResponse<List<ResumeResult>> {
        val response = resumeService.saveResume(request, user.userId)
        return BaseResponse(data = response)
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
        summary = "이력서 수정",
        description = "이력서 수정 API입니다."
    )
    fun modifyResume(@RequestBody request: ResumeModifyRequest): BaseResponse<ResumeDetailResult> {
        val response = resumeService.modifyResume(request)
//        val response = resumeService.findResume(request.resumeId.toInt())
        return BaseResponse(data = response)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
        summary = "회원 이력서 전체 조회",
        description = "활성화 된 이력서 전체를 조회하는 API입니다."
    )
    fun findAllResume(@AuthenticationPrincipal user: CustomUser): BaseResponse<List<ResumeResult>> {
        val response = resumeService.findAllResume(user.userId)
        return BaseResponse(data = response)
    }

    @GetMapping("/detail")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
        summary = "이력서 상세 조회",
        description = "해당 id의 이력서를 상세 조회하는 API입니다."
    )
    fun findResume(@RequestParam(name = "resumeId") resumeId: Int): BaseResponse<ResumeDetailResult> {
        val response = resumeService.findResume(resumeId)
        return BaseResponse(data = response)
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