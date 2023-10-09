package com.tik.server.repository

import com.tik.server.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

// 이 함수는 사용자의 이메일을 매개변수로 받아 해당 이메일을 가진 User 객체를 찾아 반환하는 역할을 합니다.
// email로 중복검사
    fun findByEmail(email:String):User?

}

