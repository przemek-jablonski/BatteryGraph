package com.android.szparag.batterygraph.screen_chart

import com.android.szparag.batterygraph.base.models.DatabaseInteractor

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 02/11/2017.
 */

class BatteryGraphChartInteractor(private val databaseInteractor: DatabaseInteractor) : ChartInteractor {

  override fun attach() = databaseInteractor.attach()

  override fun detach() = databaseInteractor.detach()

  override fun subscribeBatteryStateEvents() = databaseInteractor.subscribeBatteryStateEvents()

  override fun subscribeConnectivityStateEvents() = databaseInteractor.subscribeConnectivityStateEvents()

  override fun subscribeDevicePowerEvents() = databaseInteractor.subscribeDevicePowerEvents()

  override fun subscribeFlightModeEvents() = databaseInteractor.subscribeFlightModeEvents()

}