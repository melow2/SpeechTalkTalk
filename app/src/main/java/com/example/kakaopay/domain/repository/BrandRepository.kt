package com.example.kakaopay.domain.repository

import com.example.kakaopay.domain.KakaoPayResult
import com.jeit.coconut.domain.models.brand.BrandDetail
import com.jeit.coconut.domain.models.brand.BrandFilter
import com.jeit.coconut.domain.models.brand.BrandList

interface BrandRepository {
    suspend fun getBrandList(
        offset: Int = 0,
        limit: Int = 20,
        search: String? = null,
        filters:Int?=null
    ): KakaoPayResult<BrandList>

    suspend fun getBrandDetail(
        id: String
    ): KakaoPayResult<BrandDetail>

    suspend fun getBrandFilters():KakaoPayResult<BrandFilter>
}