package com.android.szparag.batterygraph.screens.front.widgets.smallcharts

import android.content.Context
import android.util.AttributeSet
import com.android.szparag.batterygraph.common.events.BatteryHealth
import com.android.szparag.batterygraph.common.events.BatteryStateEvent
import com.android.szparag.batterygraph.common.utils.lerp
import com.android.szparag.batterygraph.common.widgets.LineChartSmallBaseWidget
import com.github.mikephil.charting.data.Entry
import timber.log.Timber

class BatteryHealthSmallChartWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LineChartSmallBaseWidget<BatteryStateEvent>(context, attrs, defStyleAttr) {

  override fun mapDataToEntry(data: BatteryStateEvent) =
      Entry(
          data.eventUnixTimestamp.toFloat(),
          lerp(yAxisMinimumValue, yAxisMaximumValue, mapBatteryHealthToFloat(data.batteryHealth)))
          .also { Timber.v("mapDataToEntry, data: $data, entry: $it") }


  private fun mapBatteryHealthToFloat(batteryHealth: BatteryHealth) =
      when (batteryHealth) {
        BatteryHealth.UNKNOWN -> 0f
        BatteryHealth.GOOD -> 1f
        BatteryHealth.OVERHEAT -> 0.45f
        BatteryHealth.DEAD -> 0f
        BatteryHealth.OVERVOLTAGE -> 0.75f
        BatteryHealth.FAILURE -> 0.15f
        BatteryHealth.COLD -> 0.45f
      }

}