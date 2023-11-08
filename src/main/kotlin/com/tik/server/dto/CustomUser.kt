package com.tik.server.dto

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
//CustomUserDtailService에서 생성된 객체 userId를 저장해서 토큰의 claim에 저장하기 위함
class CustomUser (
    val userId: Int,
    userName: String,
    password: String,
    authority: Collection<GrantedAuthority>
) : User(userName, password, authority)