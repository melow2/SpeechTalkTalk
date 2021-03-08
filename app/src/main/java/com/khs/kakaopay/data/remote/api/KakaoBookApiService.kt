package com.khs.kakaopay.data.remote.api

import com.khs.kakaopay.data.remote.response.book.KakaoBookResponse
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query


interface KakaoBookApiService {

    @GET("/v3/search/book")
    suspend fun getSearchBookList(
        @Query("query") query: String,
        @Query("size") size: Int,
        @Query("page") page: Int,
    ): KakaoBookResponse

    companion object {
        operator fun invoke(retrofit: Retrofit) = retrofit.create<KakaoBookApiService>()
    }
}