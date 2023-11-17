package com.tik.server.controller

import com.tik.server.common.BaseResponse
import com.tik.server.dto.CustomUser
import com.tik.server.dto.TechStackDTO
import com.tik.server.service.ResumeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@Tag(name = "TechStack", description = "단순 데이터 조회 관련 API입니다.")
class TechStackController(
    val resumeService: ResumeService
) {
    @GetMapping("/techStack/all")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "전체 기술 스택 조회 API")
    fun findAllTechStack(): BaseResponse<List<TechStackDTO>> {
        val response = resumeService.findAllTechStack()
        return BaseResponse(data = response)
    }

    @GetMapping("/techStack")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "기술 스택 문자열 검색 API")
    fun findTechStacks(@RequestParam text: String): BaseResponse<List<TechStackDTO>> {
        val response = resumeService.findTechStacks(text)
        return BaseResponse(data = response)
    }

//    @GetMapping("/occupation")
//    @ResponseStatus(HttpStatus.OK)
//    @Operation(summary = "전체 직무 조회 API")
//    fun findAllOccupation(): List<>
}