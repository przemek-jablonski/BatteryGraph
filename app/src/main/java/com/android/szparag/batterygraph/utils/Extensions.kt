package com.android.szparag.batterygraph.utils

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_BOOT_COMPLETED
import android.content.Intent.ACTION_LOCKED_BOOT_COMPLETED
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.BatteryManager.EXTRA_HEALTH
import android.os.BatteryManager.EXTRA_LEVEL
import android.os.BatteryManager.EXTRA_PLUGGED
import android.os.BatteryManager.EXTRA_SCALE
import android.os.BatteryManager.EXTRA_STATUS
import android.os.BatteryManager.EXTRA_TEMPERATURE
import android.os.BatteryManager.EXTRA_VOLTAGE
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import com.android.szparag.batterygraph.events.BatteryStatusEvent
import com.android.szparag.batterygraph.events.ConnectivityNetworkType
import com.android.szparag.batterygraph.events.ConnectivityStateEvent
import com.android.szparag.batterygraph.events.DevicePowerEvent
import com.android.szparag.batterygraph.events.FlightModeEvent
import com.android.szparag.batterygraph.events.UnixTimestamp
import timber.log.Timber

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 01/11/2017.
 */

fun Context.createRegisteredBroadcastReceiver(
    vararg intentFilterActions: String, callback: (Intent) -> (Unit)): BroadcastReceiver {
  Timber.d(
      "createRegisteredBroadcastReceiver, intentFilterActions: $intentFilterActions, callback: $callback, context: $this")
  val broadcastReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
      intent?.takeIf { intentFilterActions.contains(it.action) }?.let(callback::invoke)
    }
  }
  this.registerReceiver(broadcastReceiver, IntentFilter().apply { intentFilterActions.forEach(this::addAction) })
  return broadcastReceiver
}

fun BroadcastReceiver.unregisterReceiverFromContext(context: Context) {
  context.unregisterReceiver(this)
}

fun Bundle.mapToBatteryStatusEvent(unixTimestamp: UnixTimestamp) = BatteryStatusEvent(
    eventUnixTimestamp = unixTimestamp,
    batteryStatusInt = getInt(EXTRA_STATUS),
    batteryHealthInt = getInt(EXTRA_HEALTH),
    batteryPowerSourceInt = getInt(EXTRA_PLUGGED),
    batteryPercentage = ((getInt(EXTRA_LEVEL) / getInt(EXTRA_SCALE).toFloat()) * 100f).toInt(),
    batteryVoltage = getInt(EXTRA_VOLTAGE) / 1000f,
    batteryTemperature = getInt(EXTRA_TEMPERATURE) / 10
)

fun ConnectivityManager.mapToConnectivityEvent() = activeNetworkInfo?.let { network ->
  ConnectivityStateEvent(
      networkType = ConnectivityNetworkType.fromInt(network.type),
      networkState = network.detailedState,
      networkStateReason = network.reason
  )
} ?: ConnectivityStateEvent()


fun Intent.mapToDevicePowerEvent(unixTimestamp: UnixTimestamp) = DevicePowerEvent(
    deviceOn = this.action == ACTION_BOOT_COMPLETED
)

@RequiresApi(Build.VERSION_CODES.N)
fun Intent.mapToDevicePowerEventApiN(unixTimestamp: UnixTimestamp) = DevicePowerEvent(
    deviceOn = this.action == ACTION_LOCKED_BOOT_COMPLETED || this.action == ACTION_BOOT_COMPLETED
)

fun Bundle.mapToFlightModeEvent(unixTimestamp: UnixTimestamp) = FlightModeEvent(
    flightModeOn = getBoolean("state", false)
)

fun invalidIntValue() = -1
fun invalidLongValue() = -1L
fun invalidFloatValue() = -1f

fun Intent.toPendingIntent(context: Context, requestCode: Int = 0, flags: Int = 0) =
    PendingIntent.getActivity(context, requestCode, this, flags)

//todo refactor that, this may be cool, but code quality needs to be better
fun Intent.asString() = StringBuilder(1024).append(
    "Intent: [action: ${this.action}, cat: ${this.categories}, component: ${this.component}, flags: ${this.flags} bundle: ${this
        .extras.asString(StringBuilder(1024))}]")

fun Bundle?.asString(stringBuilder: StringBuilder): StringBuilder {
  if (this == null) return stringBuilder.append("[null]")
  val keys = this.keySet()
  stringBuilder.append("[")
  keys.forEach { key ->
    stringBuilder.append("$key: ${this.get(key)}, ")
  }
  stringBuilder.delete(stringBuilder.length - 2, stringBuilder.length - 1).append("]")
  return stringBuilder
}

fun <E : Any> Collection<E>.safeLast() = when (this) {
  is List -> if (isEmpty()) null else this[this.lastIndex]
  else -> {
    val iterator = iterator()
    if (!iterator.hasNext()) null
    var last = iterator.next()
    while (iterator.hasNext())
      last = iterator.next()
    last
  }
}

inline fun <T, R> Iterable<T>.map(transform: (T) -> R, initialCapacity: Int) =
    mapTo(ArrayList<R>(initialCapacity), transform)

fun <T : Any> List<T>.safeLast() = if (!isEmpty()) this[lastIndex] else null

fun getUnixTimestampMillis() = System.currentTimeMillis() //todo: currenttimemillis

fun getUnixTimestampSecs() = getUnixTimestampMillis() / 1000L

private const val BGUnixTimestampOrigin = 1510099200

fun getBGUnixTimestampSecs() = getUnixTimestampSecs() - BGUnixTimestampOrigin