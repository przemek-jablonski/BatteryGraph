package com.android.szparag.batterygraph.shared.widgets

import android.content.Context
import android.support.annotation.CallSuper
import android.util.AttributeSet
import com.android.szparag.batterygraph.shared.utils.emptyString
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.IDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import timber.log.Timber

/**
 * Base class (abstract) for Line Chart UI element implementation.
 *
 * Built using MPAndroidChart library (https://github.com/PhilJay/MPAndroidChart)
 */
abstract class LineChartBaseWidget<in E : Any> @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LineChart(context, attrs, defStyleAttr), ChartWidget<E> {

  protected var dataEntries = emptyList<Entry>()
  protected var dataSet: LineDataSet? = null //todo can it be lateinit?
  private var initialized: Boolean = false

  @CallSuper override fun initialize() {
    Timber.d("initialize")
    initialized = true
    isLogEnabled = true
    data = LineData()
    stylizeChart()
    stylizeXAxis(xAxis)
    stylizeYLeftAxis(axisLeft)
    stylizeYRightAxis(axisRight)
  }


  /**
   * Stylize entire Line Chart UI widget.
   *
   * @see LineChart
   */
  protected fun stylizeChart() {
    Timber.d("stylizeChart")
    checkInitalization()
    isScaleYEnabled = false
    isDoubleTapToZoomEnabled = false
    dragDecelerationFrictionCoef = 0.83f
    setDrawBorders(false)
    isKeepPositionOnRotation = true
    setMaxVisibleValueCount(0)
    description.isEnabled = false
  }

  /**
   * Configure look and feel option for X-Axis of the chart.
   * @see XAxis
   */
  protected fun stylizeXAxis(xAxis: XAxis) {
    Timber.d("stylizeXAxis, xAxis: $xAxis")
    checkInitalization()
    xAxis.setAvoidFirstLastClipping(true)
    xAxis.setDrawGridLines(false)
    xAxis.setDrawAxisLine(false)
    xAxis.setDrawLabels(false)
  }

  /**
   * Configure look and feel option for Y-Axis (left one) of the chart.
   * @see YAxis
   */
  protected fun stylizeYLeftAxis(yAxis: YAxis) {
    Timber.d("stylizeYLeftAxis, yAxis: $yAxis")
    checkInitalization()
    yAxis.axisMinimum = 0f
    yAxis.axisMaximum = 100f
    yAxis.gridLineWidth = 0.5f
    yAxis.setDrawAxisLine(false)
    yAxis.setLabelCount(6, false)
  }

  /**
   * Configure look and feel option for Y-Axis (right one) of the chart.
   * @see YAxis
   */
  protected fun stylizeYRightAxis(yAxis: YAxis) {
    Timber.d("stylizeYRightAxis, yAxis: $yAxis")
    checkInitalization()
    yAxis.isEnabled = false
  }

  /**
   * Configure look of the data line in the chart
   */
  protected fun stylizeChartLine(dataSet: LineDataSet) {
    Timber.d("stylizeChartLine, dataSet: $dataSet")
    checkInitalization()
  }


  override fun setData(data: List<E>) {
    Timber.d("setData, data: $data")
    if (data.isEmpty()) return
    dataEntries = data.map { mapDataToEntry(it) }
    dataSet = LineDataSet(dataEntries, emptyString())
    dataSet?.let {
      stylizeChartLine(it)
      applyDataSetToChart(it)
    }
  }


  /**
   * Assigning new values for chart to render. Inputs are of chart-compatible type (ILineDataSet, subclass of IDataSet).
   * Performs data set invalidation after assignment, which actually triggers rendering process.
   *
   * @param dataSet List of items to render on chart. Type is chart compatible, not specified generic.
   *
   * @see ILineDataSet
   * @see ILineDataSet
   * @see IDataSet
   */
  protected fun applyDataSetToChart(dataSet: ILineDataSet) {
    Timber.d("applyDataSetToChart, dataSet: $dataSet")
    checkInitalization()
    data.clearValues()
    data.addDataSet(dataSet)
    invalidate()
  }

  /**
   * Mapping between generic type and chart-compatible type
   * @see Entry
   */
  abstract fun mapDataToEntry(data: E): Entry

  protected fun checkInitalization() {
    Timber.d("checkInitalization, value: $initialized")
    check(initialized, { throw RuntimeException("Method called before initialization.") }) //todo
  }

}