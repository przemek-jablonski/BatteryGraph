package com.android.szparag.batterygraph.shared.widgets

import android.content.Context
import android.util.AttributeSet
import com.github.mikephil.charting.components.YAxis
import timber.log.Timber

/**
 * Base class for creating small variant of charts in BatteryGraph.
 * Contains mostly styling changes which are common to all small charts in the app.
 *
 * @see LineChartBaseWidget
 */
abstract class LineChartSmallBaseWidget<in E : Any> @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LineChartBaseWidget<E>(context, attrs, defStyleAttr) {


  override fun stylizeChart() {
    super.stylizeChart()
    data.isHighlightEnabled = false
    isHighlightPerTapEnabled = false
    isHighlightPerDragEnabled = false
    setPinchZoom(false)
    isDoubleTapToZoomEnabled = false
  }

  override fun stylizeYLeftAxis(yAxis: YAxis) {
    Timber.d("stylizeYLeftAxis, yAxis: $yAxis")
    checkInitalization()
    yAxis.axisMinimum = 0f
    yAxis.axisMaximum = 100f
    yAxis.setDrawGridLines(false)
    yAxis.setDrawAxisLine(false)
    yAxis.setDrawLabels(false)
  }

  //todo: stylizeBatteryLineDataSet has to be protected open

}