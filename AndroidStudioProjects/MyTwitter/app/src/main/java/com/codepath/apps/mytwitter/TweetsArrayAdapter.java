package com.codepath.apps.mytwitter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mytwitter.models.Tweet;
import com.codepath.apps.mytwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by beccalee on 6/27/16.
 */
// takes tweet objects and turns them into Views
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    ImageView ivProfileImage;
    TextView tvUsername;
    TextView tvBody;
    TextView tvTime;
    User user;
    Tweet tweet;
    String screenName;
    Button btnReply;
    Button btnRetweet;
    Button btnFav;

    TwitterClient client;

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, 0, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        tweet = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet,parent,false);
        }
        user = tweet.getUser();
        screenName = user.getScreenName();
        client = TwitterApplication.getRestClient();

        tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        btnReply = (Button) convertView.findViewById(R.id.btnReply);
        btnRetweet = (Button) convertView.findViewById(R.id.btnRetweet);
        btnFav = (Button) convertView.findViewById(R.id.btnFav);
        buttonColors();

        tvUsername.setText(screenName);
        tvBody.setText(tweet.getBody());
        tvTime.setText(tweet.getRelativeDate());
        ivProfileImage.setImageResource(android.R.color.transparent);
        ivProfileImage.setTag(user.getScreenName());
        btnReply.setTag(user.getScreenName());
        btnFav.setTag(R.id.tweetId, tweet.getUid());
        btnFav.setTag(R.id.retweeted, tweet.getFavorited());
        //Log.d("DEBUG", "" + tweet.getFavorited());

        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);

        setupOnClickListeners();

        return convertView;
    }

    public void buttonColors(){
        if (tweet.getRetweeted()){
            btnRetweet.setBackgroundResource(R.drawable.retweeted);
        } else {
            btnRetweet.setBackgroundResource(R.drawable.retweet);
        }
        if (tweet.getFavorited()){
            btnFav.setBackgroundResource(R.drawable.fav);
        } else {
            btnFav.setBackgroundResource(R.drawable.no_fav);
        }

    }


    public void setupOnClickListeners(){
        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ProfileActivity.class);
                String screen_name = v.getTag().toString();
                i.putExtra("screen_name", screen_name);
                getContext().startActivity(i);
            }
        });
        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), PostActivity.class);
                String screen_name = v.getTag().toString();
                i.putExtra("screen_name", screen_name);
                getContext().startActivity(i);
            }
        });
        btnRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Long id = Long.parseLong(v.getTag(R.id.tweetId).toString());
                if (!(Boolean)v.getTag(R.id.retweeted)) {
                    client.favorite(id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            v.setBackgroundResource(R.drawable.fav);
                            v.setTag(R.id.retweeted, true);
                        }
                    });
                } else {
                    client.unfavorite(id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            v.setBackgroundResource(R.drawable.no_fav);
                            v.setTag(R.id.retweeted, false);
                        }
                    });
                }
            }
        });
    }





}
