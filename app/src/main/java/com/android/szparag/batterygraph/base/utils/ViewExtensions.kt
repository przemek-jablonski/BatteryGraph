package com.android.szparag.batterygraph.base.utils

import android.view.View

typealias Widget = View

//fun Widget.getLocationInWindow() {
//  val locationArray = IntArray(2)
//  this.getLocationInWindow(locationArray)
//}

fun Widget.getLocationInWindow() = IntArray(2).apply {
  this@getLocationInWindow.getLocationInWindow(this)
}

fun Widget.getLocationOnScreen() = IntArray(2).apply {
  this@getLocationOnScreen.getLocationOnScreen(this)
  return this
}
