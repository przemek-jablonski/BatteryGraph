package com.android.szparag.batterygraph.shared.presenters

import android.support.annotation.CallSuper
import com.android.szparag.batterygraph.shared.models.Interactor
import com.android.szparag.batterygraph.shared.utils.add
import com.android.szparag.batterygraph.shared.views.View
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 02/11/2017.
 */
abstract class BatteryGraphBasePresenter<V : View, out M : Interactor>(val model: M) : Presenter<V> {

  override var view: V? = null
  private val viewDisposables: CompositeDisposable by lazy { CompositeDisposable() }
  private val modelDisposables: CompositeDisposable by lazy { CompositeDisposable() }

  override fun attach(view: V) {
    Timber.d("attach, view: $view")
    this.view = view
    model.attach()
        .andThen(Completable.fromAction { subscribeModelEvents() })
        .andThen(Completable.fromAction { subscribeViewUserEvents() })
        .andThen(Completable.fromAction { onAttached() })
        .subscribe {
          Timber.d("attach.call, view: $view")
        }
  }

  @CallSuper override fun onAttached() {
    Timber.d("onAttached")

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

  fun Disposable?.toViewDisposable() {
    Timber.d("toViewDisposable, viewDisposables: $viewDisposables, disposed: ${viewDisposables.isDisposed}")
    viewDisposables.takeIf { !it.isDisposed }?.add(this)
  }

  fun Disposable?.toModelDisposable() {
    Timber.d("toModelDisposable, viewDisposables: $viewDisposables, disposed: ${viewDisposables.isDisposed}")
    modelDisposables.takeIf { !it.isDisposed }?.add(this)
  }
}