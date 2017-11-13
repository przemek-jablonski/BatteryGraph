package com.android.szparag.batterygraph.base.models

import io.reactivex.Completable

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 02/11/2017.
 */
interface Interactor {

  fun attach(): Completable
  fun detach(): Completable

}