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

    model.subscribeBatteryPercentageAndPowerEvents()
        .ui()
        .subscribe { events ->
          Timber.d("interactor.subscribeBatteryPercentageAndPowerEvents")
          view?.renderSmallChartBatteryPercentage(events)
        }

    model.subscribeBatteryTemperatureEvents()
        .ui()
        .subscribe { events ->
          Timber.d("interactor.subscribeBatteryTemperatureEvents")
          view?.renderSmallChartBatteryTemperature(events)
        }

    model.subscribeBatteryVoltageEvents()
        .ui()
        .subscribe { events ->
          Timber.d("interactor.subscribeBatteryVoltageEvents")
          view?.renderSmallChartBatteryVoltage(events)
        }

    model.subscribeBatteryHealthEvents()
        .ui()
        .subscribe { events ->
          Timber.d("interactor.subscribeBatteryHealthEvents")
          view?.renderSmallChartBatteryHealth(events)
        }

    model.subscribeConnectivityEvents()
        .ui()
        .subscribe { events ->
          Timber.d("interactor.subscribeConnectivityEvents")
          view?.renderSmallChartConnectivity(events)
        }

  }

}