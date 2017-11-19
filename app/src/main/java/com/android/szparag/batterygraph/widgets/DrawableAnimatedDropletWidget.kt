package com.android.szparag.batterygraph.widgets

import android.content.Context
import android.graphics.Paint.Style.FILL
import android.graphics.Paint.Style.STROKE
import android.graphics.Path
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build.VERSION_CODES
import android.support.annotation.CallSuper
import android.support.annotation.DrawableRes
import android.support.annotation.RequiresApi
import android.support.v4.view.animation.FastOutLinearInInterpolator
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.Interpolator
import android.view.animation.OvershootInterpolator
import android.view.animation.PathInterpolator
import android.view.animation.ScaleAnimation
import android.widget.FrameLayout
import com.android.szparag.batterygraph.R
import com.android.szparag.batterygraph.shared.utils.asString
import com.android.szparag.batterygraph.shared.utils.attach
import com.android.szparag.batterygraph.shared.utils.createImageViewWithDrawable
import com.android.szparag.batterygraph.shared.utils.hide
import com.android.szparag.batterygraph.shared.utils.setListenerBy
import com.android.szparag.batterygraph.shared.utils.show
import com.android.szparag.batterygraph.widgets.interpolators.CutoffInterpolator
import timber.log.Timber

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 15/11/2017.
 */

private const val BASE_ANIMATION_LENGTH_MILLIS = 5000L
private const val BASE_ANIMATION_REPEAT_DELAY_MILLIS = BASE_ANIMATION_LENGTH_MILLIS / 4
private const val BASE_ANIMATION_BACKGROUND_LENGTH_MILLIS = (BASE_ANIMATION_LENGTH_MILLIS * 2.32f).toLong()
private const val BASE_ANIMATION_BACKGROUND_REPEAT_DELAY_MILLIS = BASE_ANIMATION_LENGTH_MILLIS / 33

private const val BASE_OVAL_STROKE_THICKNESS = 50f

open class DrawableAnimatedDropletWidget : FrameLayout, DrawableAnimatedWidget {

  private lateinit var drawableView: View
  private lateinit var circularDropletBackgroundView: View
  private lateinit var circularDropletView: View
//  private val circularDropletViewLayers = arrayListOf<View>()

  private val circularDropletBackgroundPath by lazy { generateDropletBackgroundPath() }

  @DrawableRes private var drawable: Int? = null

  //todo: drawable layoutParams in xml - maybe someone wants drawable to be WRAP_CONTENT?
  private var drawableMargin: Int = 0
  private var dropletSpeed: Int = 1
  private var dropletFadeout: Int = 1

  //todo: parse so that srcCompat / src can be used to specify Drawable used
  constructor(context: Context) : this(context, null)

  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    Timber.d("ctor, this: ${widgetAsStringWidthChildren()}")
    clipChildren = false
    addOnLayoutChangeListener(this::onLayoutBoundsChanged)
    parseCustomAttributes()
    applyCustomAtrributes()

