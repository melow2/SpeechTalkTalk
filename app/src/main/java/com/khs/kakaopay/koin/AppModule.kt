package com.khs.kakaopay.koin


import android.content.Context
import com.khs.kakaopay.domain.thread.CoroutinesDispatcherProvider
import com.khs.kakaopay.domain.thread.CoroutinesDispatcherProviderImpl
import com.khs.kakaopay.domain.thread.RxSchedulerProvider
import com.khs.kakaopay.domain.thread.RxSchedulerProviderImpl
import com.lovely.deer.util.SecureSharedPreferences
import com.lovely.deer.util.SecureSharedPreferences.Companion.getPreferences
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { provideRxSchedulerProvider() }
    single { provideCoroutinesDispatchersProvider(get()) }
    single { provideAppCoroutineScope(get()) }
    single { provideMoshi() }
    single { provideSecureSharedPreference(androidContext()) }
}

fun provideSecureSharedPreference(context: Context): SecureSharedPreferences =
    SecureSharedPreferences.wrap(
        context.getPreferences(
            SecureSharedPreferences.PREFERENCE_ROSEMARY,
            0
        ), context
    )

private fun provideAppCoroutineScope(dispatcherProvider: CoroutinesDispatcherProvider): CoroutineScope {
    return CoroutineScope(dispatcherProvider.io + SupervisorJob())
}

private fun provideRxSchedulerProvider(): RxSchedulerProvider {
    return RxSchedulerProviderImpl()
}

private fun provideCoroutinesDispatchersProvider(rxSchedulerProvider: RxSchedulerProvider): CoroutinesDispatcherProvider {
    return CoroutinesDispatcherProviderImpl(rxSchedulerProvider)
}

private fun provideMoshi(): Moshi {
    return Moshi
        .Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
}
