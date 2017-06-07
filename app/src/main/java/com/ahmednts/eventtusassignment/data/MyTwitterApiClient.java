package com.ahmednts.eventtusassignment.data;

/**
 * Created by AhmedNTS on 6/5/2017.
 */

import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;

import okhttp3.OkHttpClient;

public class MyTwitterApiClient extends TwitterApiClient {

    public MyTwitterApiClient(TwitterSession session, OkHttpClient okHttpClient) {
        super(session, okHttpClient);
    }

    /**
     * Provide CustomService with defined endpoints
     */
    public MyTwitterCustomService getTwitterCustomService() {
        return getService(MyTwitterCustomService.class);
    }
}
