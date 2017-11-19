package com.android.szparag.batterygraph.shared.models

import com.android.szparag.batterygraph.shared.events.BatteryStateEvent
import com.android.szparag.batterygraph.shared.events.ConnectivityStateEvent
import com.android.szparag.batterygraph.shared.events.DevicePowerStateEvent
import com.android.szparag.batterygraph.shared.events.FlightModeStateEvent
import io.reactivex.Observable

interface DatabaseInteractor : Interactor {

  fun insertBatteryStateEvent(event: BatteryStateEvent)
  fun insertConnectivityStateEvent(event: ConnectivityStateEvent)
  fun insertDevicePowerStateEvent(event: DevicePowerStateEvent)
  fun insertFlightModeStateEvent(event: FlightModeStateEvent)

  fun subscribeBatteryStateEvents(): Observable<List<BatteryStateEvent>>
  fun subscribeConnectivityStateEvents(): Observable<List<ConnectivityStateEvent>>
  fun subscribeDevicePowerEvents(): Observable<List<DevicePowerStateEvent>>
  fun subscribeFlightModeEvents(): Observable<List<FlightModeStateEvent>>

}