package com.android.szparag.batterygraph.screenChart

import com.android.szparag.batterygraph.base.presenters.BatteryGraphBasePresenter
import com.android.szparag.batterygraph.utils.safeLast
import com.android.szparag.batterygraph.utils.ui
import timber.log.Timber

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 02/11/2017.
 */

class BatteryGraphChartPresenter(model: ChartModel) : BatteryGraphBasePresenter<ChartView, ChartModel>(model), ChartPresenter {

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
    model.subscribeBatteryEvents()
        .ui()
        .subscribe { events ->
          Timber.d("subscribeModelEvents.subscription.onNext: events.size: ${events.size}, events.last: ${events.safeLast()}")
          view?.renderBatteryStatuses(events)
        }
  }

}