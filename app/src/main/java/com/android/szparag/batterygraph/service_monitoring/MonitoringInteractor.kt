package com.android.szparag.batterygraph.service_monitoring

import com.android.szparag.batterygraph.base.events.BatteryStateEvent
import com.android.szparag.batterygraph.base.events.ConnectivityStateEvent
import com.android.szparag.batterygraph.base.events.DevicePowerStateEvent
import com.android.szparag.batterygraph.base.events.FlightModeStateEvent
import com.android.szparag.batterygraph.base.models.Interactor

interface MonitoringInteractor : Interactor {

  fun insertBatteryStateEvent(event: BatteryStateEvent)
  fun insertConnectivityStateEvent(event: ConnectivityStateEvent)
  fun insertDevicePowerStateEvent(event: DevicePowerStateEvent)
  fun insertFlightModeStateEvent(event: FlightModeStateEvent)

}