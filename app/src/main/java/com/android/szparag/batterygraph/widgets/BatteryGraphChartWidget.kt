package com.android.szparag.batterygraph.widgets

import android.content.Context
import android.util.AttributeSet
import com.android.szparag.batterygraph.R
import com.android.szparag.batterygraph.base.events.BatteryStateEvent
import com.android.szparag.batterygraph.base.utils.map
import com.android.szparag.batterygraph.base.utils.safeLast
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency.LEFT
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import timber.log.Timber
import java.text.DecimalFormat

class BatteryGraphChartWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LineChart(context, attrs, defStyleAttr), ChartWidget<BatteryStateEvent> {

  private var dataEntries: List<Entry>? = null
  private var lineDataSet: LineDataSet? = null

  init {
    isLogEnabled = true
    stylizeChart()
    stylizeXAxis(xAxis)
    stylizeYLeftAxis(axisLeft)
    stylizeYRightAxis(axisRight)
  }

  fun setDataList(data: List<BatteryStateEvent>) {
    Timber.d("setDataList, data.size: ${data.size}, data.last: ${data.safeLast()}")
    if (data.isEmpty()) return
    dataEntries = dataListToEntryList(data)
    lineDataSet = LineDataSet(dataEntries, "BatteryPercentage") //todo literal
    val lineData = LineData(lineDataSet)

    stylizeLineDataSet(lineDataSet)
    stylizeLineData(lineData)
    applyDataToChart(lineData)
  }

  private fun dataListToEntryList(dataList: List<BatteryStateEvent>) =
      dataList.map({ data -> Entry(data.eventUnixTimestamp.toFloat(), data.batteryPercentage.toFloat()) },
          dataList.size)

  /**
   * Stylizes one Data Set (one line of the chart). Governs colours, alphas, widths of both lines and value dots,
   * smooting, highlighting etc.
   */
  private fun stylizeLineDataSet(lineDataSet: LineDataSet?) {
    Timber.d("stylizeLineDataSet, lineDataSet: $lineDataSet")
    lineDataSet?.let {
      //  smoothing
      it.mode = LineDataSet.Mode.HORIZONTAL_BEZIER

      // chart filling
      it.setDrawFilled(true)
      it.fillColor = resources.getColor(R.color.colorPrimaryDark)
      it.fillAlpha = 185
      it.fillFormatter = IFillFormatter { dataSet, dataProvider -> //todo: inlining
        val data = dataProvider.lineData
        Math.max(
            0f,
            if (dataSet.yMax > 0 && dataSet.yMin < 0) {
              0f
            } else {
              val max: Float = if (data.yMax > 0) 0f else dataProvider.yChartMax
              val min: Float = if (data.yMin < 0) 0f else dataProvider.yChartMin
              if (dataSet.yMin >= 0) min else max
            })
      }

      //  lines width and colour
      it.lineWidth = 2f
      it.color = resources.getColor(R.color.colorAccent)

      //  data circles colours
      it.setCircleColor(resources.getColor(R.color.colorAccentAlpha))
      it.circleRadius = 5f
      it.setDrawCircleHole(false)

      it.highLightColor = resources.getColor(R.color.colorAccentAlpha)
      it.highlightLineWidth = 2f
    }
  }

  /**
   * Stylizes and configures Line Data (combination of all lines in the chart) more from the data perspective -
   * labels, formatters, entries etc.
   */
  private fun stylizeLineData(lineData: LineData?) {
    Timber.d("stylizeLineData, lineData: $lineData")
    lineData?.let {
    }
  }

  private fun applyDataToChart(lineData: LineData?) {
    Timber.d("applyDataToChart, lineData: $lineData")
    data = lineData
    invalidate()
  }

  private fun stylizeChart() {
    isScaleYEnabled = false
    isDoubleTapToZoomEnabled = false
    dragDecelerationFrictionCoef = 0.83f
    setDrawBorders(false)
    isKeepPositionOnRotation = true
    setMaxVisibleValueCount(0)
  }

  private fun stylizeXAxis(xAxis: XAxis) {
//    xAxis.setAvoidFirstLastClipping(true)
  }

  private fun stylizeYLeftAxis(yAxis: YAxis) {
    yAxis.axisMinimum = -3f
    yAxis.axisMaximum = 100f
    yAxis.gridLineWidth = 0.5f
    yAxis.valueFormatter = PercentFormatter(DecimalFormat("###,###,##0"))
    yAxis.setDrawAxisLine(false)
    yAxis.setLabelCount(6, false)
  }

  private fun stylizeYRightAxis(yAxis: YAxis) {
    yAxis.isEnabled = false
  }

}