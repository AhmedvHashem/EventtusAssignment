package com.ahmednts.eventtusassignment.followerdetails;

import android.support.annotation.NonNull;

import com.ahmednts.eventtusassignment.data.MyTwitterApiClient;
import com.ahmednts.eventtusassignment.data.followers.FollowerInfo;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiException;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;

import java.net.UnknownHostException;
import java.util.List;

import retrofit2.Call;

/**
 * Created by AhmedNTS on 6/9/2017.
 */

public class FollowerDetailsPresenter implements FollowerDetailsContract.Presenter {
    private static final String TAG = FollowerDetailsPresenter.class.getSimpleName();

    @NonNull
    private final FollowerDetailsContract.View followerDetailsView;
    @NonNull
    private MyTwitterApiClient myTwitterApiClient;
    @NonNull
    private final FollowerInfo followerInfo;

    private Call<List<Tweet>> followerLast10TweetsCall;

    public FollowerDetailsPresenter(@NonNull FollowerInfo followerInfo, @NonNull MyTwitterApiClient myTwitterApiClient, @NonNull FollowerDetailsContract.View followerDetailsView) {
        this.followerInfo = followerInfo;
        this.myTwitterApiClient = myTwitterApiClient;
        this.followerDetailsView = followerDetailsView;
    }

    @Override
    public void start() {
        followerDetailsView.setFollowerData(followerInfo);

        loadLastTweets();
    }

    private void loadLastTweets() {
        followerDetailsView.showIndicator();

        followerLast10TweetsCall = myTwitterApiClient.getStatusesService().userTimeline(
                followerInfo.getId(), null, 10, null, null, null, true, null, null);
        followerLast10TweetsCall.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                if (result.data != null) {
                    if (result.data.size() > 0) {
                        followerDetailsView.hideIndicator();
                        followerDetailsView.showTweetsList(result.data);
                    } else {
                        followerDetailsView.hideIndicator();
                        followerDetailsView.showNoResultMessage();
                    }
                }
            }

            @Override
            public void failure(TwitterException exception) {
                exception.printStackTrace();
                if (followerLast10TweetsCall.isCanceled()) return;

                followerDetailsView.hideIndicator();

                if (exception instanceof TwitterApiException) {
                    TwitterApiException apiException = (TwitterApiException) exception;
                    if (apiException.getErrorCode() == 429)
                        followerDetailsView.showApiLimitMessage();
                    else
                        followerDetailsView.showToastMessage(apiException.getErrorMessage());
                } else if (exception.getCause() instanceof UnknownHostException) {
                    followerDetailsView.showNoNetworkMessage();
                }
            }
        });
    }

    @Override
    public void stop() {
        followerLast10TweetsCall.cancel();
    }
}

