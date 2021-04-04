package com.mindhub.speechtalk.ui.speaking.type1

import com.mindhub.speechtalk.domain.repository.ListeningRepository
import com.mindhub.speechtalk.domain.thread.CoroutinesDispatcherProvider
import com.mindhub.speechtalk.ui.speaking.type1.Speaking1PartialChange.*
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.rx3.rxObservable
import java.io.File

@ExperimentalCoroutinesApi
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

    override fun stop(): Observable<Speaking1PartialChange> {
        return rxObservable(dispatcherProvider.main) {
            send(Stop)
        }
    }

    override fun recordCancel(): Observable<Speaking1PartialChange> {
        return rxObservable(dispatcherProvider.main) {
            send(RecordCancel)
        }
    }

    override fun hint(index: Int): Observable<Speaking1PartialChange> {
        return rxObservable(dispatcherProvider.main) {
            send(Idle)
            val hint = when (index) {
                1 -> "힌트1"
                2 -> "힌트2"
                3 -> "힌트3"
                else -> "힌트없음"
            }
            send(Hint(hint))
        }
    }

}