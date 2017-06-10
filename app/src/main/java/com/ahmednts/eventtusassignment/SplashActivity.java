package com.ahmednts.eventtusassignment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ahmednts.eventtusassignment.data.UserManager;
import com.ahmednts.eventtusassignment.followers.FollowersActivity;
import com.ahmednts.eventtusassignment.login.LoginActivity;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

public class SplashActivity extends AppCompatActivity {
    public static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        if (UserManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(this, FollowersActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
