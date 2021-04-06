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

// Networking
const val NETWORK_BASE_URL = "https://sanad.azurewebsites.net/api/"
const val NETWORK_CONNECTION_TIMEOUT: Long = 30

// Shared preferences
const val PREFS_FILE_NAME = "cache"
const val PREFS_IS_USER_CONFIRMED = "is-user-confirmed"
const val PREFS_IS_MENTORING_SOMEONE = "is-mentoring-someone"
const val PREFS_USER_FIRST_NAME = "user-first-name"

// Permission codes
const val PERMISSION_QR_ACTIVITY_CAMERA = 1

// Bundle extras
const val EXTRA_SCANNED_SERIAL = "scanned-serial"