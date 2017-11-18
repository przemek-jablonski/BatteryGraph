package com.android.szparag.batterygraph.service_monitoring

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
import com.android.szparag.batterygraph.shared.events.BatteryStateEvent
import com.android.szparag.batterygraph.shared.events.ConnectivityStateEvent
import com.android.szparag.batterygraph.shared.events.DevicePowerStateEvent
import com.android.szparag.batterygraph.shared.events.FlightModeStateEvent
import com.android.szparag.batterygraph.shared.utils.asString
import com.android.szparag.batterygraph.shared.utils.createRegisteredBroadcastReceiver
import com.android.szparag.batterygraph.shared.utils.emptyString
import com.android.szparag.batterygraph.shared.utils.getBGUnixTimestampSecs
import com.android.szparag.batterygraph.shared.utils.mapToBatteryStatusEvent
import com.android.szparag.batterygraph.shared.utils.mapToConnectivityEvent
import com.android.szparag.batterygraph.shared.utils.mapToDevicePowerEvent
import com.android.szparag.batterygraph.shared.utils.mapToDevicePowerEventApiN
import com.android.szparag.batterygraph.shared.utils.mapToFlightModeEvent
import com.android.szparag.batterygraph.shared.utils.toPendingIntent
import com.android.szparag.batterygraph.shared.utils.ui
import com.android.szparag.batterygraph.shared.utils.unregisterReceiverFromContext
import com.android.szparag.batterygraph.dagger.DaggerGlobalScopeWrapper
import com.android.szparag.batterygraph.screen_chart.BatteryGraphChartActivity
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

const val EVENTS_PERSISTENCE_SAMPLING_VALUE_SECS = 5L
const val NOTIFICATION_CHANNEL_ID = "batterygraph.notifications.monitoring.channel_id"

//todo: create presenter
class BatteryGraphMonitoringService : Service(), MonitoringService {


  @Inject lateinit var model: MonitoringInteractor
  private lateinit var batteryChangedActionReceiver: BroadcastReceiver
  private lateinit var batteryChangedSubject: Subject<BatteryStateEvent>
  private lateinit var devicePowerActionReceiver: BroadcastReceiver
  private lateinit var devicePowerSubject: Subject<DevicePowerStateEvent>
  private lateinit var connectivityActionReceiver: BroadcastReceiver
  private lateinit var connectivityManager: ConnectivityManager
  private lateinit var connectivitySubject: Subject<ConnectivityStateEvent>
  private lateinit var flightModeActionReceiver: BroadcastReceiver
  private lateinit var flightModeSubject: Subject<FlightModeStateEvent>

  private val notificationManager: NotificationManager by lazy {
    applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
  }
  private var notificationChannel: NotificationChannel? = null
  private lateinit var foregroundNotification: Notification
  private lateinit var backToAppIntent: PendingIntent


  //  Android Service lifecycle callbacks:
  //  _________________________________________________________________________

  override fun onBind(intent: Intent?): IBinder {
    Timber.d("onBind, intent: $intent")
    throw NotImplementedError()
  }

  override fun onCreate() {
    super.onCreate()
    Timber.d("onCreate")
    DaggerGlobalScopeWrapper.getComponent(this).inject(this)
    registerSystemEventsReceivers()
    subscribeInAppEvents()
    startServiceAsForegroundService()
  }

  override fun onDestroy() {
    super.onDestroy()
    Timber.d("onDestroy")
    unsubscribeInAppEvents()
    unregisterSystemEventsReceivers()
    //todo think about proper disposing
  }


  //  Creating foreground notification:
  //  _________________________________________________________________________

  override fun startServiceAsForegroundService() {
    Timber.d("startServiceAsForegroundService")
    setupBackToAppIntent()
    setupNotificationChannel()
    setupNotification(backToAppIntent)
    showNotification(foregroundNotification)
  }

  override fun setupBackToAppIntent() {
    backToAppIntent = Intent(this, BatteryGraphChartActivity::class.java)
        .toPendingIntent(context = this, requestCode = requestCode())
  }

