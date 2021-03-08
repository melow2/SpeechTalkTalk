package com.khs.kakaopay.ui.main

import com.jakewharton.rxrelay3.BehaviorRelay
import com.jakewharton.rxrelay3.PublishRelay
import com.khs.kakaopay.domain.getMessage
import com.khs.kakaopay.domain.thread.RxSchedulerProvider
import com.lovely.deer.base.BaseViewModel
import com.lovely.deer.util.data.exhaustMap
import com.lovely.deer.util.data.notOfType
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.ofType
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.kotlin.withLatestFrom

import timber.log.Timber
import java.util.concurrent.TimeUnit

class MainViewModel(
    private val interactor: MainInteractor,
    private val rxSchedulerProvider: RxSchedulerProvider
) : BaseViewModel<MainIntent, MainViewState, MainSingleEvent>(MainViewState.initial()) {

    private lateinit var searchText: Observable<String>
    private val intentS = PublishRelay.create<MainIntent>()
    private val stateS = BehaviorRelay.createDefault(intialState)

    private val searchProcessor = ObservableTransformer<MainIntent.SearchIntent, MainPartialChange> { _ ->
            searchText.switchMap { search ->
                interactor.getKakaoBooks(page = START_PAGE, sizePerPage = PAGE_SIZE, query = search)
                    .doOnNext {
                        val messageFromError = (it as? MainPartialChange.Error ?: return@doOnNext).error.getMessage()
                        sendEvent(
                            MainSingleEvent.MessageEvent(
                                "[ERROR] 검색어: '$search', 메세지: $messageFromError"
                            )
                        )
                    }
            }
        }

    private val loadNextPageProcessor = ObservableTransformer<MainIntent.LoadNextPage,MainPartialChange> { intent ->
        intent.withLatestFrom(searchText){_,term -> term}
            .withLatestFrom(stateS)
            .map { it.first to it.second.updatePage +1 }
            .doOnNext { Timber.tag(MainFragment.TAG).d("loadNextPage: $it") }
            .exhaustMap { (searchText,page) ->
                interactor.getKakaoBooks(page = page,sizePerPage = PAGE_SIZE,query = searchText)
                    .doOnNext {
                        val messageFromError = (it as? MainPartialChange.Error ?: return@doOnNext).error.getMessage()
                        sendEvent(
                            MainSingleEvent.MessageEvent(
                                "[ERROR] - loadNextPage(),  검색어: '$searchText', 메세지: $messageFromError"
                            )
                        )
                    }
            }
    }

    private val intentToViewState =
        ObservableTransformer<MainIntent, MainViewState> { intentObservable ->
            intentObservable.publish {
                Observable.mergeArray(
                    // it.ofType<MainIntent.Init>().compose(initProcessor)
                    it.ofType<MainIntent.SearchIntent>().compose(searchProcessor),
                    it.ofType<MainIntent.LoadNextPage>().compose(loadNextPageProcessor)
                )
            }.scan(intialState) { state, change -> change.reducer(state) }
                .distinctUntilChanged()
                .observeOn(rxSchedulerProvider.main)
        }

    override fun processIntents(intents: Observable<MainIntent>): Disposable =
        intents.subscribe(intentS)

    init {

        searchText = intentS.ofType<MainIntent.SearchIntent>()
            .map { it.searchText }
            .filter { it.isNotBlank() }
            .doOnNext { Timber.tag(MainFragment.TAG).d("search #1: $it") }
            .debounce(600, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .doOnNext { Timber.tag(MainFragment.TAG).d("search #2: $it") }
            .share()

        intentS.compose(intentFilter)
            .doOnNext { Timber.tag(MainFragment.TAG).d("intent: $it") }
            .compose(intentToViewState)
            .subscribeBy(onNext = stateS::accept)
            .addTo(compositeDisposable)

        stateS.subscribeBy(onNext = ::setNewState)
            .addTo(compositeDisposable)
    }

    private companion object {
        private const val START_PAGE = 1
        private const val PAGE_SIZE = 50
        private val intentFilter = ObservableTransformer<MainIntent, MainIntent> { intent ->
            intent.publish {
                Observable.mergeArray(
                    it.ofType<MainIntent.Init>().take(1),
                    it.notOfType<MainIntent.Init, MainIntent>()
                )
            }
        }
    }
}