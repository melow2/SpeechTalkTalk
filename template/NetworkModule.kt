package com.example.kakaopay.koin


import android.content.Context
import com.jeit.coconut.BuildConfig
import com.jeit.coconut.data.remote.ApiInterceptor
import com.jeit.coconut.data.remote.api.*
import com.jeit.coconut.domain.repository.MemberRepository
import com.jeit.coconut.domain.thread.CoroutinesDispatcherProvider
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tony.stark.util.SecureSharedPreferences
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

const val COCONUT_BASE_URL = "http://13.209.180.219:9000"

const val NS_COCONUT_RETROFIT = "NS_COCONUT_RETROFIT"
const val NS_COCONUT_API_BRAND = "NS_COCONUT_API_BRAND"
const val NS_COCONUT_API_LIFE = "NS_COCONUT_API_LIFE"
const val NS_COCONUT_API_HOME = "NS_COCONUT_API_HOME"
const val NS_COCONUT_API_FEED = "NS_COCONUT_API_FEED"
const val NS_COCONUT_API_MEMBER = "NS_COCONUT_API_MEMBER"
const val NS_COCONUT_API_INTERCEPTOR = "NS_COCONUT_API_INTERCEPTOR"

val networkModule = module {

    single(named(NS_COCONUT_RETROFIT)) { provideCoconutRetrofit(get(), get()) }
    single(named(NS_COCONUT_API_BRAND)) { provideBrandApiService(get(named(NS_COCONUT_RETROFIT))) }
    single(named(NS_COCONUT_API_LIFE)) { provideLifeApiService(get(named(NS_COCONUT_RETROFIT))) }
    single(named(NS_COCONUT_API_HOME)) { provideHomeApiService(get(named(NS_COCONUT_RETROFIT))) }
    single(named(NS_COCONUT_API_MEMBER)) { provideMemberApiService(get(named(NS_COCONUT_RETROFIT))) }
    single(named(NS_COCONUT_API_FEED)) { provideFeedApiService(get(named(NS_COCONUT_RETROFIT))) }
    single(named(NS_COCONUT_API_INTERCEPTOR)) { provideApiInterceptor(androidContext(),get()) }
    single { provideOkHttpClient(get(named(NS_COCONUT_API_INTERCEPTOR))) }
}

private fun provideApiInterceptor(
    context: Context,
    preferences: SecureSharedPreferences
) = ApiInterceptor(context = context,preferences= preferences)

private fun provideCoconutRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit {
    return Retrofit.Builder().baseUrl(COCONUT_BASE_URL).client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi)).build()
}

private fun provideBrandApiService(retrofit: Retrofit): BrandApiService {
    return BrandApiService(retrofit)
}

private fun provideFeedApiService(retrofit: Retrofit): FeedApiService {
    return FeedApiService(retrofit)
}

private fun provideLifeApiService(retrofit: Retrofit): LifeApiService {
    return LifeApiService(retrofit)
}

private fun provideHomeApiService(retrofit: Retrofit): HomeApiService {
    return HomeApiService(retrofit)
}

private fun provideMemberApiService(retrofit: Retrofit): MemberApiService {
    return MemberApiService(retrofit)
}

private fun provideOkHttpClient(interceptor: ApiInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
        // .connectTimeout(15, TimeUnit.SECONDS)
        // .readTimeout(15, TimeUnit.SECONDS)
        // .writeTimeout(15, TimeUnit.SECONDS)
        .apply {
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor()
                    .apply { this.level = HttpLoggingInterceptor.Level.BODY }
                    .let(::addInterceptor)
            }
        }.addInterceptor(interceptor)
        .build()
}

/*
object ApiHeaders {
    fun headersFor(
        model: String?,
        mPref: SecureSharedPreferences
    ): Map<String, String> {

        val login = Gson().fromJson<Login>(
            mPref.get(KEY_LOGIN_DATA, ""),
            object : TypeToken<Login>() {}.type
        )

        val refreshToken = login?.data?.refreshToken
        val currentTimeSecond = (System.currentTimeMillis() / 1000).toInt()
        val expireInTimeSecond = mPref.get(KEY_TOKEN_EXPIRE_IN, 0)
        val diff = currentTimeSecond - expireInTimeSecond >= EXPIRE_IN_DAY
        Timber.d("url=$model")

        return mapOf(
            "Content-Type" to "application/json; charset=utf-8",
            "Accept" to "application/json; charset=utf-8"
        )
    }
}*/
