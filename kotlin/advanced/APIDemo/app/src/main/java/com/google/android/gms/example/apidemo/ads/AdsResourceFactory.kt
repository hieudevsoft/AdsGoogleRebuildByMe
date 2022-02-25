package com.google.android.gms.example.apidemo.ads

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener

object AdsResourceFactory{
    private val builder = MobileAds.getRequestConfiguration().toBuilder()
    fun initialize(context:Context,listener:OnInitializationCompleteListener?=null) = MobileAds.initialize(context,listener)
    fun newInstance(activity: Activity,type:AdsType):BaseAds?{
        return when(type){
            AdsType.BANNER-> AdsBannerManager(activity)
            AdsType.NATIVE -> AdsNativeManager(activity)
        }
    }

    fun applyTagForChildDirectTreatmentUnspecified() = builder.setTagForChildDirectedTreatment( RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_UNSPECIFIED)
    fun applyTagForChildDirectTreatmentTrue() = builder.setTagForChildDirectedTreatment( RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE)
    fun applyTagForChildDirectTreatmentFalse() = builder.setTagForChildDirectedTreatment( RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_FALSE)
    fun applyTagForUnderTheAgeOfConsentYes() = builder.setTagForChildDirectedTreatment( RequestConfiguration.TAG_FOR_UNDER_AGE_OF_CONSENT_TRUE)
    fun applyTagForUnderTheAgeOfConsentNo() = builder.setTagForChildDirectedTreatment( RequestConfiguration.TAG_FOR_UNDER_AGE_OF_CONSENT_FALSE)
    fun applyTagForUnderTheAgeOfConsentUnspecified() = builder.setTagForChildDirectedTreatment( RequestConfiguration.TAG_FOR_UNDER_AGE_OF_CONSENT_UNSPECIFIED)
    fun applyTagForMaxAdContentRatingG() = builder.setMaxAdContentRating( RequestConfiguration.MAX_AD_CONTENT_RATING_G)
    fun applyTagForMaxAdContentRatingPG() = builder.setMaxAdContentRating( RequestConfiguration.MAX_AD_CONTENT_RATING_PG)
    fun applyTagForMaxAdContentRatingT() = builder.setMaxAdContentRating( RequestConfiguration.MAX_AD_CONTENT_RATING_T)
    fun applyTagForMaxAdContentRatingMA() = builder.setMaxAdContentRating( RequestConfiguration.MAX_AD_CONTENT_RATING_MA)
    fun applyRequestConfiguration() = MobileAds.setRequestConfiguration(builder.build())

}