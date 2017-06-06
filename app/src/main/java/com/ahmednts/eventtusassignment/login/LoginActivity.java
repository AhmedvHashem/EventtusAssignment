package com.ahmednts.eventtusassignment.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.ahmednts.eventtusassignment.R;
import com.ahmednts.eventtusassignment.utils.Logger;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

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



    @Override
    public void showFollowers(long userId) {
        //open followers activity

        Logger.withTag(TAG).log("showFollowers: " + userId);
    }
}
