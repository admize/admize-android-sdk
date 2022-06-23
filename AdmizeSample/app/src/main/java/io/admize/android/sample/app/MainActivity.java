package io.admize.android.sample.app;

import androidx.appcompat.app.AppCompatActivity;

import io.admize.android.sample.R;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.banner_test).setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, BannerSampleActivity.class));
        });

        findViewById(R.id.interstitial_test).setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, InterstitialSampleActivity.class));
        });
    }
}