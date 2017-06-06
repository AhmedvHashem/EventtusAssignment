package com.ahmednts.eventtusassignment.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.ahmednts.eventtusassignment.FollowerObject;
import com.ahmednts.eventtusassignment.R;
import com.ahmednts.eventtusassignment.data.MyTwitterApiClient;
import com.ahmednts.eventtusassignment.utils.Logger;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;

import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {
    public static final String TAG = LoginActivity.class.getSimpleName();

    Button loginButton;

    LoginContract.Presenter loginPresenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();

        TwitterAuthClient authClient = new TwitterAuthClient();

        loginPresenter = new LoginPresenter(this, authClient, this);

        loginButton.setOnClickListener(v -> {
            loginPresenter.authenticate();
        });
    }

    void initUI() {
        loginButton = (Button) findViewById(R.id.login_button);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        loginPresenter.onActivityResult(requestCode, resultCode, data);
    }

    void getFollowersList() {
        final MyTwitterApiClient customApiClient;
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        if (session != null) {
            customApiClient = new MyTwitterApiClient(session);
            TwitterCore.getInstance().addApiClient(session, customApiClient);

            customApiClient.getTwitterCustomService().show(session.getUserId()).enqueue(new retrofit2.Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Log.w(TAG, "success: " + response.body().name);
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });

            customApiClient.getTwitterCustomService().followers(session.getUserId()).enqueue(new Callback<FollowerObject>() {
                @Override
                public void success(Result<FollowerObject> result) {
                    if (result != null) {

                    }
                }

                @Override
                public void failure(TwitterException exception) {
                    exception.printStackTrace();
                }
            });
        } else {
            customApiClient = new MyTwitterApiClient();
            TwitterCore.getInstance().addGuestApiClient(customApiClient);
        }
    }

    @Override
    public void showFollowers(long userId) {
        //open followers activity

        Logger.withTag(TAG).log("showFollowers: " + userId);
    }
}
