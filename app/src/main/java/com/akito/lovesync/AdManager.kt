package com.akito.lovesync

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

object AdManager {
    private var interstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedAd? = null
    private var interstitialCounter = 0

    // Production Ad Unit IDs
    private const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-8950375321788767/9665646760"
    private const val REWARDED_AD_UNIT_ID = "ca-app-pub-8950375321788767/8465765643"

    fun loadInterstitial(context: Context) {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context, INTERSTITIAL_AD_UNIT_ID, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) {
                interstitialAd = ad
            }
            override fun onAdFailedToLoad(adError: LoadAdError) {
                interstitialAd = null
            }
        })
    }

    fun showInterstitial(activity: Activity, onAdClosed: () -> Unit) {
        interstitialCounter++
        // 2回に1回の頻度で表示 (奇数回目はスキップ)
        if (interstitialCounter % 2 != 0) {
            onAdClosed()
            return
        }

        val mainActivity = activity as? MainActivity

        if (interstitialAd != null) {
            interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    loadInterstitial(activity)
                    mainActivity?.resumeBgm()
                    onAdClosed()
                }
                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    interstitialAd = null
                    mainActivity?.resumeBgm()
                    onAdClosed()
                }
            }
            mainActivity?.pauseBgm()
            interstitialAd?.show(activity)
        } else {
            onAdClosed()
        }
    }

    fun loadRewarded(context: Context) {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(context, REWARDED_AD_UNIT_ID, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdLoaded(ad: RewardedAd) {
                rewardedAd = ad
            }
            override fun onAdFailedToLoad(adError: LoadAdError) {
                rewardedAd = null
            }
        })
    }

    fun showRewarded(activity: Activity, onRewardEarned: () -> Unit) {
        val ad = rewardedAd
        val mainActivity = activity as? MainActivity
        
        if (ad != null) {
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    rewardedAd = null
                    loadRewarded(activity)
                    mainActivity?.resumeBgm()
                }
                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    rewardedAd = null
                    loadRewarded(activity)
                    mainActivity?.resumeBgm()
                }
            }
            mainActivity?.pauseBgm()
            ad.show(activity) {
                onRewardEarned()
            }
        }
    }

    fun isRewardedAdLoaded(): Boolean = rewardedAd != null
}
