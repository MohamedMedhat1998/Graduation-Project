package com.mohamed.medhat.sanad.ui.promotion_video_activity

import android.os.Bundle
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.ui.base.BaseActivity
import com.mohamed.medhat.sanad.ui.base.network_state_awareness.NetworkStateUnaware
import kotlinx.android.synthetic.main.activity_promotion_video.*
import javax.inject.Inject

/**
 * An activity containing the promotion video of the device.
 */
class PromotionVideoActivity : BaseActivity(), PromotionVideoView {

    @Inject
    lateinit var promotionVideoPresenter: PromotionVideoPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        setNetworkStateAwareness(NetworkStateUnaware())
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promotion_video)
        activityComponent.inject(this)
        promotionVideoPresenter.setView(this)
        promotionVideoPresenter.start(savedInstanceState)
        btn_promotion_skip.setOnClickListener {
            promotionVideoPresenter.onSkipClicked()
        }
        btn_promotion_buy_device.setOnClickListener {
            promotionVideoPresenter.onBuyTheDeviceClicked()
        }
    }
}