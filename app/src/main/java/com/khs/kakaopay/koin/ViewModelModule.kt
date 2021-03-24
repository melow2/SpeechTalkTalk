package com.khs.kakaopay.koin


import com.khs.kakaopay.activity.MainActivity
import com.khs.kakaopay.activity.SplashActivity
import com.khs.kakaopay.ui.detail.DetaiInteractorImpl
import com.khs.kakaopay.ui.detail.DetailFragment
import com.khs.kakaopay.ui.detail.DetailInteractor
import com.khs.kakaopay.ui.detail.DetailViewModel
import com.khs.kakaopay.ui.main.MainFragment
import com.khs.kakaopay.ui.main.MainInteractor
import com.khs.kakaopay.ui.main.MainInteractorImpl
import com.khs.kakaopay.ui.main.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module


@ExperimentalCoroutinesApi
val viewModelModule = module {
    scope<SplashActivity> { }
    scope<MainFragment> {
        scoped<MainInteractor> {
            MainInteractorImpl(
                kakaoBookRepository = get(named(NS_KAKAO_REPOSITORY_BOOK)),
                dispatcherProvider = get()
            )
        }
        viewModel {
            MainViewModel(
                interactor = get(),
                rxSchedulerProvider = get()
            )
        }
    }
    scope<DetailFragment> {
        scoped<DetailInteractor> {
            DetaiInteractorImpl(
                dispatcherProvider = get()
            )
        }
        viewModel {
            DetailViewModel(
                interactor = get(),
                rxSchedulerProvider = get()
            )
        }
    }
    scope<MainActivity> {

    }
}