package com.ahmednts.eventtusassignment.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.ahmednts.eventtusassignment.R;
import com.ahmednts.eventtusassignment.followers.FollowersActivity;
import com.ahmednts.eventtusassignment.utils.Logger;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private LoginContract.Presenter loginPresenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        TwitterAuthClient authClient = new TwitterAuthClient();

        loginPresenter = new LoginPresenter(this, authClient, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        loginPresenter.stop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        loginPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.login_button)
    void onLoginButtonClick(View v) {
        loginPresenter.authenticate();
    }

    @Override
    public void showFollowers(long userId) {
        Logger.getInstance().withTag(TAG).log("showFollowers: " + userId);

        Intent intent = new Intent(this, FollowersActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
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
