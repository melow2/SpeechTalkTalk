package com.mindhub.speechtalk.domain

sealed class SpeechTalkAppError : Throwable()

object NetworkError : SpeechTalkAppError()

sealed class LocalStorageError : SpeechTalkAppError() {
    object DeleteFileError : LocalStorageError()
    object SaveFileError : LocalStorageError()
    data class DatabaseError(override val cause: Throwable? = null) : LocalStorageError()
}

data class ServerError(
    override val message: String?,
    val statusCode: Int?,
) : SpeechTalkAppError()

data class UnexpectedError(
    override val message: String,
    override val cause: Throwable?,
) : SpeechTalkAppError()

fun SpeechTalkAppError.getMessage(): String {
    return when (this) {
        NetworkError -> "Network error"
        is ServerError -> "$message"
        is UnexpectedError -> "An unexpected error occurred"
        LocalStorageError.DeleteFileError -> "Error when deleting file"
        LocalStorageError.SaveFileError -> "Error when saving file"
        is LocalStorageError.DatabaseError -> "Database error"
    }
}