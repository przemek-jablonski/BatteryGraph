package com.android.szparag.batterygraph.screens.front

import com.android.szparag.batterygraph.common.presenters.BatteryGraphBasePresenter
import com.android.szparag.batterygraph.common.utils.ui
import timber.log.Timber

class BatteryGraphFrontPresenter(model: FrontInteractor) : BatteryGraphBasePresenter<FrontView, FrontInteractor>(model), FrontPresenter {

  override fun onAttached() {
    Timber.d("onAttached")
    super.onAttached()
    view?.setupSmallChartsView()
    view?.forceFetchBatteryStateEvent()
  }

  override fun onBeforeDetached() {
    Timber.d("onBeforeDetached")
    super.onBeforeDetached()
  }

  override fun subscribeViewUserEvents() {
    Timber.d("subscribeViewUserEvents")
  }

  override fun subscribeModelEvents() {
    Timber.d("subscribeModelEvents")
    view?.subscribeBatteryStateEvents()
        ?.ui()
        ?.subscribe { event ->
          Timber.d("subscribeBatteryStateEvents.onNext, event: $event")
          view?.renderBatteryState(event)
        }

    model.subscribeBatteryStateEvent()
        .ui()
        .subscribe { event ->
          Timber.d("subscribeModelEvents.onNext, event: $event")
          view?.renderBatteryState(event)
        }

    model.subscribeConnectivityEvents()
        .ui()
        .subscribe { events ->
          Timber.d("model.subscribeConnectivityEvents")
          view?.renderSmallChartConnectivity(events)
        }

  }

}