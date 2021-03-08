package com.khs.kakaopay.koin


import com.khs.kakaopay.BuildConfig
import com.khs.kakaopay.data.remote.ApiInterceptor
import com.khs.kakaopay.data.remote.api.KakaoBookApiService
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

const val KAKAO_BASE_URL = "https://dapi.kakao.com/v3/"

const val NS_KAKAO_RETROFIT = "NS_KAKAO_RETROFIT"
const val NS_KAKAO_API_BOOK = "NS_KAKAO_API_BOOK"
const val NS_KAKAO_API_INTERCEPTOR = "NS_KAKAO_API_INTERCEPTOR"

val networkModule = module {
    single(named(NS_KAKAO_RETROFIT)) { provideKakaoRetrofit(get(), get()) }
    single(named(NS_KAKAO_API_BOOK)) { provideKakaoBookApiService(get(named(NS_KAKAO_RETROFIT))) }
    single(named(NS_KAKAO_API_INTERCEPTOR)) { provideApiInterceptor() }
    single { provideOkHttpClient(get(named(NS_KAKAO_API_INTERCEPTOR))) }
}

private fun provideApiInterceptor() = ApiInterceptor()

private fun provideKakaoRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit {
    return Retrofit.Builder().baseUrl(KAKAO_BASE_URL).client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi)).build()
}

private fun provideKakaoBookApiService(retrofit: Retrofit): KakaoBookApiService {
    return KakaoBookApiService(retrofit)
}

private fun provideOkHttpClient(interceptor: ApiInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .apply {
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor()
                    .apply { this.level = HttpLoggingInterceptor.Level.BODY }
                    .let(::addInterceptor)
            }
        }.addInterceptor(interceptor)
        .build()
}