package com.tik.server.security

data class TokenInfo (
    val grantType: String,
    val accessToken: String,
    val refreshToken: String
    )