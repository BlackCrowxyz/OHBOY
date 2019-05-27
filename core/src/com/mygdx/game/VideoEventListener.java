package com.mygdx.game;

public interface VideoEventListener {

    void onRewardedEvent(String type, int amount);

    void onRewardedVideoAdLoadedEvent();

    void onRewardedVideoAdClosedEvent();
}
