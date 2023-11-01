package com.tik.server.security
// 로그인 시 토큰 정보를 담아 클라이언트에게 전달
data class TokenInfo (
    val grantType: String, // jwt권한인증 타입 , 배열 사용
    val accessToken: String,
    )