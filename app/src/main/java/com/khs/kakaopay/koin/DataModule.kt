package com.khs.kakaopay.koin

import com.khs.kakaopay.data.ErrorMapper
import com.khs.kakaopay.data.remote.api.KakaoBookApiService
import com.khs.kakaopay.data.repository.KakaoBookRepositoryImpl
import com.khs.kakaopay.domain.repository.KakaoBookRepository
import com.khs.kakaopay.domain.thread.CoroutinesDispatcherProvider
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

const val NS_KAKAO_REPOSITORY_BOOK = "NS_KAKAO_REPOSITORY_BOOK"
const val NS_KAKAO_ERROR = "NS_KAKAO_ERROR"

@ObsoleteCoroutinesApi
val dataModule = module {

    single(named(NS_KAKAO_REPOSITORY_BOOK)) {
        provideKakaoBooksRepository(
            get(named(NS_KAKAO_ERROR)),
            get(named(NS_KAKAO_API_BOOK)),
            get()
        )
    }

    single(named(NS_KAKAO_ERROR)) { provideErrorMapper(get(named(NS_KAKAO_RETROFIT))) }
}

@ObsoleteCoroutinesApi
private fun provideKakaoBooksRepository(
    errorMapper: ErrorMapper,
    kakaoBookApiservice: KakaoBookApiService,
    dispatcherProvider: CoroutinesDispatcherProvider,
): KakaoBookRepository {
    return KakaoBookRepositoryImpl(
        errorMapper = errorMapper,
        kakaoBookApiservice = kakaoBookApiservice,
        dispatcherProvider = dispatcherProvider
    )
}

private fun provideErrorMapper(retrofit: Retrofit): ErrorMapper {
    return ErrorMapper(retrofit)
}