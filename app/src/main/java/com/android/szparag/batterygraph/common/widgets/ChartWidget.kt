package com.android.szparag.batterygraph.common.widgets

/**
 * Public contact for all the 'Chart' views in BatteryGraph.
 * Implementation- and library- agnostic.
 */
interface ChartWidget<in E : Any> {

  /**
   * Performs setup operations for the chart UI.
   */
  fun initialize()

  /**
   * Sets the data for the chart to display.
   * Every input will be converted to appropriate LineChart data type and rendered as chart dot.
   *
   * @param data List of data to display of specified generic type
   * @see E
   */
  fun setData(data: List<E>) //todo: this should be reactive, so taking Flowable<E> as input

}