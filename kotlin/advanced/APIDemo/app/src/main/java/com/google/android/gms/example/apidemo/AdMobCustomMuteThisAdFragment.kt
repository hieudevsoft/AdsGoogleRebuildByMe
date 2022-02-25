/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.gms.example.apidemo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MediaContent
import com.google.android.gms.ads.MuteThisAdReason
import com.google.android.gms.ads.VideoController
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.android.gms.example.apidemo.ads.*
import kotlinx.android.synthetic.main.fragment_admob_custom_mute_this_ad.ad_container
import kotlinx.android.synthetic.main.fragment_admob_custom_mute_this_ad.btn_mute_ad
import kotlinx.android.synthetic.main.fragment_admob_custom_mute_this_ad.btn_refresh

/**
 * The [AdMobCustomMuteThisAdFragment] class demonstrates how to use custom mute
 * with AdMob native ads.
 */
class AdMobCustomMuteThisAdFragment : Fragment() {
  private lateinit var adsNativeManager: AdsNativeManager

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(
      R.layout.fragment_admob_custom_mute_this_ad, container, false
    )
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    adsNativeManager = AdsResourceFactory.newInstance(requireActivity(),AdsType.NATIVE)!! as AdsNativeManager
    adsNativeManager.setUnitAd(resources.getString(R.string.custommute_fragment_ad_unit_id))
    adsNativeManager.setLayoutAddAds(ad_container)
    adsNativeManager.setNativeAdView(layoutInflater.inflate(R.layout.native_ad, null) as NativeAdView)
    adsNativeManager.setAdsListener(object:AdsCallback{
      override fun onAdsCompleteListener() {

      }

      override fun onAdsLoadedListener() {
        adsNativeManager.showAdIfAvailable()
      }

      override fun onAdsFailedListener() {

      }

      override fun onAdsOpenedListener() {

      }

      override fun onAdsCloseListener() {

      }

      override fun onAdsClickedListener() {

      }

      override fun onAddAdsLayoutChangeListener(width: Int, height: Int) {

      }

    })
    btn_refresh.setOnClickListener { refreshAd() }
    btn_mute_ad.setOnClickListener { showMuteReasonsDialog() }

    refreshAd()
  }

  /**
   * Creates a request for a new native ad based on the boolean parameters and calls the
   * "populateNativeAdView" method when one is successfully returned.
   */
  private fun refreshAd() {
    btn_refresh.isEnabled = false
    btn_mute_ad.isEnabled = false
    adsNativeManager.initCustomNativeAdView()
    if(adsNativeManager.hasVideoContent)
    adsNativeManager.onVideoEnd = fun(){ btn_refresh.isEnabled = true } else btn_refresh.isEnabled = true
    adsNativeManager.onCustomMuteThisAd = fun(b:Boolean){
      btn_mute_ad.isEnabled = b
    }
    adsNativeManager.loadAd()
  }

  private fun showMuteReasonsDialog() {
    class MuteThisAdReasonWrapper(var reason: MuteThisAdReason) {
      override fun toString(): String {
        return reason.getDescription()
      }
    }

    val builder = AlertDialog.Builder(requireActivity())
    builder.setTitle("Select a reason")
    val reasons = adsNativeManager.getNativeAd()?.muteThisAdReasons

    val wrappedReasons = reasons!!.map { r -> MuteThisAdReasonWrapper(r) }

    builder.setAdapter(
      ArrayAdapter<MuteThisAdReasonWrapper>(
        requireActivity(),
        android.R.layout.simple_list_item_1, wrappedReasons
      )
    ) { dialog, which ->
      dialog.dismiss()
      muteAdDialogDidSelectReason(wrappedReasons[which].reason)
    }

    builder.show()
  }

  private fun muteAdDialogDidSelectReason(reason: MuteThisAdReason) {
    btn_mute_ad.isEnabled = false
    adsNativeManager.setReasonMute(reason)
    adsNativeManager.removeAdIfAvailable()
  }

}
