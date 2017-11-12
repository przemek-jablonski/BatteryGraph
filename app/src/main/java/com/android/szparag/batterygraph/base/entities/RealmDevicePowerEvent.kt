package com.android.szparag.batterygraph.base.entities

import com.android.szparag.batterygraph.events.DevicePowerEvent
import com.android.szparag.batterygraph.events.UnixTimestamp
import com.android.szparag.batterygraph.utils.invalidLongValue
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

typealias DevicePowerState = Boolean

@RealmClass open class RealmDevicePowerEvent(
    @PrimaryKey var unixTimestamp: UnixTimestamp = invalidLongValue(),
    var deviceOn: DevicePowerState = true
) : RealmObject() {

  fun toDevicePowerEvent() = DevicePowerEvent(
      eventUnixTimestamp = unixTimestamp,
      deviceOn = deviceOn
  )

}