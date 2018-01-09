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

inline fun <A : Any, B : Any> ifNotNull(arg1: A?, arg2: B?, succeededBlock: (A, B) -> (Unit?)) =
    if (arg1 != null && arg2 != null) succeededBlock(arg1, arg2) else null

inline fun <A : Any, B : Any, C : Any> ifNotNull(arg1: A?, arg2: B?, arg3: C?, succeededBlock: (A, B, C) -> (Unit?)) =
    if (arg1 != null && arg2 != null && arg3 != null) succeededBlock(arg1, arg2, arg3) else null

inline fun <A : Any, B : Any, C : Any, D : Any> ifNotNull(arg1: A?, arg2: B?, arg3: C?, arg4: D?, succeededBlock: (A, B, C, D) -> (Unit?)) =
    if (arg1 != null && arg2 != null && arg3 != null && arg4 != null) succeededBlock(arg1, arg2, arg3, arg4) else null

fun <T : Comparable<T>> rangeListOf(vararg ranges: Iterable<T>) = ranges.flatMap { it }

fun <E> emptyMutableList() = mutableListOf<E>()