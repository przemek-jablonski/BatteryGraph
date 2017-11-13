package com.android.szparag.batterygraph.base.events

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 12/11/2017.
 */

typealias FlightModeStatus = Boolean

data class FlightModeStateEvent(
    val eventUnixTimestamp: UnixTimestamp,
    val flightModeOn: FlightModeStatus
)