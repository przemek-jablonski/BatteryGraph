package com.android.szparag.batterygraph.base.entities

import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.RealmClass

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 02/11/2017.
 */
@RealmClass open class BatteryEntity(
    @Index var unixTimestamp: Long,
    var batteryStatus: Int,
    var batteryHealth: Int,
    var batteryPowerSource: Int,
    var batteryPercentage: Int,
    var batteryVoltage: Float,
    var batteryTemperature: Int
    ) : RealmObject()