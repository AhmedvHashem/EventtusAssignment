package com.ahmednts.eventtusassignment.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.ahmednts.eventtusassignment.R;
import com.ahmednts.eventtusassignment.followers.FollowersActivity;
import com.ahmednts.eventtusassignment.utils.Logger;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private Button loginButton;

    private LoginContract.Presenter loginPresenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();

        TwitterAuthClient authClient = new TwitterAuthClient();

        loginPresenter = new LoginPresenter(this, authClient, this);

        loginButton.setOnClickListener(v -> loginPresenter.authenticate());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        loginPresenter.stop();
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

        Logger.getInstance().withTag(TAG).log("showFollowers: " + userId);

        Intent intent = new Intent(this, FollowersActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showNoNetworkMessage() {
        Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_network), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showAuthorizeFailedMessage() {
        Toast.makeText(this, getString(R.string.error_authorization), Toast.LENGTH_SHORT).show();
    }
}
