package com.mohamed.medhat.sanad.utils.generators

/**
 * Responsible for generating random file names based on the time.
 */
class PictureNameGenerator {
    companion object {
        /**
         * @return A random file name based on the time.
         */
        @JvmStatic
        fun getNewPictureName(): String {
            return System.currentTimeMillis().toString()
        }
    }
}