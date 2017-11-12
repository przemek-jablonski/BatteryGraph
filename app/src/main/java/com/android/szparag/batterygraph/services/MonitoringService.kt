package com.android.szparag.batterygraph.services

interface MonitoringService {

  fun registerBatteryStatusReceiver()
  fun unregisterBatteryStatusReceiver()

  fun registerDevicePowerReceiver()
  fun unregisterDevicePowerReceiver()

  fun registerConnectivityReceiver()
  fun unregisterConnectivityReceiver()
  //todo: sort this registering / unregistering / receiving (on...IntentReceived)
  fun registerFlightModeListener()

  fun unregisterFlightModeListener()

}