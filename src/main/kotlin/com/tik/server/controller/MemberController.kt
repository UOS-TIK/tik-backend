package com.tik.server.controller

import com.tik.server.common.BaseResponse
import com.tik.server.dto.MemberRequestDto
import com.tik.server.dto.SignInDto
import com.tik.server.security.TokenInfo
import com.tik.server.service.MemberService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/user")
@RestController
class MemberController(private val memberService: MemberService) {

    @PostMapping("/signUp")
    fun signUp(@RequestBody @Valid memberRequestDto: MemberRequestDto): BaseResponse<Unit> {

        val resultMsg: String = memberService.signUp(memberRequestDto)
        return BaseResponse(message = resultMsg)
    }

    @PostMapping("/signIn")
    fun signIn(@RequestBody @Valid signInDto: SignInDto) : BaseResponse<TokenInfo> {
        val tokenInfo = memberService.signIn(signInDto)
        return BaseResponse(data = tokenInfo)
    }

}