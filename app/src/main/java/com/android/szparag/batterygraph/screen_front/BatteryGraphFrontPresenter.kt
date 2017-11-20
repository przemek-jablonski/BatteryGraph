package com.android.szparag.batterygraph.screen_front

import com.android.szparag.batterygraph.shared.presenters.BatteryGraphBasePresenter
import com.android.szparag.batterygraph.shared.utils.ui
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class BatteryGraphFrontPresenter(model: FrontInteractor) : BatteryGraphBasePresenter<FrontView, FrontInteractor>(model), FrontPresenter {

  override fun onAttached() {
    view?.registerBatteryStateEventsReceiver()
    super.onAttached()
  }

  override fun onBeforeDetached() {
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
  }

  override fun subscribeViewUserEvents() {
    Timber.d("subscribeViewUserEvents")
  }

}