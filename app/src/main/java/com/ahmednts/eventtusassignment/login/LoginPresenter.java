package com.ahmednts.eventtusassignment.login;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.ahmednts.eventtusassignment.utils.Logger;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

/**
 * Created by AhmedNTS on 6/6/2017.
 */

public class LoginPresenter implements LoginContract.Presenter {
    public static final String TAG = LoginPresenter.class.getSimpleName();

    private Activity activity;

    private TwitterAuthClient authClient;

    private LoginContract.View loginView;

    public LoginPresenter(Activity activity, TwitterAuthClient authClient, LoginContract.View loginView) {
        this.activity = activity;
        this.authClient = authClient;
        this.loginView = loginView;
    }

    @Override
    public void authenticate() {
        authClient.authorize(activity, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Logger.getInstance().withTag(TAG).log("success: " + result.data.getAuthToken().toString());
                loginView.showFollowers(result.data.getUserId());
            }

            @Override
            public void failure(TwitterException exception) {
                Logger.getInstance().withTag(TAG).log("failure: " + exception.getMessage());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        authClient.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void stop() {
        authClient.cancelAuthorize();
    }
}
