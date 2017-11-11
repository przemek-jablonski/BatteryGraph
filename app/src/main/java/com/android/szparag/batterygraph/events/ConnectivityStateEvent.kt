package com.android.szparag.batterygraph.events

import android.net.NetworkInfo
import android.net.NetworkInfo.DetailedState.DISCONNECTED
import com.android.szparag.batterygraph.events.ConnectivityNetworkType.NO_NETWORK

typealias NetworkStateReason = String

//todo: android dependency is here - remove (create own enum)
data class ConnectivityStateEvent(
    val networkType: ConnectivityNetworkType,
    val networkState: NetworkInfo.DetailedState,
    val networkStateReason: NetworkStateReason
) {
  constructor() : this(NO_NETWORK, DISCONNECTED, "Disabled") //todo: hardcoded string literal
}