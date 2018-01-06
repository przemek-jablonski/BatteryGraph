package com.android.szparag.batterygraph.services.monitoring

import com.android.szparag.batterygraph.common.events.BatteryStateEvent
import com.android.szparag.batterygraph.common.events.ConnectivityStateEvent
import com.android.szparag.batterygraph.common.events.DevicePowerStateEvent
import com.android.szparag.batterygraph.common.events.FlightModeStateEvent
import com.android.szparag.batterygraph.common.models.DatabaseInteractor

class BatteryGraphMonitoringInteractor(private val databaseInteractor: DatabaseInteractor) : MonitoringInteractor {

  override fun attach() = databaseInteractor.attach()

  override fun detach() = databaseInteractor.detach()

  override fun insertBatteryStateEvent(event: BatteryStateEvent) = databaseInteractor.insertBatteryStateEvent(event)

  override fun insertConnectivityStateEvent(event: ConnectivityStateEvent) = databaseInteractor.insertConnectivityStateEvent(event)

  override fun insertDevicePowerStateEvent(event: DevicePowerStateEvent) = databaseInteractor.insertDevicePowerStateEvent(event)

  override fun insertFlightModeStateEvent(event: FlightModeStateEvent) = databaseInteractor.insertFlightModeStateEvent(event)

}