package com.tik.server.repository

import com.tik.server.entity.Member
import com.tik.server.entity.MemberRole
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {

// 이 함수는 사용자의 이메일을 매개변수로 받아 해당 이메일을 가진 Member 객체를 찾아 반환하는 역할을 합니다.
// email로 중복검사
    fun findByUid(uid:String): Member?

    fun findById(id: Int): Member?
}

interface MemberRoleRepository : JpaRepository<MemberRole, Long>
