package com.android.szparag.batterygraph.base.events

typealias DevicePowerState = Boolean

data class DevicePowerStateEvent(
    val eventUnixTimestamp: UnixTimestamp,
    val deviceOn: DevicePowerState
)