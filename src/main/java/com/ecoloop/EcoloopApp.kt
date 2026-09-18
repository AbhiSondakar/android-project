package com.ecoloop

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EcoloopApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    companion object {
        const val TAG = "ECOLOOP_APP"
    }
}
