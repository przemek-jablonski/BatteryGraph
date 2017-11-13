package com.android.szparag.batterygraph.service_monitoring

import com.android.szparag.batterygraph.base.events.BatteryStatusEvent
import com.android.szparag.batterygraph.base.events.ConnectivityStateEvent
import com.android.szparag.batterygraph.base.events.DevicePowerStateEvent
import com.android.szparag.batterygraph.base.events.FlightModeStateEvent
import com.android.szparag.batterygraph.base.models.Interactor

interface MonitoringInteractor : Interactor {

  fun insertBatteryStateEvent(event: BatteryStatusEvent)
  fun insertConnectivityStateEvent(event: ConnectivityStateEvent)
  fun insertDevicePowerStateEvent(event: DevicePowerStateEvent)
  fun insertFlightModeStateEvent(event: FlightModeStateEvent)

}