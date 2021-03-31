package com.mindhub.speechtalk.koin


import com.mindhub.speechtalk.BuildConfig
import com.mindhub.speechtalk.data.remote.ApiInterceptor
import com.mindhub.speechtalk.data.remote.api.ListeningApiService
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

const val SPEECH_TALK_BASE_URL = "https://dapi.kakao.com/v3/"

const val NS_SPEECH_TALK_RETROFIT = "NS_SPEECH_TALK_RETROFIT"
const val NS_SPEECH_TALK_API_LISTENING = "NS_SPEECH_TALK_API_LISTENING"
const val NS_SPEECH_TALK_API_INTERCEPTOR = "NS_SPEECH_TALK_API_INTERCEPTOR"

val networkModule = module {
    single(named(NS_SPEECH_TALK_RETROFIT)) { provideSpeechTalkRetrofit(get(), get()) }
    single(named(NS_SPEECH_TALK_API_LISTENING)) { provideListeningApiService(get(named(NS_SPEECH_TALK_RETROFIT))) }
    single(named(NS_SPEECH_TALK_API_INTERCEPTOR)) { provideApiInterceptor() }
    single { provideOkHttpClient(get(named(NS_SPEECH_TALK_API_INTERCEPTOR))) }
}

private fun provideApiInterceptor() = ApiInterceptor()

private fun provideSpeechTalkRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit {
    return Retrofit.Builder().baseUrl(SPEECH_TALK_BASE_URL).client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi)).build()
}

private fun provideListeningApiService(retrofit: Retrofit): ListeningApiService {
    return ListeningApiService(retrofit)
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