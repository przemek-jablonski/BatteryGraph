package com.android.szparag.batterygraph.screenChart

import com.android.szparag.batterygraph.base.entities.RealmBatteryEvent
import com.android.szparag.batterygraph.base.entities.toRealmEvent
import com.android.szparag.batterygraph.events.BatteryStatusEvent
import com.android.szparag.batterygraph.utils.toObservable
import io.reactivex.Completable
import io.reactivex.Observable
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.Sort.ASCENDING
import timber.log.Timber

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 02/11/2017.
 */


class BatteryGraphChartInteractor : ChartModel {

  private lateinit var realm: Realm

  override fun attach(): Completable {
    Timber.d("attach")
    return Completable.create {
      realm = Realm.getDefaultInstance()
      setupDebugRealmListener()
      Timber.d("attach.create")
    }
  }

  private fun setupDebugRealmListener() {

    realm.where(RealmBatteryEvent::class.java).findAllSorted("unixTimestamp", ASCENDING).addChangeListener(
        RealmChangeListener { realmResultsBatteryChanged ->
          realmResultsBatteryChanged.forEach { event ->
            Timber.v(event.toString())
          }
        }
    )
  }

  override fun detach(): Completable {
    Timber.d("detach")
    return Completable.create {
      realm.close()
      Timber.d("detach.create")
    }
  }

  override fun insertBatteryEvent(batteryStatusEvent: BatteryStatusEvent) {
    realm.executeTransaction { realm ->
      Timber.i("insertBatteryEvent, batteryStatusEvent: $batteryStatusEvent, realm: $realm")
      realm.insert(batteryStatusEvent.toRealmEvent(System.currentTimeMillis())) //todo System.currentTimeMillis()
    }
  }

  override fun subscribeBatteryEvents(): Observable<BatteryStatusEvent> {
    Timber.i("subscribeBatteryEvents")
    return realm.where(RealmBatteryEvent::class.java).findAllSorted("unixTimestamp",
        ASCENDING).toObservable().map { it.last().toBatteryStatusEvent() }
  }


}