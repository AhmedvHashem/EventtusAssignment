package com.ahmednts.eventtusassignment.data;

import android.content.Context;

import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.Map;

/**
 * Created by AhmedNTS on 6/9/2017.
 */

public class UserManager {
    private static UserManager INSTANCE;

    private Context context;

//    private MyTwitterApiClient apiClient;
//    private TwitterSession activeSession;

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
        return (MyTwitterApiClient) TwitterCore.getInstance().getApiClient();
    }

    public void setActiveUser(String username) {
        Map<Long, TwitterSession> sessions = TwitterCore.getInstance().getSessionManager().getSessionMap();
        for (TwitterSession s : sessions.values()) {
            if (username.replace("@", "").equals(s.getUserName())) {
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
