package com.android.szparag.batterygraph.screens.front.widgets.smallcharts

import android.content.Context
import android.util.AttributeSet
import com.android.szparag.batterygraph.shared.events.ConnectivityNetworkType
import com.android.szparag.batterygraph.shared.events.ConnectivityStateEvent
import com.android.szparag.batterygraph.shared.utils.lerp
import com.android.szparag.batterygraph.shared.widgets.LineChartSmallBaseWidget
import com.github.mikephil.charting.data.Entry
import timber.log.Timber

class ConnectivitySmallChartWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LineChartSmallBaseWidget<ConnectivityStateEvent>(context, attrs, defStyleAttr) {

  override fun mapDataToEntry(data: ConnectivityStateEvent) =
      Entry(data.eventUnixTimestamp.toFloat(), lerp(yAxisMinimumValue, yAxisMaximumValue, mapNetworkTypeToFloat(data.networkType)))
          .also { Timber.d("mapDataToEntry, data: $data, entry: $it") }


  private fun mapNetworkTypeToFloat(networkType: ConnectivityNetworkType) = when (networkType) {
    ConnectivityNetworkType.MOBILE -> 0.7f
    ConnectivityNetworkType.WIFI -> 1f
    ConnectivityNetworkType.ETHERNET -> 1f
    ConnectivityNetworkType.BLUETOOTH -> 0.3f
    ConnectivityNetworkType.VPN -> 1f
    ConnectivityNetworkType.NO_NETWORK -> 0f
    ConnectivityNetworkType.UNKNOWN -> 0f
  }

}