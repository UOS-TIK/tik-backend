package com.tik.server.controller

import com.tik.server.common.BaseResponse
import com.tik.server.dto.MemberRequestDto
import com.tik.server.dto.SignInDto
import com.tik.server.security.TokenInfo
import com.tik.server.service.MemberService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RequestMapping("/user")
@RestController
@CrossOrigin
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

    // token정보는 securityContextHolder에 적혀있음, token에 user정보(userId)존재
    //토큰에서 Id값을 가져와 사용
    // val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId

}