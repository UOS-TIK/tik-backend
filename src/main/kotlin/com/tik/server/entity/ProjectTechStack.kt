package com.tik.server.entity

import jakarta.persistence.*

@Table(name = "project_tech_stack")
@Entity
class ProjectTechStack(
    project: Project,
    techStack: TechStack
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    var project: Project = project

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    var techStack: TechStack = techStack
}