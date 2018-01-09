package com.android.szparag.batterygraph.common.utils

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

fun Context.createRegisteredBroadcastReceiver(
    vararg intentFilterActions: String, callback: (Intent) -> (Unit)): BroadcastReceiver {
  val broadcastReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
      intent?.takeIf { intentFilterActions.contains(it.action) }?.let(callback::invoke)
    }
  }
  this.registerReceiver(broadcastReceiver, IntentFilter().apply { intentFilterActions.forEach(this::addAction) })
  return broadcastReceiver
}

fun BroadcastReceiver.unregisterReceiverFromContext(context: Context) =
    context.unregisterReceiver(this)

fun Context.getStickyIntentFromSystem(intentFilterAction: String): Intent =
    registerReceiver(null, IntentFilter(intentFilterAction))

fun Intent.toPendingIntent(context: Context, requestCode: Int = 0, flags: Int = 0): PendingIntent =
    PendingIntent.getActivity(context, requestCode, this, flags)