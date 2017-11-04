package com.android.szparag.batterygraph.dagger

import com.android.szparag.batterygraph.screenChart.BatteryGraphChartActivity
import com.android.szparag.batterygraph.services.BatteryGraphMonitoringService
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 02/11/2017.
 */
@Singleton
@Component(modules = arrayOf(BatteryGraphMainModule::class))
interface BatteryGraphMainComponent {
  fun inject(target: BatteryGraphChartActivity)
  fun inject(target: BatteryGraphMonitoringService)
}