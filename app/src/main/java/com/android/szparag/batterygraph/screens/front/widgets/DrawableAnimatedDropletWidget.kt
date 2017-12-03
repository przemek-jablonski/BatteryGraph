package com.android.szparag.batterygraph.screens.front.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
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
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.Interpolator
import android.view.animation.PathInterpolator
import android.view.animation.ScaleAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import com.android.szparag.batterygraph.R
import com.android.szparag.batterygraph.shared.utils.asString
import com.android.szparag.batterygraph.shared.utils.attach
import com.android.szparag.batterygraph.shared.utils.createImageViewWithDrawable
import com.android.szparag.batterygraph.shared.utils.hide
import com.android.szparag.batterygraph.shared.utils.inverseLerp
import com.android.szparag.batterygraph.shared.utils.lerp
import com.android.szparag.batterygraph.shared.utils.lerpLong
import com.android.szparag.batterygraph.shared.utils.randomVariation
import com.android.szparag.batterygraph.shared.utils.setListenerBy
import com.android.szparag.batterygraph.shared.utils.show
import com.android.szparag.batterygraph.shared.widgets.DrawableAnimatedWidget
import com.android.szparag.batterygraph.shared.widgets.interpolators.CutoffInterpolator
import timber.log.Timber
import java.util.Random

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 15/11/2017.
 */

private const val BASE_ANIMATION_LENGTH_MILLIS = 5000L
private const val BASE_ANIMATION_LENGTH_MIN_MILLIS = (BASE_ANIMATION_LENGTH_MILLIS * 0.66f).toLong()
private const val BASE_ANIMATION_REPEAT_DELAY_MILLIS = BASE_ANIMATION_LENGTH_MILLIS / 2


private const val BASE_ANIMATION_BACKGROUND_LENGTH_MILLIS = (BASE_ANIMATION_LENGTH_MILLIS * 2.5f).toLong()
private const val BASE_ANIMATION_BACKGROUND_REPEAT_DELAY_MILLIS = BASE_ANIMATION_LENGTH_MILLIS / 35

private const val BASE_OVAL_STROKE_THICKNESS = 50f

private const val ANIMATION_RANDOM_START_TIME_BOUND_MILLIS = BASE_ANIMATION_LENGTH_MILLIS * 2
private const val ANIMATION_RANDOM_REPEAT_DELAY_BOUND_MILLIS = ANIMATION_RANDOM_START_TIME_BOUND_MILLIS * 2.50


typealias Millis = Long
typealias ResourceId = Int
typealias Percentage = Float

open class DrawableAnimatedDropletWidget : FrameLayout, DrawableAnimatedWidget {

  //todo: unify - there are vars, lateinit vars and vals here
  private var drawableView: ImageView
  private lateinit var circularDropletBackgroundView1: View
  private lateinit var circularDropletBackgroundView2: View
  //  private val circularBackgroundViewLayers = arrayListOf<View>()
  private val circularDropletViewLayers = arrayListOf<View>()

  private val random by lazy { Random() }
//  private val circularDropletBackgroundPath by lazy { generateDropletBackgroundPath() }

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
    drawableView.imageTintList = ColorStateList.valueOf(R.color.colorPrimaryDark)
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

    createCircularDropletsLayers(6)

    circularDropletBackgroundView1 = createCircularBackgroundView(R.color.colorAccent1)
    circularDropletBackgroundView1.hide()
    addView(circularDropletBackgroundView1)
    animateCircularBackground(
        targetView = circularDropletBackgroundView1,
        startTime = 0L,
        duration = BASE_ANIMATION_BACKGROUND_LENGTH_MILLIS,
        repeatDelay = BASE_ANIMATION_BACKGROUND_REPEAT_DELAY_MILLIS,
        pathRandomFactor = 0.002f
    )


