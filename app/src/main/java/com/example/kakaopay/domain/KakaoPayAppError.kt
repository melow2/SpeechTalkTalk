package com.example.kakaopay.domain

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

sealed class AuthError : KakaoPayAppError() {
    object InvalidCustomToken : AuthError()
    object CustomTokenMismatch : AuthError()
    object InvalidCredential : AuthError()
    object InvalidEmail : AuthError()
    object WrongPassword : AuthError()
    object UserMismatch : AuthError()
    object RequiresRecentLogin : AuthError()
    object AccountExistsWithDifferenceCredential : AuthError()
    object EmailAlreadyInUse : AuthError()
    object CredentialAlreadyInUse : AuthError()
    object UserDisabled : AuthError()
    object TokenExpired : AuthError()
    object UserNotFound : AuthError()
    object InvalidUserToken : AuthError()
    object OperationNotAllowed : AuthError()
    object WeakPassword : AuthError()
    object UploadFile : AuthError()
    object Unauthenticated : AuthError()
}

fun KakaoPayAppError.getMessage(): String {
    return when (this) {
        NetworkError -> "Network error"
        is ServerError -> "$message"
        is UnexpectedError -> "An unexpected error occurred"
        LocalStorageError.DeleteFileError -> "Error when deleting file"
        LocalStorageError.SaveFileError -> "Error when saving file"
        is LocalStorageError.DatabaseError -> "Database error"
        AuthError.InvalidCustomToken -> "Invalid custom token"
        AuthError.CustomTokenMismatch -> "Custom token mismatch"
        AuthError.InvalidCredential -> "Invalid credential"
        AuthError.InvalidEmail -> "Invalid email"
        AuthError.WrongPassword -> "Wrong password"
        AuthError.UserMismatch -> "User mismatch"
        AuthError.RequiresRecentLogin -> "Requires recent login"
        AuthError.AccountExistsWithDifferenceCredential -> "Account exists with difference credential"
        AuthError.EmailAlreadyInUse -> "Email already in use"
        AuthError.CredentialAlreadyInUse -> "Credential already in use"
        AuthError.UserDisabled -> "User disabled"
        AuthError.TokenExpired -> "Token expired"
        AuthError.UserNotFound -> "User not found"
        AuthError.InvalidUserToken -> "Invalid user token"
        AuthError.OperationNotAllowed -> "Operation not allowed"
        AuthError.WeakPassword -> "Weak password"
        AuthError.UploadFile -> "Upload file error"
        AuthError.Unauthenticated -> "Unauthenticated error"
    }
}