package com.android.szparag.batterygraph.screens.front

import com.android.szparag.batterygraph.common.events.BatteryStateEvent
import com.android.szparag.batterygraph.common.presenters.BatteryGraphBasePresenter
import com.android.szparag.batterygraph.common.utils.ui
import timber.log.Timber

class BatteryGraphFrontPresenter(model: FrontInteractor) : BatteryGraphBasePresenter<FrontView, FrontInteractor>(model), FrontPresenter {

  private var currentBatteryStateEvent: BatteryStateEvent? = null

  override fun onAttached() {
    Timber.d("onAttached")
    super.onAttached()
    view?.setupSmallChartsView()
    view?.forceFetchBatteryStateEvent()
    view?.registerBatteryStateEventsReceiver()
  }

  override fun onBeforeDetached() {
    Timber.d("onBeforeDetached")
    super.onBeforeDetached()
    view?.unregisterBatteryStateEventsReceiver()
  }

  override fun subscribeViewUserEvents() {
    Timber.d("subscribeViewUserEvents")
  }

  override fun subscribeModelEvents() {
    Timber.d("subscribeModelEvents")
    view?.subscribeRealtimeBatteryStateEvents()
        ?.ui()
        ?.subscribe { event ->
          Timber.d("view.subscribeRealtimeBatteryStateEvents.onNext, event: $event")
          renderBatteryState(event)
        }

    model.subscribeBatteryStateEvents()
        .ui()
        .subscribe { events ->
          Timber.d("model.subscribeBatteryStateEvents.onNext, events: $events")
          view?.renderSmallChartBatteryPercentage(events)
        }

    model.subscribeBatteryStateEvent()
        .ui()
        .subscribe { event ->
          Timber.d("model.subscribeBatteryStateEvent.onNext, event: $event")
          renderBatteryState(event)
        }

    model.subscribeConnectivityEvents()
        .ui()
        .subscribe { events ->
          Timber.d("model.subscribeConnectivityEvents")
          view?.renderSmallChartConnectivity(events)
        }

  }

  //todo: here, this event should be decomposed into simpler pieces and pushed onto view
  override fun renderBatteryState(event: BatteryStateEvent) {
    Timber.d("renderBatteryState, event: $event, currentBatteryStateEvent: $currentBatteryStateEvent")
    if (currentBatteryStateEvent != event) {
      view?.renderBatteryState(event)
      view?.performOneShotAnimation()
    }
  }

}