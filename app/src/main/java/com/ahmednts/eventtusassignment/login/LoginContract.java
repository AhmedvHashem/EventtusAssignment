package com.ahmednts.eventtusassignment.login;

import android.content.Intent;

/**
 * Created by AhmedNTS on 6/1/2017.
 */
/**
 * This specifies the contract between the view and the presenter.
 */
interface LoginContract {

    interface View {
        void showFollowers(long userId);

        void showNoNetworkMessage();

        void showAuthorizeFailedMessage();
    }

    interface Presenter {
        void authenticate();

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void stop();
    }
}
