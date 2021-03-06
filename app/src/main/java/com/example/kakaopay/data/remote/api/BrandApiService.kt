package com.example.kakaopay.data.remote.api

import com.example.kakaopay.data.remote.response.brand.BrandDetailResponse
import com.example.kakaopay.data.remote.response.brand.BrandFilterResponse
import com.jeit.coconut.data.remote.response.brand.BrandListResponse
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface BrandApiService {

    @GET("/api/v1/brand/list")
    suspend fun getBrandList(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
        @Query("keyword") keyword: String?,
        @Query("filters") filters: Int?
    ): BrandListResponse

    @GET("/api/v1/brand/detail/{Id}")
    suspend fun getBrandDetail(@Path("Id") brandId: String): BrandDetailResponse

    @GET("/api/v1/filter/list")
    suspend fun getBrandFilterList(): BrandFilterResponse

    companion object {
        operator fun invoke(retrofit: Retrofit) = retrofit.create<BrandApiService>()
    }
}