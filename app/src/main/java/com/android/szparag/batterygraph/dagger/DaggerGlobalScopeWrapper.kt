package com.android.szparag.batterygraph.dagger

import android.content.Context

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 02/11/2017.
 */
class DaggerGlobalScopeWrapper {

  companion object {
    private var component: BatteryGraphMainComponent? = null

    fun getComponent(context: Context): BatteryGraphMainComponent = if (component == null) constructComponent(context)
    else component!!

    private fun constructComponent(context: Context): BatteryGraphMainComponent {
      component = DaggerBatteryGraphMainComponent.builder()
          .batteryGraphMainModule(BatteryGraphMainModule(context))
          .build()
      return component!!
    }
  }

}