package com.khs.kakaopay.ui.main

import com.khs.kakaopay.domain.KakaoPayAppError
import com.khs.kakaopay.domain.model.book.KakaoBook
import com.khs.kakaopay.ui.main.MainViewState.State.*
import com.attractive.deer.base.MviIntent
import com.attractive.deer.base.MviSingleEvent
import com.attractive.deer.base.MviViewState
import io.reactivex.rxjava3.core.Observable

sealed class MainViewIntent : MviIntent {
    object Init : MainViewIntent()
    data class SearchIntent(val searchText: String) : MainViewIntent()
    object LoadNextPage : MainViewIntent()
}

data class MainViewState(
    var books: List<MainViewItem>,
    val state: State,
    val error: KakaoPayAppError?,
    val isEnd: Boolean?,
    val updatePage: Int
) : MviViewState {
    companion object {
        fun initial() = MainViewState(
            books = emptyList(),
            state = INIT,
            isEnd = false,
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
                    isEnd = data.meta?.is_end,
                    state = DATA
                )
            }
            is Error -> {
                state.copy(
                    books = if (append) state.books.filterNot(MainViewItem::isLoadingOrError).plus(MainViewItem.Error(error)) else listOf(MainViewItem.Error(error)),
                    error = error,
                    state = ERROR
                )
            }
            is Loading -> {
                state.copy(
                    books = if (append) state.books.filterNot(MainViewItem::isLoadingOrError).plus(MainViewItem.Loading) else listOf(MainViewItem.Loading),
                    state = LOADING
                )
            }
        }
    }

    data class Content(val data: KakaoBook, val append: Boolean) : MainPartialChange()
    data class Error(val error: KakaoPayAppError, val append: Boolean) : MainPartialChange()
    data class Loading(val append: Boolean) : MainPartialChange()
}

sealed class MainSingleEvent : MviSingleEvent {
    data class MessageEvent(val message: String) : MainSingleEvent()
}

interface MainInteractor {
    fun getKakaoBooks(page: Int, sizePerPage: Int, query: String): Observable<MainPartialChange>
}

fun MainViewItem.isLoadingOrError(): Boolean {
    return this is MainViewItem.Loading || this is MainViewItem.Error
}