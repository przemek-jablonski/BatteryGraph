package com.android.szparag.batterygraph.widgets

import android.content.Context
import android.graphics.Paint.Style.STROKE
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.support.annotation.CallSuper
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import android.view.animation.ScaleAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import com.android.szparag.batterygraph.R
import com.android.szparag.batterygraph.shared.utils.asString
import com.android.szparag.batterygraph.shared.utils.getLocationOnScreen
import com.android.szparag.batterygraph.shared.utils.hide
import com.android.szparag.batterygraph.shared.utils.setListenerBy
import com.android.szparag.batterygraph.shared.utils.show
import com.android.szparag.batterygraph.widgets.interpolators.CutoffInterpolator
import timber.log.Timber
import java.util.Arrays

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 15/11/2017.
 */

private const val DEBUG_VIEW_STRING_DEFAULT_CAPACITY = 256
private const val BASE_DROPLET_ANIMATION_LENGTH_MILLIS = 5000L

open class DrawableAnimatedDropletWidget : FrameLayout, DrawableAnimatedWidget {

  private lateinit var drawableView: ImageView
  private lateinit var circularDropletView: View

  @DrawableRes private var drawable: Int? = null

  //todo: drawable layoutParams in xml - maybe someone wants drawable to be WRAP_CONTENT?
  private var drawableMargin: Int = 0
  private var dropletSpeed: Int = 1
  private var dropletFadeout: Int = 1

  //todo: parse so that srcCompat / src can be used to specify Drawable used
  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    Timber.d("ctor, this: ${this.asStringWithChildren()}")
    clipChildren = false
    addOnLayoutChangeListener(this::onLayoutBoundsChanged)
    parseCustomAttributes()

    drawableView = createFrontDrawableView(R.drawable.ic_icon_battery)
    addView(drawableView)
  }

  private fun parseCustomAttributes() {
    Timber.d("parseCustomAttributes, ${this.asStringWithChildren()}")
  }

  @CallSuper protected fun onLayoutFirstMeasurementApplied() {
    Timber.d("onLayoutFirstMeasurementApplied")

    circularDropletView = createCircularDropletView()
    addView(circularDropletView, 0)
    circularDropletView.hide()

    animateCircularDroplet(circularDropletView)
  }

  private fun createCircularDropletView() =
      ImageView(context).apply {
        setImageDrawable(createCircularDropletDrawable())
      }.also {
        Timber.d("createCircularDropletView, view: ${it.asString()}")
      }


