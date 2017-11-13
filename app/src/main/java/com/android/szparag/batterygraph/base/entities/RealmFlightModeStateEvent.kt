package com.android.szparag.batterygraph.base.entities

import com.android.szparag.batterygraph.base.events.FlightModeStateEvent
import com.android.szparag.batterygraph.base.utils.invalidLongValue
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

typealias FlightModeStatus = Boolean

@RealmClass open class RealmFlightModeStateEvent(
    @PrimaryKey var unixTimestamp: Long = invalidLongValue(),
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