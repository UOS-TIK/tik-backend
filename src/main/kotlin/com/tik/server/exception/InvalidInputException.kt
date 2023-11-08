package com.tik.server.exception
//  id 중복 체크와 같이 DB를 확인하고 발생하는 exception에서 사용
class InvalidInputException(
    val fieldName: String = "",
    message: String =  "Invalid Input"
) : RuntimeException(message) // 파라미터 message 상속받은 RuntimeException으로 전달