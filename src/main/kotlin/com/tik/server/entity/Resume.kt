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
    var introduction: String,
    @Column(name = "enabled")
    var enabled: Boolean
) : BaseTimeEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Int? = null

    @OneToMany(mappedBy = "resume", fetch = FetchType.EAGER)
    var project: MutableList<Project> = ArrayList()
        protected set

    fun softDeleteResume() {
        this.enabled = false
    }
}