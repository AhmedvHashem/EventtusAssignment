package com.ahmednts.eventtusassignment.data;

import com.ahmednts.eventtusassignment.FollowerObject;
import com.twitter.sdk.android.core.models.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by AhmedNTS on 6/6/2017.
 */
public interface TwitterCustomService {
    @GET("/1.1/users/show.json")
    Call<User> show(@Query("user_id") long id);

    @GET("/1.1/followers/list.json")
    Call<FollowerObject> followers(@Query("user_id") long id);
}
