package com.tik.server.entity

import jakarta.persistence.*

@Table(name = "tech_stack")
@Entity
class TechStack(
    name: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Int = 0

    @Column(name = "name")
    var name: String = name
        protected set
}