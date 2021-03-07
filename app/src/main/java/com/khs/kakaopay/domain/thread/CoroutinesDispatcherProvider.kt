package com.khs.kakaopay.domain.thread

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutinesDispatcherProvider {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
}