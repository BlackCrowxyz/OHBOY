package com.mygdx.game;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.consent.DebugGeography;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;

import java.net.MalformedURLException;
import java.net.URL;

public class GdprHelper {

    private static final String PUBLISHER_ID = "YOUR-PUBLISHER-ID";
    private static final String PRIVACY_URL = "YOUR-PRIVACY-URL";
    private static final String MARKET_URL_PAID_VERSION = "market://details?id=com.example.app.pro";

    private final Context context;

    private ConsentForm consentForm;
    private boolean doesHideVirtualButtons;

    GdprHelper(Context context) {
        this.context = context;
        doesHideVirtualButtons = false;
    }

    // Initialises the consent information and displays consent form if needed
    public void initialise() {
        ConsentInformation consentInformation = ConsentInformation.getInstance(context);

        //DEBUG LINES
        consentInformation.setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_EEA); 		//EEA
        //consentInformation.setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_NOT_EEA);   //NOT_EEA

        //For under Aged people in Europe
        //consentInformation.setTagForUnderAgeOfConsent(true);

        consentInformation.requestConsentInfoUpdate(new String[]{PUBLISHER_ID}, new ConsentInfoUpdateListener() {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                // User's consent status successfully updated.
                System.out.println("1234567890-"+consentStatus);
                if (consentStatus == ConsentStatus.UNKNOWN) {
                    displayConsentForm();
                }
            }

            @Override
            public void onFailedToUpdateConsentInfo(String errorDescription) {
                // Consent form error. Would be nice to have proper error logging. Happens also when user has no internet connection
                Toast.makeText(context, errorDescription, Toast.LENGTH_LONG).show();
            }
        });
    }

    // Resets the consent. User will be again displayed the consent form on next call of initialise method
    public void resetConsent() {
        ConsentInformation consentInformation = ConsentInformation.getInstance(context);
        consentInformation.reset();
    }

    private void displayConsentForm() {

        consentForm = new ConsentForm.Builder(context, getPrivacyUrl())
                .withListener(new ConsentFormListener() {
                    @Override
                    public void onConsentFormLoaded() {
                        // Consent form has loaded successfully, now show it
                        System.out.println("Consent form has loaded successfully, now show it");
                        consentForm.show();
                    }

                    @Override
                    public void onConsentFormOpened() {
                        // Consent form was displayed.
                        System.out.println("// Consent form was displayed.");
                    }

                    @Override
                    public void onConsentFormClosed(
                            ConsentStatus consentStatus, Boolean userPrefersAdFree) {
                        // Consent form was closed. This callback method contains all the data about user's selection, that you can use.
                        System.out.println("// Consent form was closed. This callback method contains all the data about user's selection, that you can use.");
                        if (userPrefersAdFree) {
                            redirectToPaidVersion();
                        }

                        //?? is it correct to show it here ??
                        if (consentStatus == ConsentStatus.NON_PERSONALIZED) {
                            Bundle extras = new Bundle();
                            extras.putString("npa", "1");

                            AdRequest request = new AdRequest.Builder()
                                    .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                                    .build();
                        }

                        doesHideVirtualButtons = true;
                    }

                    @Override
                    public void onConsentFormError(String errorDescription) {
                        // Consent form error. Would be nice to have some proper logging
                        System.out.println("// Consent form error. Would be nice to have some proper logging");
                        Toast.makeText(context, errorDescription, Toast.LENGTH_LONG).show();

                    }
                })
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
                .withAdFreeOption()
                .build();
        consentForm.load();
    }

    public boolean isDoesHideVirtualButtons() {
        return doesHideVirtualButtons;
    }

    private URL getPrivacyUrl() {
        URL privacyUrl = null;
        try {
            //privacyUrl = new URL(PRIVACY_URL);
            privacyUrl = new URL("https://www.your.com/privacyurl");
        } catch (MalformedURLException e) {
            // Since this is a constant URL, the exception should never(or always) occur
            e.printStackTrace();
        }
        return privacyUrl;
    }

    private void redirectToPaidVersion() {
        Intent i = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(MARKET_URL_PAID_VERSION));
        context.startActivity(i);
    }
}
