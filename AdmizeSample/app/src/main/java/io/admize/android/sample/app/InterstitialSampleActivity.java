package io.admize.android.sample.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import io.admize.android.sample.R;
import android.os.Bundle;
import android.widget.Toast;

import io.admize.sdk.android.ads.ADMIZE_AD_SIZE;
import io.admize.sdk.android.ads.ADMIZE_AD_TYPE;
import io.admize.sdk.android.ads.AdmizeAdRequest;
import io.admize.sdk.android.ads.AdmizeAds;
import io.admize.sdk.android.ads.AdmizeInterstitialAd;
import io.admize.sdk.android.ads.AdmizeInterstitialAdListener;
import io.admize.sdk.android.ads.AdmizeLog;
import io.admize.sdk.android.ads.init.AdmizeOnInitializationCompleteListener;

public class InterstitialSampleActivity extends AppCompatActivity {

    // 전면 코드
    private AdmizeInterstitialAd mAdmizeInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial_sample);

        findViewById(R.id.request_interstitial_ad).setOnClickListener(view -> {
            // 0. Admize 로그 수준 지정 : 로그의 상세함 순서는 다음과 같다.
            // LogLevel.Verbose > LogLevel.Debug > LogLevel.Info > LogLevel.Warn > LogLevel.Error > LogLevel.None
            AdmizeLog.setLogLevel(AdmizeLog.LogLevel.Debug);

            // 1. 초기화(bundle정보, app name 정보, 디바이스 정보, Media ID 가져오기)
            AdmizeAds.initialize(this, new AdmizeOnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(int statusCode, String message) {
                    AdmizeLog.d("statusCode: " + statusCode + ", message: " + message);
                }
            });

            // 2. AdmizeAdRequest 생성
            AdmizeAdRequest admizeAdRequest =
                    new AdmizeAdRequest.Builder()
                            .admizeAdType(ADMIZE_AD_TYPE.INTERSTITIAL)
                            .mediaUid("4e67c0824b9039a2b6047d8a5d60cb1c8470f4a5")
                            .publisherUid("666fe91f-4a46-4f9a-95b4-a8255603da69")
                            .placementUid("1e0a5c9c14b38280c6a53d27b3ada5303c793853")
                            .setTest(true)
                            .build();

            // 3. 전면광고를 위한 AdmizeInterstitialAd 로드 및 전면광고 요청 결과 통지 받을 리스너(AdmizeInterstitialAdListener) 등록
            AdmizeInterstitialAd.loadAd(this, admizeAdRequest, new AdmizeInterstitialAdListener(){
                @Override
                public void onAdLoaded(@NonNull AdmizeInterstitialAd admizeInterstitialAd, String message) {
                    mAdmizeInterstitialAd = admizeInterstitialAd;
                    Toast.makeText(getApplicationContext(), getClass().getSimpleName() + ".onAdLoaded()", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onAdFailedToLoad(int statusCode, String message) {
                    AdmizeLog.d(".onAdFailedToLoad() with statusCode: " + statusCode + ", message: "+ message);
                    Toast.makeText(getApplicationContext(), getClass().getSimpleName() + ".onAdFailedToLoad() with statusCode: " + statusCode + ", message: "+ message, Toast.LENGTH_LONG).show();
                }
            });
        });

        findViewById(R.id.show_interstitial_Ad).setOnClickListener(view -> {
            if(mAdmizeInterstitialAd != null){
                mAdmizeInterstitialAd.show(InterstitialSampleActivity.this);
            }
        });

    }
}