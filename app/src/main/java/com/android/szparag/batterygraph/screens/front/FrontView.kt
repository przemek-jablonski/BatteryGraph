package com.android.szparag.batterygraph.screens.front

import com.android.szparag.batterygraph.common.events.BatteryStateEvent
import com.android.szparag.batterygraph.common.events.ConnectivityStateEvent
import com.android.szparag.batterygraph.common.views.View
import io.reactivex.Observable

interface FrontView : View {

  fun renderBatteryState(event: BatteryStateEvent)

  fun renderSmallChartBatteryPercentage(events: List<BatteryStateEvent>)
  fun renderSmallChartBatteryTemperature(events: List<BatteryStateEvent>)
  fun renderSmallChartBatteryVoltage(events: List<BatteryStateEvent>)
  fun renderSmallChartBatteryHealth(events: List<BatteryStateEvent>)
  fun renderSmallChartConnectivity(events: List<ConnectivityStateEvent>)

  fun performOneShotAnimation()

  //  fun registerBatteryStateEventsReceiver()
//  fun unregisterBatteryStateEventsReceiver()
  fun subscribeRealtimeBatteryStateEvents(): Observable<BatteryStateEvent>
  fun forceFetchBatteryStateEvent()

  fun setupSmallChartsView()
  //this is being used along with data from database, because this one reacts instantly
  //whereas database has throttling enabled, so if battery state changed (eg. charge level gone up/down)
  //this would be reflected in the UI with a full throttled delay in the worst case scenario (eg. 10 minutes late)
  fun registerBatteryStateEventsReceiver()

  fun unregisterBatteryStateEventsReceiver()

}