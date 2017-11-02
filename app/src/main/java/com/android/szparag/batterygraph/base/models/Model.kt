package com.android.szparag.batterygraph.base.models

import io.reactivex.Completable

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 02/11/2017.
 */
interface Model {

//  val logger: Logger

  fun attach(): Completable
  fun detach(): Completable
}