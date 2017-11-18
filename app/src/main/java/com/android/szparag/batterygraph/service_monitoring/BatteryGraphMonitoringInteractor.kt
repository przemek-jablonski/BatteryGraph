package com.android.szparag.batterygraph.service_monitoring

import com.android.szparag.batterygraph.shared.events.BatteryStateEvent
import com.android.szparag.batterygraph.shared.events.ConnectivityStateEvent
import com.android.szparag.batterygraph.shared.events.DevicePowerStateEvent
import com.android.szparag.batterygraph.shared.events.FlightModeStateEvent
import com.android.szparag.batterygraph.shared.models.DatabaseInteractor

class BatteryGraphMonitoringInteractor(private val databaseInteractor: DatabaseInteractor) : MonitoringInteractor {

  override fun attach() = databaseInteractor.attach()

  override fun detach() = databaseInteractor.detach()

  override fun insertBatteryStateEvent(event: BatteryStateEvent) = databaseInteractor.insertBatteryStateEvent(event)

  override fun insertConnectivityStateEvent(event: ConnectivityStateEvent) = databaseInteractor.insertConnectivityStateEvent(event)

  override fun insertDevicePowerStateEvent(event: DevicePowerStateEvent) = databaseInteractor.insertDevicePowerStateEvent(event)

  override fun insertFlightModeStateEvent(event: FlightModeStateEvent) = databaseInteractor.insertFlightModeStateEvent(event)

}