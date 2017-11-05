package com.android.szparag.batterygraph.widget

import android.content.Context
import android.util.AttributeSet
import com.android.szparag.batterygraph.events.BatteryStatusEvent
import com.android.szparag.batterygraph.utils.map
import com.android.szparag.batterygraph.utils.safeLast
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import timber.log.Timber

class BatteryGraphChartWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LineChart(context, attrs, defStyleAttr), ChartWidget<BatteryStatusEvent> {

  init {
    isScaleYEnabled = false
    isDoubleTapToZoomEnabled = false
    dragDecelerationFrictionCoef = 0.5f
  }

  fun setDataList(data: List<BatteryStatusEvent>) {
    Timber.d("setDataList, data.size: ${data.size}, data.last: ${data.safeLast()}")
    if (data.isEmpty()) return
    val dataEntriesList = dataListToEntryList(data)
    val lineDataSet = LineDataSet(dataEntriesList, "BatteryPercentage") //todo literal
    stylizeLineDataSet(lineDataSet)
    val lineData = LineData(lineDataSet)
    stylizeLineData(lineData)
    applyDataToChart(lineData)
  }

  private fun dataListToEntryList(dataList: List<BatteryStatusEvent>) =
      dataList.map({ data -> Entry(data.eventUnixTimestamp.toFloat(), data.batteryPercentage.toFloat()) }, dataList.size)

  private fun stylizeLineDataSet(lineDataSet: LineDataSet) {
    Timber.d("stylizeLineDataSet")
  }

  private fun stylizeLineData(lineData: LineData) {
    Timber.d("stylizeLineData")
  }

  private fun applyDataToChart(lineData: LineData) {
    Timber.d("applyDataToChart")
    data = lineData
    data.notifyDataChanged()
    notifyDataSetChanged()
    invalidate()
  }

}