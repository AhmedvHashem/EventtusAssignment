package com.ahmednts.eventtusassignment.login;


import android.content.Intent;

/**
 * Created by AhmedNTS on 6/1/2017.
 */
interface LoginContract {

    interface View {
        void showFollowers(long userId);
    }

    interface Presenter {
        void authenticate();

        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}
