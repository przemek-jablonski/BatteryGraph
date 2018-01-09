package com.android.szparag.batterygraph.common.utils

import android.app.Activity
import android.util.DisplayMetrics
import android.view.View
import android.view.Window

typealias Pixel = Int
typealias Dp = Float

fun Activity.getDisplayMetrics() = resources.displayMetrics

inline fun Activity.getDisplayDimensions() = getDisplayMetrics().getDisplayDimensions()

inline fun DisplayMetrics.getDisplayDimensions() = Pair(this.widthPixels, this.heightPixels)


fun Dp.toPx(displayMetrics: DisplayMetrics) = (this * displayMetrics.density).toInt()
fun Pixel.toDp(displayMetrics: DisplayMetrics) = (this * displayMetrics.density).toInt()

inline fun Activity.getStatusbarHeight(): Int {
  val visibleDisplayFrame = window.getVisibleDisplayFrame()
  val statusBarHeight = visibleDisplayFrame.top
  val contentViewTop = window.findViewById<View>(Window.ID_ANDROID_CONTENT).top
  val titleBarHeight = contentViewTop - statusBarHeight
  return Math.abs(titleBarHeight)
}
