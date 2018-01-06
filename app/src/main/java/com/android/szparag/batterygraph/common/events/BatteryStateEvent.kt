package com.android.szparag.batterygraph.common.events

import com.android.szparag.batterygraph.common.utils.UnixTimestamp
import com.android.szparag.batterygraph.common.utils.invalidFloatValue
import com.android.szparag.batterygraph.common.utils.invalidIntValue
import com.android.szparag.batterygraph.common.utils.invalidLongValue

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 01/11/2017.
 */

data class BatteryStateEvent(
    val eventUnixTimestamp: UnixTimestamp,
    val batteryStatus: BatteryStatus,
    val batteryHealth: BatteryHealth,
    val batteryPowerSource: BatteryPowerSource,
    val batteryPercentage: Int,
    val batteryVoltage: Float,
    val batteryTemperature: Int
) {

  constructor(eventUnixTimestamp: UnixTimestamp, batteryStatusInt: Int, batteryHealthInt: Int,
      batteryPowerSourceInt: Int, batteryPercentage: Int, batteryVoltage: Float, batteryTemperature: Int) : this(
      eventUnixTimestamp = eventUnixTimestamp,
      batteryStatus = BatteryStatus.fromInt(batteryStatusInt),
      batteryHealth = BatteryHealth.fromInt(batteryHealthInt),
      batteryPowerSource = BatteryPowerSource.fromInt(batteryPowerSourceInt),
      batteryPercentage = batteryPercentage,
      batteryVoltage = batteryVoltage,
      batteryTemperature = batteryTemperature
  )

  companion object {
    fun generateInvalidEvent() = BatteryStateEvent(invalidLongValue(), invalidIntValue(), invalidIntValue(),
        invalidIntValue(), invalidIntValue(), invalidFloatValue(), invalidIntValue())
  }

}


