package com.codepath.apps.mytwitter.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by beccalee on 6/27/16.
 */
public class User implements Serializable{

    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;
    private String profileBannerUrl;
    private String tagline;
    private String followersCount;
    private String followingsCount;


    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getProfileBannerUrl() {
        return profileBannerUrl;
    }

    public String getTagline() {
        return tagline;
    }

    public String getFollowersCount() {
        return followersCount;
    }

    public String getFollowingsCount() {
        return followingsCount;
    }

    public static User fromJSON(JSONObject json){
        User u = new User();
        try {
            u.name = json.getString("name");
            u.uid = json.getLong("id");
            u.screenName = json.getString("screen_name");
            u.profileImageUrl = json.getString("profile_image_url");
            u.profileBannerUrl = json.getString("profile_banner_url");
            u.tagline = json.getString("description");
            u.followersCount = json.getString("followers_count");
            u.followingsCount = json.getString("friends_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return u;
    }

}
