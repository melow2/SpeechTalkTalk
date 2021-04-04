package com.mindhub.speechtalk.koin


import com.mindhub.speechtalk.activity.LoginActivity
import com.mindhub.speechtalk.activity.MainActivity
import com.mindhub.speechtalk.ui.listening.Listening1Interactor
import com.mindhub.speechtalk.ui.listening.ListeningFragment
import com.mindhub.speechtalk.ui.listening.ListeningInteractorImpl
import com.mindhub.speechtalk.ui.listening.ListeningViewModel
import com.mindhub.speechtalk.ui.login.LoginFragment
import com.mindhub.speechtalk.ui.main.MainFragment
import com.mindhub.speechtalk.ui.speaking.type1.Speaking1Fragment
import com.mindhub.speechtalk.ui.speaking.type1.Speaking1Interactor
import com.mindhub.speechtalk.ui.speaking.type1.Speaking1InteractorImpl
import com.mindhub.speechtalk.ui.speaking.type1.Speaking1ViewModel
import com.mindhub.speechtalk.ui.writing.type1.Writing1Fragment
import com.mindhub.speechtalk.ui.writing.type1.Writing1Interactor
import com.mindhub.speechtalk.ui.writing.type1.Writing1InteractorImpl
import com.mindhub.speechtalk.ui.writing.type1.Writing1ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module


@ExperimentalCoroutinesApi
val viewModelModule = module {
    scope<MainFragment> {}
    scope<ListeningFragment> {
        scoped<Listening1Interactor> {
            ListeningInteractorImpl(
                dispatcherProvider = get(),
                listeningRepository = get(named(NS_LISTENING_REPOSITORY))
            )
        }
        viewModel {
            ListeningViewModel(
                interactor = get(),
                rxSchedulerProvider = get()
            )
        }
    }
    scope<Speaking1Fragment> {
        scoped<Speaking1Interactor> {
            Speaking1InteractorImpl(
                dispatcherProvider = get(),
                listeningRepository = get(named(NS_LISTENING_REPOSITORY))
            )
        }
        viewModel {
            Speaking1ViewModel(
                interactor = get(),
                rxSchedulerProvider = get()
            )
        }
    }
    scope<Writing1Fragment> {
        scoped<Writing1Interactor> {
            Writing1InteractorImpl(
                dispatcherProvider = get(),
                listeningRepository = get(named(NS_LISTENING_REPOSITORY))
            )
        }
        viewModel {
            Writing1ViewModel(
                interactor = get(),
                rxSchedulerProvider = get()
            )
        }
    }
    scope<LoginFragment> {}
    scope<LoginActivity> {}
    scope<MainActivity> {}
}