package com.android.szparag.batterygraph.screenChart

import android.content.BroadcastReceiver
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.android.szparag.batterygraph.R.id
import com.android.szparag.batterygraph.R.layout
import com.android.szparag.batterygraph.base.views.BatteryGraphBaseActivity
import com.android.szparag.batterygraph.dagger.DaggerGlobalScopeWrapper
import com.android.szparag.batterygraph.events.BatteryStatusEvent
import com.android.szparag.batterygraph.utils.bindView
import com.android.szparag.batterygraph.utils.createRegisteredBroadcastReceiver
import com.android.szparag.batterygraph.utils.mapToBatteryStatusEvent
import com.android.szparag.batterygraph.utils.unregisterReceiverFromActivity
import io.reactivex.Observable
import io.reactivex.subjects.ReplaySubject
import io.reactivex.subjects.Subject
import timber.log.Timber

class BatteryGraphChartActivity : BatteryGraphBaseActivity<ChartPresenter>(), ChartView {

  private lateinit var batteryChangedActionReceiver: BroadcastReceiver
  private lateinit var batteryStatusSubject: Subject<BatteryStatusEvent>
  private val textView1: TextView by bindView(id.batteryActionsTextview)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Timber.d("onCreate, savedInstanceState: $savedInstanceState")
    setContentView(layout.activity_main)
    batteryStatusSubject = ReplaySubject.create<BatteryStatusEvent>()
    DaggerGlobalScopeWrapper.getComponent(this).inject(this)
  }

  override fun onStart() {
    super.onStart()
    presenter.attach(this)
  }

  override fun onResume() {
    super.onResume()
  }

  override fun onPause() {
    super.onPause()
  }

  override fun onStop() {
    super.onStop()
    presenter.detach()
  }

  override fun renderBatteryStatus(batteryStatusEvent: BatteryStatusEvent) {
    textView1.text = batteryStatusEvent.toString()
  }

  override fun subscribeForBatteryStatusChanged(): Observable<BatteryStatusEvent> = batteryStatusSubject

  override fun registerBatteryStatusReceiver() {
    batteryChangedActionReceiver = createRegisteredBroadcastReceiver(
        intentFilterActions = Intent.ACTION_BATTERY_CHANGED,
        callback = { intent ->
          Timber.d("registerBatteryStatusReceiver, intent: $intent")
          batteryStatusSubject.onNext(intent.extras.mapToBatteryStatusEvent())
        }
    )
  }

  override fun unregisterBatteryStatusReceiver() {
    batteryChangedActionReceiver.unregisterReceiverFromActivity(this)
  }

}
