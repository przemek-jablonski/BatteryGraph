package com.android.szparag.batterygraph.common.entities

import com.android.szparag.batterygraph.common.utils.DevicePowerState
import com.android.szparag.batterygraph.common.utils.UnixTimestamp
import com.android.szparag.batterygraph.common.utils.invalidLongValue
import io.realm.RealmObject
import io.realm.annotations.RealmClass


@RealmClass open class RealmDevicePowerStateEvent(
    var unixTimestamp: UnixTimestamp = invalidLongValue(),
    var deviceOn: DevicePowerState = true
) : RealmObject()