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

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by beccalee on 6/27/16.
 */
// takes tweet objects and turns them into Views
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    User user;
    Tweet tweet;
    String screenName;

    TwitterClient client;

    static class ViewHolder {
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
        @BindView(R.id.ivMedia) ImageView ivMedia;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, R.layout.item_tweet, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        tweet = getItem(position);

        user = tweet.getUser();
        screenName = user.getScreenName();
        client = TwitterApplication.getRestClient();
        ViewHolder viewHolder;

        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        String favsCount = "" + tweet.getFavoritesCount();
        String retweetsCount = "" + tweet.getRetweetsCount();

        viewHolder.tvUsername.setText(user.getName());
        viewHolder.tvScreenName.setText("@" + screenName);
        viewHolder.tvBody.setText(tweet.getBody());
        viewHolder.tvTime.setText(tweet.getRelativeDate());
        viewHolder.tvFavs.setText(favsCount);
        viewHolder.tvRetweets.setText(retweetsCount);

        if (tweet.getMediaUrl() != null){
            viewHolder.ivMedia.setVisibility(View.VISIBLE);
            Picasso.with(getContext()).load(tweet.getMediaUrl()).into(viewHolder.ivMedia);
        } else {
            viewHolder.ivMedia.setVisibility(View.GONE);
        }

        viewHolder.ivProfileImage.setImageResource(android.R.color.transparent);
        viewHolder.ivProfileImage.setTag(user.getScreenName());
        viewHolder.btnReply.setTag(user.getScreenName());
        viewHolder.btnFav.setTag(R.id.tweetId, tweet.getUid());
        viewHolder.btnFav.setTag(R.id.favorited, tweet.getFavorited());
        viewHolder.btnRetweet.setTag(R.id.tweetId, tweet.getUid());
        viewHolder.btnRetweet.setTag(R.id.retweeted, tweet.getRetweeted());

        buttonColors(viewHolder);

        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).transform(new RoundedCornersTransformation(5, 0)).into(viewHolder.ivProfileImage);

        setupOnClickListeners(viewHolder);

        return convertView;
    }

    public void buttonColors(ViewHolder viewHolder){
        if (tweet.getRetweeted()){
            viewHolder.btnRetweet.setBackgroundResource(R.drawable.retweeted);
        } else {
            viewHolder.btnRetweet.setBackgroundResource(R.drawable.retweet);
        }
        if (tweet.getFavorited()){
            viewHolder.btnFav.setBackgroundResource(R.drawable.fav);
        } else {
            viewHolder.btnFav.setBackgroundResource(R.drawable.no_fav);
        }

    }


    public void setupOnClickListeners(final ViewHolder viewHolder){
        viewHolder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ProfileActivity.class);
                String screen_name = v.getTag().toString();
                i.putExtra("screen_name", screen_name);
                getContext().startActivity(i);
            }
        });
        viewHolder.btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), PostActivity.class);
                String screen_name = v.getTag().toString();
                i.putExtra("screen_name", screen_name);
                getContext().startActivity(i);
            }
        });
        viewHolder.btnRetweet.setOnClickListener(new View.OnClickListener() {
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
        viewHolder.btnFav.setOnClickListener(new View.OnClickListener() {
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
