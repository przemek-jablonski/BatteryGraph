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
import com.android.szparag.batterygraph.shared.utils.duration
import com.android.szparag.batterygraph.shared.utils.getLocationOnScreen
import com.android.szparag.batterygraph.shared.utils.hide
import com.android.szparag.batterygraph.shared.utils.interpolator
import com.android.szparag.batterygraph.shared.utils.setListenerBy
import com.android.szparag.batterygraph.shared.utils.show
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

  private var drawableMargin: Int = 0

  private var dropletSpeed: Int = 1
  private var dropletFadeout: Int = 1

  //todo: parse so that srcCompat / src can be used to specify Drawable used
  constructor(context: Context) : this(context, null)

  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    Timber.d("ctor, this: ${this.asStringWithChildren()}")
    addOnLayoutChangeListener(this::onLayoutBoundsChanged)
    parseCustomAttributes()

    circularDropletView = createCircularDropletView()
    addView(circularDropletView)

    drawableView = createFrontDrawableView(R.drawable.ic_icon_battery)
    addView(drawableView)
  }

  private fun parseCustomAttributes() {
    Timber.d("parseCustomAttributes, ${this.asStringWithChildren()}")
  }

  @CallSuper protected fun onLayoutFirstMeasurementApplied() {
    Timber.d("onLayoutFirstMeasurementApplied")
//    circularDropletView.shrinkViewToZero()
    circularDropletView.hide()
//    animateCircularDropletAlpha(circularDropletView)
    animateCircularDroplet(circularDropletView)
  }

  private fun createCircularDropletView() =
      ImageView(context).apply {
        setImageDrawable(createCircularDropletDrawable())
      }.also {
        Timber.d("createCircularDropletView, view: ${it.asString()}")
      }

  private fun createCircularDropletDrawable() =
      ShapeDrawable(OvalShape()).apply {
        intrinsicHeight = 100 //todo hardcoded
        intrinsicWidth = 100
        this.paint.style = STROKE
        this.paint.strokeWidth = 1f
        this.paint.color = resources.getColor(R.color.colorAccent1)
      }.also {
        Timber.d("createCircularDropletDrawable, drawable: $it")
      }

  private fun animateCircularDroplet(dropletView: View) {
    Timber.d("animateCircularDroplet, dropletView: ${dropletView.asString()}, ${this.asStringWithChildren()}")
    dropletView.show()
    val animationSet = AnimationSet(false)
        .also { set ->
          set.addAnimation(ScaleAnimation(0f, 1f, 0f, 1f, dropletView.width / 2f, dropletView.height / 2f).also { animation ->
            animation.duration = BASE_DROPLET_ANIMATION_LENGTH_MILLIS
            animation.repeatCount = Animation.INFINITE
            animation.repeatMode = Animation.RESTART
            animation.interpolator = DecelerateInterpolator(1.75f) //todo: interpolators
            animation.setListenerBy(
                onStart = { Timber.d("animateCircularDroplet.SCALEAnimation.onSTART, animation: ${it?.asString()}") },
                onRepeat = { Timber.d("animateCircularDroplet.SCALEAnimation.onREPEAT, animation: ${it?.asString()}") },
                onEnd = { Timber.d("animateCircularDroplet.SCALEAnimation.onEND, animation: ${it?.asString()}") }
            )
//            animation.setListenerBy(onRepeat = { animation.startOffset = BASE_DROPLET_ANIMATION_LENGTH_MILLIS / 4 }) //todo: repeatoffset as
            // xml and code parameter!
          })
          set.addAnimation(AlphaAnimation(1f, 0f).also { animation ->
            animation.duration = BASE_DROPLET_ANIMATION_LENGTH_MILLIS
            animation.interpolator = DecelerateInterpolator(1.75f) //todo: interpolators as params
            animation.repeatCount = Animation.INFINITE
            animation.repeatMode = Animation.RESTART
            animation.setListenerBy(
                onStart = { Timber.d("animateCircularDroplet.ALPHAAnimation.onSTART, animation: ${it?.asString()}") },
                onRepeat = { Timber.d("animateCircularDroplet.ALPHAAnimation.onREPEAT, animation: ${it?.asString()}") },
                onEnd = { Timber.d("animateCircularDroplet.ALPHAAnimation.onEND, animation: ${it?.asString()}") }
            )
            //            animation.setListenerBy(onRepeat = { animation.startOffset = BASE_DROPLET_ANIMATION_LENGTH_MILLIS / 4 })
            //todo: internal animation values should be stored as fields and shared between animations (with some multiplier)
          })
        }

    dropletView.animation = animationSet
    animationSet.start()


//    AnimationSet(context).addAnimation()
//    dropletView.animate()
//        .duration(5000)
//        .interpolator(DecelerateInterpolator(1f))
//        .scaleY(1f)
//        .scaleX(1f)
////        .alpha(0f)
//        .setListenerBy(
//            onStart = {
//              Timber.d("animateCircularDroplet.start")
//              dropletView.show()
//            }
//        )
//        .start()
  }

  private fun animateCircularDropletAlpha(dropletView: View) {
    Timber.d("animateCircularDropletAlpha, dropletView: $dropletView, ${this.asStringWithChildren()}")
    dropletView.animate()
        .duration(5000)
        .interpolator(AccelerateInterpolator(5f))
        .alpha(0.2f)
        .start()
  }

  private fun createFrontDrawableView(@DrawableRes drawableRes: Int) =
      ImageView(context).apply {
        setImageResource(drawableRes)
      }.also {
        Timber.d("createFrontDrawableView, drawableRes: $drawableRes, ${this.asStringWithChildren()}")
      }

  override final fun addView(child: View) {
    checkNotNull(child)
    Timber.d("addView, child: ${child.asString()}, ${this.asStringWithChildren()}")
    super.addView(child)
  }

  private fun onLayoutBoundsChanged(view: View, left: Int, top: Int, right: Int, bottom: Int,
      oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
    if (oldLeft == 0 && oldRight == 0 && oldTop == 0 && oldBottom == 0 && left != 0 && right != 0 && top != 0 && bottom != 0) {
      onLayoutFirstMeasurementApplied()
    }
  }

}

fun View.asString() = StringBuilder(DEBUG_VIEW_STRING_DEFAULT_CAPACITY).append(
    "${this::class.java.simpleName}, dimens: [${this.width}, ${this.height}], location: ${Arrays.toString(this.getLocationOnScreen())}"
).toString()

fun DrawableAnimatedDropletWidget.asStringWithChildren() = StringBuilder(5 * DEBUG_VIEW_STRING_DEFAULT_CAPACITY)
    .apply {
      append("\nthis: {\n\t${this@asStringWithChildren.asString()}")
      for (i in 0 until this@asStringWithChildren.childCount) {
        this.append("\n\t\t${this@asStringWithChildren.getChildAt(i).asString()}")
      }
    }.append("\n}").toString()