package com.khs.kakaopay.domain.repository

import com.khs.kakaopay.domain.KakaoPayResult
import com.khs.kakaopay.domain.model.book.KakaoBook

interface KakaoBookRepository {
    suspend fun getSearchBookList(
        query: String,
        size: Int,
        page: Int,
    ): KakaoPayResult<KakaoBook>
}