package com.android.szparag.batterygraph.screens.front

import com.android.szparag.batterygraph.shared.events.BatteryStateEvent
import com.android.szparag.batterygraph.shared.events.ConnectivityStateEvent
import com.android.szparag.batterygraph.shared.models.Interactor
import io.reactivex.Observable

interface FrontInteractor : Interactor {

  fun subscribeBatteryPercentageAndPowerEvents(): Observable<List<BatteryStateEvent>> //todo powereventszip
  fun subscribeBatteryTemperatureEvents(): Observable<List<BatteryStateEvent>>
  fun subscribeBatteryVoltageEvents(): Observable<List<BatteryStateEvent>>
  fun subscribeBatteryHealthEvents(): Observable<List<BatteryStateEvent>>
  fun subscribeConnectivityEvents(): Observable<List<ConnectivityStateEvent>>

}