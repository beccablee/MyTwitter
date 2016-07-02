package com.codepath.apps.mytwitter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.mytwitter.models.Tweet;
import com.codepath.apps.mytwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class PostActivity extends AppCompatActivity {

    TwitterClient client;
    User user;
    Tweet tweet;
    String post;

    @BindView(R.id.etPost) EditText etPost;
    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.btnTweet) Button btnTweet;
    @BindView(R.id.tvCharacterCount) TextView tvCharacterCount;
    @BindView(R.id.toolbar) Toolbar toolbar;
    int counter = 140;
    String characters = "" + counter;

    String screen_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        screen_name = getIntent().getStringExtra("screen_name");

        client = TwitterApplication.getRestClient();
        client.getUserInfo(null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJSON(response);
                setupViews();
                setCharacterCountListener();
            }
        });
    }

    private void setupViews() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (screen_name != null){
            etPost.setText("@" + screen_name + " ");
            etPost.setSelection(etPost.length());
        }

        tvCharacterCount.setText(characters);
        tvCharacterCount.setTextColor(Color.parseColor("#CCCCCC"));

        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);
    }


    private void setCharacterCountListener() {
        etPost.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                counter = 140 - s.length();
                characters = "" + counter;
                tvCharacterCount.setText(characters);
                adjustColors();
            }
        });
    }

    private void adjustColors(){
        if (counter > 19) {
            tvCharacterCount.setTextColor(Color.parseColor("#CCCCCC")); // Light grey text
        } else {
            tvCharacterCount.setTextColor(Color.parseColor("#DD1111")); // Red text
            if (counter < 0){
                btnTweet.setBackgroundColor(Color.parseColor("#EEEEEE")); // White button, looks deactivated
            } else {
                btnTweet.setBackgroundColor(Color.parseColor("#55ACEE")); // "Twitter blue" button
            }
        }
    }

    public void onTweet(View view){
        post = etPost.getText().toString();
        if (counter < 0 || counter == 140) {
            Toast.makeText(getApplicationContext(), "Too many characters!", Toast.LENGTH_SHORT).show();
        }
        else {
            client.postTweet(post, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                    tweet = Tweet.fromJSON(json);
                    submitTweet(tweet);
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject responseString) {
                }
            });
        }
    }

    private void submitTweet(Tweet tweet){
        Intent postInfo = new Intent();
        postInfo.putExtra("tweet", tweet);
        setResult(RESULT_OK, postInfo);
        finish();
    }


}
