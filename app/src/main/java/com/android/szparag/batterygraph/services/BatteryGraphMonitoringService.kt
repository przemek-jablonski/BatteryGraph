package com.android.szparag.batterygraph.services

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Intent
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.IBinder
import android.support.annotation.RequiresApi
import android.support.v7.app.NotificationCompat
import com.android.szparag.batterygraph.R
import com.android.szparag.batterygraph.screenChart.BatteryGraphChartActivity
import com.android.szparag.batterygraph.utils.asString
import com.android.szparag.batterygraph.utils.createRegisteredBroadcastReceiver
import com.android.szparag.batterygraph.utils.toPendingIntent
import com.android.szparag.batterygraph.utils.unregisterReceiverFromContext
import timber.log.Timber

class BatteryGraphMonitoringService : Service(), MonitoringService {

  private lateinit var batteryChangedActionReceiver: BroadcastReceiver

  override fun onBind(intent: Intent?): IBinder {
    Timber.d("onBind, intent: $intent")
    throw NotImplementedError()
  }

  override fun onCreate() {
    super.onCreate()
    Timber.d("onCreate")
    registerBatteryStatusReceiver()
    startServiceAsForegroundService()
  }

  override fun registerBatteryStatusReceiver() {
    Timber.d("registerBatteryStatusReceiver")
    batteryChangedActionReceiver = createRegisteredBroadcastReceiver(
        intentFilterActions = Intent.ACTION_BATTERY_CHANGED,
        callback = { intent -> Timber.w("registerBatteryStatusReceiver, intent: ${intent.asString()}") }
    )
  }

  override fun unregisterBatteryStatusReceiver() {
    Timber.d("unregisterBatteryStatusReceiver")
    batteryChangedActionReceiver.unregisterReceiverFromContext(this)
  }

  private fun startServiceAsForegroundService() {
    val backToAppIntent = Intent(this, BatteryGraphChartActivity::class.java)
        .toPendingIntent(context = this, requestCode = requestCode())

    startForeground(
        requestCode(),
        if (VERSION.SDK_INT >= VERSION_CODES.O)
          createForegroundNotificationWithChannel(backToAppIntent)
        else
          createForegroundNotificationWithoutChannel(backToAppIntent)
    )

  }

  @RequiresApi(VERSION_CODES.O)
  private fun createForegroundNotificationWithChannel(onClickIntent: PendingIntent) =
      Notification.Builder(this, notificationChannelId())
          .setContentTitle(getText(R.string.service_notification_title))
          .setContentText(getText(R.string.service_notification_text))
          .setContentIntent(onClickIntent)
          .setSmallIcon(R.drawable.mock_icon)
          .build()


  private fun createForegroundNotificationWithoutChannel(onClickIntent: PendingIntent) =
      NotificationCompat.Builder(this)
          .setContentTitle(getText(R.string.service_notification_title))
          .setContentText(getText(R.string.service_notification_text))
          .setContentIntent(onClickIntent)
          .setSmallIcon(R.drawable.mock_icon)
          .build()

  override fun onDestroy() {
    super.onDestroy()
    Timber.d("onDestroy")
    unregisterBatteryStatusReceiver()
  }

  override fun onLowMemory() {
    super.onLowMemory()
    Timber.d("onLowMemory")
  }

  private fun requestCode() = Math.abs(this.packageName.hashCode())

  private fun notificationChannelId() = requestCode().toString()

}
