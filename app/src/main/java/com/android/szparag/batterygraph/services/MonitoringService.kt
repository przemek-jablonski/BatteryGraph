package com.android.szparag.batterygraph.services

interface MonitoringService {
  fun registerBatteryStatusReceiver()
  fun unregisterBatteryStatusReceiver()
  fun registerDevicePowerReceiver()
  fun unregisterDevicePowerReceiver()
}