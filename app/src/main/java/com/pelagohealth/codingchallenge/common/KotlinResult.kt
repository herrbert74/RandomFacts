package com.pelagohealth.codingchallenge.common

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.runCatching
import kotlinx.coroutines.CancellationException

typealias ApiResult<T> = Result<T, Throwable>

/**
 *
 */
//inline infix fun <T, V> T.apiRunCatching(block: T.() -> V) = runCatching(block)
//    .onFailure { if (it is CancellationException) throw it }

inline fun <V> apiRunCatching(block: () -> V) = runCatching(block)
    .onFailure { if (it is CancellationException) throw it }
