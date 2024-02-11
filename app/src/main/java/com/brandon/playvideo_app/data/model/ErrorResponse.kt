package com.brandon.playvideo_app.data.model

data class ErrorResponse(
    val error: ErrorDetail
)

data class ErrorDetail(
    val code: Int,
    val message: String,
    val errors: List<ErrorInfo>,
    val status: String,
    val details: List<ErrorInfo>
)

data class ErrorInfo(
    val message: String,
    val domain: String,
    val reason: String,
    val metadata: Metadata?
)

data class Metadata(
    val service: String
)
