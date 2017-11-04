package com.android.szparag.batterygraph.utils

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager.EXTRA_HEALTH
import android.os.BatteryManager.EXTRA_LEVEL
import android.os.BatteryManager.EXTRA_PLUGGED
import android.os.BatteryManager.EXTRA_SCALE
import android.os.BatteryManager.EXTRA_STATUS
import android.os.BatteryManager.EXTRA_TEMPERATURE
import android.os.BatteryManager.EXTRA_VOLTAGE
import android.os.Bundle
import com.android.szparag.batterygraph.events.BatteryStatusEvent

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 01/11/2017.
 */

fun Activity.createRegisteredBroadcastReceiver(
    vararg intentFilterActions: String, callback: (Intent) -> (Unit)): BroadcastReceiver {
  val broadcastReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
      intent?.takeIf { intentFilterActions.contains(it.action) }?.let(callback::invoke)
    }
  }
  this.registerReceiver(
      broadcastReceiver,
      IntentFilter().apply { intentFilterActions.forEach(this::addAction) }
  )
  return broadcastReceiver
}

fun BroadcastReceiver.unregisterReceiverFromActivity(activity: Activity) {
  activity.unregisterReceiver(this)
}

fun Bundle.mapToBatteryStatusEvent() = BatteryStatusEvent(
    batteryStatusInt = getInt(EXTRA_STATUS),
    batteryHealthInt = getInt(EXTRA_HEALTH),
    batteryPowerSourceInt = getInt(EXTRA_PLUGGED),
    batteryPercentage = ((getInt(EXTRA_LEVEL) / getInt(EXTRA_SCALE).toFloat()) * 100f).toInt(),
    batteryVoltage = getInt(EXTRA_VOLTAGE) / 1000f,
    batteryTemperature = getInt(EXTRA_TEMPERATURE) / 10
)

fun invalidIntValue() = -1
fun invalidLongValue() = -1L
fun invalidFloatValue() = -1f
