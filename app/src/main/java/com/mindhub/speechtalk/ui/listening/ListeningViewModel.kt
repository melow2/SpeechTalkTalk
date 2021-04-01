package com.mindhub.speechtalk.ui.listening

import com.attractive.deer.base.BaseViewModel
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

class ListeningViewModel(
    private val interactor: Listening1Interactor,
    private val rxSchedulerProvider: RxSchedulerProvider
) : BaseViewModel<Listening1Intent, Listening1ViewState, Listening1SingleEvent>(Listening1ViewState.initial()) {

    private val intentS = PublishRelay.create<Listening1Intent>()
    private val stateS = BehaviorRelay.create<Listening1ViewState>()

    private val initProcessor =
        ObservableTransformer<Listening1Intent.Init, Listening1PartialChange> {
            it.flatMap {
                interactor.initialize()
            }
        }

    private val imageClickProcessor =
        ObservableTransformer<Listening1Intent.ImageClick, Listening1PartialChange> {
            it.switchMap {
                interactor.image(index = it.index).doOnNext { change ->
                    // 오답 일 경우.
                    val actionError = (change as? Listening1PartialChange.Action ?: return@doOnNext)
                }
            }
        }

    private val hintClickProcessor =
        ObservableTransformer<Listening1Intent.HintClick, Listening1PartialChange> {
            it.switchMap {
                interactor.hint()
            }
        }


    private val intentToViewState = ObservableTransformer<Listening1Intent, Listening1ViewState> {
        it.publish { intentObservable ->
            Observable.mergeArray(
                intentObservable.ofType<Listening1Intent.Init>().compose(initProcessor),
                intentObservable.ofType<Listening1Intent.ImageClick>().compose(imageClickProcessor),
                intentObservable.ofType<Listening1Intent.HintClick>().compose(hintClickProcessor)
            )
        }.scan(intialState) { state, change -> change.reducer(state) }
            .distinctUntilChanged()
            .observeOn(rxSchedulerProvider.main)
    }

    init {
        intentS.compose(intentFilter).share()
            .doOnNext { Timber.tag(ListeningFragment.TAG).d("intent:: $it") }
            .compose(intentToViewState)
            .doOnNext { Timber.tag(ListeningFragment.TAG).d("state:: $it") }
            .subscribeBy(onNext = stateS::accept)
            .addTo(compositeDisposable)

        stateS.subscribeBy(onNext = ::setNewState)
            .addTo(compositeDisposable)
    }

    override fun processIntents(intents: Observable<Listening1Intent>): Disposable =
        intents.subscribe(intentS)

    private companion object {
        private val intentFilter =
            ObservableTransformer<Listening1Intent, Listening1Intent> { intent ->
                intent.publish {
                    Observable.mergeArray(
                        it.ofType<Listening1Intent.Init>().take(1),
                        it.notOfType<Listening1Intent.Init, Listening1Intent>()
                    )
                }
            }
    }
}