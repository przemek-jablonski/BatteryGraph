package com.android.szparag.batterygraph.screens.front

import android.content.BroadcastReceiver
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import com.android.szparag.batterygraph.R
import com.android.szparag.batterygraph.common.events.BatteryStateEvent
import com.android.szparag.batterygraph.common.events.ConnectivityStateEvent
import com.android.szparag.batterygraph.common.utils.asString
import com.android.szparag.batterygraph.common.utils.createRegisteredBroadcastReceiver
import com.android.szparag.batterygraph.common.utils.getStickyIntentFromSystem
import com.android.szparag.batterygraph.common.utils.mapToBatteryStatusEvent
import com.android.szparag.batterygraph.common.utils.toPx
import com.android.szparag.batterygraph.common.utils.unregisterReceiverFromContext
import com.android.szparag.batterygraph.common.views.BatteryGraphBaseActivity
import com.android.szparag.batterygraph.dagger.DaggerGlobalScopeWrapper
import com.android.szparag.batterygraph.screens.front.widgets.BatteryStatsDetailsContainer
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.activity_front.batteryAnimatedView
import kotlinx.android.synthetic.main.activity_front.smallChartsView
import kotlinx.android.synthetic.main.layout_small_charts_group.batteryHealthSmallChart
import kotlinx.android.synthetic.main.layout_small_charts_group.batteryPercentageSmallChart
import kotlinx.android.synthetic.main.layout_small_charts_group.batteryTemperatureSmallChart
import kotlinx.android.synthetic.main.layout_small_charts_group.batteryVoltageSmallChart
import kotlinx.android.synthetic.main.layout_small_charts_group.connectivitySmallChart
import timber.log.Timber

class BatteryGraphFrontActivity : BatteryGraphBaseActivity<FrontPresenter>(), FrontView {

  private val batteryChangedSubject: Subject<BatteryStateEvent> by lazy { PublishSubject.create<BatteryStateEvent>() }
  private lateinit var batteryChangedReceiver: BroadcastReceiver
  private val batteryStatsDetailsContainer: BatteryStatsDetailsContainer by lazy { findViewById(R.layout.layout_batterystats_details) }

  //<editor-fold desc="Lifecycle">
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
  //</editor-fold>

  //<editor-fold desc="Central UI elements rendering">
  override fun renderBatteryState(event: BatteryStateEvent) {
    Timber.d("renderBatteryState, event: $event, batteryStatsDetailsContainer: $batteryStatsDetailsContainer")
    batteryStatsDetailsContainer.let {
      batteryStatsDetailsContainer.renderPercentage(event.batteryPercentage)
      batteryStatsDetailsContainer.renderHealth(event.batteryHealth.name.toLowerCase())
      batteryStatsDetailsContainer.renderPowerSource(event.batteryPowerSource.name.toLowerCase())
      batteryStatsDetailsContainer.renderStatus(event.batteryStatus.name.toLowerCase())
      batteryStatsDetailsContainer.renderVoltage(event.batteryVoltage)
      batteryStatsDetailsContainer.renderTemperature(event.batteryTemperature)
    }
  }

  override fun performOneShotAnimation() {
    Timber.d("performOneShotAnimation")
    batteryAnimatedView.performOneShotAnimation()
  }
  //</editor-fold>

  //<editor-fold desc="'Small Charts' rendering">
  override fun setupSmallChartsView() {
    Timber.d("setupSmallChartsView")
    BottomSheetBehavior.from(smallChartsView).apply {
      this.peekHeight = 100f.toPx(displayMetrics)
      this.isHideable = false
    }
    batteryPercentageSmallChart.initialize()
    batteryTemperatureSmallChart.initialize()
    batteryVoltageSmallChart.initialize()
    batteryHealthSmallChart.initialize()
    connectivitySmallChart.initialize()
  }

  override fun renderSmallChartBatteryPercentage(events: List<BatteryStateEvent>) {
    Timber.d("renderSmallChartBatteryPercentage")
    batteryPercentageSmallChart.setData(events)
  }

  override fun renderSmallChartBatteryTemperature(events: List<BatteryStateEvent>) {
    Timber.d("renderSmallChartBatteryTemperature")
    batteryTemperatureSmallChart.setData(events)
  }

  override fun renderSmallChartBatteryVoltage(events: List<BatteryStateEvent>) {
    Timber.d("renderSmallChartBatteryVoltage")
    batteryVoltageSmallChart.setData(events)
  }

  override fun renderSmallChartBatteryHealth(events: List<BatteryStateEvent>) {
    Timber.d("renderSmallChartBatteryHealth")
    batteryHealthSmallChart.setData(events)
  }

  override fun renderSmallChartConnectivity(events: List<ConnectivityStateEvent>) {
    Timber.d("renderSmallChartConnectivity")
    connectivitySmallChart.setData(events)
  }
  //</editor-fold>

  //<editor-fold desc="Fetching Android Battery State events">
  override fun forceFetchBatteryStateEvent() {
    Timber.d("forceFetchBatteryStateEvent")
    onBatteryStatusIntentReceived(getStickyIntentFromSystem(Intent.ACTION_BATTERY_CHANGED))
  }

  //this is being used along with data from database, because this one reacts instantly
  //whereas database has throttling enabled, so if battery state changed (eg. charge level gone up/down)
  //this would be reflected in the UI with a full throttled delay in the worst case scenario (eg. 10 minutes late)
  override fun registerBatteryStateEventsReceiver() {
    Timber.d("registerBatteryStateEventsReceiver")
    batteryChangedReceiver = createRegisteredBroadcastReceiver(
        intentFilterActions = Intent.ACTION_BATTERY_CHANGED,
        callback = this::onBatteryStatusIntentReceived
    )
  }

  override fun unregisterBatteryStateEventsReceiver() {
    Timber.d("unregisterBatteryStateEventsReceiver")
    batteryChangedReceiver.unregisterReceiverFromContext(this)
  }

  override fun subscribeRealtimeBatteryStateEvents(): Observable<BatteryStateEvent> {
    Timber.d("subscribeRealtimeBatteryStateEvents")
    return batteryChangedSubject
  }

  private fun onBatteryStatusIntentReceived(intent: Intent) {
    Timber.d("onBatteryStatusIntentReceived, intent: ${intent.asString()}")
    batteryChangedSubject.onNext(intent.extras.mapToBatteryStatusEvent())
  }
  //</editor-fold>

}
