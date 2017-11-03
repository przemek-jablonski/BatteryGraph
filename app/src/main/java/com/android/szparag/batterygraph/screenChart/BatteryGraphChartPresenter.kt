package com.android.szparag.batterygraph.screenChart

import com.android.szparag.batterygraph.base.presenters.BatteryGraphBasePresenter
import com.android.szparag.batterygraph.utils.ui
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 02/11/2017.
 */

class BatteryGraphChartPresenter(model: ChartModel) : BatteryGraphBasePresenter<ChartView, ChartModel>(model), ChartPresenter {

  override fun onAttached() {
    super.onAttached()
    Timber.d("onAttached")
    view?.registerBatteryStatusReceiver()
    view?.subscribeForBatteryStatusChanged()
        ?.subscribeOn(ui())
        ?.observeOn(ui())
        ?.doOnSubscribe {
          Timber.d("onAttached.subscribeForBatteryStatusChanged.doOnSubscribe")
        }
        ?.subscribeBy(
            onNext = { batteryStatusEvent ->
              Timber.d("onAttached.subscribeForBatteryStatusChanged.onNext, batteryStatusEvent: $batteryStatusEvent")
              view?.renderBatteryStatus(batteryStatusEvent)
            }
        )
        .toViewDisposable()
  }

  override fun onBeforeDetached() {
    super.onBeforeDetached()
    view?.unregisterBatteryStatusReceiver()
  }

  override fun subscribeViewUserEvents() {

  }

  override fun subscribeModelEvents() {

  }

}