package com.android.szparag.batterygraph.widgets

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 15/11/2017.
 */
class DrawableAnimatedDropletWidget : FrameLayout, DrawableAnimatedWidget {

  private lateinit var drawable: Drawable
  private var drawableMargin: Int = 0

  private var dropletSpeed: Int = 1
  private var dropletFadeout: Int = 1



  constructor(context: Context) : this(context, null)
  constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    parseCustomAttributes()
  }

  private fun parseCustomAttributes() {
    drawable = Drawable.createFromPath("asdasd")
  }

}