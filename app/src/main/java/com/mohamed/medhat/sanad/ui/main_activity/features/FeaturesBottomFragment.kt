package com.mohamed.medhat.sanad.ui.main_activity.features

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.ui.main_activity.MainActivity
import com.mohamed.medhat.sanad.utils.FRAGMENT_FEATURES_SERIAL_NUMBER

/**
 * This fragment is shown in the [MainActivity] when a user profile is clicked.
 * It contains the buttons of the available features.
 */
class FeaturesBottomFragment : BottomSheetDialogFragment() {

    private lateinit var serialNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        if (args != null) {
            val serial = args.getString(FRAGMENT_FEATURES_SERIAL_NUMBER)
            if (serial != null) {
                serialNumber = serial
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_features, container, false)
        // TODO implement the mvp-mvvm approach to send a request to the backend server to get the blind full profile.
        Toast.makeText(context, serialNumber, Toast.LENGTH_SHORT).show()
        return view
    }

    companion object {
        /**
         * Returns a new instance of [FeaturesBottomFragment].
         * @param serialNumber The serial number of the blind associated with these features.
         */
        @JvmStatic
        fun newInstance(serialNumber: String): FeaturesBottomFragment {
            val args = Bundle()
            args.putString(FRAGMENT_FEATURES_SERIAL_NUMBER, serialNumber)
            val fragment = FeaturesBottomFragment()
            fragment.arguments = args
            return fragment
        }
    }
}