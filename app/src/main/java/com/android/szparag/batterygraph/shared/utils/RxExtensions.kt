package com.android.szparag.batterygraph.shared.utils

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 02/11/2017.
 */


import hu.akarnokd.rxjava.interop.RxJavaInterop
import io.reactivex.Completable
import io.reactivex.CompletableEmitter
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.realm.RealmModel
import io.realm.RealmResults


private val onNextStub: (Any) -> Unit = {}
private val onErrorStub: (Throwable) -> Unit = {
  throw RuntimeException(it)
}
private val onCompleteStub: () -> Unit = {}

//private val realmOnSuccessStub by lazy { OnSuccess { } }
private val COMPLETABLE_NULL_ERROR_THROWABLE_TEXT = "Completable object is null, pushing error value..."
private val OBSERVABLE_NULL_VALUE_THROWABLE_TEXT = "Observable onNext() argument is null, pushing error value..."
private val REALM_COMPLETABLE_NULL_EXCEPTION_THROWABLE_TEXT = "Realm transaction callback errored (with null exception)"
private val DISPOSABLE_ALREADY_IN_CONTAINER = "Disposable is already in given container. Throwing exception"
private val DISPOSABLE_CONTAINER_IS_NULL = "Disposable container is null. Throwing exception"
val DISPOSABLE_ALREADY_IN_CONTAINER_THROWABLE: Throwable by lazy {
  Throwable(
      DISPOSABLE_ALREADY_IN_CONTAINER)
}
val DISPOSABLE_CONTAINER_NULL_THROWABLE: Throwable by lazy {
  Throwable(
      DISPOSABLE_CONTAINER_NULL_THROWABLE)
}


//inline fun RecyclerView.scrollEvents(linearLayoutManager: LinearLayoutManager): Flowable<ListScrollEvent> {
//
//}

fun <E : RealmModel> RealmResults<E>.asFlowable() = RxJavaInterop.toV2Flowable(
    this.asObservable()).map { realmResults -> realmResults.toList() }

//fun <E : RealmModel> E.asFlowable() = RxJavaInterop.toV2Flowable(
//    this<E>()).map { realmResults -> realmResults.toList() }
//fun <E : RealmModel> RealmResults<E>.asFlowable() = RxJavaInterop.toV2Flowable(
//    this.asObservable()).map { realmResults -> realmResults.toList() }

fun CompositeDisposable.add(disposable: Disposable?): Boolean {
  disposable?.let { this.add(disposable); return true }
  return false
}

//fun <E : RealmModel> RealmResults<E>.asFlowable(): Flowable<List<E>> {
//  return RxJavaInterop.toV2Flowable(
//      this.asObservable()).map { realmResults -> realmResults.toList() }
//}

/**
 * Extension function that pushes error value from Completable object into stream,
 * even if the object itself is completely nulled out in the moment of execution.
 */
fun Completable?.nonNull()
    = this ?: Completable.error { Throwable(COMPLETABLE_NULL_ERROR_THROWABLE_TEXT) }

fun CompletableEmitter.safeOnError(throwable: Throwable?) =
    this.onError(throwable ?: Throwable(REALM_COMPLETABLE_NULL_EXCEPTION_THROWABLE_TEXT))

//fun <T, R> Observable<T>.flatMap(mapper: Function<>) {
//
//}

fun <T> Observable<T>.flatMap(completable: Completable, onComplete: () -> Unit = onCompleteStub,
    onError: (Throwable) -> Unit =
    onErrorStub):
    Observable<T> {
//  var disposable: Disposable? = null
  return this.doOnNext {
    //    disposable =
    completable
        .subscribe()
//            .subscribeBy (
//        onComplete = {
////          disposable?.takeIf {it.isDisposed}?.dispose()
//          com.android.szparag.todoist.utils.onCompleteStub.invoke()
//        },
//        onError = { exc ->
////          disposable?.takeIf {it.isDisposed}?.dispose()
//          com.android.szparag.todoist.utils.onErrorStub.invoke(exc)
//        }
//    )
  }
}

//fun Realm.executeTransactionAsyncBy(transaction: Transaction, onSuccess: OnSuccess = OnSuccess {},
//    onError: OnError = OnError {}) {
//  this.executeTransactionAsync(transaction, onSuccess, onError)
//}

//fun <T : Any> Observable<T>.subscribe(
//    onNext: (T) -> Unit = onNextStub,
//    onError: (Throwable) -> Unit = onErrorStub,
//    onComplete: () -> Unit = onCompleteStub
//): Disposable = subscribe(onNext, onError, onComplete)
//
//
//fun Completable.subscribe(
//    onError: (Throwable) -> Unit = onErrorStub,
//    onComplete: () -> Unit = onCompleteStub
//): Disposable {
//  this.cl
//}
//
//
//fun <T : Any> Flowable<T>.subscribe(
//    onNext: (T) -> Unit = onNextStub,
//    onError: (Throwable) -> Unit = onErrorStub,
//    onComplete: () -> Unit = onCompleteStub
//): Disposable = subscribe(onNext, onError, onComplete)


//fun itemSelections(view: Spinner): Observable<String> {
//  ifNotNull(view, {"view == null"})
//  return SpinnerSelectionObservable(view)
//}

fun Completable.ui() = this.subscribeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.ui() = this.subscribeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.ui() = this.subscribeOn(AndroidSchedulers.mainThread())

fun <T> Flowable<T>.ui() = this.subscribeOn(AndroidSchedulers.mainThread())

fun Completable.single() = this.subscribeOn(Schedulers.single())

fun <T> Observable<T>.single() = this.subscribeOn(Schedulers.single())

fun <T> Flowable<T>.single() = this.subscribeOn(Schedulers.single())

fun <T> Observable<T>.computation() = this.subscribeOn(Schedulers.computation())

fun Completable.computation() = this.subscribeOn(Schedulers.computation())

fun ui() = AndroidSchedulers.mainThread()

//yo dawg, i herd u like observabelz
fun <E : RealmModel> RealmResults<E>.toObservable() = this.asObservable().toObservable()

fun <E> rx.Observable<E>.toObservable() = RxJavaInterop.toV2Observable(this)
