package com.tik.server.dto

class ResumeModifyRequest {
    lateinit var resumeId: Integer
        private set

    lateinit var name: String
        private set

    lateinit var introduction: String
        private set

    lateinit var projects: MutableList<ProjectDto>
        private set

    class ProjectDto (
        var techStack: List<Long>,
        var name: String,
        var summary: String,
        var description: String
    )
}