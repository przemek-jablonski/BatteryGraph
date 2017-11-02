package com.android.szparag.batterygraph

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager.*
import android.os.Bundle
import android.view.animation.ScaleAnimation
import com.android.szparag.batterygraph.events.BatteryStatusEvent

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 01/11/2017.
 */

fun Activity.createRegisteredBroadcastReceiver(
    vararg intentFilterActions: String, callback: (Intent) -> (Unit)): BroadcastReceiver {
  val broadcastReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
      intent?.let(callback::invoke)
    }
  }
  this.registerReceiver(
      broadcastReceiver,
      IntentFilter().apply { intentFilterActions.forEach(this::addAction) }
  )
  return broadcastReceiver
}

fun BroadcastReceiver.unregisterReceiver(activity: Activity) {
  activity.unregisterReceiver(this)
}

fun Bundle.mapToBatteryStatusEvent() = BatteryStatusEvent(
    batteryStatusInt = getInt(EXTRA_STATUS),
    batteryHealthInt = getInt(EXTRA_HEALTH),
    batteryPowerSourceInt = getInt(EXTRA_PLUGGED),
    batteryPercentage = (getInt(EXTRA_LEVEL) / getInt(EXTRA_SCALE).toFloat()).toInt(),
    batteryVoltage = getInt(EXTRA_VOLTAGE),
    batteryTemperature = getInt(EXTRA_TEMPERATURE),
    batteryCycleCount = getInt("charge_counter")
)