    drawableView = createFrontDrawableView(R.drawable.ic_icon_battery)
    addView(drawableView)
  }

  private fun parseCustomAttributes() {
    Timber.d("parseCustomAttributes, ${widgetAsStringWidthChildren()}")
  }

  private fun applyCustomAtrributes() {
    Timber.d("applyCustomAtrributes")
  }

  @RequiresApi(VERSION_CODES.LOLLIPOP) //todo remove
  @CallSuper protected fun onLayoutFirstMeasurementApplied() {
    Timber.d("onLayoutFirstMeasurementApplied")

    circularDropletBackgroundView = createCircularBackgroundView()
    circularDropletBackgroundView.hide()
    addView(circularDropletBackgroundView, 0)
    animateCircularBackground(circularDropletBackgroundView)

    circularDropletView = createCircularDropletView()
    circularDropletView.hide()
    addView(circularDropletView, 1)
    animateCircularDroplet(circularDropletView)
  }

  private fun createCircularDropletView() = createImageViewWithDrawable(context, createCircularDropletDrawable())

  private fun createCircularBackgroundView() = createImageViewWithDrawable(context, createCircularBackgroundDrawable())

  //todo: interpolators as params (enum)

  //todo: staralpha as a param
  //todo: endalpha as a param
  //todo: repeatoffset as a param
  //todo: internal animation values should be stored as fields and shared between animations (with some multiplier)
  //TODO: MAKE PATH INTERPOLATOR, SO THAT IT STARTS WITH 0 ALPHA, THEN GOES FAST TO MAX ALPHA AND FADES TO 0 AGAIN!!!
  //todo: make method oneShotCircle, so that when battery status changes, it is reflected in animation as well!

  @RequiresApi(VERSION_CODES.LOLLIPOP) //todo: remove, make function createInterpolator
  private fun animateCircularBackground(targetView: View) {
    Timber.d("animateCircularDroplet, targetView: ${targetView.asString()}, ${widgetAsStringWidthChildren()}")
    targetView.show()
    AnimationSet(false)
        .also { set ->
          set.addAnimation(createScalingAnimation(
              parentContainer = this,
              durationMillis = BASE_ANIMATION_BACKGROUND_LENGTH_MILLIS,
              repeatDelayMillis = BASE_ANIMATION_BACKGROUND_REPEAT_DELAY_MILLIS,
              xStart = 0f,
              xEnd = 1f,
              yStart = 0f,
              yEnd = 1f,
              interpolator = PathInterpolator(circularDropletBackgroundPath),
              timeCutoff = 1.0f
          ))
          set.addAnimation(createFadeoutAnimation(
              parentContainer = this,
              durationMillis = BASE_ANIMATION_BACKGROUND_LENGTH_MILLIS,
              repeatDelayMillis = BASE_ANIMATION_BACKGROUND_REPEAT_DELAY_MILLIS,
              alphaStart = 0.30f,
              alphaEnd = 0.10f,
              interpolator = FastOutLinearInInterpolator(),
              timeCutoff = 0.985f
          ))
          set.attach(targetView)
        }.start()
  }

  private fun animateCircularDroplet(targetView: View) {
    Timber.d("animateCircularDroplet, targetView: ${targetView.asString()}, ${widgetAsStringWidthChildren()}")
    targetView.show()
    AnimationSet(false)
        .also { set ->
          set.addAnimation(createScalingAnimation(
              parentContainer = this,
              durationMillis = BASE_ANIMATION_LENGTH_MILLIS,
              repeatDelayMillis = BASE_ANIMATION_REPEAT_DELAY_MILLIS,
              xStart = 0f,
              xEnd = 0.9f,
              yStart = 0f,
              yEnd = 0.9f,
              interpolator = OvershootInterpolator(1.05f),
              timeCutoff = 0.8f
          ))
          set.addAnimation(createFadeoutAnimation(
              parentContainer = this,
              durationMillis = BASE_ANIMATION_LENGTH_MILLIS,
              repeatDelayMillis = BASE_ANIMATION_REPEAT_DELAY_MILLIS,
              alphaStart = 0.35f,
              alphaEnd = 0.15f,
              interpolator = AccelerateInterpolator(0.85f),
              timeCutoff = 0.95f
          ))
          set.attach(targetView)
        }.start()
  }


  private fun createScalingAnimation(parentContainer: View, durationMillis: Long, repeatDelayMillis: Long, xStart: Float, xEnd: Float,
      yStart:
      Float, yEnd: Float,
      interpolator: Interpolator, timeCutoff: Float)
      = ScaleAnimation(xStart, xEnd, yStart, yEnd, parentContainer.width / 2f, parentContainer.height / 2f)
      .also { animation ->
        animation.duration = durationMillis
        animation.repeatCount = Animation.INFINITE
        animation.repeatMode = Animation.RESTART
        animation.interpolator = CutoffInterpolator(sourceInterpolator = interpolator, cutoff = timeCutoff)
        animation.setListenerBy(onRepeat = { animation.startOffset = repeatDelayMillis })
      }

  private fun createFadeoutAnimation(parentContainer: View, durationMillis: Long, repeatDelayMillis: Long, alphaStart: Float,
      alphaEnd: Float, interpolator: Interpolator,
      timeCutoff: Float) =
      AlphaAnimation(alphaStart, alphaEnd)
          .also { animation ->
            animation.duration = durationMillis
            animation.repeatCount = Animation.INFINITE
            animation.repeatMode = Animation.RESTART
            animation.interpolator = CutoffInterpolator(sourceInterpolator = interpolator, cutoff = timeCutoff)
            animation.setListenerBy(onRepeat = { animation.startOffset = repeatDelayMillis })
          }


  private fun createCircularDropletDrawable() =
      ShapeDrawable(OvalShape()).apply {
        this.intrinsicHeight = this@DrawableAnimatedDropletWidget.height
        this.intrinsicWidth = this@DrawableAnimatedDropletWidget.width
        this.paint.strokeWidth = BASE_OVAL_STROKE_THICKNESS
        this.paint.style = STROKE
        this.paint.color = resources.getColor(R.color.colorAccent1)
      }.also {
        Timber.d("createCircularDropletDrawable, drawable: $it")
      }

  private fun createCircularBackgroundDrawable() =
      ShapeDrawable(OvalShape()).apply {
        this.intrinsicHeight = this@DrawableAnimatedDropletWidget.height
        this.intrinsicWidth = this@DrawableAnimatedDropletWidget.width
        this.paint.style = FILL
        this.paint.color = resources.getColor(R.color.colorAccent1)
      }.also {
        Timber.d("createCircularDropletDrawable, drawable: $it")
      }

  private fun createFrontDrawableView(@DrawableRes drawableRes: Int)
      = createImageViewWithDrawable(context, resources.getDrawable(drawableRes))

  override final fun addView(child: View) {
    Timber.d("addView, child: ${child.asString()}, ${widgetAsStringWidthChildren()}")
    super.addView(child)
  }

  override final fun addView(child: View, index: Int) {
    Timber.d("addView, child: ${child.asString()}, index: $index, ${widgetAsStringWidthChildren()}")
    super.addView(child, index)
  }

  private fun onLayoutBoundsChanged(view: View, left: Int, top: Int, right: Int, bottom: Int,
      oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
    if (oldLeft == 0 && oldRight == 0 && oldTop == 0 && oldBottom == 0 && left != 0 && right != 0 && top != 0 && bottom != 0) {
      onLayoutFirstMeasurementApplied()
    }
  }

  //proven that this feels good in the ui with serious laboratory testing. true story
  private fun generateDropletBackgroundPath() = Path().apply {
    moveTo(0.000f, 0.000f)
    quadTo(0.065f, 0.325f, 0.150f, 0.400f)
    lineTo(0.330f, 0.300f)
    quadTo(0.390f, 0.630f, 0.420f, 0.690f)
    lineTo(0.690f, 0.480f)
    quadTo(0.725f, 0.85f, 0.740f, 0.900f)
    lineTo(0.930f, 0.710f)
    quadTo(0.965f, 0.925f, 1.000f, 1.000f)
  }

  private fun widgetAsStringWidthChildren() = StringBuilder(1024)
      .apply {
        append("\n\t${this@DrawableAnimatedDropletWidget.asString()}")
        for (i in 0 until this@DrawableAnimatedDropletWidget.childCount) {
          this.append("\n\t\t${this@DrawableAnimatedDropletWidget.getChildAt(i).asString()}")
        }
      }.append("").toString()

}