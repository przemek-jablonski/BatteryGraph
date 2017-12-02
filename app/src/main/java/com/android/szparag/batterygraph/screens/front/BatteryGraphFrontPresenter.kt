package com.android.szparag.batterygraph.screens.front

import com.android.szparag.batterygraph.shared.presenters.BatteryGraphBasePresenter
import com.android.szparag.batterygraph.shared.utils.ui
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class BatteryGraphFrontPresenter(model: FrontInteractor) : BatteryGraphBasePresenter<FrontView, FrontInteractor>(model), FrontPresenter {

  override fun onAttached() {
    Timber.d("onAttached")
    view?.registerBatteryStateEventsReceiver()
    super.onAttached()
    view?.setupSmallChartsView()
    view?.forceFetchBatteryStateEvent()
  }

  override fun onBeforeDetached() {
    Timber.d("onBeforeDetached")
    super.onBeforeDetached()
    view?.unregisterBatteryStateEventsReceiver()
  }

  override fun subscribeModelEvents() {
    Timber.d("subscribeModelEvents")
    view?.subscribeBatteryStateEvents()
        ?.ui()
        ?.subscribeBy(
            onNext = { event ->
              Timber.d("subscribeBatteryStateEvents.onNext, event: $event")
              view?.renderBatteryState(event)
            }
        )

    model.subscribeBatteryPercentageAndPowerEvents()
        .ui()
        .subscribe { events ->
          Timber.d("model.subscribeBatteryPercentageAndPowerEvents")
          view?.renderSmallChartBatteryPercentage(events)
        }

    model.subscribeBatteryTemperatureEvents()
        .ui()
        .subscribe { events ->
          Timber.d("model.subscribeBatteryTemperatureEvents")
          view?.renderSmallChartBatteryTemperature(events)
        }

    model.subscribeBatteryVoltageEvents()
        .ui()
        .subscribe { events ->
          Timber.d("model.subscribeBatteryVoltageEvents")
          view?.renderSmallChartBatteryVoltage(events)
        }

    model.subscribeBatteryHealthEvents()
        .ui()
        .subscribe { events ->
          Timber.d("model.subscribeBatteryHealthEvents")
          view?.renderSmallChartBatteryHealth(events)
        }

    model.subscribeConnectivityEvents()
        .ui()
        .subscribe { events ->
          Timber.d("model.subscribeConnectivityEvents")
          view?.renderSmallChartConnectivity(events)
        }

  }

  override fun subscribeViewUserEvents() {
    Timber.d("subscribeViewUserEvents")
  }

}