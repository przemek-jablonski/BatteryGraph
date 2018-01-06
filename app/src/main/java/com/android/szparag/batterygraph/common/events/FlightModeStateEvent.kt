package com.android.szparag.batterygraph.common.events

import com.android.szparag.batterygraph.common.utils.FlightModeStatus
import com.android.szparag.batterygraph.common.utils.UnixTimestamp

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 12/11/2017.
 */

data class FlightModeStateEvent(
    val eventUnixTimestamp: UnixTimestamp,
    val flightModeOn: FlightModeStatus
)