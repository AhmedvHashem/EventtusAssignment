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
    }

    interface Presenter {
        void loadFollowersList(long userId);
        void openFollowerDetails(User follower);
    }
}
