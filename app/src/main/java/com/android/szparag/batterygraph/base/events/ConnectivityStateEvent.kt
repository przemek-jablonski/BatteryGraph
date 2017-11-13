package com.android.szparag.batterygraph.base.events

import com.android.szparag.batterygraph.base.events.ConnectivityNetworkState.DISCONNECTED
import com.android.szparag.batterygraph.base.events.ConnectivityNetworkType.NO_NETWORK

typealias NetworkStateReason = String

//todo: android dependency is here - remove (create own enum)
data class ConnectivityStateEvent(
    val eventUnixTimestamp: UnixTimestamp,
    val networkType: ConnectivityNetworkType,
    val networkState: ConnectivityNetworkState,
    val networkStateReason: NetworkStateReason
) {
  constructor(eventUnixTimestamp: UnixTimestamp) : this(
      eventUnixTimestamp = eventUnixTimestamp,
      networkType = NO_NETWORK,
      networkState = DISCONNECTED,
      networkStateReason = "Disabled") //todo: hardcoded string literal

  constructor(eventUnixTimestamp: UnixTimestamp, networkType: Int, networkState: Int, networkStateReason: NetworkStateReason) : this(
      eventUnixTimestamp = eventUnixTimestamp,
      networkType = ConnectivityNetworkType.fromInt(networkType),
      networkState = ConnectivityNetworkState.fromInt(networkState),
      networkStateReason = networkStateReason
  )
}