package com.android.szparag.batterygraph.screens.front.widgets.smallcharts

import android.content.Context
import android.util.AttributeSet
import com.android.szparag.batterygraph.common.events.BatteryStateEvent
import com.android.szparag.batterygraph.common.utils.inverseLerp
import com.android.szparag.batterygraph.common.utils.lerp
import com.android.szparag.batterygraph.common.widgets.LineChartSmallBaseWidget
import com.github.mikephil.charting.data.Entry
import timber.log.Timber

typealias Volts = Float

//todo: http://batteryuniversity.com/learn/article/confusion_with_voltages
private const val VOLTAGE_MINIMUM_VALUE: Volts = 2.8f
private const val VOLTAGE_MAXIMUM_VALUE: Volts = 4.4f

class BatteryVoltageSmallChartWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LineChartSmallBaseWidget<BatteryStateEvent>(context, attrs, defStyleAttr) {


  override fun mapDataToEntry(data: BatteryStateEvent) =
      Entry(
          data.eventUnixTimestamp.toFloat(),
          lerp(yAxisMinimumValue, yAxisMaximumValue, inverseLerp(VOLTAGE_MINIMUM_VALUE, VOLTAGE_MAXIMUM_VALUE, data.batteryVoltage)))
          .also { Timber.d("mapDataToEntry, data: $data, entry: $it (rangeMin: $VOLTAGE_MINIMUM_VALUE, rangeMax: $VOLTAGE_MAXIMUM_VALUE") }


}