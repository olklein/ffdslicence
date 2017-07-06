package com.olklein.ffdslicence;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;

import java.util.concurrent.TimeUnit;

public class SplashActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        SystemClock.sleep(TimeUnit.SECONDS.toMillis(1));

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}