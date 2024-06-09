package com.pelagohealth.codingchallenge.data.db

import io.realm.kotlin.types.RealmInstant

const val MILLIS_IN_SECOND = 1000

const val NANOS_IN_MILLIS: Int = 1_000_000

fun RealmInstant.Companion.fromMillis(milliSeconds: Long): RealmInstant =
    from(
        milliSeconds / MILLIS_IN_SECOND,
        (milliSeconds.rem(MILLIS_IN_SECOND) * NANOS_IN_MILLIS).toInt()
    )

fun RealmInstant.toEpochMillis(): Long =
    this.epochSeconds * MILLIS_IN_SECOND + (this.nanosecondsOfSecond / NANOS_IN_MILLIS)
