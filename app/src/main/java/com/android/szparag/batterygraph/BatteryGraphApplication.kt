package com.android.szparag.batterygraph

import android.app.Application
import io.realm.Realm
import timber.log.Timber
import timber.log.Timber.DebugTree

class BatteryGraphApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    Timber.plant(DebugTree())
    Realm.init(this)
  }

}