package com.mindhub.speechtalk.koin

import com.mindhub.speechtalk.data.ErrorMapper
import com.mindhub.speechtalk.data.remote.api.ListeningApiService
import com.mindhub.speechtalk.data.repository.ListeningRepositoryImpl
import com.mindhub.speechtalk.domain.repository.ListeningRepository
import com.mindhub.speechtalk.domain.thread.CoroutinesDispatcherProvider
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

const val NS_LISTENING_REPOSITORY = "NS_LISTENING_REPOSITORY"
const val NS_SPEECH_TALK_ERROR = "NS_LISTENING_ERROR"

@ObsoleteCoroutinesApi
val dataModule = module {

    single(named(NS_LISTENING_REPOSITORY)) {
        provideSpeechTalkRepository(
            get(named(NS_SPEECH_TALK_ERROR)),
            get(named(NS_SPEECH_TALK_API_LISTENING)),
            get()
        )
    }

    single(named(NS_SPEECH_TALK_ERROR)) { provideErrorMapper(get(named(NS_SPEECH_TALK_RETROFIT))) }
}

@ObsoleteCoroutinesApi
private fun provideSpeechTalkRepository(
    errorMapper: ErrorMapper,
    listeningApiservice: ListeningApiService,
    dispatcherProvider: CoroutinesDispatcherProvider,
): ListeningRepository {
    return ListeningRepositoryImpl(
        errorMapper = errorMapper,
        listeningApiservice = listeningApiservice,
        dispatcherProvider = dispatcherProvider
    )
}

private fun provideErrorMapper(retrofit: Retrofit): ErrorMapper {
    return ErrorMapper(retrofit)
}