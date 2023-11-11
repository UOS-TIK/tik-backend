package com.tik.server.config

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

private const val AUTH_TOKEN_HEADER = "bearerAuth"

@SecurityScheme(
    type = SecuritySchemeType.HTTP, `in` = SecuritySchemeIn.HEADER,
    name = AUTH_TOKEN_HEADER, description = "Auth Token", bearerFormat = "JWT", scheme = "bearer"
)
@Configuration
class SwaggerConfig {
    @Bean
    fun openAPI(): OpenAPI = OpenAPI()
        .components(Components())
        .info(apiInfo())
        .security(
            listOf(
                SecurityRequirement().addList(AUTH_TOKEN_HEADER)
            )
        )

    private fun apiInfo() = Info()
        .title("면접왕 API 문서")
        .description("Kotlin과 SpringBoot로 구현한 면접왕 API 서버입니다.")
        .version("1.0.0")
}
