package com.tik.server.service

import com.tik.server.client.LlmClient
import org.springframework.stereotype.Service

@Service
class ExampleService(
    private val llmClient: LlmClient,
) {
    suspend fun example(): Int {
        val res1 = llmClient.speakToInterviewer(
            body = LlmClient.SpeakToInterviewer.Body(
                interviewId = 3782,
                message = "wqe"
            )
        )
        if (res1.error == LlmClient.SpeakToInterviewer.Exception.INTERVIEW_NOT_INITED) {
            println(res1.error.message)
        }

        val res2 = llmClient.finishInterview(
            body = LlmClient.FinishInterview.Body(
                interviewId = -123
            )
        )
        if (res2.data != null) {
            println(res2.data.interviewHistory)
        }

        return 1
    }
}