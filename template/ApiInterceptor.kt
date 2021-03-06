
class ApiInterceptor(
    val context: Context,
    val preferences: SecureSharedPreferences
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request.url.toString()
        var refreshFlag = false
        var refreshErrorFlag = false
        var confirmResponse: Response? = null
        if (!(url.contains(REQ_LOGOUT) || url.contains(REQ_PROFILE)) && (url.contains(REQ_MEMBER_AUTH) || url.contains(
                REQ_MEMBER_USER
            ))
        ) return chain.proceed(request)
        else {
            Gson().fromJson<Login>(
                preferences.get(SecureSharedPreferences.KEY_LOGIN_DATA, ""),
                object : TypeToken<Login>() {}.type
            )?.run {
                request = request.newBuilder().apply {
                    addHeader(
                        AUTHORIZATION,
                        BEARER + data?.accessToken
                    )
                }.build()
                // val currentTimeSecond = (System.currentTimeMillis() / 1000).toInt()
                // val tokenExpiredIn = preferences.get(KEY_TOKEN_EXPIRE_IN,0)
                // if(currentTimeSecond - tokenExpiredIn < EXPIRE_IN_FIVE_HOURS) return@run
                confirmResponse = chain.proceed(request)
                when (confirmResponse?.code) {
                    UNAUTHORIZED -> {
                        runCatching { confirmResponse?.close() }.onSuccess {
                            request.getRefreshTokenRequest(
                                "$COCONUT_BASE_URL/api/v1/auth/token/refresh",
                                data?.refreshToken
                            ).let(chain::proceed).run {
                                when (code) {
                                    OK -> {
                                        Gson().fromJson<Login>(
                                            body?.string(),
                                            object : TypeToken<Login>() {}.type
                                        ).also {
                                            preferences.put(
                                                SecureSharedPreferences.KEY_LOGIN_DATA,
                                                Gson().toJson(it)
                                            )
                                        }.run {
                                            runCatching { close() }.onSuccess {
                                                request = request.newBuilder().apply {
                                                    removeHeader(AUTHORIZATION)
                                                    addHeader(
                                                        AUTHORIZATION,
                                                        BEARER + data?.accessToken
                                                    )
                                                }.build().also {
                                                    refreshFlag = true
                                                }
                                                return@run
                                            }
                                        }
                                    }
                                    else -> {
                                        runCatching { close() }.onSuccess {
                                            refreshErrorFlag = true
                                            return@run
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else -> {
                        return@run
                    }
                }
            }
        }
        return when (refreshFlag) {
            true -> chain.proceed(request)
            else -> {
                if (refreshErrorFlag) chain.proceed(request)
                else confirmResponse ?: chain.proceed(request)
            }
        }
    }

    private fun Request.getRefreshTokenRequest(refreshUrl: String, refreshToken: String?): Request =
        newBuilder()
            .get()
            .url(refreshUrl)
            .removeHeader(AUTHORIZATION)
            .addHeader(AUTHORIZATION, BEARER + refreshToken)
            //.post(FormBody.Builder().apply {
            //    add("key","value")
            //}.build() )
            .build()

    companion object {
        const val BEARER = "Bearer "
        const val AUTHORIZATION = "Authorization"
        const val TAG = "CoconutInterceptor"

        const val REQ_PROFILE = "profile"
        const val REQ_LOGOUT = "signout"
        const val REQ_MEMBER_AUTH = "/api/v1/auth/"
        const val REQ_MEMBER_USER = "/api/v1/member/"
    }
}
