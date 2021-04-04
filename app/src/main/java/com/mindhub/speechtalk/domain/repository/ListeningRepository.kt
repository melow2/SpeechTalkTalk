package com.mindhub.speechtalk.domain.repository

import com.mindhub.speechtalk.domain.SpeechTalkResult
import com.mindhub.speechtalk.domain.model.KakaoBook


interface ListeningRepository {
    suspend fun getSearchBookList(
        query: String,
        size: Int,
        page: Int,
    ): SpeechTalkResult<KakaoBook>
}