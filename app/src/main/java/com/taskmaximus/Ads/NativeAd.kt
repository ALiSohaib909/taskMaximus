package com.taskmaximus.Ads

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.ads.*
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.taskmaximus.R
import com.taskmaximus.Ads.NativeMaster.nativeAdMobHashMap

object NativeAd {
    private val logTag = "Ads_"

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun adNativeAd(
        mContext: Activity?,
        isMedia: Boolean,
        adContainer: CardView?,
        isMediumAd: Boolean = false,
        onfailed: () -> Unit
    ) {
        Log.d(logTag, "exit nativeAdMobHashMap" + nativeAdMobHashMap!!.size)
        lateinit var defaultAdviewMedia: NativeAdView
        lateinit var defaultAdviewBanner: NativeAdView
        if (isMedia) {
            defaultAdviewMedia = if (isMediumAd) {
                mContext!!.layoutInflater.inflate(
                    R.layout.native_ad_admob_layout_large_medium,
                    null
                ) as NativeAdView
            } else {
                mContext!!.layoutInflater.inflate(
                    R.layout.native_ad_admob_layout_large_normal,
                    null
                ) as NativeAdView
            }
        } else {
            defaultAdviewBanner = if (isMediumAd) {
                mContext!!.layoutInflater.inflate(
                    R.layout.native_ad_admob_layout_banner_small,
                    null
                ) as NativeAdView
            } else {
                mContext!!.layoutInflater.inflate(
                    R.layout.native_ad_admob_layout_banner_normal,
                    null
                ) as NativeAdView
            }
        }
        if (NetworkCheck.isNetworkAvailable(mContext)){

            adContainer!!.visibility = View.VISIBLE
            if (nativeAdMobHashMap!!.containsKey(mContext::class.java.name)) {
                val nativeAd: NativeAd? = nativeAdMobHashMap!![mContext::class.java.name]
                Log.d(logTag, "Previous ad is loaded")
                if (isMedia) {
                    populateMediaNative(nativeAd!!, defaultAdviewMedia)
                    adContainer.addView(defaultAdviewMedia)
                } else {
                    populateBannerNative(nativeAd!!, defaultAdviewBanner)
                    adContainer.addView(defaultAdviewBanner)
                }
            } else {
                if (isMedia) {
                    reloadAd(mContext, adContainer, isMedia, defaultAdviewMedia, isMediumAd)
                } else {
                    reloadAd(mContext, adContainer, isMedia, defaultAdviewBanner, isMediumAd)
                }

            }

        }else{
            onfailed()
        }
    }

    private fun reloadAd(
        mContext: Activity?,
        frameLayout: CardView,
        isMedia: Boolean,
        defaultAdview: NativeAdView,
        isMediumAd: Boolean
    ) {
        val builder: AdLoader.Builder =
            AdLoader.Builder(mContext!!, mContext.getString(R.string.admob_native))
        frameLayout.visibility = View.VISIBLE

        builder.forNativeAd { mNativeAd: NativeAd ->
            nativeAdMobHashMap!![mContext::class.java.name] = mNativeAd
            Log.d("Ads_", "native_ad")

            if (isMedia) {
                populateMediaNative(mNativeAd, defaultAdview)
            } else {
                populateBannerNative(mNativeAd, defaultAdview)
            }

            frameLayout.addView(defaultAdview)
        }
        val videoOptions: VideoOptions = VideoOptions.Builder()
            .setStartMuted(false)
            .build()
        val adOptions = NativeAdOptions.Builder()
            .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT)
            .setVideoOptions(videoOptions)
            .build()
        builder.withNativeAdOptions(adOptions)
        val adLoader: AdLoader = builder.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(errorCode: LoadAdError) {
//                frameLayout.setVisibility(View.GONE);
                Log.d("Ads_", "Admob Native Failed to Load.")
                if (nativeAdMobHashMap!!.containsKey(mContext::class.java.name)) {
                    nativeAdMobHashMap!!.remove(mContext::class.java.name)
                }


            }

