package com.tik.server.entity

import jakarta.persistence.*

@Table(name = "project")
@Entity
class Project(
    name: String,
    summary: String,
    description: String
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0

    // todo: resume_id 추가(ManyToOne)

    @Column(name = "name")
    var name: String = name

    @Column(name = "summary", columnDefinition = "TEXT")
    var summary: String = summary
        protected set

    @Column(name = "description", columnDefinition = "TEXT")
    var description: String = description
        protected set
}