    circularDropletBackgroundView2 = createCircularBackgroundView(R.color.colorAccent1)
    circularDropletBackgroundView2.hide()
    addView(circularDropletBackgroundView2)
    animateCircularBackground(
        targetView = circularDropletBackgroundView2,
        startTime = BASE_ANIMATION_BACKGROUND_LENGTH_MILLIS / 2,
        duration = BASE_ANIMATION_BACKGROUND_LENGTH_MILLIS,
        repeatDelay = BASE_ANIMATION_BACKGROUND_REPEAT_DELAY_MILLIS,
        pathRandomFactor = 0.01f
    )
  }


  private fun createCircularDropletsLayers(layerCount: Int) {
    Timber.d("createCircularDropletsLayers, layerCount: $layerCount")
    (0 until layerCount).mapTo(circularDropletViewLayers) { layerIndex ->
      createCircularDropletView(
          thickness = BASE_OVAL_STROKE_THICKNESS - (layerIndex * BASE_OVAL_STROKE_THICKNESS / layerCount),
          colourId = R.color.colorAccent1
      )
    }

    addViews(children = circularDropletViewLayers, childApply = { child, index ->
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

  //todo: callback (with default implementation) onUserClicked()
  //todo: callback (with default implementation) onUserLongPressed()
  //todo: performOneShotDroplet
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
  private fun animateCircularBackground(targetView: View, startTime: Millis, duration: Millis, repeatDelay: Millis,
      pathRandomFactor: Float) {
    Timber.d("animateCircularBackground, targetView: ${targetView.asString()}")
    targetView.show()
    AnimationSet(false)
        .also { set ->
          set.addAnimation(createScalingAnimation(
              parentContainer = this,
              duration = duration,
              startTime = startTime,
              repeatDelay = repeatDelay,
              xyStart = 0f,
              xyEnd = 1f,
              interpolator = PathInterpolator(generateDropletBackgroundPath(random, pathRandomFactor)),
              timeCutoff = 1.0f
          ))
          set.addAnimation(createFadeoutAnimation(
              parentContainer = this,
              duration = duration,
              repeatDelay = repeatDelay,
              startTime = startTime,
              alphaStart = 0.12f,
              alphaEnd = 0.00f,
              interpolator = FastOutLinearInInterpolator(),
              timeCutoff = 0.99f
          ))
          set.attach(targetView)
        }.start()
  }

  private fun animateCircularDroplet(targetView: View, layerIndex: Int, layerCount: Int, layerDependency: Float) {
    val startTime = random.nextInt(ANIMATION_RANDOM_START_TIME_BOUND_MILLIS.toInt()).toLong()
    val repeatDelayAddition = random.nextInt(
        ANIMATION_RANDOM_REPEAT_DELAY_BOUND_MILLIS.toInt()).toLong()
    val layerValuesMultiplier = layerIndex / layerCount * layerDependency
    val inverseLerp = inverseLerp(0, layerCount, layerIndex.toFloat())
    Timber.d("animateCircularDroplet, layerIndex: $layerIndex, layerCount: $layerCount, layerDependency: $layerDependency, inverseLerp: " +
        "$inverseLerp, layerValuesMultiplier: $layerValuesMultiplier")
    targetView.show()
    AnimationSet(false)
        .also { set ->
          set.fillAfter = true
          set.isFillEnabled = true
          set.addAnimation(createScalingAnimation(
              parentContainer = this,
              duration = lerpLong(first = BASE_ANIMATION_LENGTH_MIN_MILLIS, second = BASE_ANIMATION_LENGTH_MILLIS, factor = inverseLerp),
              startTime = startTime,
              repeatDelay = BASE_ANIMATION_REPEAT_DELAY_MILLIS + repeatDelayAddition,
              xyStart = 0.00f,
              xyEnd = lerp(0.70f, 1.00f, inverseLerp),
              interpolator = AnticipateOvershootInterpolator(lerp(1.33f, 0.25f, inverseLerp)),
              timeCutoff = lerp(0.8f, 0.98f, inverseLerp)
          ))
          set.addAnimation(createFadeoutAnimation(
              parentContainer = this,
              duration = lerp(
                  first = BASE_ANIMATION_LENGTH_MIN_MILLIS,
                  second = BASE_ANIMATION_LENGTH_MILLIS,
                  factor = inverseLerp(0, layerCount, layerIndex.toFloat())
              ).toLong(),
              repeatDelay = BASE_ANIMATION_REPEAT_DELAY_MILLIS + repeatDelayAddition,
              startTime = startTime,
              alphaStart = lerp(0.15f, 0.55f, inverseLerp),
              alphaEnd = 0.00f,
              interpolator = AccelerateInterpolator(lerp(1.10f, 0.85f, inverseLerp)),
              timeCutoff = lerp(0.90f, 0.97f, inverseLerp)
          ))
          set.attach(targetView)
        }.start()
  }


  @SuppressLint("BinaryOperationInTimber")
  private fun createScalingAnimation(parentContainer: View, duration: Millis, startTime: Millis, repeatDelay: Millis,
      xyStart: Float, xyEnd: Float, interpolator: Interpolator, timeCutoff: Float = 1.0f)
      = ScaleAnimation(xyStart, xyEnd, xyStart, xyEnd, parentContainer.width / 2f, parentContainer.height / 2f)
      .also { animation ->
        Timber.d("createScalingAnimation, duration: $duration, startTime: $startTime, repeatDelay: $repeatDelay, " +
            "xyStart: $xyStart, xyEnd: $xyEnd, interpolator: $interpolator, timeCutoff: $timeCutoff")
        animation.duration = duration
        animation.startOffset = startTime
        animation.repeatCount = Animation.INFINITE
        animation.repeatMode = Animation.RESTART
        animation.interpolator = CutoffInterpolator(sourceInterpolator = interpolator, cutoff = timeCutoff)
        animation.setListenerBy(onRepeat = { animation.startOffset = repeatDelay })
      }

  private fun createFadeoutAnimation(parentContainer: View, duration: Millis, startTime: Millis, repeatDelay: Millis,
      alphaStart: Float, alphaEnd: Float, interpolator: Interpolator, timeCutoff: Float) =
      AlphaAnimation(alphaStart, alphaEnd)
          .also { animation ->
            Timber.d("createFadeoutAnimation, duration: $duration, startTime: $startTime, repeatDelay: $repeatDelay, " +
                "alphaStart: $alphaStart, alphaEnd: $alphaEnd, interpolator: $interpolator, timeCutoff: $timeCutoff")
            animation.duration = duration
            animation.startOffset = startTime
            animation.repeatCount = Animation.INFINITE
            animation.repeatMode = Animation.RESTART
            animation.isFillEnabled = true
            animation.fillAfter = true
            animation.fillBefore = false
//            animation.isFillEnabled = truex
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
        Timber.d("createCircularBackgroundDrawable, drawable: ${it.asString()}")
      }

  private fun createFrontDrawableView(@DrawableRes drawableRes: ResourceId?)
      = createImageViewWithDrawable(context, drawableRes?.let { resources.getDrawable(drawableRes) })

  override final fun addView(child: View) {
//    Timber.d("addView, child: ${child.asString()}")
    super.addView(child)
  }

  override final fun addView(child: View, index: Int) {
//    Timber.d("addView, child: ${child.asString()}, index: $index")
    super.addView(child, index)
  }

  //todo index not used!
  private fun addViews(children: List<View>, childApply: (View, Int) -> (Unit) = { _, _ -> }) {
//    Timber.d("addViews, children.count: ${children.size}, children: ${children.map { it.asString() }}, index: $index")
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
  private fun generateDropletBackgroundPath(random: Random, randomFactor: Float = 0.005f) = Path().apply {
    moveTo(0.000f, 0.000f)
    quadTo(0.065f, 0.325f, 0.150f, 0.400f.randomVariation(random, randomFactor))
    lineTo(0.330f, 0.300f.randomVariation(random, randomFactor / 2f))
    quadTo(0.390f, 0.630f, 0.420f, 0.690f.randomVariation(random, randomFactor))
    lineTo(0.690f, 0.480f.randomVariation(random, randomFactor / 2f))
    quadTo(0.725f, 0.85f, 0.740f, 0.900f.randomVariation(random, randomFactor))
    lineTo(0.930f, 0.710f.randomVariation(random, randomFactor / 2f))
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