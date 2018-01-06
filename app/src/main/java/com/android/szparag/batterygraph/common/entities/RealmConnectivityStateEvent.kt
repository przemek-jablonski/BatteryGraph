package com.android.szparag.batterygraph.common.entities

import com.android.szparag.batterygraph.common.events.ConnectivityStateEvent
import com.android.szparag.batterygraph.common.events.UnixTimestamp
import com.android.szparag.batterygraph.common.utils.invalidIntValue
import com.android.szparag.batterygraph.common.utils.invalidLongValue
import com.android.szparag.batterygraph.common.utils.invalidStringValue
import io.realm.RealmObject
import io.realm.annotations.RealmClass

typealias NetworkStateReason = String

@RealmClass open class RealmConnectivityStateEvent(
    var unixTimestamp: UnixTimestamp = invalidLongValue(),
    var networkType: Int = invalidIntValue(),
    var networkState: Int = invalidIntValue(),
    var networkStateReason: NetworkStateReason = invalidStringValue()
) : RealmObject() {

  fun toConnectivityStateEvent() = ConnectivityStateEvent(
      eventUnixTimestamp = unixTimestamp,
      networkType = networkType,
      networkState = networkState,
      networkStateReason = networkStateReason
  )

}

fun ConnectivityStateEvent.toRealmEvent() = RealmConnectivityStateEvent(
    unixTimestamp = eventUnixTimestamp,
    networkType = networkType.typeInt,
    networkState = networkState.stateInt,
    networkStateReason = networkStateReason
)