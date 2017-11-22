package com.android.szparag.batterygraph.screen_front

import io.reactivex.Completable
import timber.log.Timber

class BatteryGraphFrontInteractor: FrontInteractor {

  override fun attach(): Completable {
    Timber.d("attach")
    return Completable.fromAction { }
  }

  override fun detach(): Completable {
    Timber.d("detach")
    return Completable.fromAction { }
  }

}