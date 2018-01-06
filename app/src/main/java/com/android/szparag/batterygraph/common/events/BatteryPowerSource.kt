package com.android.szparag.batterygraph.common.events

import android.os.BatteryManager

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 01/11/2017.
 */

enum class BatteryPowerSource(val sourceInt: Int) {

  BATTERY(0),
  AC(BatteryManager.BATTERY_PLUGGED_AC),
  WIRELESS(BatteryManager.BATTERY_PLUGGED_WIRELESS),
  USB(BatteryManager.BATTERY_PLUGGED_USB);

  companion object {
    @JvmStatic
    fun fromInt(sourceInt: Int) = when (sourceInt) {
      AC.sourceInt -> AC
      WIRELESS.sourceInt -> WIRELESS
      USB.sourceInt -> USB
      BATTERY.sourceInt -> BATTERY
      else -> BATTERY
    }
  }
}