package com.android.szparag.batterygraph.screens.chart

import android.os.Bundle
import com.android.szparag.batterygraph.R.layout
import com.android.szparag.batterygraph.common.events.BatteryStateEvent
import com.android.szparag.batterygraph.common.events.FlightModeStateEvent
import com.android.szparag.batterygraph.common.utils.safeLast
import com.android.szparag.batterygraph.common.views.BatteryGraphBaseActivity
import com.android.szparag.batterygraph.dagger.DaggerGlobalScopeWrapper
import kotlinx.android.synthetic.main.activity_chart.chartView
import timber.log.Timber

class BatteryGraphChartActivity : BatteryGraphBaseActivity<ChartPresenter>(), ChartView {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Timber.d("onCreate, savedInstanceState: $savedInstanceState")
    setContentView(layout.activity_chart)
    DaggerGlobalScopeWrapper.getComponent(this).inject(this)
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
