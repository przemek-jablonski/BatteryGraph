package com.android.szparag.batterygraph.common.utils

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.TimeInterpolator
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewPropertyAnimator
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationSet
import android.view.animation.LayoutAnimationController.AnimationParameters
import android.widget.ImageView
import java.util.Random

typealias Widget = View

private val animatorListenerCallbackStub: (Animator?) -> Unit = {}
private val animationListenerCallbackStub: (Animation?) -> Unit = {}
private const val DEBUG_VIEW_STRING_DEFAULT_CAPACITY = 256

fun Widget.getLocationInWindow() = IntArray(2).apply {
  this@getLocationInWindow.getLocationInWindow(this)
}

fun Widget.getLocationOnScreen() = IntArray(2).apply {
  this@getLocationOnScreen.getLocationOnScreen(this)
  return this
}

fun Widget.hide() {
  if (visibility != GONE) this.visibility = GONE
}

fun Widget.show() {
  if (visibility != VISIBLE) visibility = VISIBLE
}

fun Widget.shrinkViewToZero() = configureLayoutParams(width = 0, height = 0)

fun Widget.configureLayoutParams(width: Int, height: Int,
    animationParams: AnimationParameters? = null) = this.layoutParams
    .apply {
      this.width = width
      this.height = height
      animationParams?.let { this.layoutAnimationParameters = it }
      this@configureLayoutParams.layoutParams = this
    }

fun ViewPropertyAnimator.duration(millis: Long) = this.setDuration(millis)
fun ViewPropertyAnimator.startDelay(millis: Long) = this.setStartDelay(millis)
fun ViewPropertyAnimator.interpolator(interpolator: TimeInterpolator) = this.setInterpolator(interpolator)

fun ViewPropertyAnimator.setListenerBy(
    onStart: (Animator?) -> (Unit) = animatorListenerCallbackStub,
    onEnd: (Animator?) -> (Unit) = animatorListenerCallbackStub,
    onCancel: (Animator?) -> (Unit) = animatorListenerCallbackStub,
    onRepeat: (Animator?) -> (Unit) = animatorListenerCallbackStub
) = this.setListener(object : AnimatorListener {
  override fun onAnimationRepeat(animation: Animator?) = onRepeat(animation)
  override fun onAnimationEnd(animation: Animator?) = onEnd(animation)
  override fun onAnimationCancel(animation: Animator?) = onCancel(animation)
  override fun onAnimationStart(animation: Animator?) = onStart(animation)
})

fun Animation.setListenerBy(
    onStart: (Animation?) -> (Unit) = animationListenerCallbackStub,
    onEnd: (Animation?) -> (Unit) = animationListenerCallbackStub,
    onRepeat: (Animation?) -> (Unit) = animationListenerCallbackStub
) = this.setAnimationListener(object : AnimationListener {
  override fun onAnimationRepeat(animation: Animation?) = onRepeat(animation)
  override fun onAnimationEnd(animation: Animation?) = onEnd(animation)
  override fun onAnimationStart(animation: Animation?) = onStart(animation)
})


fun Int.clamp(max: Int, min: Int) = this.coerceAtLeast(min).coerceAtMost(max)
fun Long.clamp(max: Long, min: Long) = this.coerceAtLeast(min).coerceAtMost(max)
fun Float.clamp(max: Float, min: Float) = this.coerceAtLeast(min).coerceAtMost(max)
fun Double.clamp(max: Double, min: Double) = this.coerceAtLeast(min).coerceAtMost(max)

fun lerp(first: Int, second: Int, factor: Float) = first + factor * (second - first)
fun lerp(first: Long, second: Long, factor: Float) = first + factor * (second - first)
fun lerp(first: Float, second: Float, factor: Float) = first + factor * (second - first)
fun lerp(first: Double, second: Double, factor: Double) = first + factor * (second - first)

fun lerpLong(first: Long, second: Long, factor: Float) = lerp(first, second, factor).toLong()

fun inverseLerp(first: Int, second: Int, factor: Float)
    = (factor.clamp(Math.max(first, second).toFloat(), Math.min(first, second).toFloat()) - first) / (second - first)

fun inverseLerp(first: Long, second: Long, factor: Float)
    = (factor.clamp(Math.max(first, second).toFloat(), Math.min(first, second).toFloat()) - first) / (second - first)

fun inverseLerp(first: Float, second: Float, factor: Float)
    = (factor.clamp(Math.max(first, second), Math.min(first, second)) - first) / (second - first)

fun inverseLerp(first: Double, second: Double, factor: Float)
    = (factor.clamp(Math.max(first, second).toFloat(), Math.min(first, second).toFloat()) - first) / (second - first)

fun createImageViewWithDrawable(context: Context, drawable: Drawable?) = ImageView(context).apply { setImageDrawable(drawable) }


fun AnimationSet.attach(targetView: View) {
  targetView.animation = this
}


fun Random.nextFloat(min: Float, max: Float) = nextFloat() * (max - min) + min
fun Random.nextDouble(min: Double, max: Double) = nextDouble() * (max - min) + min
fun Random.nextInt(min: Int, max: Int) = nextInt() * (max - min) + min
fun Random.nextLong(min: Long, max: Long) = nextLong() * (max - min) + min

fun Float.randomVariation(random: Random, factor: Float) = random.nextFloat(this - this * factor, this + this * factor)
fun Double.randomVariation(random: Random, factor: Float) = random.nextDouble(this - this * factor, this + this * factor)
fun Int.randomVariation(random: Random, factor: Float) = random.nextInt((this - this * factor).toInt(), (this + this * factor).toInt())
fun Long.randomVariation(random: Random, factor: Float) = random.nextLong((this - this * factor).toLong(), (this + this * factor).toLong())

fun noIdString() = "NO-ID"

//todo: make more extensions functions like this - extension val
val View.idName: String get() = if (id != View.NO_ID) resources.getResourceEntryName(id) else noIdString()