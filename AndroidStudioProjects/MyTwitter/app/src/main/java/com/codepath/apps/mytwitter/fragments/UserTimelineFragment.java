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
public class UserTimelineFragment extends TweetsListFragment {
    private TwitterClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient(); // Singleton client
        populateTimeline();
    }

    public static UserTimelineFragment newInstance(String screen_name) {
        UserTimelineFragment userFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screen_name);
        userFragment.setArguments(args);
        return userFragment;
    }

    // Send an API request to get the timeline JSON
    // Fills the ListView by creating tweet objects from JSON
    private void populateTimeline() {
        String screenName = getArguments().getString("screen_name");
        client.getUserTimeline(screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                // deserialize
                // create models and add them to the adapter
                // load model (adapter takes data nad populates into list view)
                //ArrayList<Tweet> tweets = Tweet.fromJSONArray(json);
                addAll(Tweet.fromJSONArray(json));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }
        });
    }

}
