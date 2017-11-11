package com.android.szparag.batterygraph.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.IBinder
import android.support.annotation.RequiresApi
import android.support.v7.app.NotificationCompat
import com.android.szparag.batterygraph.R
import com.android.szparag.batterygraph.dagger.DaggerGlobalScopeWrapper
import com.android.szparag.batterygraph.events.BatteryStatusEvent
import com.android.szparag.batterygraph.events.ConnectivityStateEvent
import com.android.szparag.batterygraph.events.DevicePowerEvent
import com.android.szparag.batterygraph.screenChart.BatteryGraphChartActivity
import com.android.szparag.batterygraph.screenChart.ChartModel
import com.android.szparag.batterygraph.utils.asString
import com.android.szparag.batterygraph.utils.createRegisteredBroadcastReceiver
import com.android.szparag.batterygraph.utils.getBGUnixTimestampSecs
import com.android.szparag.batterygraph.utils.mapToBatteryStatusEvent
import com.android.szparag.batterygraph.utils.mapToConnectivityEvent
import com.android.szparag.batterygraph.utils.mapToDevicePowerEvent
import com.android.szparag.batterygraph.utils.mapToDevicePowerEventApiN
import com.android.szparag.batterygraph.utils.toPendingIntent
import com.android.szparag.batterygraph.utils.ui
import com.android.szparag.batterygraph.utils.unregisterReceiverFromContext
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

const val EVENTS_PERSISTENCE_SAMPLING_VALUE_SECS = 5L

class BatteryGraphMonitoringService : Service(), MonitoringService {

  @Inject lateinit var model: ChartModel //todo this cant be chartmodel, more like MonitoringEventsInteractor or sth
  private lateinit var batteryChangedActionReceiver: BroadcastReceiver
  private lateinit var batteryChangedSubject: Subject<BatteryStatusEvent>
  private lateinit var devicePowerActionReceiver: BroadcastReceiver
  private lateinit var devicePowerSubject: Subject<DevicePowerEvent>
  private lateinit var connectivityActionReceiver: BroadcastReceiver
  private lateinit var connectivityManager: ConnectivityManager
  private lateinit var connectivitySubject: Subject<ConnectivityStateEvent>
  private val notificationManager: NotificationManager by lazy {
    applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
  }
  private var notificationChannel: NotificationChannel? = null

  override fun onBind(intent: Intent?): IBinder {
    Timber.d("onBind, intent: $intent")
    throw NotImplementedError()
  }

  override fun onCreate() {
    super.onCreate()
    Timber.d("onCreate")
    DaggerGlobalScopeWrapper.getComponent(this).inject(this)
    registerBatteryStatusReceiver()
    registerDevicePowerReceiver()
    registerConnectivityReceiver()
    subscribeBatteryStatusChanged()
    startServiceAsForegroundService()
  }

  override fun registerBatteryStatusReceiver() {
    Timber.d("registerBatteryStatusReceiver")
    batteryChangedSubject = PublishSubject.create()
    batteryChangedActionReceiver = createRegisteredBroadcastReceiver(
        intentFilterActions = Intent.ACTION_BATTERY_CHANGED,
        callback = this::onBatteryStatusIntentReceived
    )
  }

  override fun registerDevicePowerReceiver() {
    Timber.d("registerDevicePowerReceiver")
    devicePowerSubject = PublishSubject.create()
    val intentFilterActions = arrayOf(
        if (VERSION.SDK_INT >= VERSION_CODES.N) Intent.ACTION_LOCKED_BOOT_COMPLETED else Intent.ACTION_BOOT_COMPLETED,
        Intent.ACTION_SHUTDOWN
    )
    devicePowerActionReceiver = createRegisteredBroadcastReceiver(
        intentFilterActions = *intentFilterActions,
        callback = this::onDevicePowerIntentReceived
    )
  }

  override fun registerConnectivityReceiver() {
    Timber.d("registerConnectivityReceiver")
    connectivitySubject = PublishSubject.create()
    connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    connectivityActionReceiver = createRegisteredBroadcastReceiver(
        intentFilterActions = ConnectivityManager.CONNECTIVITY_ACTION,
        callback = { intent -> onConnectivityIntentReceived(intent, connectivityManager) }
    )
  }

  override fun unregisterConnectivityReceiver() {
    Timber.d("unregisterConnectivityReceiver")
    connectivityActionReceiver.unregisterReceiverFromContext(this)
  }

