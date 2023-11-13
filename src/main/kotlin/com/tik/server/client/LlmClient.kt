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
                HttpClient.create().responseTimeout(Duration.ofSeconds(180))
            )
        )
        .build()

    private suspend inline fun <reified T : Base.Response, reified U : Base.Exception> WebClient.ResponseSpec.call(): Base<T, U> {
        return try {
            Base(
                data = this.awaitBody(),
                error = null
            )
        } catch (err: WebClientResponseException) {
            val response = err.getResponseBodyAs(Map::class.java) ?: throw Exception("invalid response data type.")

            Base(
                data = null,
                error = Base.Exception.getByMessage(U::class.java, response["message"] as String),
            )
        } catch (err: Throwable) {
            print("[unknown exception] ${err.message}")
            Base(
                data = null,
                error = Base.Exception.getByMessage(U::class.java, Base.Exception.INTERNAL_SERVER_ERROR_MESSAGE),
            )
        }
    }

    data class Base<T : Base.Response, U : Base.Exception>(
        val data: T?,
        val error: U?
    ) {
        interface Response

        interface Exception {
            val message: String

            companion object {
                const val INTERNAL_SERVER_ERROR_MESSAGE = "internal server error."

                fun <T : Exception> getByMessage(enumClass: Class<T>, message: String): T {
                    val enumMap = enumClass.enumConstants.associateBy { it.message }
                    if (enumMap[message] != null) {
                        return enumMap[message] as T
                    }

                    print("[unhandled exception] $message")
                    return enumMap[INTERNAL_SERVER_ERROR_MESSAGE] as T
                }
            }
        }
    }

    suspend fun initInterview(body: InitInterview.Body): Base<InitInterview.Response, InitInterview.Exception> {
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

        data class Options(
            val resumeQuestion: Int,
            val jdQuestion: Int,
            val csQuestion: Int,
        )

        data class Response(
            val interviewId: Int
        ) : Base.Response

        enum class Exception(override val message: String) : Base.Exception {
            INTERVIEW_STARTED("interview is already started."),
            INVALID_ID("invalid interviewId."),
            INTERVIEW_LOCKED("interview is locked."),
            INTERNAL_SERVER(Base.Exception.INTERNAL_SERVER_ERROR_MESSAGE);
        }
    }

    suspend fun speakToInterviewer(body: SpeakToInterviewer.Body): Base<SpeakToInterviewer.Response, SpeakToInterviewer.Exception> {
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
            val reply: String,
            val isFinished: Boolean
        ) : Base.Response

        enum class Exception(override val message: String) : Base.Exception {
            INTERVIEW_NOT_INITED("interview is not initialized."),
            INTERVIEW_FINISHED("interview is finished."),
            INVALID_ID("invalid interviewId."),
            INTERVIEW_LOCKED("interview is locked."),
            INTERNAL_SERVER(Base.Exception.INTERNAL_SERVER_ERROR_MESSAGE);
        }
    }

    suspend fun finishInterview(body: FinishInterview.Body): Base<FinishInterview.Response, FinishInterview.Exception> {
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
        ) : Base.Response

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

        enum class Exception(override val message: String) : Base.Exception {
            INTERVIEW_NOT_INITED("interview is not initialized."),
            INTERVIEW_NOT_FINISHED("interview is not finished."),
            INVALID_ID("invalid interviewId."),
            INTERVIEW_LOCKED("interview is locked."),
            INTERNAL_SERVER(Base.Exception.INTERNAL_SERVER_ERROR_MESSAGE);
        }
    }
}


