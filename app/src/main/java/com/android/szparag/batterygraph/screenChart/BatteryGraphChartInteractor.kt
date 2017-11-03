package com.android.szparag.batterygraph.screenChart

import io.reactivex.Completable

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 02/11/2017.
 */
class BatteryGraphChartInteractor : ChartModel {

  override fun attach(): Completable {
    return Completable.create {  }
  }

  override fun detach(): Completable {
    return Completable.create {  }
  }

}