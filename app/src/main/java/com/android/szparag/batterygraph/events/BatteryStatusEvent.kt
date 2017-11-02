package com.android.szparag.batterygraph.events

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 01/11/2017.
 */
data class BatteryStatusEvent(
    val batteryStatus: BatteryStatus,
    val batteryHealth: BatteryHealth,
    val batteryPowerSource: BatteryPowerSource,
    val batteryPercentage: Int,
    val batteryVoltage: Float,
    val batteryTemperature: Int
) {

  constructor(batteryStatusInt: Int, batteryHealthInt: Int, batteryPowerSourceInt: Int,
      batteryPercentage: Int, batteryVoltage: Float, batteryTemperature: Int) : this(
      batteryStatus = BatteryStatus.fromInt(batteryStatusInt),
      batteryHealth = BatteryHealth.fromInt(batteryHealthInt),
      batteryPowerSource = BatteryPowerSource.fromInt(batteryPowerSourceInt),
      batteryPercentage = batteryPercentage,
      batteryVoltage = batteryVoltage,
      batteryTemperature = batteryTemperature
  )
}
