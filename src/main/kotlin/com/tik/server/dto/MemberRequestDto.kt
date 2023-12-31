package com.tik.server.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.tik.server.entity.Member
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class MemberRequestDto(

    @field:NotBlank
    @JsonProperty("uid")
    private val _uid: String?,

    @field:NotBlank
    @field:Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\$%^&*])[a-zA-Z0-9!@#\$%^&*]{8,20}\$",
        message = "영문, 숫자, 특수문자를 포함한 8~20자리로 입력해주세요"
    )
    @JsonProperty("password")
    private val _password: String?,

    @field:NotBlank
    @JsonProperty("username")
    private val _username: String?,

) {
    val uid: String
        get() = _uid!!
    val password: String
        get() = _password!!
    val username: String
        get() = _username!!

    fun toEntity(): Member =
        Member(null, uid, password, username)
}

data class SignInDto(
    @field:NotBlank
    @JsonProperty("uid")
    private val _uid: String?,

    @field:NotBlank
    @JsonProperty("password")
    private val _password: String?
) {
    val uid: String
        get() = _uid!!
    val password: String
        get() = _password!!

}