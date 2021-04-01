package com.mindhub.speechtalk.ui.speaking.type1

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

class Speaking1ViewModel(
    private val interactor: Speaking1Interactor,
    private val rxSchedulerProvider: RxSchedulerProvider
) : BaseViewModel<Speaking1Intent, Speaking1ViewState, Speaking1SingleEvent>(Speaking1ViewState.initial()) {

    private val intentS = PublishRelay.create<Speaking1Intent>()
    private val stateS = BehaviorRelay.create<Speaking1ViewState>()

    private val initProcessor =
        ObservableTransformer<Speaking1Intent.Init, Speaking1PartialChange> {
            it.flatMap {
                interactor.initialize()
            }
        }

    private val recordProcessor =
        ObservableTransformer<Speaking1Intent.Record, Speaking1PartialChange> {
            it.flatMap {
                interactor.record()
            }
        }

    private val recognitionProcessor =
        ObservableTransformer<Speaking1Intent.StopWithRecognition, Speaking1PartialChange> {
            it.flatMap {
                interactor.stopAndRecognition(it.recordFile, it.voiceRecord)
            }
        }

    private val playProcessor =
        ObservableTransformer<Speaking1Intent.Play, Speaking1PartialChange> {
            it.flatMap {
                interactor.play()
            }
        }

    private val stopProcessor =
        ObservableTransformer<Speaking1Intent.Stop, Speaking1PartialChange> {
            it.flatMap {
                interactor.stop()
            }
        }

    private val recordCancelProcessor =
        ObservableTransformer<Speaking1Intent.RecordCancle, Speaking1PartialChange> {
            it.flatMap {
                interactor.recordCancel()
            }
        }

    private val hintProcessor =
        ObservableTransformer<Speaking1Intent.Hint, Speaking1PartialChange> {
            it.flatMap {
                interactor.hint(it.index)
            }
        }

    private val intentToViewState = ObservableTransformer<Speaking1Intent, Speaking1ViewState> {
        it.publish { intentObservable ->
            Observable.mergeArray(
                intentObservable.ofType<Speaking1Intent.Init>().compose(initProcessor),
                intentObservable.ofType<Speaking1Intent.Record>().compose(recordProcessor),
                intentObservable.ofType<Speaking1Intent.Play>().compose(playProcessor),
                intentObservable.ofType<Speaking1Intent.Stop>().compose(stopProcessor),
                intentObservable.ofType<Speaking1Intent.Hint>().compose(hintProcessor),
                intentObservable.ofType<Speaking1Intent.RecordCancle>().compose(recordCancelProcessor),
                intentObservable.ofType<Speaking1Intent.StopWithRecognition>().compose(recognitionProcessor)
            )
        }.scan(intialState) { state, change -> change.reducer(state) }
            .distinctUntilChanged()
            .observeOn(rxSchedulerProvider.main)
    }

    init {
        intentS.compose(intentFilter).share()
            .doOnNext { Timber.tag(Speaking1Fragment.TAG).d("intent:: $it") }
            .compose(intentToViewState)
            .doOnNext { Timber.tag(Speaking1Fragment.TAG).d("state:: $it") }
            .subscribeBy(onNext = stateS::accept)
            .addTo(compositeDisposable)

        stateS.subscribeBy(onNext = ::setNewState)
            .addTo(compositeDisposable)
    }

    override fun processIntents(intents: Observable<Speaking1Intent>): Disposable =
        intents.subscribe(intentS)

    private companion object {
        private val intentFilter =
            ObservableTransformer<Speaking1Intent, Speaking1Intent> { intent ->
                intent.publish {
                    Observable.mergeArray(
                        it.ofType<Speaking1Intent.Init>().take(1),
                        it.notOfType<Speaking1Intent.Init, Speaking1Intent>()
                    )
                }
            }
    }
}