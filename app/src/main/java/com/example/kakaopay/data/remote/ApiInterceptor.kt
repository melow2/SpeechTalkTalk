package com.example.kakaopay.data.remote

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lovely.deer.util.SecureSharedPreferences
import kotlinx.coroutines.*
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class ApiInterceptor(
    val context: Context,
    val preferences: SecureSharedPreferences
) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request.url.toString()
        return chain.proceed(request)
    }
}
