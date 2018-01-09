package com.android.szparag.batterygraph.common.utils

import android.content.Intent
import android.net.ConnectivityManager
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import com.android.szparag.batterygraph.common.events.BatteryStateEvent
import com.android.szparag.batterygraph.common.events.ConnectivityStateEvent
import com.android.szparag.batterygraph.common.events.DevicePowerStateEvent
import com.android.szparag.batterygraph.common.events.FlightModeStateEvent
import hu.akarnokd.rxjava.interop.RxJavaInterop
import io.realm.RealmModel
import io.realm.RealmResults

/**
 * Extensions exclusive to BatteryGraph application
 */

typealias UnixTimestamp = Long
typealias NetworkStateReason = String
typealias DevicePowerState = Boolean
typealias FlightModeStatus = Boolean

//<editor-fold desc="Mappings from Android entities to internal Battery Graph events">
fun Bundle.mapToBatteryStatusEvent() = BatteryStateEvent(
    eventUnixTimestamp = getUnixTimestampSecs(),
    batteryStatusInt = getInt(BatteryManager.EXTRA_STATUS),
    batteryHealthInt = getInt(BatteryManager.EXTRA_HEALTH),
    batteryPowerSourceInt = getInt(BatteryManager.EXTRA_PLUGGED),
    batteryPercentage = ((getInt(BatteryManager.EXTRA_LEVEL) / getInt(BatteryManager.EXTRA_SCALE).toFloat()) * 100f).toInt(),
    batteryVoltage = getInt(BatteryManager.EXTRA_VOLTAGE) / 1000f,
    batteryTemperature = getInt(BatteryManager.EXTRA_TEMPERATURE) / 10
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
    deviceOn = this.action == Intent.ACTION_BOOT_COMPLETED
)

@RequiresApi(Build.VERSION_CODES.N)
fun Intent.mapToDevicePowerEventApiN(unixTimestamp: UnixTimestamp) = DevicePowerStateEvent(
    eventUnixTimestamp = unixTimestamp,
    deviceOn = this.action == Intent.ACTION_LOCKED_BOOT_COMPLETED || this.action == Intent.ACTION_BOOT_COMPLETED
)

fun Bundle.mapToFlightModeEvent(unixTimestamp: UnixTimestamp) = FlightModeStateEvent(
    eventUnixTimestamp = unixTimestamp,
    flightModeOn = getBoolean("state", false)
)
//</editor-fold>


//yo dawg, i herd u like observabelz
//todo: new version of realm has support for RxJava2, use that
fun <E : RealmModel> RealmResults<E>.toObservable() = this.asObservable().toObservable()

fun <E> rx.Observable<E>.toObservable() = RxJavaInterop.toV2Observable(this)

fun <E : RealmModel> RealmResults<E>.asFlowable() = RxJavaInterop.toV2Flowable(
    this.asObservable()).map { realmResults -> realmResults.toList() }