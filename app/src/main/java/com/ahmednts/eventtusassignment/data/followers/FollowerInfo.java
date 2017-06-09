package com.ahmednts.eventtusassignment.data.followers;

import org.parceler.Parcel;

/**
 * Created by AhmedNTS on 6/9/2017.
 */
@Parcel
public class FollowerInfo {

    long id;
    String name;
    String username;
    String profileImageUrl;
    String backgroundImageUrl;

    public FollowerInfo() {
    }

    public FollowerInfo(long id, String name, String username, String profileImageUrl, String backgroundImageUrl) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.profileImageUrl = profileImageUrl;
        this.backgroundImageUrl = backgroundImageUrl;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }
}
