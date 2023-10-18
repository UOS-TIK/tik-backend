package com.tik.server.entity

import jakarta.persistence.*
import java.util.*

@Table(name = "project")
@Entity
class Project(
    @Column(name = "name")
    var name: String,
    @Column(name = "summary", columnDefinition = "TEXT")
    var summary: String,
    @Column(name = "description", columnDefinition = "TEXT")
    var description: String,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resume_id")
    var resume: Resume
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Int? = null

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    var projectTechStack: MutableList<ProjectTechStack> = ArrayList()

    fun updateProject(name: String, summary: String, description: String) {
        this.name = name
        this.summary = summary
        this.description = description
    }
}