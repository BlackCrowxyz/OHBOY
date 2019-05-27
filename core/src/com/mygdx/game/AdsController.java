package com.mygdx.game;

public interface AdsController {
    void showInterstitialAd (Runnable then);

    void revokeAppConsent(Runnable then);

    void contactMe(Runnable then);
}
