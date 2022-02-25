package com.google.android.gms.example.apidemo.ads

import android.view.ViewGroup
import com.google.android.gms.ads.AdSize

interface BaseAds {
    fun loadAd()
    fun showAdIfAvailable()
    fun removeAdIfAvailable()
    fun setUnitAd(unit:String){}
    fun setAdsListener(adsCallback: AdsCallback){}
    fun setLayoutAddAds(layout: ViewGroup)
    fun setSize(adSize: AdSize){

    }
}