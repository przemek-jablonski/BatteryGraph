package com.android.szparag.batterygraph.dagger

import dagger.Component
import javax.inject.Singleton

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 02/11/2017.
 */
@Singleton
@Component(modules = arrayOf(BatteryGraphMainModule::class))
interface BatteryGraphMainComponent {
}