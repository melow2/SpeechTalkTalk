package com.mindhub.speechtalk.data.repository

import com.mindhub.speechtalk.data.ErrorMapper
import com.mindhub.speechtalk.data.remote.api.ListeningApiService
import com.mindhub.speechtalk.data.remote.response.book.KakaoBookMapper
import com.mindhub.speechtalk.domain.SpeechTalkResult
import com.mindhub.speechtalk.domain.model.book.KakaoBook
import com.mindhub.speechtalk.domain.repository.ListeningRepository
import com.mindhub.speechtalk.domain.thread.CoroutinesDispatcherProvider
import com.attractive.deer.util.data.Cache
import com.attractive.deer.util.data.RequestCacheKey
import com.attractive.deer.util.data.buildKey
import com.attractive.deer.util.data.right
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.time.ExperimentalTime
import kotlin.time.seconds


@OptIn(ExperimentalTime::class)
class ListeningRepositoryImpl(
    private val errorMapper: ErrorMapper,
    private val listeningApiservice: ListeningApiService,
    private val dispatcherProvider: CoroutinesDispatcherProvider
) : ListeningRepository {

    private val cache = Cache<RequestCacheKey, Any>(
        maxSize = 8,
        entryLifetime = 60.seconds
    )

    override suspend fun getSearchBookList(
        query: String,
        size: Int,
        page: Int
    ): SpeechTalkResult<KakaoBook> {
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
        crossinline request: suspend ListeningApiService.() -> T
    ): SpeechTalkResult<T> {
        val cacheKey = buildKey(path, queryItems)
        return try {
            when (val cachedResponse = cache[cacheKey] as? T) {
                null -> {
                    Timber.d("ListeningApiService::$cacheKey [MISS] request...")
                    withContext(dispatcherProvider.io) {
                        listeningApiservice.request()
                            .also { cache[cacheKey] = it }
                            .right()
                    }
                }
                else -> {
                    Timber.d("ListeningApiService::$cacheKey [HIT] cached.")
                    delay(250)
                    cachedResponse.right()
                }
            }
        } catch (throwable: Throwable) {
            Timber.d(throwable, "ListeningApiService::$cacheKey [ERROR] $throwable")
            delay(500)
            errorMapper.mapAsLeft(throwable)
        }
    }

}