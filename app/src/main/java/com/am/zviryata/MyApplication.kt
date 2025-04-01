package com.am.zviryata

import android.app.Application
import com.google.android.gms.ads.MobileAds

/*
 * Copyright (c) 2025-Present, Anton Muzhytskyi
 * All rights reserved.
 *
 * This code is developed and owned by Anton Muzhytskyi.
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 */

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
    }
}