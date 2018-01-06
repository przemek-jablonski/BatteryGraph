package com.android.szparag.batterygraph.common.models

import com.android.szparag.batterygraph.common.entities.RealmBatteryEvent
import com.android.szparag.batterygraph.common.entities.RealmConnectivityStateEvent
import com.android.szparag.batterygraph.common.entities.RealmDevicePowerStateEvent
import com.android.szparag.batterygraph.common.entities.RealmFlightModeStateEvent
import com.android.szparag.batterygraph.common.events.BatteryStateEvent
import com.android.szparag.batterygraph.common.events.ConnectivityStateEvent
import com.android.szparag.batterygraph.common.events.DevicePowerStateEvent
import com.android.szparag.batterygraph.common.events.FlightModeStateEvent
import com.android.szparag.batterygraph.common.utils.toObservable
import io.reactivex.Completable
import io.reactivex.Observable
import io.realm.Realm
import io.realm.Sort.ASCENDING
import timber.log.Timber

private const val REALM_UNIX_TIMESTAMP_FIELD_NAME = "unixTimestamp"

class BatteryGraphDatabaseInteractor : DatabaseInteractor {

  private lateinit var realm: Realm

  //<editor-fold desc="'Lifecycle'">
  override fun attach(): Completable {
    Timber.w("attach, thread: ${Thread.currentThread()}")
    return Completable.fromAction {
      realm = Realm.getDefaultInstance()
      setupDebugRealmListeners()
      Timber.w("attach.callback, thread: ${Thread.currentThread()}")
    }
  }

  override fun detach(): Completable {
    Timber.w("detach")
    return Completable.create {
      realm.close()
      Timber.w("detach.action")
    }
  }
  //</editor-fold>

  //<editor-fold desc="Inserts">
  override fun insertBatteryStateEvent(event: BatteryStateEvent) {
    Timber.w("insertBatteryStateEvent, event: $event")
    realm.executeTransaction { realm ->
      realm.insert(event.toRealmEvent())
      Timber.i("insertBatteryEvent, event: $event, realm: $realm, thread: ${Thread.currentThread()}")
    }
  }

  override fun insertConnectivityStateEvent(event: ConnectivityStateEvent) {
    Timber.w("insertConnectivityStateEvent, event: $event")
    realm.executeTransaction { realm ->
      realm.insert(event.toRealmEvent())
      Timber.i("insertBatteryEvent, event: $event, realm: $realm, thread: ${Thread.currentThread()}")
    }
  }

  override fun insertDevicePowerStateEvent(event: DevicePowerStateEvent) {
    Timber.w("insertDevicePowerStateEvent, event: $event")
    realm.executeTransaction { realm ->
      realm.insert(event.toRealmEvent())
      Timber.i("insertBatteryEvent, event: $event, realm: $realm, thread: ${Thread.currentThread()}")
    }
  }

  override fun insertFlightModeStateEvent(event: FlightModeStateEvent) {
    Timber.w("insertFlightModeStateEvent, event: $event")
    realm.executeTransaction { realm ->
      realm.insert(event.toRealmEvent())
      Timber.i("insertBatteryEvent, event: $event, realm: $realm, thread: ${Thread.currentThread()}")
    }
  }
  //</editor-fold>

  //<editor-fold desc="Exposed subscriptions">
  //todo: should it return list?
  //todo: i guess not, it should return flowable and on subscription - all available items should be emitted
  //todo: and if new items are coming, then stream just them
  override fun subscribeBatteryStateEvents(): Observable<List<BatteryStateEvent>> {
    Timber.w("subscribeBatteryStateEvents")
    return realm.where(RealmBatteryEvent::class.java)
        .findAllSorted(REALM_UNIX_TIMESTAMP_FIELD_NAME, ASCENDING)
        .toObservable()
        .map { results -> results.map { it.toBatteryStatusEvent() } }
  }

  override fun subscribeConnectivityStateEvents(): Observable<List<ConnectivityStateEvent>> {
    Timber.w("subscribeConnectivityStateEvents")
    return realm.where(RealmConnectivityStateEvent::class.java)
        .findAllSorted(REALM_UNIX_TIMESTAMP_FIELD_NAME, ASCENDING)
        .toObservable()
        .map { results -> results.map { it.toConnectivityStateEvent() } }
  }

  override fun subscribeDevicePowerEvents(): Observable<List<DevicePowerStateEvent>> {
    Timber.w("subscribeDevicePowerEvents")
    return realm.where(RealmDevicePowerStateEvent::class.java)
        .findAllSorted(REALM_UNIX_TIMESTAMP_FIELD_NAME, ASCENDING)
        .toObservable()
        .map { results -> results.map { it.toDevicePowerEvent() } }
  }

