package com.ahmednts.eventtusassignment.data.followers;

import com.twitter.sdk.android.core.models.User;

import java.util.List;

/**
 * Created by AhmedNTS on 6/5/2017.
 */

public class FollowersResponse {

    public List<User> users;

    public long next_cursor;
    public long previous_cursor;
    public String next_cursor_str;
    public String previous_cursor_str;
}
