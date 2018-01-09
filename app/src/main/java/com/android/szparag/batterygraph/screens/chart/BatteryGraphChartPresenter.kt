package com.android.szparag.batterygraph.screens.chart

import com.android.szparag.batterygraph.common.presenters.BatteryGraphBasePresenter
import com.android.szparag.batterygraph.common.utils.ui
import timber.log.Timber

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 02/11/2017.
 */

class BatteryGraphChartPresenter(model: ChartInteractor) : BatteryGraphBasePresenter<ChartView, ChartInteractor>(model), ChartPresenter {

  override fun onAttached() {
    super.onAttached()
    Timber.d("onAttached")
  }

  override fun onBeforeDetached() {
    super.onBeforeDetached()
    Timber.d("onBeforeDetached")
  }

  override fun subscribeViewUserEvents() {
    Timber.d("subscribeViewUserEvents")
  }

  override fun subscribeModelEvents() {
    Timber.d("subscribeModelEvents")
    model.subscribeBatteryStateEvents()
        .ui()
        .subscribe { events ->
          Timber.d(
              "subscribeModelEvents.subscribeBatteryStateEvents.onNext: events.size: ${events.size}, events.last: ${events.lastOrNull()}")
          view?.renderBatteryStatuses(events)
        }

    model.subscribeFlightModeEvents()
        .ui()
        .subscribe { events ->
          Timber.d(
              "subscribeModelEvents.subscribeFlightModeEvents.onNext, events.size: ${events.size}, events.last: ${events.lastOrNull()}")
          view?.renderFlightModeStatuses(events)
        }

  }

}