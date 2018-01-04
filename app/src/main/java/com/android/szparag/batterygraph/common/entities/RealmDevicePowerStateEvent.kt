package com.android.szparag.batterygraph.common.entities

import com.android.szparag.batterygraph.common.events.DevicePowerStateEvent
import com.android.szparag.batterygraph.common.events.UnixTimestamp
import com.android.szparag.batterygraph.common.utils.invalidLongValue
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

typealias DevicePowerState = Boolean

@RealmClass open class RealmDevicePowerStateEvent(
    @PrimaryKey var unixTimestamp: UnixTimestamp = invalidLongValue(),
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