package com.android.szparag.batterygraph.base.entities

import com.android.szparag.batterygraph.events.ConnectivityStateEvent
import com.android.szparag.batterygraph.utils.invalidIntValue
import com.android.szparag.batterygraph.utils.invalidLongValue
import com.android.szparag.batterygraph.utils.invalidStringValue
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

typealias NetworkStateReason = String

@RealmClass open class RealmConnectivityStateEvent(
    @PrimaryKey var unixTimestamp: Long = invalidLongValue(),
    var networkType: Int = invalidIntValue(),
    var networkState: Int = invalidIntValue(),
    var networkStateReason: NetworkStateReason = invalidStringValue()
) : RealmObject() {

  fun toConnectivityStateEvent() = ConnectivityStateEvent(
      eventUnixTimestamp = unixTimestamp,
      networkType = networkType,
      networkState = networkState,
      networkStateReason = networkStateReason
  )

}