package com.android.szparag.batterygraph.common.utils

import java.util.Random

fun Int.clamp(max: Int, min: Int) =
    this.coerceAtLeast(min).coerceAtMost(max)

fun Long.clamp(max: Long, min: Long) =
    this.coerceAtLeast(min).coerceAtMost(max)

fun Float.clamp(max: Float, min: Float) =
    this.coerceAtLeast(min).coerceAtMost(max)

fun Double.clamp(max: Double, min: Float) =
    this.coerceAtLeast(min.toDouble()).coerceAtMost(max)

fun lerp(first: Int, second: Int, factor: Float) =
    first + factor * (second - first)

fun lerp(first: Long, second: Long, factor: Float) =
    first + factor * (second - first)

fun lerp(first: Float, second: Float, factor: Float) =
    first + factor * (second - first)

fun lerp(first: Double, second: Double, factor: Double) =
    first + factor * (second - first)

fun lerpLong(first: Long, second: Long, factor: Float) =
    lerp(first, second, factor).toLong()

fun inverseLerp(first: Int, second: Int, factor: Float)
    = (factor.clamp(Math.max(first, second).toFloat(), Math.min(first, second).toFloat()) - first) / (second - first)

fun inverseLerp(first: Long, second: Long, factor: Float)
    = (factor.clamp(Math.max(first, second).toFloat(), Math.min(first, second).toFloat()) - first) / (second - first)

fun inverseLerp(first: Float, second: Float, factor: Float)
    = (factor.clamp(Math.max(first, second), Math.min(first, second)) - first) / (second - first)

fun inverseLerp(first: Double, second: Double, factor: Float)
    = (factor.clamp(Math.max(first, second).toFloat(), Math.min(first, second).toFloat()) - first) / (second - first)

fun Random.nextFloat(min: Float, max: Float) =
    nextFloat() * (max - min) + min

fun Random.nextDouble(min: Double, max: Double) =
    nextDouble() * (max - min) + min

fun Random.nextInt(min: Int, max: Int) =
    nextInt() * (max - min) + min

fun Random.nextLong(min: Long, max: Long) =
    nextLong() * (max - min) + min

fun Float.randomVariation(random: Random, factor: Float) =
    random.nextFloat(this - this * factor, this + this * factor)

fun Double.randomVariation(random: Random, factor: Float) =
    random.nextDouble(this - this * factor, this + this * factor)

fun Int.randomVariation(random: Random, factor: Float) =
    random.nextInt((this - this * factor).toInt(), (this + this * factor).toInt())

fun Long.randomVariation(random: Random, factor: Float) =
    random.nextLong((this - this * factor).toLong(), (this + this * factor).toLong())
