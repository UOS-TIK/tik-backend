package com.tik.server.controller

import com.tik.server.dto.UserRequestDto
import com.tik.server.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/user")
@RestController
class UserController(private val userService: UserService) {

    @PostMapping("/signUp")
    fun signUp(@RequestBody userRequestDto: UserRequestDto): String {
        return userService.signUp(userRequestDto)
    }
}