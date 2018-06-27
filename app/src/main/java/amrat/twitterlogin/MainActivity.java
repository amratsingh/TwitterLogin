package amrat.twitterlogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class MainActivity extends AppCompatActivity {

    private TextView tvUsername, tvEmail;
    private TwitterLoginButton twitterLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getString(R.string.twitter_consumer_api_key), getString(R.string.twitter_consumer_api_secret_key)))
                .debug(true)
                .build();

        com.twitter.sdk.android.core.Twitter.initialize(config);

        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.twitter_login);

        tvUsername = (TextView) findViewById(R.id.username);
        tvEmail = (TextView) findViewById(R.id.email);

        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                tvUsername.setText(result.data.getUserName());

                final TwitterAuthClient authClient = new TwitterAuthClient();
                authClient.requestEmail(result.data, new Callback<String>() {
                    @Override
                    public void success(Result<String> result) {

                        tvEmail.setText(result.data.toString());
                        authClient.cancelAuthorize();
                    }

                    @Override
                    public void failure(TwitterException exception) {
                    }
                });
            }

            @Override
            public void failure(TwitterException exception) {
                exception.printStackTrace();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }
}
