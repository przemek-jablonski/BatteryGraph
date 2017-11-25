package com.android.szparag.batterygraph.shared.utils

import android.app.Activity
import android.util.DisplayMetrics

typealias Pixel = Int
typealias Dp = Float

fun Activity.getDisplayMetrics() = resources.displayMetrics

fun Dp.toPx(displayMetrics: DisplayMetrics) = (this * displayMetrics.density).toInt()
fun Pixel.toDp(displayMetrics: DisplayMetrics) = (this * displayMetrics.density).toInt()
