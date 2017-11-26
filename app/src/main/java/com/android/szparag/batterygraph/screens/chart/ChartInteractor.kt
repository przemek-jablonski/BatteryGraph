package com.android.szparag.batterygraph.screens.chart

import com.android.szparag.batterygraph.shared.events.BatteryStateEvent
import com.android.szparag.batterygraph.shared.events.ConnectivityStateEvent
import com.android.szparag.batterygraph.shared.events.DevicePowerStateEvent
import com.android.szparag.batterygraph.shared.events.FlightModeStateEvent
import com.android.szparag.batterygraph.shared.models.Interactor
import io.reactivex.Observable

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 02/11/2017.
 */

interface ChartInteractor : Interactor {

  fun subscribeBatteryStateEvents(): Observable<List<BatteryStateEvent>>
  fun subscribeConnectivityStateEvents(): Observable<List<ConnectivityStateEvent>>
  fun subscribeDevicePowerEvents(): Observable<List<DevicePowerStateEvent>>
  fun subscribeFlightModeEvents(): Observable<List<FlightModeStateEvent>>

}