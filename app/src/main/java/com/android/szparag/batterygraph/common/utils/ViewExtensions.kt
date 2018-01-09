package com.android.szparag.batterygraph.common.utils

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.TimeInterpolator
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewPropertyAnimator
import android.view.Window
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationSet
import android.view.animation.LayoutAnimationController.AnimationParameters
import android.widget.ImageView

typealias Widget = View

private val animatorListenerCallbackStub: (Animator?) -> Unit = {}
private val animationListenerCallbackStub: (Animation?) -> Unit = {}
private const val DEBUG_VIEW_STRING_DEFAULT_CAPACITY = 256

fun noIdString() = "NO-ID"


//<editor-fold desc="Widgets visibility">
fun Widget.hide() {
  if (visibility != GONE) this.visibility = GONE
}

fun Widget.show() {
  if (visibility != VISIBLE) visibility = VISIBLE
}
//</editor-fold>

//<editor-fold desc="Widgets resizing">
fun Widget.shrinkViewToZero() =
    configureLayoutParams(width = 0, height = 0)

fun Widget.configureLayoutParams(width: Int, height: Int,
    animationParams: AnimationParameters? = null) =
    this.layoutParams
        .apply {
          this.width = width
          this.height = height
          animationParams?.let { this.layoutAnimationParameters = it }
          this@configureLayoutParams.layoutParams = this
        }
//</editor-fold>

//<editor-fold desc="Widgets position">
val Widget.locationInWindow
  get() = IntArray(2).apply { getLocationInWindow(this) }

val Widget.locationOnScreen
  get () = IntArray(2).apply { getLocationOnScreen(this) }
//</editor-fold>

//<editor-fold desc="ViewPropertyAnimator">
fun ViewPropertyAnimator.duration(millis: Long) =
    this.setDuration(millis)

fun ViewPropertyAnimator.startDelay(millis: Long) =
    this.setStartDelay(millis)

fun ViewPropertyAnimator.interpolator(interpolator: TimeInterpolator) =
    this.setInterpolator(interpolator)

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
//</editor-fold>

//<editor-fold desc="Animation and AnimationSet">
fun Animation.setListenerBy(
    onStart: (Animation?) -> (Unit) = animationListenerCallbackStub,
    onEnd: (Animation?) -> (Unit) = animationListenerCallbackStub,
    onRepeat: (Animation?) -> (Unit) = animationListenerCallbackStub
) = this.setAnimationListener(object : AnimationListener {
  override fun onAnimationRepeat(animation: Animation?) = onRepeat(animation)
  override fun onAnimationEnd(animation: Animation?) = onEnd(animation)
  override fun onAnimationStart(animation: Animation?) = onStart(animation)
})


fun AnimationSet.attach(targetView: View) {
  targetView.animation = this
}
//</editor-fold>

fun createImageViewWithDrawable(context: Context, drawable: Drawable?) =
    ImageView(context).apply { setImageDrawable(drawable) }

//todo: make more extensions functions like this - extension val
val View.idName: String
  get() = if (id != View.NO_ID) resources.getResourceEntryName(id) else noIdString()


inline fun View.setViewDimensions(itemWidth: Int?, itemHeight: Int?): View {
  if (itemWidth == null && itemHeight == null) return this
  this.layoutParams = this.layoutParams.apply {
    itemWidth?.let { this.width = it }
    itemHeight?.let { this.height = it }
  }
  return this
}

inline fun Window.getVisibleDisplayFrame(): Rect {
  val displayFrame = Rect()
  this.decorView.getWindowVisibleDisplayFrame(displayFrame)
  return displayFrame
}