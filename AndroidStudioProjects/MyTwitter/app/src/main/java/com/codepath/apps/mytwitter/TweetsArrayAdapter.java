package com.codepath.apps.mytwitter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mytwitter.models.Tweet;
import com.codepath.apps.mytwitter.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

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
    String screenName;

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, 0, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet,parent,false);
        }

        tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);

        user = tweet.getUser();
        screenName = user.getScreenName();

        tvUsername.setText(screenName);
        tvBody.setText(tweet.getBody());
        tvTime.setText(tweet.getRelativeDate());
        ivProfileImage.setImageResource(android.R.color.transparent);

        ivProfileImage.setTag(user.getScreenName());

        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);

        setupOnClickListener();

        return convertView;
    }

    public void setupOnClickListener(){
        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ProfileActivity.class);
                String screen_name = v.getTag().toString();
                i.putExtra("screen_name", screen_name);
                getContext().startActivity(i);
            }
        });
    }




}
