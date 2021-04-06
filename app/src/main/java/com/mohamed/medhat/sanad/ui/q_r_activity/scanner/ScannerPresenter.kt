package com.mohamed.medhat.sanad.ui.q_r_activity.scanner

import android.os.Bundle
import android.util.Log
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.mohamed.medhat.sanad.ui.add_blind_activity.AddBlindActivity
import com.mohamed.medhat.sanad.ui.base.SimplePresenter
import com.mohamed.medhat.sanad.utils.EXTRA_SCANNED_SERIAL
import com.mohamed.medhat.sanad.utils.TAG_SCANNER
import kotlinx.android.synthetic.main.activity_scanner.*
import javax.inject.Inject

/**
 * An mvp presenter for the [ScannerActivity].
 */
class ScannerPresenter @Inject constructor() : SimplePresenter<ScannerView>() {

    private lateinit var scannerView: ScannerView
    private lateinit var codeScanner: CodeScanner
    private lateinit var scannerActivity: ScannerActivity

    override fun start(savedInstanceState: Bundle?) {
        initScanner()
    }

    override fun setView(view: ScannerView) {
        scannerView = view
        scannerActivity = scannerView as ScannerActivity
    }

    /**
     * Initializes the scanner object for scanning QR codes.
     */
    private fun initScanner() {
        codeScanner = CodeScanner(scannerActivity, scannerActivity.scanner_view)
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            decodeCallback = DecodeCallback {
                scannerActivity.runOnUiThread {
                    onScanSuccess(it.text)
                }
            }
            errorCallback = ErrorCallback {
                scannerActivity.runOnUiThread {
                    onScanError(it.message.toString())
                }
            }
        }
    }

    /**
     * Handles what to do with the scanned text.
     * @param scannedText the result of the scanning operation.
     */
    private fun onScanSuccess(scannedText: String) {
        Log.d(TAG_SCANNER, "Scanned: $scannedText")
        val extras = Bundle()
        extras.putString(EXTRA_SCANNED_SERIAL, scannedText)
        scannerView.displayToast("Successfully scanned the QR code!")
        scannerView.navigateTo(AddBlindActivity::class.java, extras)
    }

    /**
     * Handles the errors for the scanner.
     * @param error the error message.
     */
    private fun onScanError(error: String) {
        // TODO update error message
        scannerView.displayToast(error)
    }

    fun onScannerViewClicked() {
        codeScanner.startPreview()
    }

    fun onActivityResumed() {
        codeScanner.startPreview()
    }

    fun onActivityPaused() {
        codeScanner.releaseResources()
    }
}