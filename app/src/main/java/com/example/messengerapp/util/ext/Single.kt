package com.example.messengerapp.util.ext

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

fun <T> Single<T>.retryWithDelay(times: Int, delay: Long, unit: TimeUnit = TimeUnit.SECONDS) =
    this.retryWhen { errors ->
        val counter = AtomicInteger()
        errors
            .takeWhile { counter.getAndIncrement() != times }
            .flatMap {
                Flowable.timer(delay, unit)
            }
    }!!