package com.example.nycschools.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities


object Utils {
    // Defining the application name
    const val APP_NAME = "NYC Schools"

    object NetworkUtils {
        // Base URL for network requests
        const val BASE_URL = "https://data.cityofnewyork.us"

        // Default message for an unknown exception
        const val UNKNOWN_EXCEPTION = "UNKNOWN EXCEPTION"
    }

    object ScreenUtils {
        // Screen names used for navigation
        const val HOME_SCREEN = "Home Screen"
        const val SCHOOL_INFO = "School Info"
    }

    object NavUtils {
        // Key for passing school DBN in navigation
        const val SCHOOL_DBN = "School dbn"
    }
}