package com.android.szparag.batterygraph.screenChart

import com.android.szparag.batterygraph.base.models.Model
import com.android.szparag.batterygraph.events.BatteryStatusEvent
import io.reactivex.Observable

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 02/11/2017.
 */
const val EVENTS_PERSISTENCE_SAMPLING_VALUE_SECS = 5L

interface ChartModel : Model {

  fun insertBatteryEvent(batteryStatusEvent: BatteryStatusEvent)

  fun subscribeBatteryEvents() : Observable<BatteryStatusEvent>

}