package com.ahmednts.eventtusassignment.data;

import android.content.Context;

import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.Map;

/**
 * Created by AhmedNTS on 6/9/2017.
 */

/**
 * Handles current active user and its associated ({@link TwitterSession} and {@link MyTwitterApiClient})
 */
public class UserManager {
    private static UserManager INSTANCE;

    private Context context;

    public static UserManager getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = new UserManager(context);

        return INSTANCE;
    }

    private UserManager() {
    }

    private UserManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public TwitterSession getActiveSession() {
        return TwitterCore.getInstance().getSessionManager().getActiveSession();
    }

    public MyTwitterApiClient getActiveApiClient() {
        TwitterCore.getInstance().addApiClient(getActiveSession(), new MyTwitterApiClient(context, getActiveSession()));
        return (MyTwitterApiClient) TwitterCore.getInstance().getApiClient();
    }

    public void setActiveUser(String username) {
        Map<Long, TwitterSession> sessions = TwitterCore.getInstance().getSessionManager().getSessionMap();
        for (TwitterSession s : sessions.values()) {
            if (username.equals(s.getUserName())) {
                TwitterCore.getInstance().getSessionManager().setActiveSession(s);
                TwitterCore.getInstance().addApiClient(s, new MyTwitterApiClient(context, s));
                break;
            }
        }
    }

    public boolean isLoggedIn() {
        return getActiveSession() != null;
    }
}
