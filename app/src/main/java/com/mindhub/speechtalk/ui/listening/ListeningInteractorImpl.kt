package com.mindhub.speechtalk.ui.listening

import com.mindhub.speechtalk.domain.repository.ListeningRepository
import com.mindhub.speechtalk.domain.thread.CoroutinesDispatcherProvider
import com.mindhub.speechtalk.ui.listening.Listening1PartialChange.*
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.rx3.rxObservable

class ListeningInteractorImpl(
    private val dispatcherProvider: CoroutinesDispatcherProvider,
    private val listeningRepository: ListeningRepository
):Listening1Interactor {

    override fun initialize(): Observable<Listening1PartialChange> {
        return rxObservable(dispatcherProvider.main) {
            send(Initialization("문제정보"))
        }
    }

    override fun image(index: Int): Observable<Listening1PartialChange> {
        return rxObservable(dispatcherProvider.main) {
            send(Action(index))
        }
    }

    override fun hint(): Observable<Listening1PartialChange> {
        return rxObservable(dispatcherProvider.main) {
            send(Idle)
            send(Hint)
        }
    }

}