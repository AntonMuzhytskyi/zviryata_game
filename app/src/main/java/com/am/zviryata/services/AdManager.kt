package com.am.zviryata.services

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

/**
 * Manages advertisement loading and display for the app.
 * Encapsulates Google Mobile Ads SDK interactions for better separation of concerns.
 *
 * @param context Application context for initializing and showing ads
 */
class AdManager(private val context: Context) {

    companion object {
        // Test ad unit IDs (replace with production IDs in a real app)
        private const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
        private const val BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
    }

    /**
     * Loads an interstitial ad asynchronously.
     *
     * @param onAdLoaded Callback invoked with the loaded ad or null if loading fails
     */
    fun loadInterstitialAd(onAdLoaded: (InterstitialAd?) -> Unit) {
        InterstitialAd.load(
            context,
            INTERSTITIAL_AD_UNIT_ID,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    onAdLoaded(ad)
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    // Log error in production (e.g., using Timber or Log)
                    onAdLoaded(null)
                }
            }
        )
    }

    /**
     * Shows an interstitial ad and handles its lifecycle.
     *
     * @param ad The loaded interstitial ad to display
     * @param onAdDismissed Callback invoked when the ad is dismissed
     * @param onAdFailed Callback invoked if the ad fails to show
     */
    fun showInterstitialAd(
        ad: InterstitialAd,
        onAdDismissed: () -> Unit,
        onAdFailed: () -> Unit
    ) {
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                onAdDismissed()
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                onAdFailed()
            }
        }
        ad.show(context as? Activity ?: return)
    }

    /**
     * Creates and configures a banner ad view.
     *
     * @return A configured AdView instance ready to be displayed
     */
    fun createBannerAdView(): AdView {
        return AdView(context).apply {
            setAdSize(AdSize.BANNER)
            adUnitId = BANNER_AD_UNIT_ID
            loadAd(AdRequest.Builder().build())
        }
    }
}