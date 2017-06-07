package com.ahmednts.eventtusassignment.followers;

import com.twitter.sdk.android.core.models.User;

import java.util.List;

/**
 * Created by AhmedNTS on 6/1/2017.
 */
interface FollowersContract {

    interface View {
        void showFollowers(List<User> users);

        void openFollowerDetailsUI();

        void showIndicator();

        void hideIndicator();

        void showErrorMessage(String msg);

        void showToastMessage(String msg);

        void showNoNetworkMessage();
    }

    interface Presenter {
        void loadFollowersList(long userId, boolean reload);

        void openFollowerDetails(User follower);

        void stop();
    }
}
