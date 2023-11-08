package com.tik.server.controller

import com.tik.server.common.BaseResponse
import com.tik.server.dto.CustomUser
import com.tik.server.dto.HistoryResponseList
import com.tik.server.dto.HistoryResponseView
import com.tik.server.service.HistoryService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/history")
class HistoryController (
    private val historyService: HistoryService
){

    @GetMapping("/list")
    fun searchHistoryList(): BaseResponse<List<HistoryResponseList>> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        val response = historyService.searchHistoryList(userId)
        return BaseResponse(data = response)
    }

    @GetMapping("/view")
    fun searchHistoryView(@RequestParam(name = "interviewHistoryId") interviewHistoryId: Int): BaseResponse<HistoryResponseView> {
        val response = historyService.searchHistoryView(interviewHistoryId)
        return BaseResponse(data = response)
    }

    @DeleteMapping("/delete/{interviewHistoryId}")
    fun deleteHistory(@PathVariable interviewHistoryId: Int) {
        historyService.deleteHistory(interviewHistoryId)
    }

}