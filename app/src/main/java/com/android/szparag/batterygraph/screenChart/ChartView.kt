package com.android.szparag.batterygraph.screenChart

import com.android.szparag.batterygraph.base.views.View
import com.android.szparag.batterygraph.events.BatteryStatusEvent
import io.reactivex.Observable

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 02/11/2017.
 */
interface ChartView : View {

  fun renderBatteryStatus(batteryStatusEvent: BatteryStatusEvent)

}