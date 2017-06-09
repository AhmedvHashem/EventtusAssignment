package com.ahmednts.eventtusassignment.followerdetails;

import android.support.annotation.NonNull;

import com.ahmednts.eventtusassignment.data.followers.FollowerInfo;

/**
 * Created by AhmedNTS on 6/9/2017.
 */

public class FollowerDetailsPresenter implements FollowerDetailsContract.Presenter {

    @NonNull
    private final FollowerDetailsContract.View followerDetailsView;
    @NonNull
    private final FollowerInfo followerInfo;

    public FollowerDetailsPresenter(@NonNull FollowerInfo followerInfo, @NonNull FollowerDetailsContract.View followerDetailsView) {
        this.followerDetailsView = followerDetailsView;
        this.followerInfo = followerInfo;
    }

    @Override
    public void start() {
        followerDetailsView.setFollowerData(followerInfo);


    }

    @Override
    public void stop() {

    }
}
