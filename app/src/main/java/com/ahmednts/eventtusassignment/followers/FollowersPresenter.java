package com.ahmednts.eventtusassignment.followers;

import android.support.annotation.NonNull;

import com.ahmednts.eventtusassignment.data.UserManager;
import com.ahmednts.eventtusassignment.data.followers.FollowersResponse;
import com.ahmednts.eventtusassignment.utils.Logger;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiException;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.User;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by AhmedNTS on 6/6/2017.
 */

public class FollowersPresenter implements FollowersContract.Presenter {
    private static final String TAG = FollowersPresenter.class.getSimpleName();

    @NonNull
    private final UserManager userManager;

    @NonNull
    private final FollowersContract.View followersView;

    private Call<FollowersResponse> followersResponseCall;
    private long nextCursor = -1;
    private List<User> followers = new ArrayList<>(0);

    public FollowersPresenter(@NonNull UserManager userManager, @NonNull FollowersContract.View followersView) {
        this.userManager = userManager;
        this.followersView = followersView;
    }

    @Override
    public void setActiveUser(String username) {
        userManager.setActiveUser(username);

        loadFollowersList(true);
    }

    @Override
    public void loadFollowersList(boolean reload) {
        Logger.getInstance().withTag(TAG).log("loadFollowersList: userId=" + userManager.getActiveSession().getUserId());

        this.followersView.setTitle(userManager.getActiveSession().getUserName());

        if (reload) {
            nextCursor = -1;
            followersView.showIndicator();
            followers.clear();
            followers.add(null);
        }

        if (nextCursor == 0) {
            followersView.hideIndicator();
            return;
        }

        Logger.getInstance().withTag(TAG).log("loadFollowersList: nextCursor=" + nextCursor);

        followersResponseCall = userManager.getActiveApiClient().getTwitterService().followers(userManager.getActiveSession().getUserId(), nextCursor);
        followersResponseCall.enqueue(new Callback<FollowersResponse>() {
            @Override
            public void success(Result<FollowersResponse> result) {
                if (result != null) {
                    if (result.data.users != null && result.data.users.size() > 0) {
                        nextCursor = result.data.next_cursor;
                        Logger.getInstance().withTag(TAG).log("loadFollowersList: nextCursor=" + nextCursor);

                        followers.addAll(followers.size() - 1, result.data.users);

                        if (nextCursor == 0)
                            followers.remove(followers.size() - 1);

                        followersView.hideIndicator();
                        followersView.showFollowersList(followers);
                    } else if (followers.size() == 0) {
                        followersView.hideIndicator();
                        followersView.showNoResultMessage();
                    }
                }
            }

            @Override
            public void failure(TwitterException exception) {
                exception.printStackTrace();
                if (followersResponseCall.isCanceled()) return;

                followersView.hideIndicator();

                if (exception instanceof TwitterApiException) {
                    TwitterApiException apiException = (TwitterApiException) exception;
                    if (apiException.getErrorCode() == 429)
                        followersView.showApiLimitMessage();
                    else
                        followersView.showToastMessage(apiException.getErrorMessage());
                } else if (exception.getCause() instanceof UnknownHostException) {
                    followersView.showNoNetworkMessage();
                }
            }
        });
    }

    @Override
    public void openFollowerDetails(User follower) {
        followersView.openFollowerDetailsUI(follower);
    }

    @Override
    public void stop() {
        followersResponseCall.cancel();
    }
}
