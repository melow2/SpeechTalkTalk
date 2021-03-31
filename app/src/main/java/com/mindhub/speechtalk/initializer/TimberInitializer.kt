package com.mindhub.speechtalk.initializer

import android.content.Context
import androidx.startup.Initializer
import com.mindhub.speechtalk.BuildConfig
import timber.log.Timber

class TimberInitializer:Initializer<Unit>{
    override fun create(context: Context) {
        if(BuildConfig.DEBUG){
            Timber.plant(
                Timber.DebugTree(),
            )
        }
        Timber.tag("Initializer").d("Timber initialized")
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()

}