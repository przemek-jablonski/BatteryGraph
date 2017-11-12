package com.android.szparag.batterygraph.base.entities

import com.android.szparag.batterygraph.events.FlightModeEvent
import com.android.szparag.batterygraph.utils.invalidLongValue
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

typealias FlightModeStatus = Boolean

@RealmClass open class RealmFlightModeStateEvent(
    @PrimaryKey var unixTimestamp: Long = invalidLongValue(),
    var flightModeOn: FlightModeStatus = false
) : RealmObject() {

  fun toFlightModeEvent() = FlightModeEvent(
      eventUnixTimestamp = unixTimestamp,
      flightModeOn = flightModeOn
  )

}