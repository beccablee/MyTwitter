package com.codepath.apps.mytwitter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mytwitter.models.Tweet;
import com.codepath.apps.mytwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class DetailsActivity extends AppCompatActivity {

    Tweet tweet;
    User user;
    String screenName;
    TwitterClient client;

    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.tvUsername) TextView tvUsername;
    @BindView(R.id.tvScreenName) TextView tvScreenName;
    @BindView(R.id.tvBody) TextView tvBody;
    @BindView(R.id.tvTime) TextView tvTime;
    @BindView(R.id.btnReply) Button btnReply;
    @BindView(R.id.btnRetweet) Button btnRetweet;
    @BindView(R.id.btnFav) Button btnFav;
    @BindView(R.id.tvRetweets) TextView tvRetweets;
    @BindView(R.id.tvFavs) TextView tvFavs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));

        tweet = (Tweet) getIntent().getSerializableExtra("tweet");
        user = tweet.getUser();
        screenName = user.getScreenName();
        client = TwitterApplication.getRestClient();
        String favsCount = "" + tweet.getFavoritesCount();
        String retweetsCount = "" + tweet.getRetweetsCount();

        tvUsername.setText(user.getName());
        tvScreenName.setText("@" + screenName);
        tvBody.setText(tweet.getBody());
        tvTime.setText(tweet.getRelativeDate());
        tvFavs.setText(favsCount);
        tvRetweets.setText(retweetsCount);

        ivProfileImage.setImageResource(android.R.color.transparent);
        ivProfileImage.setTag(user.getScreenName());
        btnReply.setTag(user.getScreenName());
        btnFav.setTag(R.id.tweetId, tweet.getUid());
        btnFav.setTag(R.id.favorited, tweet.getFavorited());
        btnRetweet.setTag(R.id.tweetId, tweet.getUid());
        btnRetweet.setTag(R.id.retweeted, tweet.getRetweeted());

        buttonColors();

        Picasso.with(getApplicationContext()).load(tweet.getUser().getProfileImageUrl()).transform(new RoundedCornersTransformation(5, 0)).into(ivProfileImage);

        setupOnClickListeners();
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
                getApplicationContext().startActivity(i);
            }
        });
        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), PostActivity.class);
                String screen_name = v.getTag().toString();
                i.putExtra("screen_name", screen_name);
                getApplicationContext().startActivity(i);
            }
        });
        btnRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Long id = Long.parseLong(v.getTag(R.id.tweetId).toString());
                if (!(Boolean)v.getTag(R.id.retweeted)) {
                    client.retweet(id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            v.setBackgroundResource(R.drawable.retweeted);
                            v.setTag(R.id.retweeted, true);
                        }
                    });
                } else {
                    client.unRetweet(id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            v.setBackgroundResource(R.drawable.retweet);
                            v.setTag(R.id.retweeted, false);
                        }
                    });
                }
            }
        });
        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Long id = Long.parseLong(v.getTag(R.id.tweetId).toString());
                if (!(Boolean)v.getTag(R.id.favorited)) {
                    client.favorite(id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            v.setBackgroundResource(R.drawable.fav);
                            v.setTag(R.id.favorited, true);
                        }
                    });
                } else {
                    client.unfavorite(id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            v.setBackgroundResource(R.drawable.no_fav);
                            v.setTag(R.id.favorited, false);
                        }
                    });
                }
            }
        });
    }

}
