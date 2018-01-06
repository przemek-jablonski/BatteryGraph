package com.android.szparag.batterygraph.common.entities

import com.android.szparag.batterygraph.common.events.DevicePowerStateEvent
import com.android.szparag.batterygraph.common.events.UnixTimestamp
import com.android.szparag.batterygraph.common.utils.invalidLongValue
import io.realm.RealmObject
import io.realm.annotations.RealmClass

typealias DevicePowerState = Boolean

@RealmClass open class RealmDevicePowerStateEvent(
    var unixTimestamp: UnixTimestamp = invalidLongValue(),
    var deviceOn: DevicePowerState = true
) : RealmObject() {

  fun toDevicePowerEvent() = DevicePowerStateEvent(
      eventUnixTimestamp = unixTimestamp,
      deviceOn = deviceOn
  )

}

fun DevicePowerStateEvent.toRealmEvent() = RealmDevicePowerStateEvent(
    unixTimestamp = eventUnixTimestamp,
    deviceOn = deviceOn
)