package com.android.szparag.batterygraph.events

typealias DevicePowerState = Boolean

data class DevicePowerEvent(
    val deviceOn: DevicePowerState
)