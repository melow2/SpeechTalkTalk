package com.mindhub.speechtalk.koin


import com.mindhub.speechtalk.activity.LoginActivity
import com.mindhub.speechtalk.activity.MainActivity
import com.mindhub.speechtalk.ui.listening.Listening1Fragment
import com.mindhub.speechtalk.ui.listening.Listening1Interactor
import com.mindhub.speechtalk.ui.listening.Listening1InteractorImpl
import com.mindhub.speechtalk.ui.listening.Listening1ViewModel
import com.mindhub.speechtalk.ui.login.LoginFragment
import com.mindhub.speechtalk.ui.main.MainFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module


@ExperimentalCoroutinesApi
val viewModelModule = module {
    scope<MainFragment> {}
    scope<Listening1Fragment> {
        scoped<Listening1Interactor> {
            Listening1InteractorImpl(
                dispatcherProvider = get(),
                listeningRepository = get(named(NS_LISTENING_REPOSITORY))
            )
        }
        viewModel {
            Listening1ViewModel(
                interactor = get(),
                rxSchedulerProvider = get()
            )
        }
    }
    scope<LoginFragment> {}
    scope<LoginActivity> {}
    scope<MainActivity> {}
}