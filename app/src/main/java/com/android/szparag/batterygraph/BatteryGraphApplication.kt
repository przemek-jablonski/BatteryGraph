package com.android.szparag.batterygraph

import android.app.Application
import timber.log.Timber
import timber.log.Timber.DebugTree

class BatteryGraphApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    Timber.plant(DebugTree())
    Timber.d("planted")
  }

}