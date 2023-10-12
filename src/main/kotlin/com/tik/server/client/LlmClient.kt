package com.tik.server.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.client.awaitBody
import reactor.netty.http.client.HttpClient
import java.time.Duration

@Component
class LlmClient(
    @Value("\${internal.llm.url}")
    private val url: String,
    @Value("\${internal.llm.secret}")
    private val secret: String
) {
    private val client = WebClient.builder()
        .baseUrl(url)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .defaultHeader(HttpHeaders.AUTHORIZATION, secret)
        .codecs { it.defaultCodecs().maxInMemorySize(2 * 1024 * 1024) }
        .clientConnector(
            ReactorClientHttpConnector(
                HttpClient.create().responseTimeout(Duration.ofSeconds(30))
            )
        )
        .build()

    private suspend inline fun <reified T : Any> WebClient.ResponseSpec.call(): Base<T> {
        return try {
            Base(
                data = this.awaitBody(),
                error = null
            )
        } catch (err: WebClientResponseException) {
            val response = err.getResponseBodyAs(Map::class.java) ?: throw Exception("exception")

            Base(
                data = null,
                error = Base.Error(
                    statusCode = response["statusCode"] as Int,
                    name = response["name"] as String,
                    message = response["message"] as String,
                )
            )
        } catch (err: Throwable) {
            Base(
                data = null,
                error = Base.Error(
                    statusCode = 500,
                    name = "INTERNAL_SERVER_ERROR",
                    message = "internal server error"
                )
            )
        }
    }

    data class Base<T>(
        val data: T?,
        val error: Error?
    ) {
        data class Error(
            val statusCode: Int,
            val name: String,
            val message: String
        )
    }

    suspend fun initInterview(body: InitInterview.Body): Base<InitInterview.Response> {
        return client
            .post()
            .uri("/init")
            .body(BodyInserters.fromValue(body))
            .retrieve()
            .call()
    }

    class InitInterview {
        data class Body(
            val interviewId: Int,
            val techStack: List<String>,
            val jobDescription: List<String>,
            val options: Options,
        )

        data class Options(val questionCount: Int)

        data class Response(
            val interviewId: Int
        )
    }

    suspend fun speakToInterviewer(body: SpeakToInterviewer.Body): Base<SpeakToInterviewer.Response> {
        return client
            .post()
            .uri("/speak")
            .body(BodyInserters.fromValue(body))
            .retrieve()
            .call()
    }

    class SpeakToInterviewer {
        data class Body(
            val interviewId: Int,
            val message: String,
        )

        data class Response(
            val reply: String
        )
    }

    suspend fun finishInterview(body: FinishInterview.Body): Base<FinishInterview.Response> {
        return client
            .post()
            .uri("/finish")
            .body(BodyInserters.fromValue(body))
            .retrieve()
            .call()
    }

    class FinishInterview {
        data class Body(
            val interviewId: Int
        )

        data class Response(
            val interviewHistory: List<String>,
            val interviewPaper: InterviewPaper
        )

        data class InterviewPaper(
            val items: List<Item>,
            val finalOneLineReview: String,
            val finalScore: Int
        )

        data class Item(
            val question: String,
            val answer: String,
            val evaluation: Evaluation,
            val tailQuestions: List<TailQuestion>
        )

        data class Evaluation(
            val comment: String,
            val score: Int
        )

        data class TailQuestion(
            val question: String,
            val answer: String,
            val evaluation: Evaluation
        )
    }
}


