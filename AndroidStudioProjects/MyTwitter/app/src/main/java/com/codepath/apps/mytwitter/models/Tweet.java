package com.codepath.apps.mytwitter.models;

import android.net.ParseException;
import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

//import org.ocpsoft.prettytime.TimeUnit;


/**
 * Created by beccalee on 6/27/16.
 */
// Parse the jSON, store, encapsulate state logic/display logic


public class Tweet implements Serializable{
    // List of attributes
    private String body;
    private long uid;
    private String createdAt;
    private User user;
    private String relativeDate;

    public Tweet() {
    }

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public String getRelativeDate() {
        return relativeDate;
    }

    // Deserialize JSON and build tweet objects
    // com.codepath.apps.mytwitter.models.Tweet.fromJSON("(..)") -> com.codepath.apps.mytwitter.models.Tweet

    public static Tweet fromJSON(JSONObject jsonObject){
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
            try {
                tweet.relativeDate = getRelativeTimeAgo(tweet.createdAt);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            try {
                JSONObject tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJson);
                if (tweet != null){
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return tweets;
    }

    public static String getRelativeTimeAgo1(String rawJsonDate){
        String rDate = "";
        //PrettyTime p = new PrettyTime();
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(dateMillis);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(dateMillis);
            long hours = TimeUnit.MILLISECONDS.toHours(dateMillis);
            long days = TimeUnit.MILLISECONDS.toDays(dateMillis);

            if (days != 0){
                rDate = "" + days + "d";
            } else {
                rDate = "" + minutes + "m";
            }
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }


        //String date = sf.format(rawJsonDate);
        //sf.applyPattern("MMM");
        //rDate = sf.format(date);
        //rDate = p.format();
        //List<Date> dates = new PrettyTimeParser().parse("I'm going to the beach in three days!");
        return rDate;
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public static String getRelativeTimeAgo(String rawJsonDate) throws java.text.ParseException {
        // s, m, h, d, 1/25/16
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS,
                    DateUtils.FORMAT_ABBREV_ALL).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

}
