package com.ahmednts.eventtusassignment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ahmednts.eventtusassignment.followers.FollowersActivity;
import com.ahmednts.eventtusassignment.login.LoginActivity;
import com.ahmednts.eventtusassignment.utils.Logger;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Map<Long, TwitterSession> sessions = TwitterCore.getInstance().getSessionManager().getSessionMap();
        for (Long s : sessions.keySet()) {
            Logger.getInstance().withTag(TAG).log("getSessionMap: " + s);
        }

        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        if (session != null) {
            TwitterAuthToken authToken = session.getAuthToken();
            Logger.getInstance().withTag(TAG).log("success: " + authToken.toString());

            Intent intent = new Intent(this, FollowersActivity.class);
            startActivity(intent);
            finish();
        } else
            {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
