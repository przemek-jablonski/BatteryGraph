package com.android.szparag.batterygraph

import android.app.Application
import android.content.Intent
import com.android.szparag.batterygraph.services.monitoring.BatteryGraphMonitoringService
import io.realm.Realm
import timber.log.Timber
import timber.log.Timber.DebugTree

class BatteryGraphApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    Timber.plant(DebugTree())
    Timber.d("onCreate")
    Realm.init(this)
    startService(Intent(this, BatteryGraphMonitoringService::class.java))
  }

}