package com.android.szparag.batterygraph.common.events

//todo android dependency here, remove it from here (to some extensions or something)
import android.os.BatteryManager

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 01/11/2017.
 */

enum class BatteryHealth(val healthInt: Int) {

  UNKNOWN(BatteryManager.BATTERY_HEALTH_UNKNOWN),
  GOOD(BatteryManager.BATTERY_HEALTH_GOOD),
  OVERHEAT(BatteryManager.BATTERY_HEALTH_OVERHEAT),
  DEAD(BatteryManager.BATTERY_HEALTH_DEAD),
  OVERVOLTAGE(BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE),
  FAILURE(BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE),
  COLD(BatteryManager.BATTERY_HEALTH_COLD);

  companion object {
    @JvmStatic
    fun fromInt(healthInt: Int) = when (healthInt) {
      GOOD.healthInt -> GOOD
      OVERHEAT.healthInt -> OVERHEAT
      DEAD.healthInt -> DEAD
      OVERVOLTAGE.healthInt -> OVERVOLTAGE
      FAILURE.healthInt -> FAILURE
      COLD.healthInt -> COLD
      UNKNOWN.healthInt -> UNKNOWN
      else -> UNKNOWN
    }
  }
}