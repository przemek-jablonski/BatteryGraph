package com.android.szparag.batterygraph.screen_front

import com.android.szparag.batterygraph.shared.events.BatteryStateEvent
import com.android.szparag.batterygraph.shared.views.View
import io.reactivex.Observable

interface FrontView: View {

  fun renderBatteryState(event: BatteryStateEvent)

  fun performOneShotAnimation()

  fun registerBatteryStateEventsReceiver()
  fun unregisterBatteryStateEventsReceiver()
  fun subscribeBatteryStateEvents(): Observable<BatteryStateEvent>
  fun setupSmallChartsView()
  fun setupSmallChartsData()

}