package com.mohamed.medhat.sanad.ui.q_r_activity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.local.SharedPrefs
import com.mohamed.medhat.sanad.ui.base.SimplePresenter
import com.mohamed.medhat.sanad.ui.q_r_activity.scanner.ScannerActivity
import com.mohamed.medhat.sanad.utils.NETWORK_BUY_PRODUCT_URL
import com.mohamed.medhat.sanad.utils.PERMISSION_QR_ACTIVITY_CAMERA
import com.mohamed.medhat.sanad.utils.PREFS_USER_FIRST_NAME
import javax.inject.Inject

/**
 * An mvp presenter for the [QRActivity].
 */
class QRPresenter @Inject constructor(val sharedPrefs: SharedPrefs, val context: Context) :
    SimplePresenter<QRView>() {

    private lateinit var qrView: QRView

    override fun start(savedInstanceState: Bundle?) {
        qrView.playRadarAnimation()
        qrView.updateHelloMessage(
            context.getString(
                R.string.q_r_welcome_message,
                sharedPrefs.read(PREFS_USER_FIRST_NAME)
            )
        )
    }

    override fun setView(view: QRView) {
        qrView = view
    }

    fun onScanClicked() {
        qrView.requestPermission(
            permission = Manifest.permission.CAMERA,
            message = context.getString(R.string.q_r_camera_permission_message),
            permissionCode = PERMISSION_QR_ACTIVITY_CAMERA
        ) {
            qrView.navigateTo(ScannerActivity::class.java)
        }
    }

    fun handleOnRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_QR_ACTIVITY_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                qrView.navigateTo(ScannerActivity::class.java)
            } else {
                qrView.displayToast(context.getString(R.string.permission_denied_message))
            }
        }
    }

    fun onNoDeviceClicked() {
        qrView.navigateToWebsite(NETWORK_BUY_PRODUCT_URL)
    }
}