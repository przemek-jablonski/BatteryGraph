package com.android.szparag.batterygraph.common.utils

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.animation.Animation

fun Intent.asString() =
    StringBuilder(1024).append("Intent: [action: ${this.action}, cat: ${this.categories}, component: ${this.component}, " +
        "flags: ${this.flags} bundle: ${this.extras.asString(StringBuilder(1024))}]").toString()

fun Bundle?.asString(stringBuilder: StringBuilder): String {
  if (this == null) return nullString()
  val keys = this.keySet()
  stringBuilder.append("[")
  keys.forEach { key -> stringBuilder.append("$key: ${this.get(key)}, ") }
  stringBuilder.delete(stringBuilder.length - 2, stringBuilder.length - 1).append("]")
  return stringBuilder.toString()
}

fun View.asString() = asShortString()

fun View.asShortString() = "${this::class.java.simpleName}@${hashCode()}"

fun Drawable.asString() = "${this::class.java.simpleName}@${hashCode()}, bounds: ${this.bounds}, iHeight: ${this.intrinsicHeight}, " +
    "iWidth: ${this.intrinsicWidth}, alpha: ${this.alpha}, opacity: ${this.opacity}, visible: ${this.isVisible}"

fun Animation.asString() = "${this::class.java.simpleName}@${hashCode()}, duration: $duration, offset: $startOffset, starttime: " +
    "${this.startTime}, repeats: $repeatCount, interpolator: ${interpolator::class.java.simpleName}, fillEnabled: ${this.isFillEnabled}"



