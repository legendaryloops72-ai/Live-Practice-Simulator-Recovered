package com.livepractice.simulator

import android.app.Application
import com.google.android.gms.ads.MobileAds

class LivePracticeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Next-Gen Google Mobile Ads SDK
        MobileAds.initialize(this) {}
    }
}
