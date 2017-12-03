package com.android.szparag.batterygraph.shared.events

import android.net.ConnectivityManager
import com.android.szparag.batterygraph.shared.utils.invalidIntValue

//todo: find out if this is exclusive or not (if there is mobile, wifi and bluetooth, what will be inserted into db?)
enum class ConnectivityNetworkType(val typeInt: Int) {
  MOBILE(ConnectivityManager.TYPE_MOBILE),
  WIFI(ConnectivityManager.TYPE_WIFI),
  ETHERNET(ConnectivityManager.TYPE_ETHERNET),
  BLUETOOTH(ConnectivityManager.TYPE_BLUETOOTH),
  VPN(ConnectivityManager.TYPE_VPN),
  NO_NETWORK(invalidIntValue()),
  UNKNOWN(invalidIntValue());

  companion object {
    @JvmStatic
    fun fromInt(typeInt: Int) = when (typeInt) {
      MOBILE.typeInt -> MOBILE
      WIFI.typeInt -> WIFI
      ETHERNET.typeInt -> ETHERNET
      BLUETOOTH.typeInt -> BLUETOOTH
      VPN.typeInt -> VPN
      else -> UNKNOWN
    }
  }

}
