package com.tik.server.entity

import jakarta.persistence.*

@Table(name = "resume")
@Entity
class Resume(
    name: String
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Int? = null

    // todo: user_id 추가(ManyToOne)

    @Column(name = "name")
    var name: String = name
        protected set
}