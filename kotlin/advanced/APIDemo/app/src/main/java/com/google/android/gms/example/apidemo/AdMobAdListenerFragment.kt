package com.google.android.gms.example.apidemo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.example.apidemo.ads.*
import kotlinx.android.synthetic.main.fragment_admob_ad_listener.*


/**
 * The [AdMobAdListenerFragment] demonstrates the use of the [com.google.android.gms.ads.AdListener]
 * class.
 */
class AdMobAdListenerFragment : Fragment() {
  private lateinit var ads:BaseAds
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_admob_ad_listener, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    ads = AdsResourceFactory.newInstance(requireActivity(),AdsType.BANNER)!!
    ads.apply {
      setSize(AdSize.BANNER)
      setUnitAd(getString(R.string.admob_banner_ad_unit_id))
      setLayoutAddAds(containerAdsView)
      setAdsListener(object:AdsCallback{
        override fun onAdsCompleteListener() {

        }

        override fun onAdsLoadedListener() {
          showAdIfAvailable()
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
          Log.d("AdsBannerManager", "onAddAdsLayoutChangeListener: $width $height")
        }
      })
      loadAd()
    }

  }
}
