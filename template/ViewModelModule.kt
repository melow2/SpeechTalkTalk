package com.example.kakaopay.koin


import com.jeit.coconut.activity.LoginActivity
import com.jeit.coconut.activity.main.MainActivity
import com.jeit.coconut.activity.main.MainInteractor
import com.jeit.coconut.activity.main.MainInteractorImpl
import com.jeit.coconut.activity.main.MainViewModel
import com.jeit.coconut.ui.login.email.EmailLoginContract
import com.jeit.coconut.ui.login.email.EmailLoginFragment
import com.jeit.coconut.ui.login.email.EmailLoginInteractorImpl
import com.jeit.coconut.ui.login.email.EmailLoginViewModel
import com.jeit.coconut.ui.login.join.JoinFragment
import com.jeit.coconut.ui.login.join.JoinInteractor
import com.jeit.coconut.ui.login.join.JoinInteractorImpl
import com.jeit.coconut.ui.login.join.JoinViewModel
import com.jeit.coconut.ui.main.brand.BrandFragment
import com.jeit.coconut.ui.main.brand.detail.BrandDetailActivity
import com.jeit.coconut.ui.main.brand.detail.BrandDetailInteractor
import com.jeit.coconut.ui.main.brand.detail.BrandDetailInteractorImpl
import com.jeit.coconut.ui.main.brand.detail.BrandDetailViewModel
import com.jeit.coconut.ui.main.brand.filter.BrandFilterActivity
import com.jeit.coconut.ui.main.brand.filter.BrandFilterInteractor
import com.jeit.coconut.ui.main.brand.filter.BrandFilterInteractorImpl
import com.jeit.coconut.ui.main.brand.filter.BrandFilterViewModel
import com.jeit.coconut.ui.main.brand.list.BrandListFragment
import com.jeit.coconut.ui.main.brand.list.BrandListInteractor
import com.jeit.coconut.ui.main.brand.list.BrandListInteractorImpl
import com.jeit.coconut.ui.main.brand.list.BrandListViewModel
import com.jeit.coconut.ui.main.brand.search.BrandSearchFragment
import com.jeit.coconut.ui.main.brand.search.BrandSearchInteractor
import com.jeit.coconut.ui.main.brand.search.BrandSearchInteractorImpl
import com.jeit.coconut.ui.main.brand.search.BrandSearchViewModel
import com.jeit.coconut.ui.main.home.HomeContract
import com.jeit.coconut.ui.main.home.HomeFragment
import com.jeit.coconut.ui.main.home.HomeInteractorImpl
import com.jeit.coconut.ui.main.home.HomeViewModel
import com.jeit.coconut.ui.main.life.LifeFragment
import com.jeit.coconut.ui.main.life.LifeInteractor
import com.jeit.coconut.ui.main.life.LifeInteractorImpl
import com.jeit.coconut.ui.main.life.LifeViewModel
import com.jeit.coconut.ui.main.life.detail.LifeListDetailActivity
import com.jeit.coconut.ui.main.life.detail.LifeListDetailInteractor
import com.jeit.coconut.ui.main.life.detail.LifeListDetailInteractorImpl
import com.jeit.coconut.ui.main.life.detail.LifeListDetailViewModel
import com.jeit.coconut.ui.main.life.list.LifeListActivity
import com.jeit.coconut.ui.main.life.list.LifeListInteractor
import com.jeit.coconut.ui.main.life.list.LifeListInteractorImpl
import com.jeit.coconut.ui.main.life.list.LifeListViewModel
import com.jeit.coconut.ui.main.main.MainFragment
import com.jeit.coconut.ui.main.search.SearchFragment
import com.jeit.coconut.ui.main.search.SearchInteractor
import com.jeit.coconut.ui.main.search.SearchInteractorImpl
import com.jeit.coconut.ui.main.search.SearchViewModel
import com.jeit.coconut.ui.main.talk.TalkFragment
import com.jeit.coconut.ui.main.talk.TalkInteractor
import com.jeit.coconut.ui.main.talk.TalkInteractorImpl
import com.jeit.coconut.ui.main.talk.TalkViewModel
import com.jeit.coconut.ui.main.talk.write.TalkWriteActivity
import com.jeit.coconut.ui.main.talk.write.TalkWriteInteractor
import com.jeit.coconut.ui.main.talk.write.TalkWriteInteractorImpl
import com.jeit.coconut.ui.main.talk.write.TalkWriteViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.core.qualifier.named
import org.koin.dsl.module


