package com.ahmednts.eventtusassignment.login;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.ahmednts.eventtusassignment.utils.Logger;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthException;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import java.net.UnknownHostException;

/**
 * Created by AhmedNTS on 6/6/2017.
 */
/**
 * Listens to user actions from the UI ({@link LoginActivity}), retrieves the data and updates the
 * UI as required.
 */
public class LoginPresenter implements LoginContract.Presenter {
    private static final String TAG = LoginPresenter.class.getSimpleName();

    private Activity activity;

    @NonNull
    private final TwitterAuthClient authClient;

    @NonNull
    private final LoginContract.View loginView;

    public LoginPresenter(Activity activity, @NonNull TwitterAuthClient authClient, @NonNull LoginContract.View loginView) {
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

                //if there is no internet connection then display an error msg
                if (exception.getCause() instanceof UnknownHostException) {
                    loginView.showNoNetworkMessage();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            authClient.onActivityResult(requestCode, resultCode, data);
        } catch (TwitterAuthException twitterAuthException) {
            loginView.showAuthorizeFailedMessage();
        }
    }

    @Override
    public void stop() {
        authClient.cancelAuthorize();
    }
}
