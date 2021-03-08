package com.khs.kakaopay.data.repository

import com.khs.kakaopay.data.ErrorMapper
import com.khs.kakaopay.data.remote.api.KakaoBookApiService
import com.khs.kakaopay.data.remote.response.book.KakaoBookMapper
import com.khs.kakaopay.domain.KakaoPayResult
import com.khs.kakaopay.domain.model.book.KakaoBook
import com.khs.kakaopay.domain.repository.KakaoBookRepository
import com.khs.kakaopay.domain.thread.CoroutinesDispatcherProvider
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
class KakaoBookRepositoryImpl(
    private val errorMapper: ErrorMapper,
    private val kakaoBookApiservice: KakaoBookApiService,
    private val dispatcherProvider: CoroutinesDispatcherProvider
) : KakaoBookRepository {

    private val cache = Cache<RequestCacheKey, Any>(
        maxSize = 8,
        entryLifetime = 60.seconds
    )

    override suspend fun getSearchBookList(
        query: String,
        size: Int,
        page: Int
    ): KakaoPayResult<KakaoBook> {
        return executeApiRequest(
            "getSearchBookList",
            mapOf(
                "query" to query,
                "size" to size,
                "page" to page
            )
        ) {
            getSearchBookList(
                query = query,
                size = size,
                page = page
            ).let(KakaoBookMapper::responseToKakaoBookListModel)
        }
    }


    private suspend inline fun <reified T : Any> executeApiRequest(
        path: String,
        queryItems: Map<String, Any?> = emptyMap(),
        crossinline request: suspend KakaoBookApiService.() -> T
    ): KakaoPayResult<T> {
        val cacheKey = buildKey(path, queryItems)
        return try {
            when (val cachedResponse = cache[cacheKey] as? T) {
                null -> {
                    Timber.d("KakaoBookApiService::$cacheKey [MISS] request...")
                    withContext(dispatcherProvider.io) {
                        kakaoBookApiservice.request()
                            .also { cache[cacheKey] = it }
                            .right()
                    }
                }
                else -> {
                    Timber.d("KakaoBookApiService::$cacheKey [HIT] cached.")
                    delay(250)
                    cachedResponse.right()
                }
            }
        } catch (throwable: Throwable) {
            Timber.d(throwable, "KakaoBookApiService::$cacheKey [ERROR] $throwable")
            delay(500)
            errorMapper.mapAsLeft(throwable)
        }
    }

}