@OptIn(KoinApiExtension::class)
@ExperimentalCoroutinesApi
val viewModelModule = module {


    scope<MainFragment> {}

    scope<EmailLoginFragment> {
        scoped<EmailLoginContract.Interactor> {
            EmailLoginInteractorImpl(
                memberRepository = get(named(NS_COCONUT_REPOSITORY_MEMBER)),
                dispatcherProvider = get()
            )
        }
        viewModel {
            EmailLoginViewModel(
                interactor = get(),
                rxSchedulerProvider = get(),
            )
        }
    }

    scope<JoinFragment> {
        scoped<JoinInteractor> {
            JoinInteractorImpl(
                memberRepository = get(named(NS_COCONUT_REPOSITORY_MEMBER)),
                dispatcherProvider = get()
            )
        }
        viewModel {
            JoinViewModel(
                interactor = get(),
                rxSchedulerProvider = get()
            )
        }
    }

    scope<HomeFragment> {
        scoped<HomeContract.Interactor> {
            HomeInteractorImpl(
                homeRepository = get(named(NS_COCONUT_REPOSITORY_HOME)),
                dispatcherProvider = get(),
                memberRepository = get(named(NS_COCONUT_REPOSITORY_MEMBER))
            )
        }
        viewModel {
            HomeViewModel(
                interactor = get(),
                rxSchedulerProvider = get()
            )
        }
    }

    scope<SearchFragment> {
        scoped<SearchInteractor> {
            SearchInteractorImpl(
                mPrefer = get(),
                brandRepository = get(named(NS_COCONUT_REPOSITORY_BRAND)),
                lifeRepository = get(named(NS_COCONUT_REPOSITORY_LIFE)),
                feedRepository = get(named(NS_COCONUT_REPOSITORY_FEED)),
                dispatcherProvider = get()
            )
        }
        viewModel {
            SearchViewModel(
                interactor = get(),
                dispatcherProvider = get()
            )
        }
    }

    scope<MainActivity> {
        scoped<MainInteractor> {
            MainInteractorImpl(
                memberRepository = get(named(NS_COCONUT_REPOSITORY_MEMBER)),
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

    scope<TalkWriteActivity> {
        scoped<TalkWriteInteractor> {
            TalkWriteInteractorImpl(
                feedRepository = get(named(NS_COCONUT_REPOSITORY_FEED)),
                dispatcherProvider = get()
            )
        }
        viewModel {
            TalkWriteViewModel(
                interactor = get(),
                rxSchedulerProvider = get()
            )
        }
    }

    scope<BrandDetailActivity> {}
    scope<BrandFilterActivity> {
        scoped<BrandFilterInteractor> {
            BrandFilterInteractorImpl(
                dispatcherProvider = get(),
                brandRepository = get(named(NS_COCONUT_REPOSITORY_BRAND)),
                prefer = get()
            )
        }
        viewModel {
            BrandFilterViewModel(
                interactor = get(),
                rxSchedulerProvider = get()
            )
        }
    }
    scope<LoginActivity> {}

    scope<LifeListActivity> {
        scoped<LifeListInteractor> {
            LifeListInteractorImpl(
                lifeRepository = get(named(NS_COCONUT_REPOSITORY_LIFE)),
                dispatcherProvider = get()
            )
        }
        viewModel {
            LifeListViewModel(
                interactor = get(),
                rxSchedulerProvider = get()
            )
        }
    }

    scope<LifeListDetailActivity> {
        scoped<LifeListDetailInteractor> {
            LifeListDetailInteractorImpl(
                lifeRepository = get(named(NS_COCONUT_REPOSITORY_LIFE)),
                dispatcherProvider = get()
            )
        }
        viewModel {
            LifeListDetailViewModel(
                interactor = get(),
                rxSchedulerProvider = get()
            )
        }
    }

    scope<BrandFragment> {

        scope<BrandSearchFragment> {
            scoped<BrandSearchInteractor> {
                BrandSearchInteractorImpl(
                    context = androidContext(),
                    dispatcherProvider = get()
                )
            }
            viewModel {
                BrandSearchViewModel(
                    rxSchedulerProvider = get(),
                    interactor = get()
                )
            }
        }

        scope<BrandListFragment> {
            scoped<BrandListInteractor> {
                BrandListInteractorImpl(
                    brandRepository = get(named(NS_COCONUT_REPOSITORY_BRAND)),
                    dispatcherProvider = get()
                )
            }
            viewModel {
                BrandListViewModel(
                    interactor = get(),
                    rxSchedulerProvider = get()
                )
            }
        }
    }

    scope<BrandDetailActivity> {
        scoped<BrandDetailInteractor> {
            BrandDetailInteractorImpl(
                brandRepository = get(named(NS_COCONUT_REPOSITORY_BRAND)),
                dispatcherProvider = get()
            )
        }
        viewModel {
            BrandDetailViewModel(
                interactor = get(),
                rxSchedulerProvider = get()
            )
        }
    }

    scope<LifeFragment> {
        scoped<LifeInteractor> {
            LifeInteractorImpl(
                lifeRepository = get(named(NS_COCONUT_REPOSITORY_LIFE)),
                dispatcherProvider = get()
            )
        }
        viewModel {
            LifeViewModel(
                interactor = get(),
                rxSchedulerProvider = get()
            )
        }
    }

    scope<TalkFragment> {
        scoped<TalkInteractor> {
            TalkInteractorImpl(
                feedRepository = get(named(NS_COCONUT_REPOSITORY_FEED)),
                dispatcherProvider = get()
            )
        }
        viewModel {
            TalkViewModel(
                rxSchedulerProvider = get(),
                interactor = get()
            )
        }
    }

}