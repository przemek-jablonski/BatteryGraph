package com.android.szparag.batterygraph.common.entities

import com.android.szparag.batterygraph.common.utils.UnixTimestamp
import com.android.szparag.batterygraph.common.utils.invalidFloatValue
import com.android.szparag.batterygraph.common.utils.invalidIntValue
import com.android.szparag.batterygraph.common.utils.invalidLongValue
import io.realm.RealmObject
import io.realm.annotations.RealmClass


@RealmClass open class RealmBatteryEvent(
    var unixTimestamp: UnixTimestamp = invalidLongValue(),
    var batteryStatus: Int = invalidIntValue(),
    var batteryHealth: Int = invalidIntValue(),
    var batteryPowerSource: Int = invalidIntValue(),
    var batteryPercentage: Int = invalidIntValue(),
    var batteryVoltage: Float = invalidFloatValue(),
    var batteryTemperature: Int = invalidIntValue()
) : RealmObject()
