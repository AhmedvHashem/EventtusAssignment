package com.ahmednts.eventtusassignment.followerdetails;

import com.ahmednts.eventtusassignment.data.followers.FollowerInfo;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;

import java.util.List;

/**
 * Created by AhmedNTS on 6/1/2017.
 */
/**
 * This specifies the contract between the view and the presenter.
 */
interface FollowerDetailsContract {

    interface View {
        void setFollowerData(FollowerInfo followerInfo);

        void showTweetsList(List<Tweet> tweets);

        void showIndicator();

        void hideIndicator();

        void showNoResultMessage();

        void showApiLimitMessage();

        void showToastMessage(String msg);

        void showNoNetworkMessage();
    }

    interface Presenter {
        void start();

        void stop();
    }
}
