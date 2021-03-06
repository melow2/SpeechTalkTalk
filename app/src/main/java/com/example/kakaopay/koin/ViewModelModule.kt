package com.example.kakaopay.koin


import com.example.kakaopay.activity.MainActivity
import com.example.kakaopay.activity.SplashActivity
import com.example.kakaopay.ui.main.MainFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module


@OptIn(KoinApiExtension::class)
@ExperimentalCoroutinesApi
val viewModelModule = module {
    scope<SplashActivity> {  }
    scope<MainActivity> {
        scope<MainFragment> {

        }
    }
}