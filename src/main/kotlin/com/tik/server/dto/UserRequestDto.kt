package com.tik.server.dto

data class UserRequestDto(
    val id: Long?,
    val email: String,
    val password: String,
    val userName: String
)