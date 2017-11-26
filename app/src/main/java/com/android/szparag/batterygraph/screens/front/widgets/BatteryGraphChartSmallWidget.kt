package com.android.szparag.batterygraph.screens.front.widgets

import android.content.Context
import android.util.AttributeSet
import com.android.szparag.batterygraph.shared.widgets.BatteryGraphLineChartBaseWidget
import com.github.mikephil.charting.data.LineDataSet
import timber.log.Timber

abstract class BatteryGraphChartSmallWidget<in E : Any> @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BatteryGraphLineChartBaseWidget<E>(context, attrs, defStyleAttr) {

  override fun initialize() {
    Timber.d("initialize")
    super.initialize()
  }

  override fun setData(data: List<E>) {
    Timber.d("setData, data: $data")
    if (data.isEmpty()) return
    dataEntries = data.map { mapDataToEntry(it) }
    dataSet = LineDataSet(dataEntries, "asdasd") //todo literal
    dataSet?.let {
      stylizeChartLine(it)
      applyDataSetToChart(it)
    }
  }
}