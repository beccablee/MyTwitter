package com.codepath.apps.mytwitter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.codepath.apps.mytwitter.models.Tweet;
import com.codepath.apps.mytwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class PostActivity extends AppCompatActivity {
    TwitterClient client;
    User user;
    String post;
    EditText etPost;
    Tweet tweet;
    ImageView ivProfileImage;
    Button btnTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        client = TwitterApplication.getRestClient();
        client.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJSON(response);
                setupViews();
            }
        });
    }

    private void setupViews() {
        etPost = (EditText) findViewById(R.id.etPost);
        btnTweet = (Button) findViewById(R.id.btnTweet);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);
    }

    public void onTweet(View view){
        post = etPost.getText().toString();
        Toast.makeText(getApplicationContext(), "clicked!", Toast.LENGTH_SHORT).show();
        client.postTweet(post, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                //Toast.makeText(getApplicationContext(), "success!", Toast.LENGTH_SHORT).show();

                tweet = Tweet.fromJSON(json);
                submitTweet(tweet);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject responseString) {
                //Log.d("DEBUG", "");
            }
        });
    }

    private void submitTweet(Tweet tweet){
        Intent postInfo = new Intent();
        postInfo.putExtra("tweet", tweet);
        setResult(RESULT_OK, postInfo);
        finish();
    }


}
