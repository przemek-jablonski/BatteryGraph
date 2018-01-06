package com.android.szparag.batterygraph.common.events

import com.android.szparag.batterygraph.common.utils.DevicePowerState
import com.android.szparag.batterygraph.common.utils.UnixTimestamp


data class DevicePowerStateEvent(
    val eventUnixTimestamp: UnixTimestamp,
    val deviceOn: DevicePowerState
)