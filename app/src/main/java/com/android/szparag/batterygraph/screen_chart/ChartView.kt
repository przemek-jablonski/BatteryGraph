package com.android.szparag.batterygraph.screen_chart

import com.android.szparag.batterygraph.base.events.BatteryStateEvent
import com.android.szparag.batterygraph.base.views.View

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 02/11/2017.
 */
interface ChartView : View {

  fun renderBatteryStatuses(events: List<BatteryStateEvent>)

}