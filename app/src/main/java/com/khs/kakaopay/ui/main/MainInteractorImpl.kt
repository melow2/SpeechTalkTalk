package com.khs.kakaopay.ui.main

import com.khs.kakaopay.domain.repository.KakaoBookRepository
import com.khs.kakaopay.domain.thread.CoroutinesDispatcherProvider
import com.khs.kakaopay.ui.main.MainPartialChange.Loading
import com.lovely.deer.util.data.fold
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.rx3.rxObservable

@ExperimentalCoroutinesApi
class MainInteractorImpl(
    private val kakaoBookRepository: KakaoBookRepository,
    private val dispatcherProvider: CoroutinesDispatcherProvider
) : MainInteractor {

    /**
     * 카카오 도서 검색.
     *
     * @param page: 페이지 번호.
     * @param size: 페이징 할 페이지 갯수.
     * @param query: 검색어.
     * @author 권혁신
     * @version 0.0.8
     * @since 2021-03-08 오후 4:48
     **/
    override fun getKakaoBooks(
        page: Int,
        size: Int,
        query: String
    ): Observable<MainPartialChange> {
        return rxObservable(dispatcherProvider.main) {
            send(Loading(append = page!=1))
            kakaoBookRepository.getSearchBookList(
                query = query,
                size = size,
                page = page
            ).fold({
                MainPartialChange.Error(error = it,append = page!=1)
            },{
                it.documents?.forEach { it.like = false}
                MainPartialChange.Content(it,append = page!=1)
            }).let {
                send(it)
            }
        }
    }
}
