package com.android.szparag.batterygraph.widgets

import android.content.Context
import android.graphics.Paint.Style.STROKE
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.android.szparag.batterygraph.R
import com.android.szparag.batterygraph.base.utils.getLocationOnScreen
import timber.log.Timber

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 15/11/2017.
 */

private const val DEBUG_VIEW_STRING_DEFAULT_CAPACITY = 256

class DrawableAnimatedDropletWidget : FrameLayout, DrawableAnimatedWidget {

  private lateinit var drawableView: ImageView
//  private lateinit

  @DrawableRes private var drawable: Int? = null

  private var drawableMargin: Int = 0

  private var dropletSpeed: Int = 1
  private var dropletFadeout: Int = 1

  //todo: parse so that srcCompat / src can be used to specify Drawable used
  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    Timber.d("ctor, this: ${this.asString()}")
    addOnLayoutChangeListener(this::onLayoutBoundsChanged)
    parseCustomAttributes()

    addView(createCircularDropletView())
//    drawableView = createDrawableView(R.drawable.ic_icon_battery)
//    addView(drawableView)
  }

  private fun onLayoutBoundsChanged(view: View, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int,
      oldBottom: Int) {
    Timber.d("onLayoutBoundsChanged, view: $view, left: $left, top: $top, right: $right, bottom: $bottom, " +
        "oldLeft: $oldLeft, oldTop: $oldTop, oldRight: $oldRight, oldBottom: $oldBottom")
    if (oldBottom)
  }


  override fun onFinishInflate() {
    super.onFinishInflate()
    Timber.d("onFinishInflate, this: ${this.asString()}")
  }

  private fun parseCustomAttributes() {
    Timber.d("parseCustomAttributes")
//    drawable = Drawable.createFromPath("asdasd")
  }

  private fun createCircularDropletView() =
      ImageView(context).apply {
        //        background = ColorDrawable(resources.getColor(R.color.colorAccent2Alpha))
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

  private fun createDrawableView(@DrawableRes drawableRes: Int) =
      ImageView(context).apply {
        setImageResource(drawableRes)
      }.also {
        Timber.d("createDrawableView, drawableRes: $drawableRes, view: ${it.asString()}")
      }

  override fun addView(child: View) {
    checkNotNull(child)
    Timber.d("addView, child: ${child.asString()}")
    super.addView(child)
  }
}

fun View.asString() = StringBuilder(DEBUG_VIEW_STRING_DEFAULT_CAPACITY).append(
    "${this::class.java.name}, width: ${this.width}, height: ${this.height}, ${this.getLocationOnScreen()}"
).toString()