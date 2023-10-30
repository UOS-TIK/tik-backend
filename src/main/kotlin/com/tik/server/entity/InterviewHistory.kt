package com.tik.server.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Table(name = "interview_history")
@Entity
class InterviewHistory(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id")
    var resume: Resume,
    @Column(name = "job_description")
    var jobDescription: String,
    @Column(name = "company")
    var company: String
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Int = 0

    @Column(name = "script")
    var script: String? = null

    @Column(name = "score")
    var score: Int? = null

    @Column(name = "comment")
    var comment: String? = null

    @Column(name = "begin_time")
    var beginTime: LocalDateTime? = null

    @Column(name = "end_time")
    var endTime: LocalDateTime? = null
}