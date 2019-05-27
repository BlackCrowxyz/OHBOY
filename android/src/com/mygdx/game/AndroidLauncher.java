package com.mygdx.game;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.ads.consent.ConsentForm;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class AndroidLauncher extends AndroidApplication implements AdsController{
	//private static final String REWARDED_VIDEO_AD_UNIT_ID = "ca-app-pub-3168308579664800/4586008585"; //mine
	private static final String INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"; //video - ca-app-pub-3940256099942544/8691691433 // ca-app-pub-3940256099942544/1033173712
	private static final String OHBOY_ADMOB_UNIT_ID = "ca-app-pub-3168308579664800~4586008585"; //mine

	private InterstitialAd mInterstitialAd;

	private ConsentForm form;
	private GdprHelper gdprHelper;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		// Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
		MobileAds.initialize(this, OHBOY_ADMOB_UNIT_ID);

		gdprHelper = new GdprHelper(this);
		gdprHelper.initialise();

		System.out.println("HHHHHHHHHHHHEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");

		//requestConsentInfo();
		//displayConsentForm();

		setupAd();

		initialize(new CubeGame(this), config);

		// In KITKAT (4.4) and next releases, hide the virtual buttons
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			hideVirtualButtons();
		}
	}

//	private void displayConsentForm() {
//		URL privacyUrl = null;
//		try {
//			// TODO: Replace with your app's privacy policy URL.
//			System.out.println("******************** HELLO");
//			privacyUrl = new URL("https://www.your.com/privacyurl");
//		} catch (MalformedURLException e) {
//			System.out.println("******************** BYE");
//			e.printStackTrace();
//			// Handle error.
//		}
//		form = new ConsentForm.Builder(getContext(), privacyUrl)
//				.withListener(new ConsentFormListener() {
//					@Override
//					public void onConsentFormLoaded() {
//						// Consent form loaded successfully.
//						System.out.println("******************** Consent form loaded successfully.");
//						form.show();
//					}
//
//					@Override
//					public void onConsentFormOpened() {
//						// Consent form was displayed.
//						System.out.println("******************** Consent form was displayed.");
//					}
//
//					@Override
//					public void onConsentFormClosed(ConsentStatus consentStatus, Boolean userPrefersAdFree) {
//						// Consent form was closed.
//
//						System.out.println("******************** Consent form was closed.");
//						//?
//						Bundle extras = new Bundle();
//						extras.putString("npa", "1");
//
//						AdRequest request = new AdRequest.Builder()
//								.addNetworkExtrasBundle(AdMobAdapter.class, extras)
//								.build();
//
//						// Consent form was closed. This callback method contains all the data about user's selection, that you can use.
//						if (userPrefersAdFree) {
//							redirectToPaidVersion();
//						}
//
//						hideVirtualButtons();
//
//					}
//
//					@Override
//					public void onConsentFormError(String errorDescription) {
//						// Consent form error.
//						System.out.println("******************** Consent form error.");
//						System.out.println(errorDescription);
//
//						// Consent form error. Would be nice to have proper error logging. Happens also when user has no internet connection
//						Toast.makeText(getContext(), errorDescription, Toast.LENGTH_LONG).show();
//					}
//				})
//				.withPersonalizedAdsOption()
//				.withNonPersonalizedAdsOption()
//				.withAdFreeOption()
//				.build();
//		form.load();
//	}

//	//TODO: Reset user consent again in setting tabe of the game
//	// Resets the consent. User will be again displayed the consent form on next call of initialise method
//	public void resetConsent() {
//		ConsentInformation consentInformation = ConsentInformation.getInstance(getContext());
//		consentInformation.reset();
//	}

//	private void requestConsentInfo() {
//		ConsentInformation consentInformation = ConsentInformation.getInstance(getContext());
//
//		//DEBUG LINES
//		consentInformation.setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_EEA); 		//EEA
//		//consentInformation.setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_NOT_EEA);   //NOT_EEA
//
//		//For under Aged people in Europe
//		//consentInformation.setTagForUnderAgeOfConsent(true);
//
//		String[] publisherIds = {"pub-0123456789012345"};
//		consentInformation.requestConsentInfoUpdate(publisherIds, new ConsentInfoUpdateListener() {
//			@Override
//			public void onConsentInfoUpdated(ConsentStatus consentStatus) {
//				// User's consent status successfully updated.
//				System.out.println("******* User's consent status successfully updated. consent status = " + consentStatus.name());
//				System.out.println("******* " + ConsentInformation.getInstance(getContext()).isRequestLocationInEeaOrUnknown());
//				if (consentStatus == ConsentStatus.UNKNOWN) {
//					displayConsentForm();
//				}
//			}
//
//			@Override
//			public void onFailedToUpdateConsentInfo(String errorDescription) {
//				// User's consent status failed to update.
//				System.out.println("******* User's consent status failed to update. errorDescription : " + errorDescription);
//
//				// Consent form error. Would be nice to have proper error logging. Happens also when user has no internet connection
//				Toast.makeText(getContext(), errorDescription, Toast.LENGTH_LONG).show();
//			}
//		});
//	}

	private void setupAd() {
		mInterstitialAd = new InterstitialAd(this);
		mInterstitialAd.setAdUnitId(INTERSTITIAL_AD_UNIT_ID);
		mInterstitialAd.loadAd(new AdRequest.Builder().build());
	}

	@Override
	public void showInterstitialAd(Runnable then) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (then != null) {
					mInterstitialAd.setAdListener(new AdListener() {
						@Override
						public void onAdClosed() {
							setupAd();
						}
					});
				}
				mInterstitialAd.show();
			}
		});
	}

	// TODO : Implement in Settings
	@Override
	public void revokeAppConsent(final Runnable then) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (then != null) {
					gdprHelper.resetConsent();
					gdprHelper.initialise();
					Toast.makeText(getContext(), "Ad Consent reseted", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Override
	public void contactMe(final Runnable then) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (then != null) {
					System.out.println("***********************************");
					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/Mkh_Pouya"));
					startActivity(i);
				}
			}
		});
	}

	//TODO: Implement in Settings

	@Override
	protected void onResume() {
		super.onResume();
		gdprHelper.initialise();

		//TODO TEST
		//displayConsentForm();

		// In KITKAT (4.4) and next releases, hide the virtual buttons
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) hideVirtualButtons();
	}

	@TargetApi(19)
	private void hideVirtualButtons() {
		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
						| View.SYSTEM_UI_FLAG_FULLSCREEN
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
	}

}
