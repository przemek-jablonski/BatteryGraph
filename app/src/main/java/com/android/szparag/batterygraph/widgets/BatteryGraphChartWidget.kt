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
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.PercentFormatter
import timber.log.Timber
import java.text.DecimalFormat

class BatteryGraphChartWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LineChart(context, attrs, defStyleAttr), ChartWidget<BatteryStateEvent> {

  var dataEntries: List<Entry>? = null
  var lineDataSet: LineDataSet? = null

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
    Timber.d("setDataList, entries.size: ${dataEntries?.size}, entries.last: ${dataEntries?.safeLast()}")
    lineDataSet = LineDataSet(dataEntries, "BatteryPercentage") //todo literal
    stylizeLineDataSet(lineDataSet)
    val lineData = LineData(lineDataSet)
    stylizeLineData(lineData)
    applyDataToChart(lineData)
  }

  private fun dataListToEntryList(dataList: List<BatteryStateEvent>) =
      dataList.map({ data -> Entry(data.eventUnixTimestamp.toFloat(), data.batteryPercentage.toFloat()) }, dataList.size)

  private fun stylizeLineDataSet(lineDataSet: LineDataSet?) {
    Timber.d("stylizeLineDataSet, lineDataSet: $lineDataSet")
    lineDataSet?.let {
      //  smoothing
      it.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
      // chart filling
      it.setDrawFilled(true)
      it.fillColor = resources.getColor(R.color.colorPrimaryDark)
      it.fillAlpha = 185
      //  lines width and colour
      it.lineWidth = 2f
      it.color = resources.getColor(R.color.colorAccent)
      //  data circles colours
      it.setCircleColor(resources.getColor(R.color.colorAccentAlpha))
      it.circleRadius = 5f
    }
  }

  private fun stylizeLineData(lineData: LineData?) {
    Timber.d("stylizeLineData, lineData: $lineData")
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
    yAxis.axisMinimum = 0f
    yAxis.axisMaximum = 100f
    yAxis.gridLineWidth = 0.5f
    yAxis.valueFormatter = PercentFormatter(DecimalFormat("###,###,##0"))
    yAxis.setDrawAxisLine(false)
    yAxis.setLabelCount(6, true)
  }

  private fun stylizeYRightAxis(yAxis: YAxis) {
    yAxis.isEnabled = false
  }

}