//  drawable.setStroke((int)cET.dpToPx(2), Color.parseColor("#EEEEEE"));
//  drawable.setSize((int)cET.dpToPx(240), (int)cET.dpToPx(240));

  // https://stackoverflow.com/questions/18693721/oval-shape-clipped-when-created-programmatically
  // https://stackoverflow.com/questions/44462844/edges-of-shapedrawable-oval-stroke-style-are-cut-android-studio
  private fun createCircularDropletDrawable() =
      ShapeDrawable(OvalShape()).apply {
        this.intrinsicHeight = this@DrawableAnimatedDropletWidget.height //todo hardcoded
        this.intrinsicWidth = this@DrawableAnimatedDropletWidget.width
        this.paint.strokeWidth = 30f
        this.paint.style = STROKE
        this.paint.color = resources.getColor(R.color.colorAccent1)
      }.also {
        Timber.d("createCircularDropletDrawable, drawable: $it")
      }

  //todo: staralpha as a param
  //todo: endalpha as a param
  //todo: repeatoffset as a param
  //todo: internal animation values should be stored as fields and shared between animations (with some multiplier)
  private fun animateCircularDroplet(targetView: View) {
    Timber.d("animateCircularDroplet, targetView: ${targetView.asString()}, ${this.asStringWithChildren()}")
    targetView.show()
    val animationSet = AnimationSet(false)
        .also { set ->
          set.addAnimation(createScalingAnimation(targetView, 0f, 1f, 0f, 1f))
          set.addAnimation(createFadeoutAnimation(targetView, 0.3f, 0f))
        }

    targetView.animation = animationSet
    animationSet.start()
  }

  private fun createScalingAnimation(targetView: View, xStart: Float, xEnd: Float, yStart: Float, yEnd: Float)
      = ScaleAnimation(xStart, xEnd, yStart, yEnd, this@DrawableAnimatedDropletWidget.width / 2f,
      this@DrawableAnimatedDropletWidget.height / 2f)
      .also { animation ->
        animation.duration = BASE_DROPLET_ANIMATION_LENGTH_MILLIS
        animation.repeatCount = Animation.INFINITE
        animation.repeatMode = Animation.RESTART
        animation.interpolator = DecelerateInterpolator(2.10f) //todo: interpolators
        animation.setListenerBy(
            onStart = { Timber.d("createScalingAnimation.onStart, animation: ${it?.asString()}") },
            onEnd = { Timber.d("createScalingAnimation.onEnd, animation: ${it?.asString()}") },
            onRepeat = {
              animation.startOffset = BASE_DROPLET_ANIMATION_LENGTH_MILLIS / 4
              Timber.d("createScalingAnimation.onRepeat, animation: ${it?.asString()}")
            })
      }

  private fun createFadeoutAnimation(targetView: View, alphaStart: Float, alphaEnd: Float) = AlphaAnimation(alphaStart, alphaEnd)
      .also { animation ->
        animation.duration = BASE_DROPLET_ANIMATION_LENGTH_MILLIS
        animation.interpolator = CutoffInterpolator(sourceInterpolator = AccelerateInterpolator(), cutoff = 0.30f)
        //todo: interpolators as params
        animation.isFillEnabled
        animation.repeatCount = Animation.INFINITE
        animation.repeatMode = Animation.RESTART
        animation.setListenerBy(
            onStart = { Timber.d("createFadeoutAnimation.onStart, animation: ${it?.asString()}") },
            onEnd = { Timber.d("createFadeoutAnimation.onEnd, animation: ${it?.asString()}") },
            onRepeat = {
              animation.startOffset = BASE_DROPLET_ANIMATION_LENGTH_MILLIS / 4
              Timber.d("createFadeoutAnimation.onRepeat, animation: ${it?.asString()}")
            })
      }

  private fun createFrontDrawableView(@DrawableRes drawableRes: Int) =
      ImageView(context).apply {
        setImageResource(drawableRes)
      }.also {
        Timber.d("createFrontDrawableView, drawableRes: $drawableRes, ${this.asStringWithChildren()}")
      }

  override final fun addView(child: View?) {
    checkNotNull(child)
    Timber.d("addView, child: ${child?.asString()}, ${this.asStringWithChildren()}")
    super.addView(child)
  }

  override final fun addView(child: View?, index: Int) {
    checkNotNull(child)
    Timber.d("addView, child: ${child?.asString()}, index: $index, ${this.asStringWithChildren()}")
    super.addView(child, index)
  }

  private fun onLayoutBoundsChanged(view: View, left: Int, top: Int, right: Int, bottom: Int,
      oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
    if (oldLeft == 0 && oldRight == 0 && oldTop == 0 && oldBottom == 0 && left != 0 && right != 0 && top != 0 && bottom != 0) {
      onLayoutFirstMeasurementApplied()
    }
  }

}

//todo move this into ViewExtensions, with different name
fun View.asString() = StringBuilder(DEBUG_VIEW_STRING_DEFAULT_CAPACITY).append(
    "${this::class.java.simpleName}, dimens: [${this.width}, ${this.height}], location: ${Arrays.toString(this.getLocationOnScreen())}"
).toString()

//todo push this inside class as private method
fun DrawableAnimatedDropletWidget.asStringWithChildren() = StringBuilder(5 * DEBUG_VIEW_STRING_DEFAULT_CAPACITY)
    .apply {
      append("\nthis: {\n\t${this@asStringWithChildren.asString()}")
      for (i in 0 until this@asStringWithChildren.childCount) {
        this.append("\n\t\t${this@asStringWithChildren.getChildAt(i).asString()}")
      }
    }.append("\n}").toString()