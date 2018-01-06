package com.android.szparag.batterygraph.common.entities

import com.android.szparag.batterygraph.common.utils.NetworkStateReason
import com.android.szparag.batterygraph.common.utils.UnixTimestamp
import com.android.szparag.batterygraph.common.utils.invalidIntValue
import com.android.szparag.batterygraph.common.utils.invalidLongValue
import com.android.szparag.batterygraph.common.utils.invalidStringValue
import io.realm.RealmObject
import io.realm.annotations.RealmClass


@RealmClass open class RealmConnectivityStateEvent(
    var unixTimestamp: UnixTimestamp = invalidLongValue(),
    var networkType: Int = invalidIntValue(),
    var networkState: Int = invalidIntValue(),
    var networkStateReason: NetworkStateReason = invalidStringValue()
) : RealmObject()

