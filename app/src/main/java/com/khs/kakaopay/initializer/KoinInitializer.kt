package com.khs.kakaopay.initializer

import android.content.Context
import androidx.startup.Initializer
import com.khs.kakaopay.BuildConfig
import com.khs.kakaopay.koin.appModule
import com.khs.kakaopay.koin.dataModule
import com.khs.kakaopay.koin.networkModule
import com.khs.kakaopay.koin.viewModelModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.Koin
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class KoinInitializer : Initializer<Koin> {
    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    override fun create(context: Context): Koin = context.intializeKoin().also {
        Timber.tag("Initializer").d("Koin intialized.")
    }
    override fun dependencies(): List<Class<out Initializer<*>>> =
        listOf(TimberInitializer::class.java)
}

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
fun Context.intializeKoin(): Koin {
    return GlobalContext.getOrNull() ?: startKoin {
        androidLogger(level = if (BuildConfig.DEBUG) Level.DEBUG else Level.NONE)
        androidContext(applicationContext)
        modules(
            networkModule,
            dataModule,
            appModule,
            viewModelModule
        )
    }.koin
}


