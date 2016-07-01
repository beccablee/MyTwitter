package com.codepath.apps.mytwitter;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.codepath.apps.mytwitter.fragments.SearchTweetsFragment;

public class SearchActivity extends AppCompatActivity {

    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        query = getIntent().getStringExtra("query");

        //search = new SearchTweetsFragment();

        if (savedInstanceState == null){
            //Toast.makeText(SearchActivity.this, query, Toast.LENGTH_SHORT).show();
            SearchTweetsFragment searchTweetsFragment = SearchTweetsFragment.newInstance(query);
            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
            ft2.replace(R.id.flContainer2, searchTweetsFragment);
            ft2.commit();
        }

    }

}
