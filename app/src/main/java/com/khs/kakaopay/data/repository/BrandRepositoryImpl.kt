package com.khs.kakaopay.data.repository

import com.khs.kakaopay.data.ErrorMapper
import com.khs.kakaopay.data.remote.api.BrandApiService
import com.khs.kakaopay.data.remote.response.brand.BrandMapper
import com.khs.kakaopay.domain.KakaoPayResult
import com.khs.kakaopay.domain.repository.BrandRepository
import com.khs.kakaopay.domain.thread.CoroutinesDispatcherProvider
import com.khs.kakaopay.domain.model.brand.BrandDetail
import com.khs.kakaopay.domain.model.brand.BrandFilter
import com.khs.kakaopay.domain.model.brand.BrandList
import com.lovely.deer.util.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.time.ExperimentalTime
import kotlin.time.seconds


@OptIn(ExperimentalTime::class)
@ObsoleteCoroutinesApi
class BrandRepositoryImpl(
    private val errorMapper: ErrorMapper,
    private val brandApiService: BrandApiService,
    private val dispatcherProvider: CoroutinesDispatcherProvider,
    appCoroutineScope: CoroutineScope
) : BrandRepository {

    private val cache = Cache<RequestCacheKey, Any>(
        maxSize = 8,
        entryLifetime = 60.seconds
    )

    override suspend fun getBrandList(offset: Int, limit: Int, search:String?,filters:Int?): KakaoPayResult<BrandList> {
        return executeApiRequest(
            "getBrandList",
            mapOf(
                "offest" to offset,
                "limit" to limit,
                "searchText" to search,
                "filters" to filters
            )
        ) {
            getBrandList(
                offset = offset,
                limit = limit,
                keyword = search,
                filters = filters
            ).let(BrandMapper::responseToBrandListModel)
        }
    }

    override suspend fun getBrandDetail(id: String): KakaoPayResult<BrandDetail> {
        return executeApiRequest(
            "getBrandDetail",
            mapOf(
                "id" to id
            )
        ){
            getBrandDetail(brandId = id).let(BrandMapper::responseToBrandDetailModel)
        }
    }

    override suspend fun getBrandFilters(): KakaoPayResult<BrandFilter> {
        return runCatching {
            withContext(dispatcherProvider.io) {
                brandApiService.getBrandFilterList().let(BrandMapper::responseToBrandFilterModel)
            }
        }.fold(
            onSuccess = { Right(it) },
            onFailure = { errorMapper.mapAsLeft(it) }
        )
    }

    private suspend inline fun <reified T : Any> executeApiRequest(
        path: String,
        queryItems: Map<String, Any?> = emptyMap(),
        crossinline request: suspend BrandApiService.() -> T
    ): KakaoPayResult<T> {
        val cacheKey = buildKey(path, queryItems)
        return try {
            when (val cachedResponse = cache[cacheKey] as? T) {
                null -> {
                    Timber.d("BrandApiService::$cacheKey [MISS] request...")
                    withContext(dispatcherProvider.io) {
                        brandApiService.request()
                            .also { cache[cacheKey] = it }
                            .right()
                    }
                }
                else -> {
                    Timber.d("BrandApiService::$cacheKey [HIT] cached.")
                    delay(250)
                    cachedResponse.right()
                }
            }
        } catch (throwable: Throwable) {
            Timber.d(throwable, "BrandApiService::$cacheKey [ERROR] $throwable")
            delay(500)
            errorMapper.mapAsLeft(throwable)
        }
    }

}