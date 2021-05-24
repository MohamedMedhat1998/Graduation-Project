package com.mohamed.medhat.sanad.ui.confirmation_activity

import android.widget.EditText
import androidx.core.widget.addTextChangedListener

/**
 * A helper class that handles the confirmation EditTexts.
 */
class ConfirmationHandler {
    companion object {
        /**
         * A helper function that handles the auto-jump behavior of the confirmation EditTexts.
         * @param inputFields The EditText views of the confirmation activity.
         */
        @JvmStatic
        fun handle(inputFields: List<EditText>) {
            inputFields.forEachIndexed { index, editText ->
                editText.addTextChangedListener {
                    if (it.isNullOrEmpty()) {
                        if (index != 0) {
                            inputFields[index - 1].requestFocus()
                        }
                    } else {
                        if (index != inputFields.size - 1) {
                            inputFields[index + 1].requestFocus()
                        }
                    }
                }
            }
        }
    }
}