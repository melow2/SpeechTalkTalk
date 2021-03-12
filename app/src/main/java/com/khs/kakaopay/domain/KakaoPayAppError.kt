package com.khs.kakaopay.domain

sealed class KakaoPayAppError : Throwable()

object NetworkError : KakaoPayAppError()

sealed class LocalStorageError : KakaoPayAppError() {
    object DeleteFileError : LocalStorageError()
    object SaveFileError : LocalStorageError()
    data class DatabaseError(override val cause: Throwable? = null) : LocalStorageError()
}

data class ServerError(
    override val message: String?,
    val statusCode: Int?,
) : KakaoPayAppError()

data class UnexpectedError(
    override val message: String,
    override val cause: Throwable?,
) : KakaoPayAppError()

fun KakaoPayAppError.getMessage(): String {
    return when (this) {
        NetworkError -> "Network error"
        is ServerError -> "$message"
        is UnexpectedError -> "An unexpected error occurred"
        LocalStorageError.DeleteFileError -> "Error when deleting file"
        LocalStorageError.SaveFileError -> "Error when saving file"
        is LocalStorageError.DatabaseError -> "Database error"
    }
}