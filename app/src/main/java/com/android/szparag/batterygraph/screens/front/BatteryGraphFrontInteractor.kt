package com.android.szparag.batterygraph.screens.front

import com.android.szparag.batterygraph.common.events.BatteryStateEvent
import com.android.szparag.batterygraph.common.events.ConnectivityStateEvent
import com.android.szparag.batterygraph.common.models.DatabaseInteractor
import com.android.szparag.batterygraph.common.utils.lastOr
import io.reactivex.Observable

class BatteryGraphFrontInteractor(private val databaseInteractor: DatabaseInteractor) : FrontInteractor {

  override fun subscribeBatteryStateEvent(): Observable<BatteryStateEvent> =
      databaseInteractor.subscribeBatteryStateEvents().map { it -> it.lastOr(BatteryStateEvent.generateInvalidEvent()) }

  override fun attach() = databaseInteractor.attach()

  override fun detach() = databaseInteractor.detach()

  override fun subscribeBatteryStateEvents(): Observable<List<BatteryStateEvent>> = databaseInteractor.subscribeBatteryStateEvents()

  override fun subscribeConnectivityEvents(): Observable<List<ConnectivityStateEvent>> = databaseInteractor.subscribeConnectivityStateEvents()

}