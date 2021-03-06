package com.example.kakaopay.domain.thread

import io.reactivex.rxjava3.core.Scheduler

interface RxSchedulerProvider {
    val main: Scheduler
    val io: Scheduler
}