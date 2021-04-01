package com.mindhub.speechtalk.ui.speaking.type1

import com.attractive.deer.base.MviIntent
import com.attractive.deer.base.MviSingleEvent
import com.attractive.deer.base.MviViewState
import com.mindhub.speechtalk.domain.SpeechTalkAppError
import com.mindhub.speechtalk.ui.speaking.type1.Speaking1ViewState.State.*
import io.reactivex.rxjava3.core.Observable
import java.io.File

sealed class Speaking1Intent : MviIntent {
    object Init : Speaking1Intent()
    object Record : Speaking1Intent()
    data class StopWithRecognition(val recordFile: File?, val voiceRecord: String?) : Speaking1Intent()
    object Play: Speaking1Intent()
}

data class Speaking1ViewState(
    val answer: String?,
    val recordFile: File?,
    val voiceRecord: String?,
    val error: SpeechTalkAppError?,
    val state: State?
) : MviViewState {
    companion object {
        fun initial() = Speaking1ViewState(
            answer = null,
            recordFile = null,
            voiceRecord = null,
            error = null,
            state = DEFAULT
        )
    }

    enum class State { DEFAULT, IDLE, INIT, LOADING, WRONG, CORRECT, ERROR, HINT, PLAYING, STOP_WITH_RECOGNITION, RECORD }
}

sealed class Speaking1SingleEvent : MviSingleEvent {
    object WrongSound : Speaking1SingleEvent()
    object AnswerSound : Speaking1SingleEvent()
}

sealed class Speaking1PartialChange {
    fun reducer(state: Speaking1ViewState): Speaking1ViewState {
        return when (this) {
            // todo 정답 망고로 고정.
            is Initialization -> {
                state.copy(answer = info, state = INIT)
            }
            is Error -> {
                state.copy(error = error, state = ERROR)
            }
            is StopWithRecognition -> state.copy(recordFile = mediaFile, voiceRecord = voiceRecord,state = STOP_WITH_RECOGNITION)
            Play -> state.copy(state = PLAYING)
            Record -> state.copy(state = RECORD)
            Loading -> state.copy(state = LOADING)
            Idle -> state.copy(state = IDLE)
            Hint -> state.copy(state = HINT)

        }
    }

    object Loading : Speaking1PartialChange()
    data class Initialization(val info: String) : Speaking1PartialChange()
    data class StopWithRecognition(val mediaFile: File?, val voiceRecord: String?) : Speaking1PartialChange()
    object Play : Speaking1PartialChange()
    object Record : Speaking1PartialChange()
    object Hint : Speaking1PartialChange()
    data class Error(val error: SpeechTalkAppError) : Speaking1PartialChange()
    object Idle : Speaking1PartialChange()
}

interface Speaking1Interactor {
    fun initialize(): Observable<Speaking1PartialChange>
    fun stopAndRecognition(mediaFile: File?, voiceRecord:String?): Observable<Speaking1PartialChange>
    fun record():Observable<Speaking1PartialChange>
    fun play(): Observable<Speaking1PartialChange>
}