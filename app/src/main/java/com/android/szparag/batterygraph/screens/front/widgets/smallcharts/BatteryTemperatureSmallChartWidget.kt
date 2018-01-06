package com.android.szparag.batterygraph.screens.front.widgets.smallcharts

import android.content.Context
import android.util.AttributeSet
import com.android.szparag.batterygraph.common.events.BatteryStateEvent
import com.android.szparag.batterygraph.common.utils.inverseLerp
import com.android.szparag.batterygraph.common.utils.lerp
import com.android.szparag.batterygraph.common.widgets.LineChartSmallBaseWidget
import com.github.mikephil.charting.data.Entry
import timber.log.Timber

typealias Centigrade = Float
//todo: https://www.quora.com/what-is-the-average-workable-battery-temperature-for-a-smart-phone
private const val TEMPERATURE_MINIMUM_VALUE = 0f
private const val TEMPERATURE_MAXIMUM_VALUE = 45f

class BatteryTemperatureSmallChartWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LineChartSmallBaseWidget<BatteryStateEvent>(context, attrs, defStyleAttr) {


  override fun mapDataToEntry(data: BatteryStateEvent) =
      Entry(
          data.eventUnixTimestamp.toFloat(),
          lerp(yAxisMinimumValue, yAxisMaximumValue,
              inverseLerp(TEMPERATURE_MINIMUM_VALUE, TEMPERATURE_MAXIMUM_VALUE, data.batteryTemperature.toFloat())))
          .also { Timber.v("mapDataToEntry, data: $data, entry: $it") }

}