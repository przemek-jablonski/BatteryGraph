package com.android.szparag.batterygraph.widgets

import android.content.Context
import android.graphics.Paint.Style.FILL
import android.graphics.Paint.Style.STROKE
import android.graphics.Path
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build.VERSION_CODES
import android.support.annotation.CallSuper
import android.support.annotation.ColorRes
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
import java.util.Random

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 15/11/2017.
 */

private const val BASE_ANIMATION_LENGTH_MILLIS = 5000L
private const val BASE_ANIMATION_REPEAT_DELAY_MILLIS = BASE_ANIMATION_LENGTH_MILLIS / 4
private const val BASE_ANIMATION_BACKGROUND_LENGTH_MILLIS = (BASE_ANIMATION_LENGTH_MILLIS * 2.32f).toLong()
private const val BASE_ANIMATION_BACKGROUND_REPEAT_DELAY_MILLIS = BASE_ANIMATION_LENGTH_MILLIS / 33

private const val BASE_OVAL_STROKE_THICKNESS = 50f

private const val ANIMATION_RANDOM_START_TIME_BOUND_MILLIS = (BASE_ANIMATION_LENGTH_MILLIS * 1.69f).toLong()
private const val ANIMATION_RANDOM_REPEAT_DELAY_BOUND_MILLIS = ANIMATION_RANDOM_START_TIME_BOUND_MILLIS * 1.33f


typealias Millis = Long
typealias ResourceId = Int
typealias Percentage = Float

open class DrawableAnimatedDropletWidget : FrameLayout, DrawableAnimatedWidget {

  //todo: unify - there are vars, lateinit vars and vals here
  private var drawableView: View
  private lateinit var circularDropletBackgroundView: View
  private val circularDropletViewLayers = arrayListOf<View>()

  private val random by lazy { Random() }
  private val circularDropletBackgroundPath by lazy { generateDropletBackgroundPath() }

  @DrawableRes private var drawable = android.R.color.transparent

//  private var drawableMargin: Int = 0
//  private var dropletSpeed: Int = 1
//  private var dropletFadeout: Int = 1
  //todo: drawable layoutParams in xml - maybe someone wants drawable to be WRAP_CONTENT?
  //todo: parse so that srcCompat / src can be used to specify Drawable used

  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    Timber.d("ctor")
    setupView()
    parseCustomAttributes()
    applyCustomAtrributes()

