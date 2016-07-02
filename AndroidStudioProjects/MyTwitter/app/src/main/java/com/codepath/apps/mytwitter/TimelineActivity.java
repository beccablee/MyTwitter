package com.codepath.apps.mytwitter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mytwitter.fragments.HomeTimelineFragment;
import com.codepath.apps.mytwitter.fragments.MentionsTimelineFragment;
import com.codepath.apps.mytwitter.models.Tweet;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimelineActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 8;
    HomeTimelineFragment home;
    MentionsTimelineFragment mentions;
    Tweet tweet;
    TwitterClient client;
    @BindView(R.id.viewpager) ViewPager vpPager;
    @BindView(R.id.tabs) PagerSlidingTabStrip tabStrip;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        home = new HomeTimelineFragment();
        mentions = new MentionsTimelineFragment();
        client = TwitterApplication.getRestClient();

        //ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        //PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabStrip.setViewPager(vpPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        setupSearch(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    private void setupSearch(Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        int searchName = android.support.v7.appcompat.R.id.search_button;
        ImageView searchIcon = (ImageView) searchView.findViewById(searchName);
        searchIcon.setImageResource(R.drawable.search_icon);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                onTweetSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchItem.expandActionView();
        searchView.requestFocus();
    }

    private void onTweetSearch(final String query) {
        Intent i = new Intent(TimelineActivity.this, SearchActivity.class);
        i.putExtra("query", query);
        startActivity(i);
    }

    public void onProfileView(MenuItem mi){
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    public void postTweet(MenuItem mi){
        Intent i = new Intent(TimelineActivity.this, PostActivity.class);
        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            tweet = (Tweet) intent.getSerializableExtra("tweet");
            home.add(tweet);
            Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
        }
    }

    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        private String tabTitles[] = {"Home", "Mentions"};

        public TweetsPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0){
                return home;
            } else if (position == 1) {
                return mentions;
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



}
