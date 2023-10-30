package com.tik.server.dto

class InterviewCreateRequest {
    lateinit var resumeId: Integer
        private set

    lateinit var company: String
        private set

    lateinit var occupation: String
        private set

    lateinit var jobDescription: String
        private set

    lateinit var options: Option
        private set

    class Option (
        var questionCount: Int
    )
}