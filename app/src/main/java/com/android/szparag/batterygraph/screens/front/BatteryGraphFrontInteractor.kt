package com.android.szparag.batterygraph.screens.front

import com.android.szparag.batterygraph.common.events.BatteryStateEvent
import com.android.szparag.batterygraph.common.events.ConnectivityStateEvent
import com.android.szparag.batterygraph.common.models.DatabaseInteractor
import io.reactivex.Observable

class BatteryGraphFrontInteractor(private val databaseInteractor: DatabaseInteractor) : FrontInteractor {

  override fun attach() = databaseInteractor.attach()

  override fun detach() = databaseInteractor.detach()

  override fun subscribeBatteryPercentageAndPowerEvents(): Observable<List<BatteryStateEvent>> = databaseInteractor.subscribeBatteryStateEvents()

  override fun subscribeBatteryTemperatureEvents(): Observable<List<BatteryStateEvent>> = databaseInteractor.subscribeBatteryStateEvents()

  override fun subscribeBatteryVoltageEvents(): Observable<List<BatteryStateEvent>> = databaseInteractor.subscribeBatteryStateEvents()

  override fun subscribeBatteryHealthEvents(): Observable<List<BatteryStateEvent>> = databaseInteractor.subscribeBatteryStateEvents()

  override fun subscribeConnectivityEvents(): Observable<List<ConnectivityStateEvent>> = databaseInteractor.subscribeConnectivityStateEvents()

}