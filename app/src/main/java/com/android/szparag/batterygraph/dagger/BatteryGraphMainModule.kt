package com.android.szparag.batterygraph.dagger

import android.content.Context
import com.android.szparag.batterygraph.shared.models.BatteryGraphDatabaseInteractor
import com.android.szparag.batterygraph.shared.models.DatabaseInteractor
import com.android.szparag.batterygraph.screen_chart.BatteryGraphChartInteractor
import com.android.szparag.batterygraph.screen_chart.BatteryGraphChartPresenter

import com.android.szparag.batterygraph.screen_chart.ChartInteractor
import com.android.szparag.batterygraph.screen_chart.ChartPresenter
import com.android.szparag.batterygraph.service_monitoring.BatteryGraphMonitoringInteractor
import com.android.szparag.batterygraph.service_monitoring.MonitoringInteractor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 02/11/2017.
 */
@Module
class BatteryGraphMainModule(private val applicationContext: Context) {

  @Provides fun providesChartPresenter(chartModel: ChartInteractor): ChartPresenter = BatteryGraphChartPresenter(chartModel)

  @Provides @Singleton fun providesDatabaseInteractor(): DatabaseInteractor = BatteryGraphDatabaseInteractor()

  @Provides @Singleton fun providesChartInteractor(databaseInteractor: DatabaseInteractor): ChartInteractor =
      BatteryGraphChartInteractor(databaseInteractor)

  @Provides @Singleton fun providesMonitoringInteractor(databaseInteractor: DatabaseInteractor): MonitoringInteractor =
      BatteryGraphMonitoringInteractor(databaseInteractor)

}