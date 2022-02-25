package com.google.android.gms.example.apidemo.ads

import android.app.Activity
import android.util.Log
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerAdView

class AdsBannerManager(private val activity: Activity):BaseAds {
    val TAG = "AdsBannerManager"
    private var adManagerAdView: AdManagerAdView?=null
    private lateinit var adsCallback: AdsCallback
    private lateinit var containerAdsView:ViewGroup
    private lateinit var mAdListener: AdListener
    private lateinit var adRequest: AdManagerAdRequest
    private lateinit var adSize: AdSize
    private lateinit var adUnit: String
    private var isLoadingSuccessfully = false
    init {
        adManagerAdView = AdManagerAdView(activity)
        mAdListener = object :AdListener(){
            override fun onAdClosed() {
                super.onAdClosed()
                adsCallback.onAdsCloseListener()
                Log.d(TAG, "onAdClosed")
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                adsCallback.onAdsFailedListener()
                Log.d(TAG, "onAdFailedToLoad")
                isLoadingSuccessfully = false
            }

            override fun onAdOpened() {
                super.onAdOpened()
                adsCallback.onAdsOpenedListener()
                Log.d(TAG, "onAdOpened")
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                Log.d(TAG, "onAdLoaded")
                isLoadingSuccessfully = true
                adsCallback.onAdsLoadedListener()
            }

            override fun onAdClicked() {
                super.onAdClicked()
                adsCallback.onAdsClickedListener()
                Log.d(TAG, "onAdClicked")
            }
        }
        adRequest = AdManagerAdRequest.Builder().build()
    }

    override fun loadAd() {
        if(adManagerAdView==null) {
            adManagerAdView = AdManagerAdView(activity)
            setSize(adSize)
            setUnitAd(adUnit)
        }
            adManagerAdView!!.apply {
                adListener = mAdListener
                loadAd(adRequest)
            }
    }

    override fun showAdIfAvailable() {
        if(!isLoadingSuccessfully) {
            loadAd()
            return
        }
        containerAdsView.addView(adManagerAdView)
        val viewObserverTree = containerAdsView.viewTreeObserver
        viewObserverTree.addOnPreDrawListener(object:ViewTreeObserver.OnPreDrawListener{
            override fun onPreDraw(): Boolean {
                viewObserverTree.removeOnPreDrawListener(this)
                adsCallback.onAddAdsLayoutChangeListener(containerAdsView.measuredWidth,containerAdsView.measuredHeight)
                return true
            }

        })
    }

    override fun removeAdIfAvailable() {
        if(isLoadingSuccessfully&&adManagerAdView!=null){
            containerAdsView.removeView(adManagerAdView)
            adManagerAdView!!.destroy()
            adManagerAdView = null
        }
    }

    fun onResume(){
        if(isLoadingSuccessfully&&adManagerAdView!=null){
            adManagerAdView!!.resume()
        }
    }

    fun onPause(){
        if(isLoadingSuccessfully&&adManagerAdView!=null){
            adManagerAdView!!.pause()
        }
    }

    override fun setUnitAd(unit: String) {
        this.adUnit = unit
        if(adManagerAdView!=null)
        adManagerAdView!!.adUnitId = unit
    }

    override fun setAdsListener(adsCallback: AdsCallback) {
        this.adsCallback = adsCallback
    }

    override fun setLayoutAddAds(layout: ViewGroup) {
        containerAdsView = layout
    }

    override fun setSize(adSize: AdSize) {
        Log.d(TAG, "setSize: ${adSize.height}")
        this.adSize = adSize
        if(adManagerAdView!=null)
        adManagerAdView!!.adSize = adSize
    }
}