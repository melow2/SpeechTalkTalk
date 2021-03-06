package com.example.kakaopay.koin

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

    single(named(NS_COCONUT_REPOSITORY_LIFE)) {
        provideLifeRepository(
            get(named(NS_COCONUT_ERROR)),
            get(named(NS_COCONUT_API_LIFE)),
            get(),
            get()
        )
    }

    single(named(NS_COCONUT_REPOSITORY_HOME)) {
        provideHomeRepository(
            get(named(NS_COCONUT_ERROR)),
            get(named(NS_COCONUT_API_HOME)),
            get(),
            get()
        )
    }

    single(named(NS_COCONUT_REPOSITORY_MEMBER)) {
        provideMemberRepository(
            get(named(NS_COCONUT_ERROR)),
            get(named(NS_COCONUT_RETROFIT)),
            get(named(NS_COCONUT_API_MEMBER)),
            get(),
            get(),
            get()
        )
    }

    single(named(NS_COCONUT_REPOSITORY_FEED)) {
        provideFeedRepository(
            get(named(NS_COCONUT_ERROR)),
            get(named(NS_COCONUT_API_FEED)),
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

@ObsoleteCoroutinesApi
private fun provideFeedRepository(
    errorMapper: ErrorMapper,
    feedApiService: FeedApiService,
    dispatcherProvider: CoroutinesDispatcherProvider,
): FeedRepository {
    return FeedRepositoryImpl(
        errorMapper = errorMapper,
        feedApiService = feedApiService,
        dispatcherProvider = dispatcherProvider,
    )
}

@ObsoleteCoroutinesApi
private fun provideLifeRepository(
    errorMapper: ErrorMapper,
    lifeApiService: LifeApiService,
    dispatcherProvider: CoroutinesDispatcherProvider,
    appCoroutineScope: CoroutineScope
): LifeRepository {
    return LifeRepositoryImpl(
        errorMapper = errorMapper,
        lifeApiService = lifeApiService,
        dispatcherProvider = dispatcherProvider,
        appCoroutineScope = appCoroutineScope
    )
}

@ObsoleteCoroutinesApi
private fun provideHomeRepository(
    errorMapper: ErrorMapper,
    homeApiService: HomeApiService,
    dispatcherProvider: CoroutinesDispatcherProvider,
    appCoroutineScope: CoroutineScope
): HomeRepository {
    return HomeRepositoryImpl(
        errorMapper = errorMapper,
        homeApiService = homeApiService,
        dispatcherProvider = dispatcherProvider,
        appCoroutineScope = appCoroutineScope
    )
}

@ObsoleteCoroutinesApi
private fun provideMemberRepository(
    errorMapper: ErrorMapper,
    retrofit:Retrofit,
    memberApiService: MemberApiService,
    dispatcherProvider: CoroutinesDispatcherProvider,
    preferences: SecureSharedPreferences,
    appCoroutineScope: CoroutineScope
): MemberRepository {
    return MemberRepositoryImpl(
        errorMapper = errorMapper,
        retrofit = retrofit,
        memberApiService = memberApiService,
        dispatcherProvider = dispatcherProvider,
        preferences = preferences,
        appCoroutineScope = appCoroutineScope
    )
}


private fun provideErrorMapper(retrofit: Retrofit): ErrorMapper {
    return ErrorMapper(retrofit)
}