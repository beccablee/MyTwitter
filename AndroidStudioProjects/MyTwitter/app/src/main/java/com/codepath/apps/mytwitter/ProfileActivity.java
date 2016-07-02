package com.codepath.apps.mytwitter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mytwitter.fragments.LikesTimelineFragment;
import com.codepath.apps.mytwitter.fragments.UserTimelineFragment;
import com.codepath.apps.mytwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ProfileActivity extends AppCompatActivity {
    TwitterClient client;
    User user;
    String screenName;
    @BindView(R.id.viewpager) ViewPager vpPager;
    @BindView(R.id.tabs) PagerSlidingTabStrip tabStrip;

    @BindView(R.id.tvFullName) TextView tvName;
    @BindView(R.id.tvScreenName) TextView tvScreeName;
    @BindView(R.id.tvTagLine) TextView tvTagLine;
    @BindView(R.id.tvFollowers) TextView tvFollowers;
    @BindView(R.id.tvFollowing) TextView tvFollowing;
    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.ivHeaderImage) ImageView ivHeaderImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));

        screenName = getIntent().getStringExtra("screen_name");

        //ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        //PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(vpPager);

        client = TwitterApplication.getRestClient();
        client.getUserInfo(screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJSON(response);
                if (screenName == null){
                    screenName = user.getScreenName();
                }
                getSupportActionBar().setTitle("@" + screenName);
                populateProfileHeader(user);
            }
        });

    }

    private void populateProfileHeader(User user) {
        tvName.setText(user.getName());
        tvScreeName.setText("@" + user.getScreenName());
        tvTagLine.setText(user.getTagline());
        tvFollowers.setText(user.getFollowersCount() + " FOLLOWERS");
        tvFollowing.setText(user.getFollowingsCount() + " FOLLOWING");
        Picasso.with(this).load(user.getProfileImageUrl()).transform(new RoundedCornersTransformation(10, 0)).into(ivProfileImage);
        Picasso.with(this).load(user.getProfileBannerUrl()).into(ivHeaderImage);
    }

    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        private String tabTitles[] = {"Tweets", "Likes"};

        public TweetsPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0){
                return UserTimelineFragment.newInstance(screenName);
            } else if (position == 1) {
                return new LikesTimelineFragment();
            } else {
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