  override fun setupNotificationChannel() {
    Timber.d("createNotificationChannel")
    if (VERSION.SDK_INT >= VERSION_CODES.O && notificationChannel == null) {
      notificationChannel = NotificationChannel(
          NOTIFICATION_CHANNEL_ID,
          getText(R.string.service_notification_channel_name),
          NotificationManager.IMPORTANCE_LOW)
      notificationManager.createNotificationChannel(notificationChannel)
    }
  }

  override fun setupNotification(backToAppIntent: PendingIntent) {
    if (VERSION.SDK_INT >= VERSION_CODES.O)
      setupForegroundNotificationWithChannel(backToAppIntent)
    else
      setupForegroundNotificationWithoutChannel(backToAppIntent)
  }

  override fun showNotification(foregroundNotification: Notification) {
    startForeground(requestCode(), foregroundNotification)
  }

  @RequiresApi(VERSION_CODES.O) private fun setupForegroundNotificationWithChannel(onClickIntent: PendingIntent) {
    foregroundNotification = Notification.Builder(this, notificationChannelId())
        .setContentTitle(getText(R.string.service_notification_title))
        .setContentText(getText(R.string.service_notification_text))
        .setContentIntent(onClickIntent)
        .setSmallIcon(R.drawable.mock_icon)
        .build()
        .also { Timber.d("createForegroundNotificationWithChannel, return: $it") }
  }

  private fun setupForegroundNotificationWithoutChannel(onClickIntent: PendingIntent) {
    foregroundNotification = NotificationCompat.Builder(this)
        .setContentTitle(getText(R.string.service_notification_title))
        .setContentText(getText(R.string.service_notification_text))
        .setContentIntent(onClickIntent)
        .setSmallIcon(R.drawable.mock_icon)
        .build()
        .also { Timber.d("createForegroundNotificationWithoutChannel, return: $it") }
  }


  //  Registering / unregistering system events listeners:
  //  _________________________________________________________________________


  override fun registerSystemEventsReceivers() {
    Timber.d("registerSystemEventsReceivers")
    registerBatteryStatusReceiver()
    registerConnectivityReceiver()
    registerDevicePowerReceiver()
    registerFlightModeListener()
  }

