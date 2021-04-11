package com.mohamed.medhat.sanad.ui.q_r_activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import com.mohamed.medhat.sanad.local.SharedPrefs
import com.mohamed.medhat.sanad.ui.base.SimplePresenter
import com.mohamed.medhat.sanad.ui.q_r_activity.scanner.ScannerActivity
import com.mohamed.medhat.sanad.utils.PERMISSION_QR_ACTIVITY_CAMERA
import com.mohamed.medhat.sanad.utils.PREFS_USER_FIRST_NAME
import javax.inject.Inject

/**
 * An mvp presenter for the [QRActivity].
 */
class QRPresenter @Inject constructor(val sharedPrefs: SharedPrefs) : SimplePresenter<QRView>() {

    private lateinit var qrView: QRView

    override fun start(savedInstanceState: Bundle?) {
        qrView.updateHelloMessage("Hello ${sharedPrefs.read(PREFS_USER_FIRST_NAME)}")
    }

    override fun setView(view: QRView) {
        qrView = view
    }

    fun onScanClicked() {
        qrView.requestPermission(
            permission = Manifest.permission.CAMERA,
            message = "The app wants to access the camera to scan the QR code on the device.",
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
                qrView.displayToast("Unable to proceed without the permission.")
            }
        }
    }

    fun onNoDeviceClicked() {
        // TODO update the website to be the buy the product website.
        qrView.navigateToWebsite("https://www.google.com")
    }
}