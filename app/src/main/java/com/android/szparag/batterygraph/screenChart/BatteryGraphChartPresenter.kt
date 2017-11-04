package com.android.szparag.batterygraph.screenChart

import com.android.szparag.batterygraph.base.presenters.BatteryGraphBasePresenter
import com.android.szparag.batterygraph.events.BatteryStatusEvent
import com.android.szparag.batterygraph.utils.ui
import timber.log.Timber

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 02/11/2017.
 */

class BatteryGraphChartPresenter(model: ChartModel) : BatteryGraphBasePresenter<ChartView, ChartModel>(model), ChartPresenter {

  override fun onAttached() {
    super.onAttached()
    Timber.d("onAttached")
//    view?.registerBatteryStatusReceiver()
//    view?.subscribeForBatteryStatusChanged()
//        ?.subscribeOn(ui())
//        ?.sample(EVENTS_PERSISTENCE_SAMPLING_VALUE_SECS, SECONDS, true)
//        ?.observeOn(ui())
//        ?.subscribe(this::onBatteryStatusChanged)
//        .toViewDisposable()
  }

  override fun onBeforeDetached() {
    super.onBeforeDetached()
    Timber.d("onBeforeDetached")
    view?.unregisterBatteryStatusReceiver()
  }

  override fun onBatteryStatusChanged(event: BatteryStatusEvent) {
    Timber.d("onBatteryStatusChanged, event: $event")
    model.insertBatteryEvent(event)
  }

  override fun subscribeViewUserEvents() {
    Timber.d("subscribeViewUserEvents")
  }

  override fun subscribeModelEvents() {
    Timber.d("subscribeModelEvents")
    model.subscribeBatteryEvents().ui().subscribe { batteryStatusEvent -> view?.renderBatteryStatus(batteryStatusEvent) }
  }

}