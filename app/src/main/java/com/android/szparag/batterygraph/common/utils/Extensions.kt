package com.android.szparag.batterygraph.common.utils

import android.annotation.SuppressLint
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
import com.android.szparag.batterygraph.common.events.BatteryStateEvent
import com.android.szparag.batterygraph.common.events.ConnectivityStateEvent
import com.android.szparag.batterygraph.common.events.DevicePowerStateEvent
import com.android.szparag.batterygraph.common.events.FlightModeStateEvent
import timber.log.Timber
import java.util.Arrays

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 01/11/2017.
 */

//https://github.com/JakeWharton/timber/issues/132#issuecomment-347117478
@SuppressLint("BinaryOperationInTimber")
fun Context.createRegisteredBroadcastReceiver(
    vararg intentFilterActions: String, callback: (Intent) -> (Unit)): BroadcastReceiver {
  Timber.d("createRegisteredBroadcastReceiver, intentFilterActions: ${intentFilterActions.arrayAsString()}, " +
      "callback: $callback, context: ${this::class.java.simpleName}")
  val broadcastReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
      intent?.takeIf { intentFilterActions.contains(it.action) }?.let(callback::invoke)
    }
  }
  this.registerReceiver(broadcastReceiver, IntentFilter().apply { intentFilterActions.forEach(this::addAction) })
  return broadcastReceiver
}

fun BroadcastReceiver.unregisterReceiverFromContext(context: Context) =
    context.unregisterReceiver(this)

fun Context.getStickyIntentFromSystem(intentFilterAction: String): Intent = registerReceiver(null, IntentFilter(intentFilterAction))
    .also { Timber.d("getStickyIntentFromSystem, intentFilterAction: $intentFilterAction, result: ${it.asString()}") }

fun Bundle.mapToBatteryStatusEvent() = BatteryStateEvent(
    eventUnixTimestamp = getUnixTimestampSecs(),
    batteryStatusInt = getInt(EXTRA_STATUS),
    batteryHealthInt = getInt(EXTRA_HEALTH),
    batteryPowerSourceInt = getInt(EXTRA_PLUGGED),
    batteryPercentage = ((getInt(EXTRA_LEVEL) / getInt(EXTRA_SCALE).toFloat()) * 100f).toInt(),
    batteryVoltage = getInt(EXTRA_VOLTAGE) / 1000f,
    batteryTemperature = getInt(EXTRA_TEMPERATURE) / 10
)

fun ConnectivityManager.mapToConnectivityEvent() = activeNetworkInfo?.let { network ->
  ConnectivityStateEvent(
      eventUnixTimestamp = getUnixTimestampSecs(),
      networkType = network.type,
      networkState = network.detailedState.ordinal,
      networkStateReason = network.reason ?: emptyString()
  )
} ?: ConnectivityStateEvent(getUnixTimestampSecs())


fun Intent.mapToDevicePowerEvent(unixTimestamp: UnixTimestamp) = DevicePowerStateEvent(
    eventUnixTimestamp = unixTimestamp,
    deviceOn = this.action == ACTION_BOOT_COMPLETED
)

@RequiresApi(Build.VERSION_CODES.N)
fun Intent.mapToDevicePowerEventApiN(unixTimestamp: UnixTimestamp) = DevicePowerStateEvent(
    eventUnixTimestamp = unixTimestamp,
    deviceOn = this.action == ACTION_LOCKED_BOOT_COMPLETED || this.action == ACTION_BOOT_COMPLETED
)

fun Bundle.mapToFlightModeEvent(unixTimestamp: UnixTimestamp) = FlightModeStateEvent(
    eventUnixTimestamp = unixTimestamp,
    flightModeOn = getBoolean("state", false)
)

fun nullString() = "NULL"
fun emptyString() = ""
fun invalidStringValue() = emptyString()
fun invalidIntValue() = -1
fun invalidLongValue() = -1L
fun invalidFloatValue() = -1f

fun Intent.toPendingIntent(context: Context, requestCode: Int = 0, flags: Int = 0): PendingIntent =
    PendingIntent.getActivity(context, requestCode, this, flags)

//todo refactor that, this may be cool, but code quality needs to be better
//fun <E : Any> Collection<E>.safeLast() = when (this) {
//  is List -> if (isEmpty()) null else this[this.lastIndex]
//  else -> {
//    val iterator = iterator()
//    if (!iterator.hasNext()) null
//    var last = iterator.next()
//    while (iterator.hasNext())
//      last = iterator.next()
//    last
//  }
//}

inline fun <T, R> Iterable<T>.map(transform: (T) -> R, initialCapacity: Int) =
    mapTo(ArrayList(initialCapacity), transform)

fun <T : Any> List<T>.safeLast() = if (!isEmpty()) this[lastIndex] else null

fun getUnixTimestampMillis() = System.currentTimeMillis() //todo: currenttimemillis

fun getUnixTimestampSecs() = getUnixTimestampMillis() / 1000L

private const val BGUnixTimestampOrigin = 1510099200

fun getBGUnixTimestampSecs() = getUnixTimestampSecs() - BGUnixTimestampOrigin

fun <T : Any> Array<T>.arrayAsString() = Arrays.toString(this) ?: nullString()

fun <T : Any> List<T>.lastOr(default: T) = if (this.isNotEmpty()) this.last() else default