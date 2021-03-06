package com.example.kakaopay.domain.thread

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.rx3.asCoroutineDispatcher

class CoroutinesDispatcherProviderImpl(
    private val rxSchedulerProvider: RxSchedulerProvider,
    override val main: CoroutineDispatcher = rxSchedulerProvider.main.asCoroutineDispatcher(),
    override val io: CoroutineDispatcher = rxSchedulerProvider.io.asCoroutineDispatcher()
) : CoroutinesDispatcherProvider