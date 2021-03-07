package com.khs.kakaopay.domain.repository

import com.khs.kakaopay.domain.KakaoPayResult
import com.khs.kakaopay.domain.model.brand.BrandDetail
import com.khs.kakaopay.domain.model.brand.BrandFilter
import com.khs.kakaopay.domain.model.brand.BrandList

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