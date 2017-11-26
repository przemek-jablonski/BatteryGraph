package com.android.szparag.batterygraph.screens.chart.widgets

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.renderer.LineChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler

private const val CIRCLE_SIZE_MULTIPLIER = 5

class CustomDrawableLineChartRenderer(
    lineChart: LineChart,
    private val animator: ChartAnimator,
    private val viewPortHandler: ViewPortHandler,
    private val image: Bitmap) : LineChartRenderer(lineChart, animator, viewPortHandler) {

  private val mCirclesBuffer = FloatArray(2)

  override fun drawCircles(c: Canvas) {
    mRenderPaint.style = Paint.Style.FILL
    val phaseY = mAnimator.phaseY
    mCirclesBuffer[0] = 0f
    mCirclesBuffer[1] = 0f
    val dataSets = mChart.lineData.dataSets

    val targetBitmaps = arrayOfNulls<Bitmap>(dataSets.size)
    val targetBitmapOffsets = FloatArray(dataSets.size)

    dataSets.forEachIndexed { index, dataSet ->
      val imageSize = dataSet.circleRadius * CIRCLE_SIZE_MULTIPLIER
      targetBitmapOffsets[index] = imageSize / 2f
      targetBitmaps[index] = scaleImage(image, imageSize.toInt())
    }

    dataSets.forEachIndexed { index, dataSet ->
      if (!dataSet.isVisible || !dataSet.isDrawCirclesEnabled || dataSet.entryCount == 0) return

      mCirclePaintInner.color = dataSet.circleHoleColor
      val trans = mChart.getTransformer(dataSet.axisDependency)
      mXBounds.set(mChart, dataSet)


      val boundsRangeCount = mXBounds.range + mXBounds.min
      for (j in mXBounds.min..boundsRangeCount) {
        val e = dataSet.getEntryForIndex(j) ?: break
        mCirclesBuffer[0] = e.x
        mCirclesBuffer[1] = e.y * phaseY
        trans.pointValuesToPixel(mCirclesBuffer)
        if (!mViewPortHandler.isInBoundsRight(mCirclesBuffer[0]))
          break
        if (!mViewPortHandler.isInBoundsLeft(mCirclesBuffer[0]) || !mViewPortHandler.isInBoundsY(mCirclesBuffer[1]))
          continue

        if (targetBitmaps[index] != null) {
          c.drawBitmap(targetBitmaps[index],
              mCirclesBuffer[0] - targetBitmapOffsets[index],
              mCirclesBuffer[1] - targetBitmapOffsets[index],
              mRenderPaint)
        }
      }
    }

  }


  private fun scaleImage(image: Bitmap, radius: Int) = Bitmap.createScaledBitmap(image, radius, radius, false)

}