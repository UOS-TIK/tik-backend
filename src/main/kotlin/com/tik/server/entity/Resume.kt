package com.tik.server.entity

import jakarta.persistence.*
import java.util.*

@Table(name = "resume")
@Entity
class Resume(
    @Column(name = "name")
    var name: String
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Int? = null

    // todo: user_id 추가(ManyToOne)
    @Column(name = "user_id", nullable = false)
    var userId: Int = 1

    @OneToMany(mappedBy = "resume", fetch = FetchType.LAZY)
    var project: MutableList<Project> = ArrayList()
        protected set
}