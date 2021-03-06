package com.example.kakaopay.koin

import com.example.kakaopay.data.ErrorMapper
import com.example.kakaopay.data.remote.api.BrandApiService
import com.example.kakaopay.data.repository.BrandRepositoryImpl
import com.example.kakaopay.domain.repository.BrandRepository
import com.example.kakaopay.domain.thread.CoroutinesDispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

const val NS_COCONUT_REPOSITORY_BRAND = "NS_COCONUT_REPOSITORY_BRAND"
const val NS_COCONUT_REPOSITORY_LIFE = "NS_COCONUT_REPOSITORY_LIFE"
const val NS_COCONUT_REPOSITORY_HOME = "NS_COCONUT_REPOSITORY_HOME"
const val NS_COCONUT_ERROR = "NS_COCONUT_ERROR"
const val NS_COCONUT_REPOSITORY_MEMBER = "NS_COCONUT_REPOSITORY_MEMBER"
const val NS_COCONUT_REPOSITORY_FEED= "NS_COCONUT_REPOSITORY_FEED"

@ObsoleteCoroutinesApi
val dataModule = module {

    single(named(NS_COCONUT_REPOSITORY_BRAND)) {
        provideBrandRepository(
            get(named(NS_COCONUT_ERROR)),
            get(named(NS_COCONUT_API_BRAND)),
            get(),
            get()
        )
    }

    single(named(NS_COCONUT_ERROR)) { provideErrorMapper(get(named(NS_COCONUT_RETROFIT))) }
}

@ObsoleteCoroutinesApi
private fun provideBrandRepository(
    errorMapper: ErrorMapper,
    brandApiService: BrandApiService,
    dispatcherProvider: CoroutinesDispatcherProvider,
    appCoroutineScope: CoroutineScope
): BrandRepository {
    return BrandRepositoryImpl(
        errorMapper = errorMapper,
        brandApiService = brandApiService,
        dispatcherProvider = dispatcherProvider,
        appCoroutineScope = appCoroutineScope
    )
}

private fun provideErrorMapper(retrofit: Retrofit): ErrorMapper {
    return ErrorMapper(retrofit)
}