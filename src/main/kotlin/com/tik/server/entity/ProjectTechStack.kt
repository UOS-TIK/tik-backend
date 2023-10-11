package com.tik.server.entity

import jakarta.persistence.*

@Table(name = "project_tech_stack")
@Entity
class ProjectTechStack(
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id")
    var project: Project,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tech_stack_id")
    var techStack: TechStack
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Int? = null
}