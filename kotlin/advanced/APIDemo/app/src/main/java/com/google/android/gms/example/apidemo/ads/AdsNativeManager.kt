package com.google.android.gms.example.apidemo.ads

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*
import com.google.android.gms.ads.*
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.admanager.AdManagerAdView
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.android.gms.example.apidemo.R
import kotlinx.android.synthetic.main.fragment_admob_custom_mute_this_ad.*

class AdsNativeManager(private val activity: Activity):BaseAds {
    private var navtiveAd:NativeAd?=null
    private lateinit var containerAdView:ViewGroup
    private lateinit var adLoadedBuilder:AdLoader.Builder
    private lateinit var nativeAdView:NativeAdView
    private lateinit var adsCallback: AdsCallback
    private lateinit var reasonMute:MuteThisAdReason
    var hasVideoContent = false
    var onVideoEnd: (() -> Unit?)? =null
    var onCustomMuteThisAd: ((Boolean) -> Unit?)? =null

    fun getNativeAd() = navtiveAd
    fun setNativeAdView(nativeAdView: NativeAdView){
        this.nativeAdView = nativeAdView
    }
    fun getReasonMute() = navtiveAd
    fun setReasonMute(reason: MuteThisAdReason){
        this.reasonMute = reason
    }

    override fun setUnitAd(unit: String) {
        adLoadedBuilder = AdLoader.Builder(activity, unit)
    }

    private fun populateNativeAdView(
        nativeAd: NativeAd,
        adView: NativeAdView
    ) {
        adView.mediaView = adView.findViewById<MediaView>(R.id.ad_media)

        // Set other ad assets.
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        adView.priceView = adView.findViewById(R.id.ad_price)
        adView.starRatingView = adView.findViewById(R.id.ad_stars)
        adView.storeView = adView.findViewById(R.id.ad_store)
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

        (adView.headlineView as TextView).text = nativeAd.headline
        adView.mediaView.setMediaContent(nativeAd.mediaContent)

        if (nativeAd.body == null) {
            adView.bodyView.visibility = View.INVISIBLE
        } else {
            (adView.bodyView as TextView).text = nativeAd.body
            adView.bodyView.visibility = View.VISIBLE
        }

        if (nativeAd.callToAction == null) {
            adView.callToActionView.visibility = View.INVISIBLE
        } else {
            (adView.callToActionView as Button).text = nativeAd.callToAction
            adView.callToActionView.visibility = View.VISIBLE
        }

        if (nativeAd.icon == null) {
            adView.iconView.visibility = View.GONE
        } else {
            (adView.iconView as ImageView).setImageDrawable(
                nativeAd.icon.drawable
            )
            adView.iconView.visibility = View.VISIBLE
        }

        if (nativeAd.price == null) {
            adView.priceView.visibility = View.INVISIBLE
        } else {
            (adView.priceView as TextView).text = nativeAd.price
            adView.priceView.visibility = View.VISIBLE
        }

        if (nativeAd.store == null) {
            adView.storeView.visibility = View.INVISIBLE
        } else {
            (adView.storeView as TextView).text = nativeAd.store
            adView.storeView.visibility = View.VISIBLE
        }

        if (nativeAd.starRating == null) {
            adView.starRatingView.visibility = View.INVISIBLE
        } else {
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
            adView.starRatingView.visibility = View.VISIBLE
        }

        if (nativeAd.advertiser == null) {
            adView.advertiserView.visibility = View.INVISIBLE
        } else {
            (adView.advertiserView as TextView).text = nativeAd.advertiser
            adView.advertiserView.visibility = View.VISIBLE
        }

        adView.setNativeAd(nativeAd)

        val mediaContent: MediaContent = nativeAd.mediaContent

        if (mediaContent.hasVideoContent()) {
            hasVideoContent = true
            mediaContent.videoController.videoLifecycleCallbacks =
                object : VideoController.VideoLifecycleCallbacks() {
                    override fun onVideoEnd() {
                        onVideoEnd()
                        super.onVideoEnd()
                    }
                }
        } else {
           hasVideoContent = false
        }
    }

    override fun setAdsListener(adsCallback: AdsCallback) {
        this.adsCallback = adsCallback
    }

    override fun loadAd() {
        adLoadedBuilder.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                super.onAdFailedToLoad(loadAdError)
                adsCallback.onAdsFailedListener()
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                adsCallback.onAdsLoadedListener()
            }
        }).build().loadAd(AdRequest.Builder().build())
    }

    fun initCustomNativeAdView(){
        adLoadedBuilder.forNativeAd {
            navtiveAd = it
            val isCustomMuteAd = it.isCustomMuteThisAdEnabled
            onCustomMuteThisAd?.let { callback -> callback(isCustomMuteAd) }
            populateNativeAdView(it,nativeAdView)
        }
        val adOptions = NativeAdOptions.Builder()
            .setRequestCustomMuteThisAd(true)
            .build()
        adLoadedBuilder.withNativeAdOptions(adOptions)
    }

    override fun showAdIfAvailable() {
        if(navtiveAd!=null && getNativeAd()!=null){
            containerAdView.removeAllViews()
            containerAdView.addView(nativeAdView)
            val viewObserverTree = containerAdView.viewTreeObserver
            viewObserverTree.addOnPreDrawListener(object:ViewTreeObserver.OnPreDrawListener{
                override fun onPreDraw(): Boolean {
                    viewObserverTree.removeOnPreDrawListener(this)
                    adsCallback.onAddAdsLayoutChangeListener(containerAdView.measuredWidth,containerAdView.measuredHeight)
                    return true
                }

            })
        } else{
            initCustomNativeAdView()
            loadAd()
        }
    }

    override fun removeAdIfAvailable() {
        containerAdView.removeAllViews()
        if(getNativeAd()!=null && getReasonMute()!=null){
            getNativeAd()!!.muteThisAd(reasonMute)
        }
    }


    override fun setLayoutAddAds(layout: ViewGroup) {
        containerAdView = layout
    }




}