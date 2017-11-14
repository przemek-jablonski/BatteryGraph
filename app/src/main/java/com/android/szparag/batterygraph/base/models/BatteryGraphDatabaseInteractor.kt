package com.android.szparag.batterygraph.base.models

import com.android.szparag.batterygraph.base.entities.RealmBatteryEvent
import com.android.szparag.batterygraph.base.entities.RealmConnectivityStateEvent
import com.android.szparag.batterygraph.base.entities.RealmDevicePowerStateEvent
import com.android.szparag.batterygraph.base.entities.RealmFlightModeStateEvent
import com.android.szparag.batterygraph.base.entities.toRealmEvent
import com.android.szparag.batterygraph.base.events.BatteryStateEvent
import com.android.szparag.batterygraph.base.events.ConnectivityStateEvent
import com.android.szparag.batterygraph.base.events.DevicePowerStateEvent
import com.android.szparag.batterygraph.base.events.FlightModeStateEvent
import com.android.szparag.batterygraph.base.utils.toObservable
import io.reactivex.Completable
import io.reactivex.Observable
import io.realm.Realm
import io.realm.Sort.ASCENDING
import timber.log.BuildConfig
import timber.log.Timber

class BatteryGraphDatabaseInteractor : DatabaseInteractor {

  private lateinit var realm: Realm

  override fun attach(): Completable {
    Timber.d("attach, thread: ${Thread.currentThread()}")
    return Completable.create {
      realm = Realm.getDefaultInstance()
      setupDebugRealmListeners()
      Timber.d("attach.action, thread: ${Thread.currentThread()}")
    }
  }

  override fun detach(): Completable {
    Timber.d("detach")
    return Completable.create {
      realm.close()
      Timber.d("detach.action")
    }
  }

  override fun insertBatteryStateEvent(event: BatteryStateEvent) {
    Timber.d("insertBatteryStateEvent, event: $event")
    realm.executeTransaction { realm ->
      realm.insert(event.toRealmEvent())
      Timber.i("insertBatteryEvent, event: $event, realm: $realm, thread: ${Thread.currentThread()}")
    }
  }

  override fun insertConnectivityStateEvent(event: ConnectivityStateEvent) {
    Timber.d("insertConnectivityStateEvent, event: $event")
    realm.executeTransaction { realm ->
      realm.insert(event.toRealmEvent())
      Timber.i("insertBatteryEvent, event: $event, realm: $realm, thread: ${Thread.currentThread()}")
    }
  }

  override fun insertDevicePowerStateEvent(event: DevicePowerStateEvent) {
    Timber.d("insertDevicePowerStateEvent, event: $event")
    realm.executeTransaction { realm ->
      realm.insert(event.toRealmEvent())
      Timber.i("insertBatteryEvent, event: $event, realm: $realm, thread: ${Thread.currentThread()}")
    }
  }

  override fun insertFlightModeStateEvent(event: FlightModeStateEvent) {
    Timber.d("insertFlightModeStateEvent, event: $event")
    realm.executeTransaction { realm ->
      realm.insert(event.toRealmEvent())
      Timber.i("insertBatteryEvent, event: $event, realm: $realm, thread: ${Thread.currentThread()}")
    }
  }

  //todo: should it return list?
  //todo: i guess not, it should return flowable and on subscription - all available items should be emitted
  //todo: and if new items are coming, then stream just them
  override fun subscribeBatteryStateEvents(): Observable<List<BatteryStateEvent>> {
    Timber.d("subscribeBatteryStateEvents")
    return realm.where(RealmBatteryEvent::class.java)
        .findAllSorted("unixTimestamp", ASCENDING)
        .toObservable()
        .map { results -> results.map(RealmBatteryEvent::toBatteryStatusEvent) }
  }

  override fun subscribeConnectivityStateEvents(): Observable<List<ConnectivityStateEvent>> {
    Timber.d("subscribeConnectivityStateEvents")
    return realm.where(RealmConnectivityStateEvent::class.java)
        .findAllSorted("unixTimestamp", ASCENDING)
        .toObservable()
        .map { results -> results.map(RealmConnectivityStateEvent::toConnectivityStateEvent) }
  }

  override fun subscribeDevicePowerEvents(): Observable<List<DevicePowerStateEvent>> {
    Timber.d("subscribeDevicePowerEvents")
    return realm.where(RealmDevicePowerStateEvent::class.java)
        .findAllSorted("unixTimestamp", ASCENDING)
        .toObservable()
        .map { results -> results.map(RealmDevicePowerStateEvent::toDevicePowerEvent) }
  }

  override fun subscribeFlightModeEvents(): Observable<List<FlightModeStateEvent>> {
    Timber.d("subscribeFlightModeEvents")
    return realm.where(RealmFlightModeStateEvent::class.java)
        .findAllSorted("unixTimestamp", ASCENDING)
        .toObservable()
        .map { results -> results.map(RealmFlightModeStateEvent::toFlightModeEvent) }
  }

  private fun setupDebugRealmListeners() {
    Timber.d("setupDebugRealmListeners")
    if (BuildConfig.DEBUG) { //todo do something with this lint warning
      setupDebugRealmBatteryEventListener()
      setupDebugRealmConnectivityEventListener()
      setupDebugRealmDevicePowerEventListener()
      setupDebugRealmFlightModeEventListener()

    }
  }

  private fun setupDebugRealmBatteryEventListener() {
    Timber.d("setupDebugRealmBatteryEventListener")
    realm.where(RealmBatteryEvent::class.java).findAllSorted("unixTimestamp", ASCENDING).addChangeListener({ events ->
      Timber.v("debugRealmListener.RealmBatteryEvent::class.java.callback, events: $events")
    })
  }

  private fun setupDebugRealmConnectivityEventListener() {
    Timber.d("setupDebugRealmConnectivityEventListener")
    realm.where(RealmConnectivityStateEvent::class.java).findAllSorted("unixTimestamp", ASCENDING).addChangeListener({ events ->
      Timber.v("debugRealmListener.RealmConnectivityStateEvent::class.java.callback, events: $events")
    })
  }

  private fun setupDebugRealmDevicePowerEventListener() {
    Timber.d("setupDebugRealmDevicePowerEventListener")
    realm.where(RealmDevicePowerStateEvent::class.java).findAllSorted("unixTimestamp", ASCENDING).addChangeListener({ events ->
      Timber.v("debugRealmListener.RealmDevicePowerStateEvent::class.java.callback, events: $events")
    })
  }

  private fun setupDebugRealmFlightModeEventListener() {
    Timber.d("setupDebugRealmFlightModeEventListener")
    realm.where(RealmFlightModeStateEvent::class.java).findAllSorted("unixTimestamp", ASCENDING).addChangeListener({ events ->
      Timber.v("debugRealmListener.RealmFlightModeStateEvent::class.java.callback, events: $events")
    })

  }


}