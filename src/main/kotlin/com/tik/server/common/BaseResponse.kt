package com.tik.server.common

data class BaseResponse<T> (
    val resultCode: String = ResultCode.SUCCESS.name,
    val data: T? =null, // 조회나 처리시에 결과를 반환해줄 값들
    val message: String = ResultCode.SUCCESS.msg

)