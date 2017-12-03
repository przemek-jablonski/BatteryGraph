package com.android.szparag.batterygraph.shared.widgets

import android.content.Context
import android.util.AttributeSet
import com.android.szparag.batterygraph.R
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.LineDataSet
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
    Timber.d("stylizeChart")
    super.stylizeChart()
    data.isHighlightEnabled = false
    isHighlightPerTapEnabled = false
    isHighlightPerDragEnabled = false
    setPinchZoom(false)
    isDoubleTapToZoomEnabled = false
    description.isEnabled = false
    legend.isEnabled = false
  }

  override fun stylizeYLeftAxis(yAxis: YAxis) {
    Timber.d("stylizeYLeftAxis, yAxis: $yAxis")
    checkInitalization()
    yAxis.axisMinimum = 0f
    yAxis.axisMaximum = 100f
    yAxis.setDrawGridLines(false)
    yAxis.setDrawAxisLine(false)
    yAxis.setDrawLabels(false)
    yAxis.isDrawLimitLinesBehindDataEnabled

    yAxis.addLimitLine(LimitLine(yAxisMinimumValue).apply { this.lineColor = resources.getColor(R.color.colorGreyAlpha) })
    yAxis.addLimitLine(LimitLine(yAxisMaximumValue).apply { this.lineColor = resources.getColor(R.color.colorGreyAlpha) })
  }

  override fun stylizeChartLine(dataSet: LineDataSet) {
    Timber.d("stylizeChartLine, dataSet: $dataSet")
    dataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
    dataSet.color = resources.getColor(R.color.colorAccent1)
    dataSet.setDrawCircleHole(false)
    dataSet.setDrawCircles(false)
    dataSet.lineWidth = 2f
    dataSet.setDrawFilled(true)
    dataSet.fillDrawable = resources.getDrawable(R.drawable.gradient_chart_fill_alpha_lighter)
    //todo: this should be parametrizable
  }

}