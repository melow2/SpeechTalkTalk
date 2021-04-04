package com.mindhub.speechtalk.ui.writing.type1

import android.view.View
import com.attractive.deer.base.MviIntent
import com.attractive.deer.base.MviSingleEvent
import com.attractive.deer.base.MviViewState
import com.mindhub.speechtalk.SelectedCard
import com.mindhub.speechtalk.SelectedCard.CardType.WORD
import com.mindhub.speechtalk.domain.SpeechTalkAppError
import com.mindhub.speechtalk.ui.writing.type1.Writing1ViewState.State.*
import io.reactivex.rxjava3.core.Observable

/**
 * 1) 사용자가 낱말을 클릭.
 * 2) 정답 Map에서 낱말이 있는지 확인.
 * 3) 있으면 SEARCH_SUCCESS, 없으면 SEARCH_FAIL.
 * ==> 정답이 있을 경우.
 *  - 있을 경우 인덱스와 낱말이 담긴 map을 return.
 *  - 인덱스와 낱말이 담긴 map을 찾아 transition.
 * ==> 정답이 없을 경우.
 *  - 없을 경우 해당 인덱스를 찾아서, fadeOut.
 * 4) 힌트 클릭 시.
 * ==> 정답 맵에서 false인 index를 찾아서 hint로 emit.
 * 5) 정답을 모두 찾았을 경우.
 * ==> 맵에서 체크한 후 State를 바꿈.
 * @author Scarlett
 * @version 1.0.0
 * @since 2021-03-31 오후 7:09
 **/
sealed class Writing1Intent : MviIntent {
    object Init : Writing1Intent()
    data class CardClick(
        val index: Int,
        val content: String,
        val rootView: View,
        val subView: View,
        val cardTextView: View
    ) : Writing1Intent()

    object HintClick : Writing1Intent()
}

data class Writing1ViewState(
    val answer: HashMap<Map<Int, String>, Boolean>,
    val selected: SelectedCard?,
    val searchedAnswer: Map<Int, String>?,
    val error: SpeechTalkAppError?,
    val state: State?
) : MviViewState {
    companion object {
        fun initial() = Writing1ViewState(
            answer = hashMapOf(),
            selected = null,
            searchedAnswer = null,
            error = null,
            state = DEFAULT
        )
    }

    enum class State {
        DEFAULT, IDLE, INIT, LOADING,
        SEARCH_WRONG, SEARCH_CORRECT, SEARCH_CORRECT_CLEAR,
        ERROR, HINT
    }
}

sealed class Writing1SingleEvent : MviSingleEvent {
    object Wrong : Writing1SingleEvent()
    object Answer : Writing1SingleEvent()
}

sealed class Writing1PartialChange {
    fun reducer(state: Writing1ViewState): Writing1ViewState {
        return when (this) {

            is Initialization -> {
                // todo 정답 셋팅.
                val answerMap = LinkedHashMap<Map<Int, String>, Boolean>()
                answerMap[mapOf(Pair(1, "망"))] = false
                answerMap[mapOf(Pair(2, "고"))] = false
                state.copy(answer = answerMap , state = INIT)
            }

            is Error -> {
                state.copy(error = error, state = ERROR)
            }

            is Select -> {
                var searchedAnswer = mapOf<Int, String>()
                // #1 선택한 카드 정보.
                val selectedCard = SelectedCard(
                    locationIdx = index,
                    content = content,
                    rootView = rootView,
                    subView = subView,
                    cardTextView = cardTextView,
                    type = WORD
                )
                // #2 카드 정보가 있는 확인
                for (map in state.answer) {
                    val answerMap = map.key
                    if (answerMap.containsValue(content) && !map.value) {
                        searchedAnswer = answerMap
                        state.answer.remove(answerMap)
                        map.setValue(true)
                        break
                    }
                }
                // #3 카드 정보를 셋팅.
                state.copy(
                    answer = state.answer,
                    selected = selectedCard,
                    searchedAnswer = searchedAnswer,
                    state = when {
                        searchedAnswer.isNullOrEmpty() -> SEARCH_WRONG
                        state.answer.size == 0 -> SEARCH_CORRECT_CLEAR
                        else -> SEARCH_CORRECT
                    }
                )
            }
            Loading -> {
                state.copy(state = LOADING)
            }
            Idle -> state.copy(state = IDLE)
            is Hint -> state.copy(state = HINT)
        }
    }

    object Loading : Writing1PartialChange()
    data class Initialization(val info: String) : Writing1PartialChange()
    data class Select(
        val index: Int,
        val content: String,
        val rootView: View,
        val subView: View,
        val cardTextView: View
    ) :
        Writing1PartialChange()

    object Hint : Writing1PartialChange()
    data class Error(val error: SpeechTalkAppError) : Writing1PartialChange()
    object Idle : Writing1PartialChange()
}

interface Writing1Interactor {
    fun initialize(): Observable<Writing1PartialChange>
    fun cardClick(
        index: Int,
        content: String,
        rootView: View,
        subView: View,
        cardTextView: View
    ): Observable<Writing1PartialChange>

    fun hint(): Observable<Writing1PartialChange>
}