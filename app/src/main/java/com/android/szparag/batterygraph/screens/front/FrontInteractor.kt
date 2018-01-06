package com.android.szparag.batterygraph.screens.front

import com.android.szparag.batterygraph.common.events.BatteryStateEvent
import com.android.szparag.batterygraph.common.events.ConnectivityStateEvent
import com.android.szparag.batterygraph.common.models.Interactor
import io.reactivex.Observable

interface FrontInteractor : Interactor {

  fun subscribeBatteryStateEvent(): Observable<BatteryStateEvent>
  fun subscribeBatteryStateEvents(): Observable<List<BatteryStateEvent>> //todo: as flowable
  fun subscribeConnectivityEvents(): Observable<List<ConnectivityStateEvent>> //todo: as flowable

}