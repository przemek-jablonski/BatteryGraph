package com.android.szparag.batterygraph.common.events

import com.android.szparag.batterygraph.common.events.ConnectivityNetworkState.DISCONNECTED
import com.android.szparag.batterygraph.common.events.ConnectivityNetworkType.NO_NETWORK
import com.android.szparag.batterygraph.common.utils.NetworkStateReason
import com.android.szparag.batterygraph.common.utils.UnixTimestamp

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