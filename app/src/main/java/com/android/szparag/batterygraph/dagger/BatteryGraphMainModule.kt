package com.android.szparag.batterygraph.dagger

import android.content.Context
import com.android.szparag.batterygraph.screenChart.BatteryGraphChartInteractor
import com.android.szparag.batterygraph.screenChart.BatteryGraphChartPresenter
import com.android.szparag.batterygraph.screenChart.ChartModel
import com.android.szparag.batterygraph.screenChart.ChartPresenter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 02/11/2017.
 */
@Module
class BatteryGraphMainModule(private val applicationContext: Context) {

  @Provides fun providesChartPresenter(chartModel: ChartModel) : ChartPresenter = BatteryGraphChartPresenter(chartModel)

  @Provides @Singleton fun providesChartModel(): ChartModel = BatteryGraphChartInteractor()

}