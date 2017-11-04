package com.android.szparag.batterygraph.screenChart

import com.android.szparag.batterygraph.base.presenters.Presenter
import com.android.szparag.batterygraph.events.BatteryStatusEvent

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 02/11/2017.
 */
interface ChartPresenter : Presenter<ChartView> {
  fun onBatteryStatusChanged(event: BatteryStatusEvent)
}