  override fun unregisterDevicePowerReceiver() {
    Timber.d("unregisterDevicePowerReceiver")
    devicePowerActionReceiver.unregisterReceiverFromContext(this)
  }

  override fun unregisterBatteryStatusReceiver() {
    Timber.d("unregisterBatteryStatusReceiver")
    batteryChangedActionReceiver.unregisterReceiverFromContext(this)
  }

  private fun subscribeBatteryStatusChanged() {
    Timber.d("subscribeBatteryStatusChanged")
    batteryChangedSubject
        .subscribeOn(ui())
        .sample(EVENTS_PERSISTENCE_SAMPLING_VALUE_SECS, TimeUnit.SECONDS, true)
        .observeOn(ui())
        .subscribe(this::onBatteryStatusChanged)
    //todo remember about disposing in ondestroy!
  }

  private fun startServiceAsForegroundService() {
    Timber.d("startServiceAsForegroundService")
    val backToAppIntent = Intent(this, BatteryGraphChartActivity::class.java)
        .toPendingIntent(context = this, requestCode = requestCode())

    startForeground(
        requestCode(),
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
          createNotificationChannel()
          createForegroundNotificationWithChannel(backToAppIntent)
        } else
          createForegroundNotificationWithoutChannel(backToAppIntent)
    )
  }

  @RequiresApi(VERSION_CODES.O)
  private fun createNotificationChannel() {
    Timber.d("createNotificationChannel")
    if (notificationChannel == null) {
      notificationChannel = NotificationChannel(
          notificationChannelId(),
          getText(R.string.service_notification_channel_name),
          NotificationManager.IMPORTANCE_DEFAULT)
      notificationManager.createNotificationChannel(notificationChannel)
    }
  }

  @RequiresApi(VERSION_CODES.O)
  private fun createForegroundNotificationWithChannel(onClickIntent: PendingIntent) =
      Notification.Builder(this, notificationChannelId())
          .setContentTitle(getText(R.string.service_notification_title))
          .setContentText(getText(R.string.service_notification_text))
          .setContentIntent(onClickIntent)
          .setSmallIcon(R.drawable.mock_icon)
          .build()
          .also { Timber.d("createForegroundNotificationWithChannel, return: $it") }


  private fun createForegroundNotificationWithoutChannel(onClickIntent: PendingIntent) =
      NotificationCompat.Builder(this)
          .setContentTitle(getText(R.string.service_notification_title))
          .setContentText(getText(R.string.service_notification_text))
          .setContentIntent(onClickIntent)
          .setSmallIcon(R.drawable.mock_icon)
          .build()
          .also { Timber.d("createForegroundNotificationWithoutChannel, return: $it") }

  private fun onBatteryStatusIntentReceived(intent: Intent) {
    Timber.v("onBatteryStatusIntentReceived, intent: ${intent.asString()}")
    batteryChangedSubject.onNext(intent.extras.mapToBatteryStatusEvent(getBGUnixTimestampSecs()))
  }

  private fun onDevicePowerIntentReceived(intent: Intent) {
    Timber.d("onDevicePowerIntentReceived, intent: ${intent.asString()}")
    devicePowerSubject.onNext(
        if (VERSION.SDK_INT >= VERSION_CODES.N) intent.mapToDevicePowerEventApiN(getBGUnixTimestampSecs()) else intent
            .mapToDevicePowerEvent(getBGUnixTimestampSecs()))
  }

  private fun onConnectivityIntentReceived(intent: Intent, connectivityManager: ConnectivityManager) {
    Timber.d("onConnectivityIntentReceived, intent: ${intent.asString()}, connectivityManager: $connectivityManager")
    connectivitySubject.onNext(connectivityManager.mapToConnectivityEvent())
  }

  private fun onBatteryStatusChanged(batteryStatusEvent: BatteryStatusEvent) {
    Timber.v("onBatteryStatusChanged, batteryStatusEvent: $batteryStatusEvent")
    model.insertBatteryEvent(batteryStatusEvent)
  }

  override fun onDestroy() {
    super.onDestroy()
    Timber.d("onDestroy")
    //todo think about proper disposing
    unregisterBatteryStatusReceiver()
    unregisterDevicePowerReceiver()
    unregisterConnectivityReceiver()
  }

  override fun onLowMemory() {
    super.onLowMemory()
    Timber.d("onLowMemory")
  }

  private fun requestCode() = Math.abs(this.packageName.hashCode())

  private fun notificationChannelId() = requestCode().toString()

  private fun notificationId() = requestCode()

}
