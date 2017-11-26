package com.android.szparag.batterygraph.screens.front

import com.android.szparag.batterygraph.shared.events.BatteryStateEvent
import com.android.szparag.batterygraph.shared.events.ConnectivityStateEvent
import com.android.szparag.batterygraph.shared.views.View
import io.reactivex.Observable

interface FrontView : View {

  fun renderBatteryState(event: BatteryStateEvent)

  fun renderSmallChartBatteryPercentage(events: List<BatteryStateEvent>)
  fun renderSmallChartBatteryTemperature(events: List<BatteryStateEvent>)
  fun renderSmallChartBatteryVoltage(events: List<BatteryStateEvent>)
  fun renderSmallChartBatteryHealth(events: List<BatteryStateEvent>)
  fun renderSmallChartConnectivity(events: List<ConnectivityStateEvent>)

  fun performOneShotAnimation()

  fun registerBatteryStateEventsReceiver()
  fun unregisterBatteryStateEventsReceiver()
  fun subscribeBatteryStateEvents(): Observable<BatteryStateEvent>

  fun setupSmallChartsView()

}