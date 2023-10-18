package com.tik.server.entity

import jakarta.persistence.*
import java.util.*

@Table(name = "resume")
@Entity
class Resume(
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    var member: Member,
    @Column(name = "name")
    var name: String,
    @Column(name = "introduction")
    var introduction: String
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Int? = null

    @OneToMany(mappedBy = "resume", fetch = FetchType.LAZY)
    var project: MutableList<Project> = ArrayList()
        protected set
}