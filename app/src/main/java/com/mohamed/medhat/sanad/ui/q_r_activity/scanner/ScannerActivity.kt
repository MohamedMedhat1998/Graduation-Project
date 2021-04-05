package com.mohamed.medhat.sanad.ui.q_r_activity.scanner

import android.os.Bundle
import android.widget.Toast
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.ui.base.BaseActivity
import com.mohamed.medhat.sanad.ui.base.network_state_awareness.NetworkStateUnaware
import kotlinx.android.synthetic.main.activity_scanner.*
import javax.inject.Inject

/**
 * An activity for scanning QR codes.
 */
class ScannerActivity : BaseActivity(), ScannerView {

    @Inject
    lateinit var scannerPresenter: ScannerPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        setNetworkStateAwareness(NetworkStateUnaware())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)
        activityComponent.inject(this)
        scannerPresenter.setView(this)
        scannerPresenter.start(savedInstanceState)
        scanner_view.setOnClickListener {
            scannerPresenter.onScannerViewClicked()
        }
    }

    override fun onResume() {
        super.onResume()
        scannerPresenter.onActivityResumed()
    }

    override fun onPause() {
        scannerPresenter.onActivityPaused()
        super.onPause()
    }
}