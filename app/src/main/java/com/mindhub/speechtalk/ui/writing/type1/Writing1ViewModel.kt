package com.mindhub.speechtalk.ui.writing.type1

import com.attractive.deer.base.BaseViewModel
import com.attractive.deer.util.data.exhaustMap
import com.attractive.deer.util.data.notOfType
import com.jakewharton.rxrelay3.BehaviorRelay
import com.jakewharton.rxrelay3.PublishRelay
import com.mindhub.speechtalk.domain.thread.RxSchedulerProvider
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.ofType
import io.reactivex.rxjava3.kotlin.subscribeBy
import timber.log.Timber

class Writing1ViewModel(
    private val interactor: Writing1Interactor,
    private val rxSchedulerProvider: RxSchedulerProvider
) : BaseViewModel<Writing1Intent, Writing1ViewState, Writing1SingleEvent>(Writing1ViewState.initial()) {

    private val intentS = PublishRelay.create<Writing1Intent>()
    private val stateS = BehaviorRelay.create<Writing1ViewState>()

    private val initProcessor =
        ObservableTransformer<Writing1Intent.Init, Writing1PartialChange> {
            it.exhaustMap {
                interactor.initialize()
            }
        }

    private val imageClickProcessor =
        ObservableTransformer<Writing1Intent.CardClick, Writing1PartialChange> {
            it.exhaustMap {
                interactor.cardClick(
                    index = it.index,
                    content = it.content,
                    rootView = it.rootView,
                    subView = it.subView,
                    cardTextView = it.cardTextView
                )
                    .doOnNext { change ->
                        // 오답 일 경우.
                        val actionError =
                            (change as? Writing1PartialChange.Select ?: return@doOnNext)
                    }
            }
        }

    private val hintClickProcessor =
        ObservableTransformer<Writing1Intent.HintClick, Writing1PartialChange> {
            it.switchMap {
                interactor.hint()
            }
        }


    private val intentToViewState = ObservableTransformer<Writing1Intent, Writing1ViewState> {
        it.publish { intentObservable ->
            Observable.mergeArray(
                intentObservable.ofType<Writing1Intent.Init>().compose(initProcessor),
                intentObservable.ofType<Writing1Intent.CardClick>().compose(imageClickProcessor),
                intentObservable.ofType<Writing1Intent.HintClick>().compose(hintClickProcessor)
            )
        }.scan(intialState) { state, change -> change.reducer(state) }
            .distinctUntilChanged()
            .observeOn(rxSchedulerProvider.main)
    }

    init {
        intentS.compose(intentFilter).share()
            .doOnNext { Timber.tag(Writing1Fragment.TAG).d("intent:: $it") }
            .compose(intentToViewState)
            .doOnNext { Timber.tag(Writing1Fragment.TAG).d("state:: $it") }
            .subscribeBy(onNext = stateS::accept)
            .addTo(compositeDisposable)

        stateS.subscribeBy(onNext = ::setNewState)
            .addTo(compositeDisposable)
    }

    override fun processIntents(intents: Observable<Writing1Intent>): Disposable =
        intents.subscribe(intentS)

    private companion object {
        private val intentFilter =
            ObservableTransformer<Writing1Intent, Writing1Intent> { intent ->
                intent.publish {
                    Observable.mergeArray(
                        it.ofType<Writing1Intent.Init>().take(1),
                        it.notOfType<Writing1Intent.Init, Writing1Intent>()
                    )
                }
            }
    }
}