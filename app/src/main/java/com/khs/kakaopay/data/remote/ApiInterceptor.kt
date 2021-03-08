package com.khs.kakaopay.data.remote

import android.content.Context
import com.lovely.deer.util.SecureSharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ApiInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder().apply {
            addHeader(AUTHORIZATION, BEARER + API_KEY)
        }.build()
        return chain.proceed(request)
    }
    companion object{
        const val AUTHORIZATION = "Authorization"
        const val BEARER = "KakaoAK "
        const val API_KEY = "4de907c5603758606a619f75c90ef4c1"
    }
}
