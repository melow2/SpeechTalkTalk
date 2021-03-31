package com.mindhub.speechtalk.ui.listening

import com.attractive.deer.base.MviIntent
import com.attractive.deer.base.MviSingleEvent
import com.attractive.deer.base.MviViewState
import com.mindhub.speechtalk.domain.SpeechTalkAppError
import com.mindhub.speechtalk.ui.listening.Listening1ViewState.State.*
import io.reactivex.rxjava3.core.Observable


sealed class Listening1Intent : MviIntent {
    object Init : Listening1Intent()
    data class ImageClick(val index: Int) : Listening1Intent()
    object HintClick : Listening1Intent()
}

data class Listening1ViewState(
    val answer: Int?,
    val choice: Int?,
    val error: SpeechTalkAppError?,
    val state: State?
) : MviViewState {
    companion object {
        fun initial() = Listening1ViewState(
            answer = null,
            choice = null,
            error = null,
            state = DEFAULT
        )
    }
    enum class State { DEFAULT, IDLE, INIT, LOADING, WRONG, ANSWER, ERROR, HINT }
}

sealed class Listening1SingleEvent : MviSingleEvent {
    object WrongSound : Listening1SingleEvent()
    object AnswerSound : Listening1SingleEvent()
}

sealed class Listening1PartialChange {
    fun reducer(state: Listening1ViewState): Listening1ViewState {
        return when (this) {
            // todo 정답 2로 고정.
            is Initialization -> {
                state.copy(answer = 2, state = INIT)
            }
            is Error -> {
                state.copy(error = error, state = ERROR)
            }
            is Action -> {
                state.copy(choice = index, state = if (state.answer == index) ANSWER else WRONG)
            }
            Loading -> {
                state.copy(state = LOADING)
            }
            Idle -> state.copy(state = IDLE)
            Hint -> state.copy(state = HINT)
        }
    }
    object Loading : Listening1PartialChange()
    data class Initialization(val info: String) : Listening1PartialChange()
    data class Action(val index: Int) : Listening1PartialChange()
    object Hint : Listening1PartialChange()
    data class Error(val error: SpeechTalkAppError) : Listening1PartialChange()
    object Idle : Listening1PartialChange()
}

interface Listening1Interactor {
    fun initialize(): Observable<Listening1PartialChange>
    fun image(index: Int): Observable<Listening1PartialChange>
    fun hint(): Observable<Listening1PartialChange>
}