package com.android.szparag.batterygraph.base.presenters

import android.support.annotation.CallSuper
import com.android.szparag.batterygraph.base.models.Model
import com.android.szparag.batterygraph.base.views.View
import com.android.szparag.batterygraph.utils.add
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 02/11/2017.
 */
abstract class BatteryGraphBasePresenter<V : View, out M : Model>(val model: M) : Presenter<V> {

  //  override val logger by lazy { Logger.create(this::class.java, this.hashCode()) }
  override var view: V? = null
  override lateinit var viewDisposables: CompositeDisposable //todo: i can access this in implemented presenters, that's bad
  override lateinit var modelDisposables: CompositeDisposable

  //todo: logger creation is probably heavy, move to another thread
  override fun attach(view: V) {
    Timber.d("attach, view: $view")
    this.view = view
    model.attach().subscribe {}
    onAttached()
  }

  @CallSuper override fun onAttached() {
    Timber.d("onAttached")
    viewDisposables = CompositeDisposable()
    modelDisposables = CompositeDisposable()
    subscribeModelEvents()
//    subscribeViewReadyEvents()
//    subscribeViewPermissionsEvents()
    subscribeViewUserEvents()
  }


  override final fun detach() {
    Timber.d("detach")
    onBeforeDetached()
    view = null
  }

  @CallSuper override fun onBeforeDetached() {
    Timber.d("onBeforeDetached")
    viewDisposables.clear()
    modelDisposables.clear()
    model.detach()
  }

//  override fun onViewReady() {
//    Log.d("asd","onViewReady")
////    view?.setupViews()
//  }

//  @CallSuper override fun subscribeViewPermissionsEvents() {
//    Log.d("asd","subscribeViewPermissionsEvents")
//  }
//
//  private fun subscribeViewReadyEvents() {
//    Log.d("asd","subscribeViewReadyEvents")
//  }


  fun Disposable?.toViewDisposable() {
    Timber.d("toViewDisposable, viewDisposables: $viewDisposables, disposed: ${viewDisposables.isDisposed}")
    viewDisposables.takeIf { !it.isDisposed }?.add(this)
  }

  fun Disposable?.toModelDisposable() {
    Timber.d("toModelDisposable, viewDisposables: $viewDisposables, disposed: ${viewDisposables.isDisposed}")
    modelDisposables.takeIf { !it.isDisposed }?.add(this)
  }
}