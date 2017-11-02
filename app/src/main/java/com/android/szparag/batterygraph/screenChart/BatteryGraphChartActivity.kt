package com.android.szparag.batterygraph.screenChart

import android.content.BroadcastReceiver
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.android.szparag.batterygraph.R.id
import com.android.szparag.batterygraph.R.layout
import com.android.szparag.batterygraph.base.views.BatteryGraphBaseActivity
import com.android.szparag.batterygraph.events.BatteryStatusEvent
import com.android.szparag.batterygraph.utils.bindView
import com.android.szparag.batterygraph.utils.createRegisteredBroadcastReceiver
import com.android.szparag.batterygraph.utils.mapToBatteryStatusEvent
import com.android.szparag.batterygraph.utils.unregisterReceiver
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class BatteryGraphChartActivity : BatteryGraphBaseActivity<ChartPresenter>() {

  private lateinit var batteryChangedActionReceiver: BroadcastReceiver
  private val batteryStatusSubject by lazy { PublishSubject.create<BatteryStatusEvent>() }
  private val textView1: TextView by bindView(id.batteryActionsTextview)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_main)
  }

  override fun onResume() {
    super.onResume()
    registerBatteryStatusReceiver()
  }

  override fun onPause() {
    super.onPause()
    unregisterBatteryStatusReceiver()
  }

  private fun registerBatteryStatusReceiver() {
    batteryChangedActionReceiver = createRegisteredBroadcastReceiver(
        intentFilterActions = Intent.ACTION_BATTERY_CHANGED,
        callback = { intent ->
          val batteryStatusEvent = intent.extras.mapToBatteryStatusEvent()
          textView1.text = batteryStatusEvent.toString()
          batteryStatusSubject.onNext(batteryStatusEvent)
        }
    )
  }

  private fun unregisterBatteryStatusReceiver() {
    batteryChangedActionReceiver.unregisterReceiver(this)
  }

  fun subscribeForBatteryStatusChanged(): Observable<BatteryStatusEvent> = batteryStatusSubject


  //___________________

  override fun subscribeUserBackButtonPressed(): Observable<Any> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

}
