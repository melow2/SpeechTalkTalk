package com.khs.kakaopay.ui.detail

import com.attractive.deer.base.BaseViewModel
import com.attractive.deer.util.data.exhaustMap
import com.attractive.deer.util.data.notOfType
import com.jakewharton.rxrelay3.BehaviorRelay
import com.jakewharton.rxrelay3.PublishRelay
import com.khs.kakaopay.domain.getMessage
import com.khs.kakaopay.domain.thread.RxSchedulerProvider
import com.khs.kakaopay.ui.main.MainFragment
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.ofType
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.kotlin.withLatestFrom
import timber.log.Timber

class DetailViewModel(
    private val interactor: DetailInteractor,
    private val rxSchedulerProvider: RxSchedulerProvider
) : BaseViewModel<DetailViewIntent, DetailViewState, DetailSingleEvent>(DetailViewState.initial()) {

    private val intentS = PublishRelay.create<DetailViewIntent>()
    private val stateS = BehaviorRelay.createDefault(intialState)

    private val initProcessor =
        ObservableTransformer<DetailViewIntent.Init, DetailPartialChange> { intent ->
            intent.flatMap {
                interactor.init(book = it.book)
            }
        }

    private val likeProcessor =
        ObservableTransformer<DetailViewIntent.Like, DetailPartialChange> { intent ->
            intent.withLatestFrom(stateS)
                .map { it.first to it.second }
                .exhaustMap {
                    interactor.toggleLike(it.second.books).doOnNext { change ->
                        val messageFromError = (change as? DetailPartialChange.Error
                            ?: return@doOnNext).error.getMessage()
                        sendEvent(
                            DetailSingleEvent.MessageEvent(
                                "[ERROR]  메세지: $messageFromError"
                            )
                        )
                    }
                }
        }

    private val intentToViewState =
        ObservableTransformer<DetailViewIntent, DetailViewState> { intentObservable ->
            intentObservable.publish {
                Observable.mergeArray(
                    it.ofType<DetailViewIntent.Init>().compose(initProcessor),
                    it.ofType<DetailViewIntent.Like>().compose(likeProcessor)
                )
            }.scan(intialState) { state, change -> change.reducer(state) }
                .distinctUntilChanged()
                .observeOn(rxSchedulerProvider.main)
        }

    override fun processIntents(intents: Observable<DetailViewIntent>): Disposable =
        intents.subscribe(intentS)

    init {

        intentS.compose(intentFilter)
            .doOnNext { Timber.tag(DetailFragment.TAG).d("intent: $it") }
            .compose(intentToViewState)
            .subscribeBy(onNext = stateS::accept)
            .addTo(compositeDisposable)

        stateS.subscribeBy(onNext = ::setNewState)
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        Timber.tag(DetailFragment.TAG).d("ViewModel:: onCleared()")
    }

    private companion object {
        private val intentFilter =
            ObservableTransformer<DetailViewIntent, DetailViewIntent> { intent ->
                intent.publish {
                    Observable.mergeArray(
                        it.ofType<DetailViewIntent.Init>().take(1),
                        it.notOfType<DetailViewIntent.Init, DetailViewIntent>()
                    )
                }
            }
    }
}