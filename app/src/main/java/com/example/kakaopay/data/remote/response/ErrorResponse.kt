package com.example.kakaopay.data.remote.response

import com.google.gson.annotations.JsonAdapter
import com.squareup.moshi.Json

data class ErrorResponse(
    @Json(name = "message") val message: String,
    @Json(name = "status_code") val statusCode: Int,
)