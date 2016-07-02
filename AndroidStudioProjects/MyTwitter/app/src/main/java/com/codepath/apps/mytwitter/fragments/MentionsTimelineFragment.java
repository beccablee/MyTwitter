package com.codepath.apps.mytwitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.codepath.apps.mytwitter.TwitterApplication;
import com.codepath.apps.mytwitter.TwitterClient;
import com.codepath.apps.mytwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import cz.msebera.android.httpclient.Header;

/**
 * Created by beccalee on 6/28/16.
 */
public class MentionsTimelineFragment extends TweetsListFragment {
    private TwitterClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient(); // Singleton client
        populateTimeline();
    }

    // Send an API request to get the timeline JSON
    // Fills the ListView by creating tweet objects from JSON
    private void populateTimeline() {
        client.getMentionsTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {

                addAll(Tweet.fromJSONArray(json));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }
        });
    }

}
