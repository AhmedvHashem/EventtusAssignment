package com.ahmednts.eventtusassignment;

import com.twitter.sdk.android.core.models.User;

import java.util.List;

/**
 * Created by AhmedNTS on 6/5/2017.
 */

public class FollowerObject {

    public List<User> users;

    public int nextCursor;
    public String nextCursorStr;
    public int previousCursor;
    public String previousCursorStr;
}
