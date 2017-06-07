package com.ahmednts.eventtusassignment.followers;

import android.util.Log;

import com.ahmednts.eventtusassignment.data.FollowersResponse;
import com.ahmednts.eventtusassignment.data.MyTwitterApiClient;
import com.ahmednts.eventtusassignment.utils.Logger;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.User;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by AhmedNTS on 6/6/2017.
 */

public class FollowersPresenter implements FollowersContract.Presenter {
    public static final String TAG = FollowersPresenter.class.getSimpleName();

    private MyTwitterApiClient apiClient;

    private FollowersContract.View followersView;

    private boolean firstLoad = true;

    public FollowersPresenter(MyTwitterApiClient apiClient, FollowersContract.View followersView) {
        this.apiClient = apiClient;
        this.followersView = followersView;
    }

    @Override
    public void loadFollowersList(long userId) {
        Logger.withTag(TAG).log("loadFollowersList: " + userId);

        followersView.showIndicator();

        apiClient.getTwitterCustomService().followers(userId).enqueue(new Callback<FollowersResponse>() {
            @Override
            public void success(Result<FollowersResponse> result) {
                if (result != null) {
                    if (result.data.users != null && result.data.users.size() > 0) {
                        followersView.hideIndicator();
                        followersView.showFollowers(result.data.users);
                    } else {
                        followersView.showErrorMessage("You have no followers! Yet");
                    }
                }
            }

            @Override
            public void failure(TwitterException exception) {
                exception.printStackTrace();
            }
        });
    }

    @Override
    public void openFollowerDetails(User follower) {
        followersView.openFollowerDetailsUI();
    }
}
