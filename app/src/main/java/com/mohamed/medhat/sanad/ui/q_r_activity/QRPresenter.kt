package com.mohamed.medhat.sanad.ui.q_r_activity

import android.os.Bundle
import com.mohamed.medhat.sanad.local.SharedPrefs
import com.mohamed.medhat.sanad.ui.base.SimplePresenter
import com.mohamed.medhat.sanad.ui.q_r_activity.scanner.ScannerActivity
import com.mohamed.medhat.sanad.utils.USER_FIRST_NAME
import javax.inject.Inject

/**
 * An mvp presenter for the [QRActivity].
 */
class QRPresenter @Inject constructor(val sharedPrefs: SharedPrefs) : SimplePresenter<QRView>() {

    private lateinit var qrView: QRView

    override fun start(savedInstanceState: Bundle?) {
        qrView.updateHelloMessage("Hello ${sharedPrefs.read(USER_FIRST_NAME)}")
    }

    override fun setView(view: QRView) {
        qrView = view
    }

    fun onScanClicked() {
        // TODO Check for the camera permission
        (qrView as QRActivity).navigateTo(ScannerActivity::class.java)
    }
}