package com.tik.server.exception

class InvalidInputException(
    val fieldName: String = "",
    message: String =  "Invalid Input"
) : RuntimeException(message)