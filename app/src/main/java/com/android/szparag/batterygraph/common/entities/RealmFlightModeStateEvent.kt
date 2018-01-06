package com.android.szparag.batterygraph.common.entities

import com.android.szparag.batterygraph.common.utils.FlightModeStatus
import com.android.szparag.batterygraph.common.utils.invalidLongValue
import io.realm.RealmObject
import io.realm.annotations.RealmClass

@RealmClass open class RealmFlightModeStateEvent(
    var unixTimestamp: Long = invalidLongValue(),
    var flightModeOn: FlightModeStatus = false
) : RealmObject()

