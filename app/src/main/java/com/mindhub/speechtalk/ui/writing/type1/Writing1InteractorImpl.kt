package com.mindhub.speechtalk.ui.writing.type1

import android.view.View
import com.mindhub.speechtalk.domain.repository.ListeningRepository
import com.mindhub.speechtalk.domain.thread.CoroutinesDispatcherProvider
import com.mindhub.speechtalk.ui.writing.type1.Writing1PartialChange.*
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.rx3.rxObservable

@ExperimentalCoroutinesApi
class Writing1InteractorImpl(
    private val dispatcherProvider: CoroutinesDispatcherProvider,
    private val listeningRepository: ListeningRepository
) : Writing1Interactor {

    override fun initialize(): Observable<Writing1PartialChange> {
        return rxObservable(dispatcherProvider.main) {
            send(Loading)
            send(Initialization("초기화 데이터."))
        }
    }

    override fun cardClick(
        index: Int,
        content: String,
        rootView: View,
        subView: View,
        cardTextView:View,
    ): Observable<Writing1PartialChange> {
        return rxObservable(dispatcherProvider.main) {
            send(Select(index = index, content = content, rootView = rootView, subView = subView,cardTextView = cardTextView))
        }
    }

    override fun hint(): Observable<Writing1PartialChange> {
        return rxObservable(dispatcherProvider.main) {
            send(Idle)
            send(Hint)
        }
    }
}