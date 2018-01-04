package com.android.szparag.batterygraph.common.events

typealias DevicePowerState = Boolean

data class DevicePowerStateEvent(
    val eventUnixTimestamp: UnixTimestamp,
    val deviceOn: DevicePowerState
)