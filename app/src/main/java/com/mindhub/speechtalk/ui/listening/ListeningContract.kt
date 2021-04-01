package com.mindhub.speechtalk.ui.listening

import com.attractive.deer.base.MviIntent
import com.attractive.deer.base.MviSingleEvent
import com.attractive.deer.base.MviViewState
import com.mindhub.speechtalk.domain.SpeechTalkAppError
import com.mindhub.speechtalk.ui.listening.Listening1ViewState.State.*
import io.reactivex.rxjava3.core.Observable

/**
 * 1) 서버로 듣기 1단계를 수행한다고 데이터를 전송.
 * 2) 서버로 부터 듣기 1단계 문제정보(정답,이미지 등)를 받음
 * 3) 문제정보를 화면에 세팅.
 * 4) 그리고 이미지 클릭 시 서버로 데이터 전달.
 * 5) 정답 일경우와 정답이 아닐 경우로 응답을 받음.
 * - 그림 순서대로 고르기 같은 문제일 경우, 순서를 모두 맞췄을 때 최종 정답 flag를 받아야 함.
 *
 * @author Scarlett
 * @version 1.0.0
 * @since 2021-03-31 오후 7:09
 **/
sealed class Listening1Intent : MviIntent {
    object Init : Listening1Intent()
    data class ImageClick(val index: Int) : Listening1Intent()
    object HintClick : Listening1Intent()
}

data class Listening1ViewState(
    val answer: Int?,
    val hint:String?,
    val choice: Int?,
    val error: SpeechTalkAppError?,
    val state: State?
) : MviViewState {
    companion object {
        fun initial() = Listening1ViewState(
            answer = null,
            hint = null,
            choice = null,
            error = null,
            state = DEFAULT
        )
    }

    enum class State { DEFAULT, IDLE, INIT, LOADING, WRONG, CORRECT, ERROR, HINT }
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
                state.copy(choice = index, state = if (state.answer == index) CORRECT else WRONG)
            }
            Loading -> {
                state.copy(state = LOADING)
            }
            Idle -> state.copy(state = IDLE)
            is Hint -> state.copy(state = HINT,hint = hint)
        }
    }

    object Loading : Listening1PartialChange()
    data class Initialization(val info: String) : Listening1PartialChange()
    data class Action(val index: Int) : Listening1PartialChange()
    data class Hint(val hint:String) : Listening1PartialChange()
    data class Error(val error: SpeechTalkAppError) : Listening1PartialChange()
    object Idle : Listening1PartialChange()
}

interface Listening1Interactor {
    fun initialize(): Observable<Listening1PartialChange>
    fun image(index: Int): Observable<Listening1PartialChange>
    fun hint(): Observable<Listening1PartialChange>
}