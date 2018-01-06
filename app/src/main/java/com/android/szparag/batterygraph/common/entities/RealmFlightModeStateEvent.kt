package com.android.szparag.batterygraph.common.entities

import com.android.szparag.batterygraph.common.events.FlightModeStateEvent
import com.android.szparag.batterygraph.common.utils.invalidLongValue
import io.realm.RealmObject
import io.realm.annotations.RealmClass

typealias FlightModeStatus = Boolean

@RealmClass open class RealmFlightModeStateEvent(
    var unixTimestamp: Long = invalidLongValue(),
    var flightModeOn: FlightModeStatus = false
) : RealmObject() {

  fun toFlightModeEvent() = FlightModeStateEvent(
      eventUnixTimestamp = unixTimestamp,
      flightModeOn = flightModeOn
  )

}

fun FlightModeStateEvent.toRealmEvent() = RealmFlightModeStateEvent(
    unixTimestamp = eventUnixTimestamp,
    flightModeOn = flightModeOn
)