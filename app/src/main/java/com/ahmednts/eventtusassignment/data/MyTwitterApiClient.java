package com.ahmednts.eventtusassignment.data;

/**
 * Created by AhmedNTS on 6/5/2017.
 */

import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;

public class MyTwitterApiClient extends TwitterApiClient {
    public MyTwitterApiClient() {
        super();
    }

    public MyTwitterApiClient(TwitterSession session) {
        super(session);
    }

    /**
     * Provide CustomService with defined endpoints
     */
    public MyTwitterCustomService getTwitterCustomService() {
        return getService(MyTwitterCustomService.class);
    }
}
