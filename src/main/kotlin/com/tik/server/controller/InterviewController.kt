package com.tik.server.controller

import com.tik.server.common.BaseResponse
import com.tik.server.dto.*
import com.tik.server.service.InterviewService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/interview")
@Tag(name = "Interview", description = "면접 진행 관련 API입니다.")
class InterviewController(
    private val interviewService: InterviewService
) {
    @PostMapping("/init")
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun initInterview(@RequestBody request: InterviewCreateRequest): BaseResponse<InterviewCreateResponse> {
        val response = interviewService.initInterview(request)
        return BaseResponse(data = response)
    }

    @PostMapping("/answer")
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun speakToInterviewer(@RequestBody request: InterviewAnswerRequest): BaseResponse<InterviewQuestion> {
        val response = interviewService.speakToInterviewer(request)
        return BaseResponse(data = response)
    }

    @PostMapping("/finish")
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun finishInterview(@RequestBody request: FinishInterviewRequest): BaseResponse<FinishInterviewResponse> {
        val response = interviewService.finishInterview(request)
        return BaseResponse(data = response)
    }
}