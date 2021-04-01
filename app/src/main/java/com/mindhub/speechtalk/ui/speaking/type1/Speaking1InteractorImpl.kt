package com.mindhub.speechtalk.ui.speaking.type1

import com.mindhub.speechtalk.domain.repository.ListeningRepository
import com.mindhub.speechtalk.domain.thread.CoroutinesDispatcherProvider
import com.mindhub.speechtalk.ui.speaking.type1.Speaking1PartialChange.*
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.rx3.rxObservable
import java.io.File

class Speaking1InteractorImpl(
    private val dispatcherProvider: CoroutinesDispatcherProvider,
    private val listeningRepository: ListeningRepository
) : Speaking1Interactor {

    override fun initialize(): Observable<Speaking1PartialChange> {
        return rxObservable(dispatcherProvider.main) {
            send(Loading)
            send(Initialization("망고"))
        }
    }

    override fun stopAndRecognition(
        mediaFile: File?,
        voiceRecord: String?
    ): Observable<Speaking1PartialChange> {
        return rxObservable {
            send(StopWithRecognition(mediaFile, voiceRecord))
        }
    }

    override fun record(): Observable<Speaking1PartialChange> {
        return rxObservable(dispatcherProvider.main) {
            send(Record)
        }
    }

    override fun play(): Observable<Speaking1PartialChange> {
        return rxObservable(dispatcherProvider.main) {
            send(Idle)
            send(Play)
        }
    }

}