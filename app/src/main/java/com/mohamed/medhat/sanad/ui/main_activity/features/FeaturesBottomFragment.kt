package com.mohamed.medhat.sanad.ui.main_activity.features

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.model.BlindMiniProfile
import com.mohamed.medhat.sanad.ui.main_activity.MainActivity
import com.mohamed.medhat.sanad.ui.persons_manager_activity.PersonsManagerActivity
import com.mohamed.medhat.sanad.utils.FRAGMENT_FEATURES_BLIND_PROFILE
import kotlinx.android.synthetic.main.fragment_main_features.view.*

/**
 * This fragment is shown in the [MainActivity] when a user profile is clicked.
 * It contains the buttons of the available features.
 */
class FeaturesBottomFragment : BottomSheetDialogFragment() {

    private lateinit var blindMiniProfile: BlindMiniProfile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        if (args != null) {
            val profile = args.getSerializable(FRAGMENT_FEATURES_BLIND_PROFILE)
            if (profile != null) {
                blindMiniProfile = profile as BlindMiniProfile
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_features, container, false)
        val extras = Bundle()
        extras.putSerializable(FRAGMENT_FEATURES_BLIND_PROFILE, blindMiniProfile)
        view.tv_features_name.text = getString(
            R.string.blind_profile_name_holder,
            blindMiniProfile.firstName,
            blindMiniProfile.lastName
        )
        // TODO populate battery percentage
        // TODO populate isOnline icon
        view.btn_features_call.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            // TODO use the real mobile number from the back-end
            intent.data = Uri.parse("tel:01063863298")
            startActivity(intent)
        }
        view.btn_features_chat.setOnClickListener {
            // TODO navigate to chat screen
            Toast.makeText(context, "Not implemented yet", Toast.LENGTH_SHORT).show()
        }
        view.btn_features_gps.setOnClickListener {
            // TODO navigate to gps screen
            Toast.makeText(context, "Not implemented yet", Toast.LENGTH_SHORT).show()
        }
        view.btn_features_people.setOnClickListener {
            startActivity(Intent(activity, PersonsManagerActivity::class.java).apply {
                putExtras(extras)
            })
        }
        return view
    }

    companion object {
        /**
         * Returns a new instance of [FeaturesBottomFragment].
         * @param blindMiniProfile The blind mini profile object that will be used in this fragment.
         */
        @JvmStatic
        fun newInstance(blindMiniProfile: BlindMiniProfile): FeaturesBottomFragment {
            val args = Bundle()
            args.putSerializable(FRAGMENT_FEATURES_BLIND_PROFILE, blindMiniProfile)
            val fragment = FeaturesBottomFragment()
            fragment.arguments = args
            return fragment
        }
    }
}