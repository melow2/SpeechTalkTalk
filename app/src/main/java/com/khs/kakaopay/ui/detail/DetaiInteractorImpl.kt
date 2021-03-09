package com.khs.kakaopay.ui.detail

import com.khs.kakaopay.domain.model.book.KakaoBook
import com.khs.kakaopay.domain.thread.CoroutinesDispatcherProvider
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.rx3.rxObservable

@ExperimentalCoroutinesApi
class DetaiInteractorImpl(
    private val dispatcherProvider: CoroutinesDispatcherProvider
) : DetailInteractor {

    /**
     * 좋아요 토글.
     *
     * 현재 데이터에서 isLike만 변경.
     *
     * - 좋아요 여부를 API로 받아올 수 없음.
     * - 좋아요 여부는 검색할 때마다 초기화 됨.
     * 따라서, 굳이 SharedPreference나 Room을 사용할필요가 없음.
     * 데이터가 저장되는 개념이 아니기에, UI만 바꿔주면 됨.
     *
     * @author 권혁신
     * @version 0.0.8
     * @since 2021-03-09 오전 9:39
     **/
    override fun toggleLike(book: KakaoBook.Document?): Observable<DetailPartialChange> {
        return rxObservable(dispatcherProvider.main) {
            send(DetailPartialChange.Loading)
            book?.let { book.like = !book.like }
            send(DetailPartialChange.Content(data = book))
        }
    }

    /**
     * 최초 뷰 초기화.
     *
     *
     * @author 권혁신
     * @version 0.0.8
     * @since 2021-03-09 오전 10:42
     **/
    override fun init(book: KakaoBook.Document): Observable<DetailPartialChange> {
        return rxObservable(dispatcherProvider.main) {
            send(DetailPartialChange.Content(data = book))
        }
    }
}
