package com.android.szparag.batterygraph.base.models

import com.android.szparag.batterygraph.base.events.BatteryStatusEvent
import com.android.szparag.batterygraph.base.events.ConnectivityStateEvent
import com.android.szparag.batterygraph.base.events.DevicePowerStateEvent
import com.android.szparag.batterygraph.base.events.FlightModeStateEvent
import io.reactivex.Observable

interface DatabaseInteractor : Interactor {

  fun insertBatteryStateEvent(event: BatteryStatusEvent)
  fun insertConnectivityStateEvent(event: ConnectivityStateEvent)
  fun insertDevicePowerStateEvent(event: DevicePowerStateEvent)
  fun insertFlightModeStateEvent(event: FlightModeStateEvent)

  fun subscribeBatteryStateEvents(): Observable<List<BatteryStatusEvent>>
  fun subscribeConnectivityStateEvents(): Observable<List<ConnectivityStateEvent>>
  fun subscribeDevicePowerEvents(): Observable<List<DevicePowerStateEvent>>
  fun subscribeFlightModeEvents(): Observable<List<FlightModeStateEvent>>

}