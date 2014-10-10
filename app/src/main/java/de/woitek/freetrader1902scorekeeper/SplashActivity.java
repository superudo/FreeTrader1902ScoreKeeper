package de.woitek.freetrader1902scorekeeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;


public class SplashActivity extends Activity {
    /**
     * Duration of wait *
     */
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splashscreen);

        ((TextView) findViewById(R.id.tExplanation)).setText(
                "Based on the game Free Trader by Felbrigg Herriot with the 1902 card set by Malechi\n\nExtended score keeper\nby\nUdo Woitek"
        );

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
