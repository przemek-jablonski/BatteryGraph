package com.android.szparag.batterygraph.base.views

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.android.szparag.batterygraph.base.presenters.Presenter
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 02/11/2017.
 */
abstract class BatteryGraphBaseActivity<P : Presenter<*>> : AppCompatActivity(), View {

  @Inject lateinit open var presenter: P //todo: close and private this somehow

  @CallSuper
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Timber.d("onCreate, savedInstanceState: $savedInstanceState")
  }

  @CallSuper
  override fun onStart() {
    super.onStart()
    Timber.d("onStart")
  }

  @CallSuper
  override fun onStop() {
    super.onStop()
    Timber.d("onStop")
  }

  //todo this has to come back
//  @CallSuper
//  override fun setupViews() {
//    Log.d("asd","setupViews")
//  }

//  override final fun onWindowFocusChanged(hasFocus: Boolean) {
//    Log.d("asd","onWindowFocusChanged, hasFocus: $hasFocus, windowFocusCache: $windowFocusCache")
//    super.onWindowFocusChanged(hasFocus)
//    if (windowFocusCache != hasFocus) viewReadySubject.onNext(hasFocus)
//    windowFocusCache = hasFocus
//  }
//
//  override final fun subscribeOnViewReady(): Observable<Boolean> {
//    Log.d("asd","subscribeOnViewReady")
//    return viewReadySubject
//  }

  //todo: this has to be fixed
//  override final fun gotoScreen(targetScreen: Screen) {
//    Log.d("asd","gotoScreen, targetScreen: $targetScreen")
//
//  }

//  override final fun goToDayScreen(unixTimestamp: UnixTimestamp) {
//    startActivity(TodoistDayActivity.prepareDayActivityRoutingIntent(packageContext = this, dayUnixTimestamp = unixTimestamp))
//  }

//  override fun resolveStartupData() {
//    Log.d("asd","resolveStartupData")
//  }
//
//  override final fun checkPermissions(vararg permissions: PermissionType) {
//    Log.d("asd","checkPermissions, permissions: $permissions")
//    permissions.forEach {
//      val permissionResponseInt = checkSelfPermission(permissionTypeToString(it))
//      val permissionResponseToType = permissionResponseToType(permissionResponseInt)
//      val permissionEvent = PermissionEvent(it, permissionResponseToType)
//      permissionsSubject.onNext(permissionEvent)
//    }
//  }
//
//  override final fun requestPermissions(vararg permissions: PermissionType) {
//    Log.d("asd","requestPermissions, permissions: $permissions")
//    requestPermissions(permissions.map(this::permissionTypeToString).toTypedArray(), requestCode())
//  }
//
//  override final fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//    Log.d("asd","onRequestPermissionsResult, requestCode: $requestCode, permissions: $permissions, results: $grantResults")
//  }
//
//  override fun renderUserAlertMessage(userAlertMessage: UserAlertMessage) {
//    Log.d("asd","renderUserAlertMessage, alert: $userAlertMessage")
//  }
//
//  override fun stopRenderUserAlertMessage(userAlertMessage: UserAlertMessage) {
//    Log.d("asd","stopRenderUserAlertMessage, alert: $userAlertMessage")
//    defaultUserAlert?.dismiss() //omitting userAlertMessage check, since Snackbars can be dismissed manually any time.
//  }
//
//  override final fun subscribeForPermissionsChange(): Observable<PermissionEvent> {
//    Log.d("asd","requestPermissions")
//    return permissionsSubject
//  }
//
//  private fun permissionTypeToString(permissionType: PermissionType): String {
//    Log.d("asd","permissionTypeToString, permissionType: $permissionType")
//    return emptyString()
//  }
//
//  private fun permissionStringToType(permissionString: String): PermissionType {
//    Log.d("asd","permissionStringToType, permissionString: $permissionString")
//    return NULL
//  }
//
//  private fun permissionResponseToType(permissionResponseInt: Int): PermissionResponse {
//    Log.d("asd","permissionResponseToType, permissionResponseInt: $permissionResponseInt")
//    return PERMISSION_DENIED
//  }

  private fun requestCode() = Math.abs(this.packageName.hashCode())

}