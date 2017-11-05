package com.android.szparag.batterygraph.base.entities

import com.android.szparag.batterygraph.events.BatteryStatusEvent
import com.android.szparag.batterygraph.utils.invalidFloatValue
import com.android.szparag.batterygraph.utils.invalidIntValue
import com.android.szparag.batterygraph.utils.invalidLongValue
import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.RealmClass

@RealmClass open class RealmBatteryEvent(
    @Index var unixTimestamp: Long = invalidLongValue(),
    var batteryStatus: Int = invalidIntValue(),
    var batteryHealth: Int = invalidIntValue(),
    var batteryPowerSource: Int = invalidIntValue(),
    var batteryPercentage: Int = invalidIntValue(),
    var batteryVoltage: Float = invalidFloatValue(),
    var batteryTemperature: Int = invalidIntValue()
) : RealmObject() {

  fun toBatteryStatusEvent() = BatteryStatusEvent(
      unixTimestamp, batteryStatus, batteryHealth, batteryPowerSource, batteryPercentage, batteryVoltage, batteryTemperature
  )

}

fun BatteryStatusEvent.toRealmEvent() = RealmBatteryEvent(
    unixTimestamp = eventUnixTimestamp,
    batteryStatus = batteryStatus.statusInt,
    batteryHealth = batteryHealth.healthInt,
    batteryPowerSource = batteryPowerSource.sourceInt,
    batteryPercentage = batteryPercentage,
    batteryVoltage = batteryVoltage,
    batteryTemperature = batteryTemperature
)