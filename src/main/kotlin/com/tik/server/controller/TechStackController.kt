package com.tik.server.controller

import com.tik.server.dto.TechStackDTO
import com.tik.server.service.ResumeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
@Tag(name = "TechStack", description = "단순 데이터 조회 관련 API입니다.")
class TechStackController(
    val resumeService: ResumeService
) {
    @GetMapping("/techStack")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "전체 기술 스택 조회 API")
    fun findAllTechStack(): List<TechStackDTO> {
        return resumeService.findAllTechStack()
    }

//    @GetMapping("/occupation")
//    @ResponseStatus(HttpStatus.OK)
//    @Operation(summary = "전체 직무 조회 API")
//    fun findAllOccupation(): List<>
}