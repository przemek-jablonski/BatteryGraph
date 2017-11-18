package com.android.szparag.batterygraph.screen_chart

import com.android.szparag.batterygraph.shared.events.BatteryStateEvent
import com.android.szparag.batterygraph.shared.events.FlightModeStateEvent
import com.android.szparag.batterygraph.shared.views.View

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 02/11/2017.
 */
interface ChartView : View {

  fun renderBatteryStatuses(events: List<BatteryStateEvent>)
  fun renderFlightModeStatuses(events: List<FlightModeStateEvent>)

}