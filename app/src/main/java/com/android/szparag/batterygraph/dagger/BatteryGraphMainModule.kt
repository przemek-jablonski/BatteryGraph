package com.android.szparag.batterygraph.dagger

import android.content.Context
import com.android.szparag.batterygraph.shared.models.BatteryGraphDatabaseInteractor
import com.android.szparag.batterygraph.shared.models.DatabaseInteractor
import com.android.szparag.batterygraph.screen_chart.BatteryGraphChartInteractor
import com.android.szparag.batterygraph.screen_chart.BatteryGraphChartPresenter

import com.android.szparag.batterygraph.screen_chart.ChartInteractor
import com.android.szparag.batterygraph.screen_chart.ChartPresenter
import com.android.szparag.batterygraph.screen_front.BatteryGraphFrontInteractor
import com.android.szparag.batterygraph.screen_front.BatteryGraphFrontPresenter
import com.android.szparag.batterygraph.screen_front.FrontInteractor
import com.android.szparag.batterygraph.screen_front.FrontPresenter
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

  //todo: refactor module (new way of injecting stuff)

  @Provides fun providesChartPresenter(chartModel: ChartInteractor): ChartPresenter = BatteryGraphChartPresenter(chartModel)

  @Provides fun providesFrontPresenter(frontModel: FrontInteractor) : FrontPresenter = BatteryGraphFrontPresenter(frontModel)

  @Provides @Singleton fun providesChartInteractor(databaseInteractor: DatabaseInteractor): ChartInteractor =
      BatteryGraphChartInteractor(databaseInteractor)

  @Provides @Singleton fun providesFrontInteractor() : FrontInteractor = BatteryGraphFrontInteractor()

  @Provides @Singleton fun providesDatabaseInteractor(): DatabaseInteractor = BatteryGraphDatabaseInteractor()

  @Provides @Singleton fun providesMonitoringInteractor(databaseInteractor: DatabaseInteractor): MonitoringInteractor =
      BatteryGraphMonitoringInteractor(databaseInteractor)

}