  override fun registerBatteryStatusReceiver() {
    Timber.d("registerBatteryStatusReceiver")
    batteryChangedSubject = PublishSubject.create()
    batteryChangedActionReceiver = createRegisteredBroadcastReceiver(
        intentFilterActions = Intent.ACTION_BATTERY_CHANGED,
        callback = this::onBatteryStatusIntentReceived
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

  override fun registerFlightModeListener() {
    Timber.d("registerFlightModeListener")
    flightModeSubject = PublishSubject.create()
    flightModeActionReceiver = createRegisteredBroadcastReceiver(
        intentFilterActions = Intent.ACTION_AIRPLANE_MODE_CHANGED,
        callback = this::onFlightModeIntentReceived
    )
  }

  override fun unregisterSystemEventsReceivers() {
    Timber.d("unregisterSystemEventsReceivers")
    unregisterBatteryStatusReceiver()
    unregisterConnectivityReceiver()
    unregisterDevicePowerReceiver()
    unregisterFlightModeListener()
  }

  override fun unregisterBatteryStatusReceiver() {
    Timber.d("unregisterBatteryStatusReceiver")
    batteryChangedActionReceiver.unregisterReceiverFromContext(this)
  }

  override fun unregisterConnectivityReceiver() {
    Timber.d("unregisterConnectivityReceiver")
    connectivityActionReceiver.unregisterReceiverFromContext(this)
  }

  override fun unregisterDevicePowerReceiver() {
    Timber.d("unregisterDevicePowerReceiver")
    devicePowerActionReceiver.unregisterReceiverFromContext(this)
  }

  override fun unregisterFlightModeListener() {
    Timber.d("unregisterFlightModeListener")
    flightModeActionReceiver.unregisterReceiverFromContext(this)
  }


  //  Handling incoming system events:
  //  _________________________________________________________________________

  private fun onBatteryStatusIntentReceived(intent: Intent) {
    Timber.v("onBatteryStatusIntentReceived, intent: ${intent.asString()}")
    batteryChangedSubject.onNext(intent.extras.mapToBatteryStatusEvent(getBGUnixTimestampSecs()))
  }

  private fun onConnectivityIntentReceived(intent: Intent, connectivityManager: ConnectivityManager) {
    Timber.v("onConnectivityIntentReceived, intent: ${intent.asString()}, connectivityManager: $connectivityManager")
    connectivitySubject.onNext(connectivityManager.mapToConnectivityEvent(getBGUnixTimestampSecs()))
  }

  private fun onDevicePowerIntentReceived(intent: Intent) {
    Timber.v("onDevicePowerIntentReceived, intent: ${intent.asString()}")
    devicePowerSubject.onNext(
        if (VERSION.SDK_INT >= VERSION_CODES.N) intent.mapToDevicePowerEventApiN(getBGUnixTimestampSecs()) else intent
            .mapToDevicePowerEvent(getBGUnixTimestampSecs()))
  }

  private fun onFlightModeIntentReceived(intent: Intent) {
    Timber.v("onFlightModeIntentReceived, intent: ${intent.asString()}")
    flightModeSubject.onNext(intent.extras.mapToFlightModeEvent(getBGUnixTimestampSecs()))
  }


  // Receiving system events converted to in-app events:
  //  _________________________________________________________________________

  override fun subscribeInAppEvents() {
    Timber.d("subscribeInAppEvents")
    subscribeBatteryStateChanges()
    subscribeConnectivityStateChanges()
    subscribeDevicePowerStateChanges()
    subscribeFlightModeStateChanges()
  }

  private fun subscribeBatteryStateChanges() {
    Timber.d("subscribeBatteryStateChanges")
    batteryChangedSubject
        .subscribeOn(ui())
        .sample(EVENTS_PERSISTENCE_SAMPLING_VALUE_SECS, TimeUnit.SECONDS, true)
        .observeOn(ui())
        .subscribe(this::onBatteryStateChanged)
  }

  private fun subscribeConnectivityStateChanges() {
    Timber.d("subscribeConnectivityStateChanges")
    connectivitySubject
        .subscribeOn(ui())
        .sample(EVENTS_PERSISTENCE_SAMPLING_VALUE_SECS, TimeUnit.SECONDS, true)
        .observeOn(ui())
        .subscribe(this::onConnectivityStateChanged)
  }

  private fun subscribeDevicePowerStateChanges() {
    Timber.d("subscribeDevicePowerStateChanges")
    devicePowerSubject
        .subscribeOn(ui())
        .sample(EVENTS_PERSISTENCE_SAMPLING_VALUE_SECS, TimeUnit.SECONDS, true)
        .observeOn(ui())
        .subscribe(this::onDevicePowerStateChanged)
  }

  private fun subscribeFlightModeStateChanges() {
    Timber.d("subscribeFlightModeStateChanges")
    flightModeSubject
        .subscribeOn(ui())
        .sample(EVENTS_PERSISTENCE_SAMPLING_VALUE_SECS, TimeUnit.SECONDS, true)
        .observeOn(ui())
        .subscribe(this::onFlightModeStateChaned)
  }

  override fun unsubscribeInAppEvents() {
    Timber.d("unsubscribeInAppEvents")
    //todo: how to do it properly?
  }


  // Processing received in-app events:
  //  _________________________________________________________________________

  private fun onBatteryStateChanged(event: BatteryStateEvent) {
    Timber.d("onBatteryStatusChanged, event: $event")
    model.insertBatteryStateEvent(event)
  }

  private fun onConnectivityStateChanged(event: ConnectivityStateEvent) {
    Timber.d("onConnectivityStateChanged, event: $event")
    model.insertConnectivityStateEvent(event)
  }

  private fun onDevicePowerStateChanged(event: DevicePowerStateEvent) {
    Timber.d("onDevicePowerStateChanged, event: $event")
    model.insertDevicePowerStateEvent(event)
  }

  private fun onFlightModeStateChaned(event: FlightModeStateEvent) {
    Timber.d("onFlightModeStateChaned, event: $event")
    model.insertFlightModeStateEvent(event)
  }


  // Misc:
  //  _________________________________________________________________________

  override fun requestCode() = Math.abs(this.packageName.hashCode())

  override fun notificationChannelId() = if (VERSION.SDK_INT >= VERSION_CODES.O) notificationChannel?.id ?: emptyString() else emptyString()

  override fun notificationId() = requestCode()

}
