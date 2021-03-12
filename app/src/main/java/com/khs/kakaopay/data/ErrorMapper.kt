package com.khs.kakaopay.data

import android.database.sqlite.SQLiteException
import com.khs.kakaopay.domain.*
import com.attractive.deer.util.data.left
import retrofit2.HttpException
import retrofit2.Retrofit
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ErrorMapper(private val retrofit: Retrofit) {

    fun map(throwable: Throwable): KakaoPayAppError {
        return when (throwable) {
            is KakaoPayAppError -> throwable

            is SQLiteException -> {
                LocalStorageError.DatabaseError(throwable)
            }

            is IOException -> {
                when (throwable) {
                    is UnknownHostException -> NetworkError
                    is SocketTimeoutException -> NetworkError
                    else -> UnexpectedError(
                        cause = throwable,
                        message = "Unknown IOException $this"
                    )
                }
            }

            is HttpException -> {
                ErrorResponseParser
                    .getError(
                        throwable.response() ?: return ServerError("Response is null", -1),
                        retrofit
                    )?.let{ ServerError(message = it.message,statusCode = it.errorType) } ?: ServerError("", -1)
            }

            else -> {
                UnexpectedError(
                    cause = throwable,
                    message = "Unknown throwable $throwable"
                )
            }
        }
    }


    fun <T> mapAsLeft(throwable: Throwable): KakaoPayResult<T> = map(throwable).left()
}