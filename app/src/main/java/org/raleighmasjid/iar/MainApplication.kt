package org.raleighmasjid.iar

import android.app.Application
import com.onesignal.OneSignal
import dagger.hilt.android.HiltAndroidApp

const val ONESIGNAL_APP_ID = "01fcf852-7b3f-4b53-a733-4cb8241bd193"

@HiltAndroidApp
class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        // Logging set to help debug issues, remove before releasing your app.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
    }
}