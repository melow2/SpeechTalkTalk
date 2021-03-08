package com.khs.kakaopay.ui.main

import com.khs.kakaopay.domain.KakaoPayAppError
import com.khs.kakaopay.domain.model.book.KakaoBook
import com.khs.kakaopay.ui.main.MainViewState.State.*
import com.lovely.deer.base.MviIntent
import com.lovely.deer.base.MviSingleEvent
import com.lovely.deer.base.MviViewState
import io.reactivex.rxjava3.core.Observable

sealed class MainIntent : MviIntent {
    object Init:MainIntent()
    data class SearchIntent(val searchText: String) : MainIntent()
    object LoadNextPage : MainIntent()
}

data class MainViewState(
    val books: List<MainViewItem>,
    val state: State,
    val error: KakaoPayAppError?,
    val updatePage: Int
) : MviViewState {
    companion object {
        fun initial() = MainViewState(
            books = emptyList(),
            state = INIT,
            error = null,
            updatePage = 0
        )
    }
    enum class State { INIT, DATA, LOADING, ERROR }
}

sealed class MainViewItem {
    data class Content(val book: KakaoBook.Document) : MainViewItem()
    object Loading : MainViewItem()
    data class Error(val errorMessage: KakaoPayAppError) : MainViewItem()
}

sealed class MainPartialChange {
    fun reducer(state: MainViewState): MainViewState {
        return when (this) {
            is Content -> {
                val newBooks = data.documents?.map { MainViewItem.Content(it) } ?: emptyList()
                state.copy(
                    books = if (append) state.books.filterNot(MainViewItem::isLoadingOrError).plus(newBooks) else newBooks,
                    updatePage = if (append) state.updatePage + 1 else 1,
                    state = DATA
                )
            }
            is Error -> {
                state.copy(
                    books = state.books.filterNot(MainViewItem::isLoadingOrError).plus(MainViewItem.Error(error)),
                    error = error,
                    state = ERROR
                )
            }
            Loading -> {
                state.copy(
                    books = state.books.filterNot(MainViewItem::isLoadingOrError)
                        .plus(MainViewItem.Loading),
                    state = LOADING
                )
            }
        }
    }
    data class Content(val data: KakaoBook, val append: Boolean) : MainPartialChange()
    data class Error(val error: KakaoPayAppError) : MainPartialChange()
    object Loading : MainPartialChange()
}

sealed class MainSingleEvent:MviSingleEvent {
    data class MessageEvent(val message: String) : MainSingleEvent()
}

interface MainInteractor {
    fun getKakaoBooks(page: Int, sizePerPage: Int, query: String): Observable<MainPartialChange>
}

fun MainViewItem.isLoadingOrError(): Boolean {
    return this is MainViewItem.Loading || this is MainViewItem.Error
}