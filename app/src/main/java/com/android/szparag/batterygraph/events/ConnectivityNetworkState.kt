package com.android.szparag.batterygraph.events

import android.net.NetworkInfo

enum class ConnectivityNetworkState(val stateInt: Int) {
  CONNECTING(NetworkInfo.State.CONNECTING.ordinal),
  CONNECTED(NetworkInfo.State.CONNECTED.ordinal),
  SUSPENDED(NetworkInfo.State.SUSPENDED.ordinal),
  DISCONNECTING(NetworkInfo.State.DISCONNECTING.ordinal),
  DISCONNECTED(NetworkInfo.State.DISCONNECTED.ordinal),
  UNKNOWN(NetworkInfo.State.UNKNOWN.ordinal);

  companion object {
    @JvmStatic
    fun fromInt(stateInt: Int) = when (stateInt) {
      CONNECTING.stateInt -> CONNECTING
      CONNECTED.stateInt -> CONNECTED
      SUSPENDED.stateInt -> SUSPENDED
      DISCONNECTING.stateInt -> DISCONNECTING
      DISCONNECTED.stateInt -> DISCONNECTED
      UNKNOWN.stateInt -> UNKNOWN
      else -> UNKNOWN
    }
  }
}