            override fun onAdClicked() {
                super.onAdClicked()
                if (nativeAdMobHashMap!!.containsKey(mContext::class.java.name)) {
                    nativeAdMobHashMap!!.remove(mContext::class.java.name)
                }

            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                Log.d("Ads_", "Admob Native Loaded..")
                frameLayout.visibility = View.VISIBLE
            }
        }).build()
        adLoader.loadAd(AdRequest.Builder().build())
    }

    fun populateMediaNative(nativeAd: NativeAd, adView: NativeAdView) {


        // Set other ad assets.
        adView.headlineView = adView.findViewById(R.id.adHeadline)
        adView.bodyView = adView.findViewById(R.id.adBody)
        adView.callToActionView = adView.findViewById(R.id.adCallToAction)
        adView.iconView = adView.findViewById(R.id.adAppIcon)
        adView.mediaView = adView.findViewById<View>(R.id.adMedia) as MediaView
        // The headline and mediaContent are guaranteed to be in every NativeAd.
        (adView.headlineView as TextView).text = nativeAd.headline
        (adView.findViewById<View>(R.id.adMedia) as MediaView).setOnHierarchyChangeListener(object :
            ViewGroup.OnHierarchyChangeListener {
            override fun onChildViewAdded(parent: View, child: View) {
                if (child is ImageView) {
                    child.scaleType = ImageView.ScaleType.FIT_XY
                }
            }

            override fun onChildViewRemoved(view: View, view1: View) {}
        })
        adView.mediaView!!.setMediaContent(nativeAd.mediaContent!!)

        // These assets aren't guaranteed to be in every NativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.body == null) {
            adView.bodyView!!.visibility = View.INVISIBLE
        } else {
            adView.bodyView!!.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }
        if (nativeAd.callToAction == null) {
            adView.callToActionView!!.visibility = View.INVISIBLE
        } else {
            adView.callToActionView!!.visibility = View.VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction
        }
        if (nativeAd.icon == null) {
            adView.iconView!!.visibility = View.GONE

        } else {
            (adView.iconView as ImageView).setImageDrawable(
                nativeAd.icon!!.drawable
            )
            adView.iconView!!.visibility = View.VISIBLE
        }


        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd)
        adView.visibility = View.VISIBLE

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        val vc: VideoController = nativeAd.mediaContent!!.videoController

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {


            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.videoLifecycleCallbacks = object : VideoController.VideoLifecycleCallbacks() {
            }
        }
    }

    private fun populateBannerNative(nativeAd: NativeAd, adView: NativeAdView) {


        // Set other ad assets.
        adView.headlineView = adView.findViewById(R.id.adHeadline)
        adView.bodyView = adView.findViewById(R.id.adBody)
        adView.callToActionView = adView.findViewById(R.id.adCallToAction)
        adView.iconView = adView.findViewById(R.id.adAppIcon)
        //        adView.setMediaView((MediaView) adView.findViewById(R.id.adMedia));
        // The headline and mediaContent are guaranteed to be in every NativeAd.
        (adView.headlineView as TextView).text = nativeAd.headline
        /* ((MediaView) adView.findViewById(R.id.adMedia)).setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                if (child instanceof ImageView){
                    ((ImageView) child).setScaleType(ImageView.ScaleType.CENTER_CROP);
                }

            }

            @Override
            public void onChildViewRemoved(View view, View view1) {

            }
        });
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());*/

        // These assets aren't guaranteed to be in every NativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.body == null) {
            adView.bodyView!!.visibility = View.INVISIBLE
        } else {
            adView.bodyView!!.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }
        if (nativeAd.callToAction == null) {
            adView.callToActionView!!.visibility = View.INVISIBLE
        } else {
            adView.callToActionView!!.visibility = View.VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction
        }
        if (nativeAd.icon == null) {

            adView.iconView!!.visibility = View.GONE
        } else {
            (adView.iconView as ImageView).setImageDrawable(
                nativeAd.icon!!.drawable
            )
            adView.findViewById<CardView>(R.id.adIconCard).visibility= View.VISIBLE
            adView.iconView!!.visibility = View.VISIBLE
        }


        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd)
        adView.visibility = View.VISIBLE
    }
}