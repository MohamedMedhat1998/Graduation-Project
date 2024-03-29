package com.mohamed.medhat.sanad.ui.q_r_activity

import android.animation.ValueAnimator
import android.os.Bundle
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.ui.base.BaseActivity
import com.mohamed.medhat.sanad.ui.base.network_state_awareness.NetworkStateUnaware
import kotlinx.android.synthetic.main.activity_q_r.*
import javax.inject.Inject

/**
 * An activity for the QR screen that should scan the QR on the hardware device.
 */
class QRActivity : BaseActivity(), QRView {

    @Inject
    lateinit var qrPresenter: QRPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        setNetworkStateAwareness(NetworkStateUnaware())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_q_r)
        activityComponent.inject(this)
        qrPresenter.setView(this)
        qrPresenter.start(savedInstanceState)
        iv_q_r_scan.setOnClickListener {
            qrPresenter.onScanClicked()
        }
        btn_q_r_no_device.setOnClickListener {
            qrPresenter.onNoDeviceClicked()
        }
    }

    override fun updateHelloMessage(message: String) {
        tv_q_r_welcome.text = message
    }

    override fun playRadarAnimation() {
        val alphaAnimation = ValueAnimator.ofFloat(1f, 0f)
        alphaAnimation.addUpdateListener {
            iv_q_r_radar.alpha = it.animatedValue as Float
        }
        val scaleAnimation = ValueAnimator.ofFloat(1f, 1.17f)
        scaleAnimation.addUpdateListener {
            iv_q_r_radar.scaleX = it.animatedValue as Float
            iv_q_r_radar.scaleY = it.animatedValue as Float
        }
        alphaAnimation.apply {
            repeatCount = ValueAnimator.INFINITE
            duration = 1200
        }
        scaleAnimation.apply {
            repeatCount = ValueAnimator.INFINITE
            duration = 1200
        }
        alphaAnimation.start()
        scaleAnimation.start()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        qrPresenter.handleOnRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}