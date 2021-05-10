package com.mohamed.medhat.sanad.utils

const val BLANK = ""

// Log tags
const val TAG_REPORT_ERROR = "DEVELOPER_DEFINED_ERROR"
const val TAG_PREFS_WRITE = "PREFS_WRITE"
const val TAG_PREFS_READ = "PREFS_READ"
const val TAG_PREFS_CLEAR = "PREFS_CLEAR"
const val TAG_NETWORK_STATE_AWARENESS = "NETWORK_STATE_AWARENESS"
const val TAG_INTERNET = "Internet"
const val TAG_APP_ERROR_FAMILY = "APP_ERROR"
const val TAG_SPLASH = "SPLASH"
const val TAG_LOGIN = "LOGIN"
const val TAG_SCANNER = "SCANNER"
const val TAG_ADD_BLIND = "ADD_BLIND"
const val TAG_MARKER_ICON = "MARKER_ICON"
const val TAG_MAIN = "MAIN"

// Networking
const val NETWORK_BASE_URL = "https://sanad.azurewebsites.net/api/"
const val NETWORK_CONNECTION_TIMEOUT: Long = 30

// TODO update the website to be the buy the product website.
const val NETWORK_BUY_PRODUCT_URL = "https://www.google.com"

// Shared preferences
const val PREFS_FILE_NAME = "cache"
const val PREFS_IS_USER_CONFIRMED = "is-user-confirmed"
const val PREFS_USER_FIRST_NAME = "user-first-name"

// Permission codes
const val PERMISSION_QR_ACTIVITY_CAMERA = 1
const val PERMISSION_ADD_BLIND_ACTIVITY_CAMERA = 2

// Bundle extras
const val EXTRA_SCANNED_SERIAL = "scanned-serial"

// Gender
const val GENDER_MALE = 1
const val GENDER_FEMALE = 0

// Fragment bundles
const val FRAGMENT_FEATURES_BLIND_PROFILE = "blind-profile"

// Fragment Tags
const val TAG_FRAGMENT_FEATURES = "features-bottom-fragment"

// View Types
const val VIEW_TYPE_MAIN_BLIND_PROFILE = 1
const val VIEW_TYPE_MAIN_ADD_PROFILE = 2

// MAP
// TODO update the zoom level
const val MAP_CAMERA_ZOOM_LEVEL = 18f