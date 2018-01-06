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
    view?.subscribeForceFetchedBatteryStateEvent()
        ?.ui()
        ?.subscribe { event ->
          Timber.d("view.subscribeForceFetchedBatteryStateEvent.onNext, event: $event")
          view?.renderBatteryState(event)
          view?.performOneShotAnimation()
        }

    model.subscribeBatteryStateEvents()
        .ui()
        .subscribe { events ->
          Timber.d("model.subscribeBatteryStateEvents.onNext, events: $events")
          view?.renderSmallChartBatteryPercentage(events)
          view?.performOneShotAnimation()
        }

    model.subscribeBatteryStateEvent()
        .ui()
        .subscribe { event ->
          Timber.d("model.subscribeBatteryStateEvent.onNext, event: $event")
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