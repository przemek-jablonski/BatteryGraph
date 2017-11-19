package com.android.szparag.batterygraph.shared.utils

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
import java.util.Arrays

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

fun Animation.asString() = "${this::class.java.simpleName}@${hashCode()}, duration: $duration, offset: $startOffset, starttime: " +
    "${this.startTime}, repeats: $repeatCount, interpolator: ${interpolator::class.java.simpleName}, fillEnabled: ${this.isFillEnabled}"

//fun Float.lerp(second: Float, factor: Float) = this + factor * (second - this)

fun Float.clamp(max: Float, min: Float) = this.coerceAtLeast(min).coerceAtMost(max)

fun lerp(first: Float, second: Float, factor: Float) = first + factor * (second - first)

fun inverseLerp(first: Float, second: Float, factor: Float) = (factor.clamp(Math.max(first, second),
    Math.min(first, second)) - first) / (second - first)

fun createImageViewWithDrawable(context: Context, drawable: Drawable?) = ImageView(context).apply { setImageDrawable(drawable) }

fun View.asString() = StringBuilder(DEBUG_VIEW_STRING_DEFAULT_CAPACITY).append(
    "${this::class.java.simpleName}, dimens: [${this.width}, ${this.height}], location: ${Arrays.toString(this.getLocationOnScreen())}"
).toString()

fun AnimationSet.attach(targetView: View) {
  targetView.animation = this
}

fun Drawable.asString() = "${this::class.java.simpleName}@${hashCode()}, bounds: ${this.bounds}, iHeight: ${this.intrinsicHeight}, " +
    "iWidth: ${this.intrinsicWidth}, alpha: ${this.alpha}, opacity: ${this.opacity}, state: ${this.state}, visible: ${this.isVisible}"