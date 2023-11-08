package com.tik.server.common
// validation에 걸리면 메세지가 길어짐
// 클라이언트에게 전달하는 메세지 규격화
enum class ResultCode(val msg: String ) {
    SUCCESS("정상 처리 되었습니다."),
    ERROR("에러가 발생하였습니다.")
}
enum class ROLE {
    MEMBER
}