    drawableView = createFrontDrawableView(drawable)
    addView(drawableView)
  }

  private fun setupView() {
    Timber.d("setupView")
    addOnLayoutChangeListener(this::onLayoutBoundsChanged)
    clipChildren = false
    drawable = R.drawable.ic_icon_battery //todo hardcoded
  }

  //todo: typedarray as input
  @CallSuper protected fun parseCustomAttributes() {
    Timber.d("parseCustomAttributes")
  }

  @CallSuper protected fun applyCustomAtrributes() {
    Timber.d("applyCustomAtrributes")
  }

  @RequiresApi(VERSION_CODES.LOLLIPOP) //todo remove
  @CallSuper protected fun onLayoutFirstMeasurementApplied() {
    Timber.d("onLayoutFirstMeasurementApplied")

    createCircularDropletsLayers(4)
    circularDropletBackgroundView = createCircularBackgroundView(R.color.colorAccent1)
    circularDropletBackgroundView.hide()
    addView(circularDropletBackgroundView, 0)
    animateCircularBackground(circularDropletBackgroundView)
  }


  private fun createCircularDropletsLayers(layerCount: Int) {
    Timber.d("createCircularDropletsLayers, layerCount: $layerCount")
    (0 until layerCount).mapTo(circularDropletViewLayers) { layerIndex ->
      createCircularDropletView(
          thickness = BASE_OVAL_STROKE_THICKNESS - (layerIndex * BASE_OVAL_STROKE_THICKNESS / layerCount),
          colourId = R.color.colorAccent1)
    }

    addViews(children = circularDropletViewLayers, index = 0, childApply = { child, index ->
      child.hide()
      animateCircularDroplet(child, index, layerCount, 1.0f)
    })
  }

  private fun createCircularDropletView(thickness: Float, @ColorRes colourId: ResourceId)
      = createImageViewWithDrawable(context, createCircularDropletDrawable(thickness, colourId))
      .also { Timber.d("createCircularDropletView, thickness: $thickness, view: ${it.asString()}") }

  private fun createCircularBackgroundView(@ColorRes colourId: ResourceId)
      = createImageViewWithDrawable(context, createCircularBackgroundDrawable(colourId))
      .also { Timber.d("createCircularBackgroundView, view: ${it.asString()}") }

  //todo: color(s?) as params (reference)
  //todo: interpolators as params (enum)
  //todo: staralpha as a param
  //todo: endalpha as a param
  //todo: repeatoffset as a param
  //todo: internal animation values should be stored as fields and shared between animations (with some multiplier)
  //TODO: MAKE PATH INTERPOLATOR, SO THAT IT STARTS WITH 0 ALPHA, THEN GOES FAST TO MAX ALPHA AND FADES TO 0 AGAIN!!!
  //todo: make method oneShotCircle, so that when battery status changes, it is reflected in animation as well!
  //todo:

  @RequiresApi(VERSION_CODES.LOLLIPOP) //todo: remove, make function createInterpolator
  private fun animateCircularBackground(targetView: View) {
    Timber.d("animateCircularDroplet, targetView: ${targetView.asString()}")
    targetView.show()
    AnimationSet(false)
        .also { set ->
          set.addAnimation(createScalingAnimation(
              parentContainer = this,
              duration = BASE_ANIMATION_BACKGROUND_LENGTH_MILLIS,
              repeatDelay = BASE_ANIMATION_BACKGROUND_REPEAT_DELAY_MILLIS,
              startTime = 0L,
              xStart = 0f,
              xEnd = 1f,
              yStart = 0f,
              yEnd = 1f,
              interpolator = PathInterpolator(circularDropletBackgroundPath),
              timeCutoff = 1.0f
          ))
          set.addAnimation(createFadeoutAnimation(
              parentContainer = this,
              duration = BASE_ANIMATION_BACKGROUND_LENGTH_MILLIS,
              repeatDelay = BASE_ANIMATION_BACKGROUND_REPEAT_DELAY_MILLIS,
              startTime = 0L,
              alphaStart = 0.16f,
              alphaEnd = 0.00f,
              interpolator = FastOutLinearInInterpolator(),
              timeCutoff = 0.99f
          ))
          set.attach(targetView)
        }.start()
  }

  private fun animateCircularDroplet(targetView: View, layerIndex: Int, layerCount: Int, layerDependency: Float) {
    val startTime = random.nextInt(ANIMATION_RANDOM_START_TIME_BOUND_MILLIS.toInt()).toLong()
    val repeatDelayAddition = random.nextInt(ANIMATION_RANDOM_REPEAT_DELAY_BOUND_MILLIS.toInt()).toLong()
    val layerValuesMultiplier = layerIndex / layerCount * layerDependency
    Timber.d("animateCircularDroplet, targetView: ${targetView.asString()}")
    targetView.show()
    AnimationSet(false)
        .also { set ->
          set.addAnimation(createScalingAnimation(
              parentContainer = this,
              duration = BASE_ANIMATION_LENGTH_MILLIS,
              repeatDelay = BASE_ANIMATION_REPEAT_DELAY_MILLIS + repeatDelayAddition,
              startTime = startTime,
              xStart = 0f,
              xEnd = 0.9f,
              yStart = 0f,
              yEnd = 0.9f,
              interpolator = OvershootInterpolator(1.05f),
              timeCutoff = 0.8f
          ))
          set.addAnimation(createFadeoutAnimation(
              parentContainer = this,
              duration = BASE_ANIMATION_LENGTH_MILLIS,
              repeatDelay = BASE_ANIMATION_REPEAT_DELAY_MILLIS + repeatDelayAddition,
              startTime = startTime,
              alphaStart = 0.15f,
              alphaEnd = 0.00f,
              interpolator = AccelerateInterpolator(0.85f),
              timeCutoff = 0.99f
          ))
          set.attach(targetView)
        }.start()
  }


  private fun createScalingAnimation(parentContainer: View, duration: Millis, repeatDelay: Millis, startTime: Millis,
      xStart: Float, xEnd: Float, yStart: Float, yEnd: Float, interpolator: Interpolator, timeCutoff: Float = 1.0f)
      = ScaleAnimation(xStart, xEnd, yStart, yEnd, parentContainer.width / 2f, parentContainer.height / 2f)
      .also { animation ->
        animation.duration = duration
        animation.startOffset = startTime
        animation.repeatCount = Animation.INFINITE
        animation.repeatMode = Animation.RESTART
        animation.interpolator = CutoffInterpolator(sourceInterpolator = interpolator, cutoff = timeCutoff)
        animation.setListenerBy(onRepeat = { animation.startOffset = repeatDelay })
      }

  private fun createFadeoutAnimation(parentContainer: View, duration: Millis, startTime: Millis, repeatDelay: Millis, alphaStart: Float,
      alphaEnd: Float, interpolator: Interpolator, timeCutoff: Float) =
      AlphaAnimation(alphaStart, alphaEnd)
          .also { animation ->
            animation.duration = duration
            animation.startOffset = startTime
            animation.repeatCount = Animation.INFINITE
            animation.repeatMode = Animation.RESTART
            animation.interpolator = CutoffInterpolator(sourceInterpolator = interpolator, cutoff = timeCutoff)
            animation.setListenerBy(onRepeat = { animation.startOffset = repeatDelay })
          }

  private fun createCircularDropletDrawable(strokeThickness: Float, @ColorRes colourId: ResourceId) =
      ShapeDrawable(OvalShape()).apply {
        this.intrinsicHeight = this@DrawableAnimatedDropletWidget.height
        this.intrinsicWidth = this@DrawableAnimatedDropletWidget.width
        this.paint.strokeWidth = strokeThickness
        this.paint.style = STROKE
        this.paint.color = resources.getColor(colourId)
      }.also {
        Timber.d("createCircularDropletDrawable, drawable: ${it.asString()}")
      }

  private fun createCircularBackgroundDrawable(@ColorRes colourId: ResourceId) =
      ShapeDrawable(OvalShape()).apply {
        this.intrinsicHeight = this@DrawableAnimatedDropletWidget.height
        this.intrinsicWidth = this@DrawableAnimatedDropletWidget.width
        this.paint.style = FILL
        this.paint.color = resources.getColor(colourId)
      }.also {
        Timber.d("createCircularDropletDrawable, drawable: ${it.asString()}")
      }

  private fun createFrontDrawableView(@DrawableRes drawableRes: ResourceId?)
      = createImageViewWithDrawable(context, drawableRes?.let { resources.getDrawable(drawableRes) })

  override final fun addView(child: View) {
    Timber.d("addView, child: ${child.asString()}")
    super.addView(child)
  }

  override final fun addView(child: View, index: Int) {
    Timber.d("addView, child: ${child.asString()}, index: $index")
    super.addView(child, index)
  }

  //todo index not used!
  private fun addViews(children: List<View>, index: Int = 0, childApply: (View, Int) -> (Unit) = { _, _ -> }) {
    Timber.d("addViews, children.count: ${children.size}, children: ${children.map { it.asString() }}, index: $index")
    for (i in children.size - 1 downTo 0) {
      children[i].apply { childApply.invoke(this, i); addView(this) }
    }
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
        append("\n\t\t${this@DrawableAnimatedDropletWidget.asString()}")
        for (i in 0 until this@DrawableAnimatedDropletWidget.childCount) {
          this.append("\n\t\t\t${this@DrawableAnimatedDropletWidget.getChildAt(i).asString()}")
        }
      }.append("").toString()

}