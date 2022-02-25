package com.google.android.gms.example.apidemo.ads

interface AdsCallback {
    fun onAdsCompleteListener()
    fun onAdsLoadedListener()
    fun onAdsFailedListener()
    fun onAdsOpenedListener()
    fun onAdsCloseListener()
    fun onAdsClickedListener()
    fun onAddAdsLayoutChangeListener(width:Int,height:Int)
}