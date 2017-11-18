package com.android.szparag.batterygraph.shared.events

typealias DevicePowerState = Boolean

data class DevicePowerStateEvent(
    val eventUnixTimestamp: UnixTimestamp,
    val deviceOn: DevicePowerState
)