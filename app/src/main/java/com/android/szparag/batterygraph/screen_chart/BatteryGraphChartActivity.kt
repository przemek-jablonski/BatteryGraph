package com.android.szparag.batterygraph.screen_chart

import android.content.Intent
import android.os.Bundle
import com.android.szparag.batterygraph.R.layout
import com.android.szparag.batterygraph.base.events.BatteryStatusEvent
import com.android.szparag.batterygraph.base.utils.safeLast
import com.android.szparag.batterygraph.base.views.BatteryGraphBaseActivity
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

  override fun onResume() {
    Timber.d("onResume")
    super.onResume()
  }

  override fun onPause() {
    Timber.d("onPause")
    super.onPause()
  }


  override fun onStop() {
    super.onStop()
    Timber.d("onStop")
    presenter.detach()
  }

  override fun renderBatteryStatuses(events: List<BatteryStatusEvent>) {
    Timber.d("renderBatteryStatus, events.size: ${events.size}, events.last: ${events.safeLast()}")
    chartView.setDataList(data = events)
  }

}
