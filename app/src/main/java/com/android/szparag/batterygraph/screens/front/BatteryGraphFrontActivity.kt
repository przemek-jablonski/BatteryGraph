package com.android.szparag.batterygraph.screens.front

import android.content.BroadcastReceiver
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetBehavior.BottomSheetCallback
import android.view.View
import com.android.szparag.batterygraph.R
import com.android.szparag.batterygraph.dagger.DaggerGlobalScopeWrapper
import com.android.szparag.batterygraph.shared.events.BatteryStateEvent
import com.android.szparag.batterygraph.shared.events.ConnectivityStateEvent
import com.android.szparag.batterygraph.shared.utils.asString
import com.android.szparag.batterygraph.shared.utils.createRegisteredBroadcastReceiver
import com.android.szparag.batterygraph.shared.utils.getBGUnixTimestampSecs
import com.android.szparag.batterygraph.shared.utils.mapToBatteryStatusEvent
import com.android.szparag.batterygraph.shared.utils.toPx
import com.android.szparag.batterygraph.shared.utils.unregisterReceiverFromContext
import com.android.szparag.batterygraph.shared.views.BatteryGraphBaseActivity
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.activity_front.batteryStatusView
import kotlinx.android.synthetic.main.activity_front.smallChartsView
import kotlinx.android.synthetic.main.layout_batterystats_details.view.contentHealth
import kotlinx.android.synthetic.main.layout_batterystats_details.view.contentPercentage
import kotlinx.android.synthetic.main.layout_batterystats_details.view.contentSource
import kotlinx.android.synthetic.main.layout_batterystats_details.view.contentStatus
import kotlinx.android.synthetic.main.layout_batterystats_details.view.contentTemperature
import kotlinx.android.synthetic.main.layout_batterystats_details.view.contentVoltage
import kotlinx.android.synthetic.main.layout_small_charts_group.batteryPercentageSmallChart
import timber.log.Timber

class BatteryGraphFrontActivity : BatteryGraphBaseActivity<FrontPresenter>(), FrontView {

  override fun renderSmallChartBatteryPercentage(events: List<BatteryStateEvent>) {
    Timber.d("renderSmallChartBatteryPercentage")
    batteryPercentageSmallChart.setData(events)
  }

  override fun renderSmallChartBatteryTemperature(events: List<BatteryStateEvent>) {
    Timber.d("renderSmallChartBatteryTemperature")
  }

  override fun renderSmallChartBatteryVoltage(events: List<BatteryStateEvent>) {
    Timber.d("renderSmallChartBatteryVoltage")
  }

  override fun renderSmallChartBatteryHealth(events: List<BatteryStateEvent>) {
    Timber.d("renderSmallChartBatteryHealth")
  }

  override fun renderSmallChartConnectivity(events: List<ConnectivityStateEvent>) {
    Timber.d("renderSmallChartConnectivity")
  }

  private lateinit var batteryChangedSubject: Subject<BatteryStateEvent>
  private lateinit var batteryChangedReceiver: BroadcastReceiver

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Timber.d("onCreate, savedInstanceState: $savedInstanceState")
    setContentView(R.layout.activity_front)
    DaggerGlobalScopeWrapper.getComponent(this).inject(this)
  }

  //todo: everything that setupViews do should be split into methods and called from the presenter

  override fun onStart() {
    super.onStart()
    Timber.d("onStart")
    presenter.attach(this)
  }

  override fun onStop() {
    super.onStop()
    Timber.d("onStop")
    presenter.detach()
  }

  override fun registerBatteryStateEventsReceiver() {
    Timber.d("registerBatteryStateEventsReceiver")
    batteryChangedSubject = PublishSubject.create()
    batteryChangedReceiver = createRegisteredBroadcastReceiver(
        intentFilterActions = Intent.ACTION_BATTERY_CHANGED,
        callback = this::onBatteryStatusIntentReceived
    )
  }

  override fun unregisterBatteryStateEventsReceiver() {
    Timber.d("unregisterBatteryStateEventsReceiver")
    batteryChangedReceiver.unregisterReceiverFromContext(this)
  }

  override fun subscribeBatteryStateEvents(): Observable<BatteryStateEvent> {
    Timber.d("subscribeBatteryStateEvents")
    return batteryChangedSubject
  }

  private fun onBatteryStatusIntentReceived(intent: Intent) {
    Timber.v("onBatteryStatusIntentReceived, intent: ${intent.asString()}")
    batteryChangedSubject.onNext(intent.extras.mapToBatteryStatusEvent(getBGUnixTimestampSecs()))
  }

  override fun setupSmallChartsView() {
    Timber.d("setupSmallChartsView")
    BottomSheetBehavior.from(smallChartsView).apply {
      this.peekHeight = 100f.toPx(displayMetrics) //todo hardcoded
      this.isHideable = false
      this.setBottomSheetCallback(object: BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {}

        override fun onStateChanged(bottomSheet: View, newState: Int) {}

      })
    }
    batteryPercentageSmallChart.initialize()
  }


  override fun renderBatteryState(event: BatteryStateEvent) {
    Timber.d("renderBatteryState, event: $event")
    batteryStatusView.contentPercentage.text = event.batteryPercentage.toString()
    batteryStatusView.contentHealth.text = event.batteryHealth.name.toLowerCase()
    batteryStatusView.contentSource.text = event.batteryPowerSource.name.toLowerCase()
    batteryStatusView.contentStatus.text = event.batteryStatus.name.toLowerCase()
    batteryStatusView.contentVoltage.text = event.batteryVoltage.toString()
    batteryStatusView.contentTemperature.text = event.batteryTemperature.toString()
  }

  override fun performOneShotAnimation() {
    Timber.d("performOneShotAnimation")
//    batteryAnimatedView.performOneShotAnimation()
  }


}
