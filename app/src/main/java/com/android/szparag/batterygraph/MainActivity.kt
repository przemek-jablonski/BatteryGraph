package com.android.szparag.batterygraph

import android.content.BroadcastReceiver
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView

class MainActivity : AppCompatActivity() {

  private lateinit var batteryChangedActionReceiver: BroadcastReceiver
  private val textView1 : TextView by bindView(R.id.batteryActionsTextview)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  override fun onResume() {
    super.onResume()
    subscribeForBatteryStatusChanged()
  }

  override fun onPause() {
    super.onPause()
    batteryChangedActionReceiver.unregisterReceiver(this)
  }


  fun subscribeForBatteryStatusChanged() {
    batteryChangedActionReceiver = createRegisteredBroadcastReceiver(
        intentFilterActions = Intent.ACTION_BATTERY_CHANGED,
        callback = {intent ->
          val batteryStatusEvent = intent.extras.mapToBatteryStatusEvent()
          Log.d("batteryChanged", batteryStatusEvent.toString())
          textView1.text = batteryStatusEvent.toString()
        }
    )
  }

}
