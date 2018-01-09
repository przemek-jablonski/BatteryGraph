package com.android.szparag.batterygraph.screens.chart.widgets

import android.content.Context
import android.graphics.BitmapFactory
import android.util.AttributeSet
import com.android.szparag.batterygraph.R
import com.android.szparag.batterygraph.R.drawable
import com.android.szparag.batterygraph.common.events.BatteryStateEvent
import com.android.szparag.batterygraph.common.events.FlightModeStateEvent
import com.android.szparag.batterygraph.common.utils.map
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.PercentFormatter
import timber.log.Timber
import java.text.DecimalFormat

private const val CIRCLE_TYPE_BATTERY = 0
private const val CIRCLE_TYPE_FLIGHT_MODE = 1

//todo: refactor so that it takes only one type of data, put it into interface
//todo: and put common logic (with SmallChartWidget) into BatteryGraphChartWidget
class BatteryGraphChartWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LineChart(context, attrs, defStyleAttr) {
  //todo: this does not extend from LineChartBaseWidget

  private var batteryEntries = emptyList<Entry>() //todo: refactor to emptyArrayList()
  private var batteryDataSet: LineDataSet? = null
  private var flightModeEntries = emptyList<Entry>()
  private var flightModeDataSet: LineDataSet? = null

  init {
    Timber.d("init")
    isLogEnabled = true
    data = LineData()
    stylizeChart()
    stylizeXAxis(xAxis)
    stylizeYLeftAxis(axisLeft)
    stylizeYRightAxis(axisRight)
  }

  fun setBatteryData(data: List<BatteryStateEvent>) {
    Timber.d("setBatteryData, data.size: ${data.size}, data.last: ${data.lastOrNull()}")
    if (data.isEmpty()) return
    batteryEntries = batteryListToEntryList(data)
    batteryDataSet = LineDataSet(batteryEntries, "BatteryPercentage") //todo literal

    stylizeBatteryLineDataSet(batteryDataSet)
    applyDataToChart()
  }

  //todo wtf is this, this should be PowerOnOffEvent
  fun setFlightModeData(data: List<FlightModeStateEvent>) {
    Timber.d("setFlightModeData, data.size: ${data.size}, data.last: ${data.lastOrNull()}")
    if (data.isEmpty()) return
    flightModeEntries = flightModeListToEntryList(data)
    flightModeDataSet = LineDataSet(flightModeEntries, "Flight Mode State") //todo literal

    stylizeFlightLineDataSet(flightModeDataSet)
    applyDataToChart()
  }

  private fun applyDataToChart() {
    Timber.d("applyDataToChart, lineData: $lineData")
    data.clearValues()
    invalidate()
    data.addDataSet(batteryDataSet)
    data.addDataSet(flightModeDataSet)
    invalidate()
  }

  private fun stylizeChart() {
    isScaleYEnabled = false
    isDoubleTapToZoomEnabled = false
    dragDecelerationFrictionCoef = 0.83f
    setDrawBorders(false)
    isKeepPositionOnRotation = true
    setMaxVisibleValueCount(0)
    description.isEnabled = false
    renderer = CustomDrawableLineChartRenderer(this, animator, viewPortHandler,
        BitmapFactory.decodeResource(resources, drawable.ic_icon_battery))
  }

  private fun stylizeXAxis(xAxis: XAxis) {
    xAxis.setAvoidFirstLastClipping(true)
    xAxis.setDrawGridLines(false)
    xAxis.setDrawAxisLine(false)
    xAxis.setDrawLabels(false)
  }


  private fun stylizeYLeftAxis(yAxis: YAxis) {
    yAxis.axisMinimum = -10f
    yAxis.axisMaximum = 100f
    yAxis.gridLineWidth = 0.5f
    yAxis.valueFormatter = PercentFormatter(DecimalFormat("###,###,##0"))
    yAxis.setDrawAxisLine(false)
    yAxis.setLabelCount(6, false)
  }

  private fun stylizeYRightAxis(yAxis: YAxis) {
    yAxis.isEnabled = false
  }

  /**
   * Stylizes one Data Set (one line of the chart). Governs colours, alphas, widths of both lines and value dots,
   * smooting, highlighting etc.
   */
  private fun stylizeBatteryLineDataSet(lineDataSet: LineDataSet?) {
    Timber.d("stylizeBatteryLineDataSet, lineDataSet: $lineDataSet")
    lineDataSet?.let {
      //  smoothing
      it.mode = LineDataSet.Mode.HORIZONTAL_BEZIER

      //  lines width and colour
      it.lineWidth = 2f
      it.color = resources.getColor(R.color.colorAccent1)

      //  data circles colours
      it.setCircleColor(resources.getColor(R.color.colorAccent1))
      it.circleRadius = 5f
      it.setDrawCircleHole(false)

      it.highLightColor = resources.getColor(R.color.colorAccent1AlphaMore)
      it.highlightLineWidth = 1f

      // chart filling
//      it.fillColor = resources.getColor(R.color.colorPrimaryDark)
//      it.fillAlpha = 185
//      it.fillFormatter = IFillFormatter { dataSet, dataProvider -> //todo: inlining
//        val data = dataProvider.lineData
//        Math.max(
//            0f,
//            if (dataSet.yMax > 0 && dataSet.yMin < 0) {
//              0f
//            } else {
//              val max: Float = if (data.yMax > 0) 0f else dataProvider.yChartMax
//              val min: Float = if (data.yMin < 0) 0f else dataProvider.yChartMin
//              if (dataSet.yMin >= 0) min else max
//            })
//      }
      it.setDrawFilled(true)
      it.fillDrawable = resources.getDrawable(R.drawable.gradient_chart_fill_alpha)
    }
  }

  private fun stylizeFlightLineDataSet(lineDataSet: LineDataSet?) {
    Timber.d("stylizeFlightLineDataSet, lineDataSet: $lineDataSet")
    lineDataSet?.let {

      //  lines width and colour
      it.lineWidth = 4f
      it.color = resources.getColor(R.color.colorAccent2Alpha)

      //  data circles colours
      it.setCircleColor(resources.getColor(R.color.colorAccent2))
      it.circleRadius = 7f
      it.setDrawCircleHole(true)

      it.highLightColor = resources.getColor(R.color.colorAccent2AlphaMore)
      it.highlightLineWidth = 1f

      it.enableDashedLine(0.5f, 0.5f, 0f)
    }
  }

  private fun batteryListToEntryList(dataList: List<BatteryStateEvent>) =
      dataList.map({ data ->
        Entry(
            data.eventUnixTimestamp.toFloat(),
            data.batteryPercentage.toFloat(),
            resources.getDrawable(R.drawable.ic_icon_battery))
      }, dataList.size)

  private fun flightModeListToEntryList(dataList: List<FlightModeStateEvent>) =
      dataList.map({ data ->
        Entry(
            data.eventUnixTimestamp.toFloat(),
            -3f,
            resources.getDrawable(R.drawable.ic_icon_airplane))
      }, dataList.size)

}