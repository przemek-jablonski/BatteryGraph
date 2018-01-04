package com.android.szparag.batterygraph.shared.utils

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.animation.Animation

fun Intent.asString() = StringBuilder(1024).append(
    "Intent: [action: ${this.action}, cat: ${this.categories}, component: ${this.component}, flags: ${this.flags} bundle: ${this
        .extras.asString(StringBuilder(1024))}]")

fun Bundle?.asString(stringBuilder: StringBuilder): StringBuilder {
  if (this == null) return stringBuilder.append("[null]")
  val keys = this.keySet()
  stringBuilder.append("[")
  keys.forEach { key ->
    stringBuilder.append("$key: ${this.get(key)}, ")
  }
  stringBuilder.delete(stringBuilder.length - 2, stringBuilder.length - 1).append("]")
  return stringBuilder
}


fun View.asString() = asShortString()

//fun View.asString() = StringBuilder(DEBUG_VIEW_STRING_DEFAULT_CAPACITY).append(
//    "${asShortString()}, id: ${this.id}, dimens: [${this.width}, ${this.height}], " +
//        "location: ${Arrays.toString(this.getLocationOnScreen
//    ())}"
//).toString()

fun View.asShortString() = "${this::class.java.simpleName}@${hashCode()}"

fun Drawable.asString() = "${this::class.java.simpleName}@${hashCode()}, bounds: ${this.bounds}, iHeight: ${this.intrinsicHeight}, " +
    "iWidth: ${this.intrinsicWidth}, alpha: ${this.alpha}, opacity: ${this.opacity}, visible: ${this.isVisible}"

fun Animation.asString() = "${this::class.java.simpleName}@${hashCode()}, duration: $duration, offset: $startOffset, starttime: " +
    "${this.startTime}, repeats: $repeatCount, interpolator: ${interpolator::class.java.simpleName}, fillEnabled: ${this.isFillEnabled}"



