package com.android.szparag.batterygraph.shared.events

import android.os.BatteryManager

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 01/11/2017.
 */

enum class BatteryStatus(val statusInt: Int) {

  FULL(BatteryManager.BATTERY_STATUS_FULL),
  NOT_CHARGING(BatteryManager.BATTERY_STATUS_NOT_CHARGING),
  DISCHARGING(BatteryManager.BATTERY_STATUS_DISCHARGING),
  CHARGING(BatteryManager.BATTERY_STATUS_CHARGING),
  UNKNOWN(BatteryManager.BATTERY_STATUS_UNKNOWN);

  companion object {
    @JvmStatic
    fun fromInt(statusInt: Int) = when (statusInt) {
      FULL.statusInt -> FULL
      NOT_CHARGING.statusInt -> NOT_CHARGING
      DISCHARGING.statusInt -> DISCHARGING
      CHARGING.statusInt -> CHARGING
      UNKNOWN.statusInt -> UNKNOWN
      else -> UNKNOWN
    }
  }
}
