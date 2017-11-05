package com.android.szparag.batterygraph.screenChart

import com.android.szparag.batterygraph.base.entities.RealmBatteryEvent
import com.android.szparag.batterygraph.base.entities.toRealmEvent
import com.android.szparag.batterygraph.events.BatteryStatusEvent
import com.android.szparag.batterygraph.utils.safeLast
import com.android.szparag.batterygraph.utils.toObservable
import io.reactivex.Completable
import io.reactivex.Observable
import io.realm.Realm
import io.realm.Sort.ASCENDING
import timber.log.Timber

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 02/11/2017.
 */


class BatteryGraphChartInteractor : ChartModel {

  private lateinit var realm: Realm

  override fun attach(): Completable {
    Timber.d("attach, thread: ${Thread.currentThread()}")
    return Completable.create {
      realm = Realm.getDefaultInstance()
      setupDebugRealmListener()
      Timber.d("attach.create, thread: ${Thread.currentThread()}")
    }
  }

  private fun setupDebugRealmListener() {
    Timber.d("setupDebugRealmListener, thread: ${Thread.currentThread()}")
    realm.where(RealmBatteryEvent::class.java).findAllSorted("unixTimestamp", ASCENDING).addChangeListener(
        { events ->
          Timber.v("debugRealmListener.callback, events.size: ${events.size}, events.last: ${events.safeLast()}")
        }
    )
  }

  override fun detach(): Completable {
    Timber.d("detach, thread: ${Thread.currentThread()}")
    return Completable.create {
      realm.close()
      Timber.d("detach.create")
    }
  }

  override fun insertBatteryEvent(batteryStatusEvent: BatteryStatusEvent) {
    realm.executeTransaction { realm ->
      Timber.i("insertBatteryEvent, batteryStatusEvent: $batteryStatusEvent, realm: $realm, thread: ${Thread.currentThread()}")
      realm.insert(batteryStatusEvent.toRealmEvent())
    }
  }

  override fun subscribeBatteryEvents(): Observable<List<BatteryStatusEvent>> {
    Timber.i("subscribeBatteryEvents, thread: ${Thread.currentThread()}")
    return realm.where(RealmBatteryEvent::class.java)
        .findAllSorted("unixTimestamp", ASCENDING)
        .toObservable()
        .map { results -> results.map { it.toBatteryStatusEvent() } }
  }


}