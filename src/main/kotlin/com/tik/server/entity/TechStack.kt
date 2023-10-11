package com.tik.server.entity

import jakarta.persistence.*

@Table(name = "tech_stack")
@Entity
class TechStack(
    @Column(name = "name")
    var name: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Int? = null
}