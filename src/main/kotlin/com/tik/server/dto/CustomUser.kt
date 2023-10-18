package com.tik.server.dto

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class CustomUser (
    val userId: Int,
    userName: String,
    password: String,
    authority: Collection<GrantedAuthority>
) : User(userName, password, authority)