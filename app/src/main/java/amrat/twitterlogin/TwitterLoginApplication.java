package amrat.twitterlogin;

import android.app.Application;

import com.twitter.sdk.android.core.Twitter;

public class TwitterLoginApplication extends Application {

    public void onCreate() {
        Twitter.initialize(this);
    }
}