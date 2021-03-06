package com.mindhub.speechtalk.data


import com.mindhub.speechtalk.data.remote.response.ErrorResponse
import retrofit2.Response
import retrofit2.Retrofit

object ErrorResponseParser {
    fun getError(response: Response<*>, retrofit: Retrofit): ErrorResponse?{
        return when{
            response.isSuccessful -> null
            else -> response.errorBody()?.let{
                retrofit.responseBodyConverter<ErrorResponse>(
                    ErrorResponse::class.java,
                    ErrorResponse::class.java.annotations
                ).convert(it)
            }
        }
    }

}