  override fun subscribeFlightModeEvents(): Observable<List<FlightModeStateEvent>> {
    Timber.w("subscribeFlightModeEvents")
    return realm.where(RealmFlightModeStateEvent::class.java)
        .findAllSorted(REALM_UNIX_TIMESTAMP_FIELD_NAME, ASCENDING)
        .toObservable()
        .map { results -> results.map { it.toFlightModeEvent() } }
  }
  //</editor-fold>

  //<editor-fold desc="Debug stuff">
  private fun setupDebugRealmListeners() {
    Timber.w("setupDebugRealmListeners")
//    if (BuildConfig.DEBUG) { //todo do something with this lint warning
    setupDebugRealmBatteryEventListener()
    setupDebugRealmConnectivityEventListener()
    setupDebugRealmDevicePowerEventListener()
    setupDebugRealmFlightModeEventListener()
//    }
  }

  private fun setupDebugRealmBatteryEventListener() {
    Timber.w("setupDebugRealmBatteryEventListener")
    realm.where(RealmBatteryEvent::class.java).findAllSorted("unixTimestamp", ASCENDING).apply {
      this.forEach { event -> Timber.w("debugRealmListener.RealmBatteryEvent, event: $event") }
      this.addChangeListener({ events -> Timber.w("debugRealmListener.RealmBatteryEvent, events: $events") })
    }
  }

  private fun setupDebugRealmConnectivityEventListener() {
    Timber.w("setupDebugRealmConnectivityEventListener")
    realm.where(RealmConnectivityStateEvent::class.java).findAllSorted("unixTimestamp", ASCENDING).apply {
      this.forEach { event -> Timber.w("debugRealmListener.RealmConnectivityStateEvent, event: $event") }
      this.addChangeListener({ events -> Timber.w("debugRealmListener.RealmConnectivityStateEvent, events: $events") })
    }
  }

  private fun setupDebugRealmDevicePowerEventListener() {
    Timber.w("setupDebugRealmDevicePowerEventListener")
    realm.where(RealmDevicePowerStateEvent::class.java).findAllSorted("unixTimestamp", ASCENDING).apply {
      this.forEach { event -> Timber.w("debugRealmListener.RealmDevicePowerStateEvent, event: $event") }
      this.addChangeListener({ events -> Timber.w("debugRealmListener.RealmDevicePowerStateEvent, events: $events") })
    }
  }

  private fun setupDebugRealmFlightModeEventListener() {
    Timber.w("setupDebugRealmFlightModeEventListener")
    realm.where(RealmFlightModeStateEvent::class.java).findAllSorted("unixTimestamp", ASCENDING).apply {
      this.forEach { event -> Timber.w("debugRealmListener.RealmFlightModeStateEvent, event: $event") }
      this.addChangeListener({ events -> Timber.w("debugRealmListener.RealmFlightModeStateEvent, events: $events") })
    }
  }
  //</editor-fold>

  //<editor-fold desc="Mappings between Realm entities and BatteryGraph internal entities">
  fun RealmBatteryEvent.toBatteryStatusEvent() = BatteryStateEvent(
      unixTimestamp, batteryStatus, batteryHealth, batteryPowerSource, batteryPercentage,
      batteryVoltage, batteryTemperature
  )

  fun RealmConnectivityStateEvent.toConnectivityStateEvent() = ConnectivityStateEvent(
      eventUnixTimestamp = unixTimestamp,
      networkType = networkType,
      networkState = networkState,
      networkStateReason = networkStateReason
  )

  fun RealmDevicePowerStateEvent.toDevicePowerEvent() = DevicePowerStateEvent(
      eventUnixTimestamp = unixTimestamp,
      deviceOn = deviceOn
  )

  fun RealmFlightModeStateEvent.toFlightModeEvent() = FlightModeStateEvent(
      eventUnixTimestamp = unixTimestamp,
      flightModeOn = flightModeOn
  )
  //</editor-fold>

  //<editor-fold desc="Mappings between BatteryGraph internal entities and Realm entities">
  fun BatteryStateEvent.toRealmEvent() = RealmBatteryEvent(
      unixTimestamp = eventUnixTimestamp,
      batteryStatus = batteryStatus.statusInt,
      batteryHealth = batteryHealth.healthInt,
      batteryPowerSource = batteryPowerSource.sourceInt,
      batteryPercentage = batteryPercentage,
      batteryVoltage = batteryVoltage,
      batteryTemperature = batteryTemperature
  )

  fun ConnectivityStateEvent.toRealmEvent() = RealmConnectivityStateEvent(
      unixTimestamp = eventUnixTimestamp,
      networkType = networkType.typeInt,
      networkState = networkState.stateInt,
      networkStateReason = networkStateReason
  )

  fun DevicePowerStateEvent.toRealmEvent() = RealmDevicePowerStateEvent(
      unixTimestamp = eventUnixTimestamp,
      deviceOn = deviceOn
  )

  fun FlightModeStateEvent.toRealmEvent() = RealmFlightModeStateEvent(
      unixTimestamp = eventUnixTimestamp,
      flightModeOn = flightModeOn
  )
  //</editor-fold>

}