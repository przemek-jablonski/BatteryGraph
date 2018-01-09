package com.android.szparag.batterygraph.common.utils

import java.util.Arrays

fun nullString() = "NULL"
fun emptyString() = ""
fun invalidStringValue() = emptyString()
fun invalidIntValue() = -1
fun invalidLongValue() = -1L
fun invalidFloatValue() = -1f

fun getUnixTimestampMillis() = System.currentTimeMillis() //todo: does this work in every timezone?

fun getUnixTimestampSecs() = getUnixTimestampMillis() / 1000L


inline fun <T, R> Iterable<T>.map(transform: (T) -> R, initialCapacity: Int = count()) =
    mapTo(ArrayList(initialCapacity), transform)

fun <T : Any> Array<T>.arrayAsString() =
    Arrays.toString(this) ?: nullString()

fun <T : Any> List<T>.lastOr(default: T) =
    if (this.isNotEmpty()) this.last() else default