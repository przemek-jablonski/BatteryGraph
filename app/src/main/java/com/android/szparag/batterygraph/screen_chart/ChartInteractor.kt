package com.android.szparag.batterygraph.screen_chart

import com.android.szparag.batterygraph.base.events.BatteryStateEvent
import com.android.szparag.batterygraph.base.events.ConnectivityStateEvent
import com.android.szparag.batterygraph.base.events.DevicePowerStateEvent
import com.android.szparag.batterygraph.base.events.FlightModeStateEvent
import com.android.szparag.batterygraph.base.models.Interactor
import io.reactivex.Observable

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 02/11/2017.
 */
const val EVENTS_PERSISTENCE_SAMPLING_VALUE_SECS = 5L

interface ChartInteractor : Interactor {

  fun subscribeBatteryStateEvents(): Observable<List<BatteryStateEvent>>
  fun subscribeConnectivityStateEvents(): Observable<List<ConnectivityStateEvent>>
  fun subscribeDevicePowerEvents(): Observable<List<DevicePowerStateEvent>>
  fun subscribeFlightModeEvents(): Observable<List<FlightModeStateEvent>>

}