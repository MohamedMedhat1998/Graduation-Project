package com.mohamed.medhat.sanad.ui.promotion_video_activity

import android.net.Uri
import android.os.Bundle
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.ui.base.SimplePresenter
import com.mohamed.medhat.sanad.ui.main_activity.MainActivity
import com.mohamed.medhat.sanad.utils.NETWORK_BUY_PRODUCT_URL
import kotlinx.android.synthetic.main.activity_promotion_video.*
import javax.inject.Inject

/**
 * An mvp presenter for [PromotionVideoActivity].
 */
class PromotionVideoPresenter @Inject constructor() : SimplePresenter<PromotionVideoView>() {

    private lateinit var promotionVideoView: PromotionVideoView
    private lateinit var promotionVideoActivity: PromotionVideoActivity

    override fun start(savedInstanceState: Bundle?) {
        playPromotionVideo()
    }

    override fun setView(view: PromotionVideoView) {
        promotionVideoView = view
        promotionVideoActivity = view as PromotionVideoActivity
    }

    fun onSkipClicked() {
        promotionVideoView.navigateToThenFinish(MainActivity::class.java)
    }

    fun onBuyTheDeviceClicked() {
        promotionVideoView.navigateToWebsite(NETWORK_BUY_PRODUCT_URL)
    }

    /**
     * Plays the promotion video from the raw files.
     */
    private fun playPromotionVideo() {
        val videoPath =
            "android.resource://" + promotionVideoActivity.packageName + "/" + R.raw.test
        promotionVideoActivity.vv_promotion.setVideoURI(Uri.parse(videoPath))
        promotionVideoActivity.vv_promotion.start()
    }
}