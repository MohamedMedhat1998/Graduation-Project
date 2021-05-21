package com.mohamed.medhat.sanad.ui.on_boarding_activity.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.model.OnBoardingCard
import com.mohamed.medhat.sanad.ui.login_activity.LoginActivity
import com.mohamed.medhat.sanad.ui.on_boarding_activity.OnBoardingActivity
import com.mohamed.medhat.sanad.utils.TAG_REPORT_ERROR
import kotlinx.android.synthetic.main.fragment_on_boarding_card.view.*

private const val ON_BOARDING_CARD_KEY = "on_boarding_card_key"

/**
 * A fragment that will be used inside the [OnBoardingActivity] ViewPager.
 */
class OnBoardingCardFragment : Fragment() {

    private var onBoardingCard: OnBoardingCard? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            onBoardingCard = it.getSerializable(ON_BOARDING_CARD_KEY) as OnBoardingCard
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_on_boarding_card, container, false)
        if (onBoardingCard != null) {
            Glide.with(this)
                .load(onBoardingCard!!.imageId)
                .circleCrop()
                .into(view.iv_on_boarding_card_image)
            view.tv_on_boarding_card_text.text = onBoardingCard!!.text
        } else {
            Log.e(TAG_REPORT_ERROR, getString(R.string.null_on_boarding_card))
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param onBoardingCard the [OnBoardingCard] that contain the data.
         * @return A new instance of fragment OnBoardingCardFragment.
         */
        @JvmStatic
        fun newInstance(onBoardingCard: OnBoardingCard) =
            OnBoardingCardFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ON_BOARDING_CARD_KEY, onBoardingCard)
                }
            }
    }
}