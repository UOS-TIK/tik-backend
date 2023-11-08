package com.tik.server.entity

import com.tik.server.common.ROLE
import jakarta.persistence.*

@Table(name = "member")
@Entity
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(name = "uid", nullable = false, unique = true)
    var uid: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Column(name = "username", nullable = false)
    var username: String? = null
) {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    val memberRole: List<MemberRole>? = null
}

@Entity
class MemberRole(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    val role: ROLE,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "member_role_member_id"))
    val member: Member
)