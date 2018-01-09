package com.android.szparag.batterygraph.screens.front

import com.android.szparag.batterygraph.common.events.BatteryStateEvent
import com.android.szparag.batterygraph.common.presenters.Presenter

interface FrontPresenter : Presenter<FrontView> {
  //todo: here, this event should be decomposed into simpler pieces and pushed onto view
  fun renderBatteryState(event: BatteryStateEvent)
}