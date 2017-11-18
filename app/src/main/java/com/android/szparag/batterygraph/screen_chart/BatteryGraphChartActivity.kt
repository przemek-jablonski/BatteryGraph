package com.android.szparag.batterygraph.screen_chart

import android.content.Intent
import android.os.Bundle
import com.android.szparag.batterygraph.R.layout
import com.android.szparag.batterygraph.shared.events.BatteryStateEvent
import com.android.szparag.batterygraph.shared.events.FlightModeStateEvent
import com.android.szparag.batterygraph.shared.utils.safeLast
import com.android.szparag.batterygraph.shared.views.BatteryGraphBaseActivity
import com.android.szparag.batterygraph.dagger.DaggerGlobalScopeWrapper
import com.android.szparag.batterygraph.service_monitoring.BatteryGraphMonitoringService
import kotlinx.android.synthetic.main.activity_chart.chartView
import timber.log.Timber

class BatteryGraphChartActivity : BatteryGraphBaseActivity<ChartPresenter>(), ChartView {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Timber.d("onCreate, savedInstanceState: $savedInstanceState")
    setContentView(layout.activity_chart)
    DaggerGlobalScopeWrapper.getComponent(this).inject(this)
    startService(Intent(this, BatteryGraphMonitoringService::class.java))
  }

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

  override fun renderBatteryStatuses(events: List<BatteryStateEvent>) {
    Timber.d("renderBatteryStatus, events.size: ${events.size}, events.last: ${events.safeLast()}")
    chartView.setBatteryData(events)
  }

  override fun renderFlightModeStatuses(events: List<FlightModeStateEvent>) {
    Timber.d("renderFlightModeStatuses, events.size: ${events.size}, events.last: ${events.safeLast()}")
    chartView.setFlightModeData(events)
  }

}
