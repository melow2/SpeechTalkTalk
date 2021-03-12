package com.khs.kakaopay.ui.detail

import com.khs.kakaopay.domain.KakaoPayAppError
import com.khs.kakaopay.domain.model.book.KakaoBook
import com.khs.kakaopay.ui.detail.DetailViewState.State.*
import com.attractive.deer.base.MviIntent
import com.attractive.deer.base.MviSingleEvent
import com.attractive.deer.base.MviViewState
import io.reactivex.rxjava3.core.Observable

sealed class DetailViewIntent : MviIntent {
    data class Init(val book: KakaoBook.Document) : DetailViewIntent()
    object Like : DetailViewIntent()
}

data class DetailViewState(
    val books: KakaoBook.Document?,
    val state: State,
    val error: KakaoPayAppError?
) : MviViewState {
    companion object {
        fun initial() = DetailViewState(
            books = null,
            state = INIT,
            error = null
        )
    }
    enum class State { INIT, DATA, LOADING, ERROR }
}

sealed class DetailPartialChange {
    fun reducer(state: DetailViewState): DetailViewState {
        return when (this) {
            is Content -> state.copy(books = data, state = DATA)
            is Error -> state.copy(state = ERROR, error = error)
            Loading -> state.copy(state = LOADING)
        }
    }
    data class Content(val data: KakaoBook.Document?) : DetailPartialChange()
    data class Error(val error: KakaoPayAppError) : DetailPartialChange()
    object Loading : DetailPartialChange()
}

sealed class DetailSingleEvent:MviSingleEvent{
    data class MessageEvent(val message:String):DetailSingleEvent()
}

interface DetailInteractor{
    fun toggleLike(book: KakaoBook.Document?):Observable<DetailPartialChange>
    fun init(book:KakaoBook.Document):Observable<DetailPartialChange>
}