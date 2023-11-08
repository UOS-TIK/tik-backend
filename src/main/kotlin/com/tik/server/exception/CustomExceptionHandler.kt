package com.tik.server.exception

import com.tik.server.common.BaseResponse
import com.tik.server.common.ResultCode
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.MethodArgumentBuilder

@RestControllerAdvice
class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class) // Member dto에 적용한 validation exception 발생할 시
    protected fun methodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mutableMapOf<String, String>()
        ex.bindingResult.allErrors.forEach { error -> // exception에 들어있는 에러들을 전부 가져온 후
            val fieldName = (error as FieldError).field // 해당 에러의 필드 네임과
            val errorMessage = error.defaultMessage     // 에러 메세지들을
            errors[fieldName] = errorMessage ?: "Not Exception Message"    // errors mutablemap에 저장
        }

        return ResponseEntity(BaseResponse(ResultCode.ERROR.name ,errors, ResultCode.ERROR.msg), HttpStatus.BAD_REQUEST)
                // errors를 baseresponse의 데이터 위치에 집어넣고 반환
    }

    @ExceptionHandler(InvalidInputException::class) // InvalidInputException class의 예외처리
    protected fun invalidInputException(ex: InvalidInputException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf(ex.fieldName to (ex.message ?: "Not Exception Message"))
        return ResponseEntity(BaseResponse(ResultCode.ERROR.name ,errors, ResultCode.ERROR.msg), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(BadCredentialsException::class)
    protected fun badCredentialsException(ex: BadCredentialsException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf("로그인 실패" to "이메일 혹은 비밀번호를 다시 확인해주세요.")
        return ResponseEntity(BaseResponse(ResultCode.ERROR.name ,errors, ResultCode.ERROR.msg), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    protected fun defaultException(ex: Exception): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf("미처리 에러" to (ex.message ?: "Not Exception Message"))
        return ResponseEntity(BaseResponse(ResultCode.ERROR.name ,errors, ResultCode.ERROR.msg), HttpStatus.BAD_REQUEST)
    }

}