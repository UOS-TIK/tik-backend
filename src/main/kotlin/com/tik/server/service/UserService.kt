package com.tik.server.service

import com.tik.server.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Transactional
@Service
class UserService(
    private val userRepository: UserRepository
) {

}