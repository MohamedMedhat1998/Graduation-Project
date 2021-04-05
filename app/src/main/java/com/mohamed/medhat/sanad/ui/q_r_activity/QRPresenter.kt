package com.mohamed.medhat.sanad.ui.q_r_activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import com.mohamed.medhat.sanad.local.SharedPrefs
import com.mohamed.medhat.sanad.ui.base.SimplePresenter
import com.mohamed.medhat.sanad.ui.q_r_activity.scanner.ScannerActivity
import com.mohamed.medhat.sanad.utils.QR_ACTIVITY_CAMERA_PERMISSION
import com.mohamed.medhat.sanad.utils.USER_FIRST_NAME
import javax.inject.Inject

/**
 * An mvp presenter for the [QRActivity].
 */
class QRPresenter @Inject constructor(val sharedPrefs: SharedPrefs) : SimplePresenter<QRView>() {

    private lateinit var qrView: QRView
    private lateinit var qrActivity: QRActivity

    override fun start(savedInstanceState: Bundle?) {
        qrView.updateHelloMessage("Hello ${sharedPrefs.read(USER_FIRST_NAME)}")
    }

    override fun setView(view: QRView) {
        qrView = view
        qrActivity = qrView as QRActivity
    }

    fun onScanClicked() {
        qrActivity.requestPermission(
            permission = Manifest.permission.CAMERA,
            message = "The app wants to access the camera to scan the QR code on the device.",
            permissionCode = QR_ACTIVITY_CAMERA_PERMISSION
        ) {
            qrActivity.navigateTo(ScannerActivity::class.java)
        }
    }

    fun handleOnRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == QR_ACTIVITY_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                qrActivity.navigateTo(ScannerActivity::class.java)
            } else {
                qrView.displayToast("Unable to proceed without the permission.")
            }
        }
    }
}