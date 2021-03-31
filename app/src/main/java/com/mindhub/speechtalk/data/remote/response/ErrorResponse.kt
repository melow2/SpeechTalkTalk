package com.mindhub.speechtalk.data.remote.response

import com.squareup.moshi.Json

data class ErrorResponse(
    @Json(name = "message") val message: String,
    @Json(name = "errorType") val errorType: Int,
)