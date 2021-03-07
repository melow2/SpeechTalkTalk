package com.khs.kakaopay.koin


import com.khs.kakaopay.activity.MainActivity
import com.khs.kakaopay.activity.SplashActivity
import com.khs.kakaopay.ui.main.MainFragment
import com.khs.kakaopay.ui.search.SearchFragment
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

        scope<SearchFragment>{

        }
    }
}