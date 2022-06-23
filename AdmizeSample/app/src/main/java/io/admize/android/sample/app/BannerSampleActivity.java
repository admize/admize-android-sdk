package io.admize.android.sample.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import io.admize.android.sample.R;
import io.admize.sdk.android.ads.ADMIZE_AD_SIZE;
import io.admize.sdk.android.ads.ADMIZE_AD_TYPE;
import io.admize.sdk.android.ads.AdmizeAdListener;
import io.admize.sdk.android.ads.AdmizeAdRequest;
import io.admize.sdk.android.ads.AdmizeAdView;
import io.admize.sdk.android.ads.AdmizeAds;
import io.admize.sdk.android.ads.AdmizeLog;
import io.admize.sdk.android.ads.init.AdmizeOnInitializationCompleteListener;

public class BannerSampleActivity extends AppCompatActivity {

    // 배너 코드
    private AdmizeAdView admizeAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_sample);

        findViewById(R.id.request_banner_ad).setOnClickListener(view -> {
            // 0. Admize 로그 수준 지정 : 로그의 상세함 순서는 다음과 같다.
            // LogLevel.Verbose > LogLevel.Debug > LogLevel.Info > LogLevel.Warn > LogLevel.Error > LogLevel.None
            AdmizeLog.setLogLevel(AdmizeLog.LogLevel.Debug);

            // 1. 초기화(디바이스 정보, Media ID 가져오기)
            AdmizeAds.initialize(this, new AdmizeOnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(int statusCode, String message) {
                    AdmizeLog.d("onInitializationComplete() with statusCode: " + statusCode + ", message: "+ message);
                }
            });

            // 2. Bid size(SingleBid 또는 MultiBids)
            List<ADMIZE_AD_SIZE> admizeAdSizeList = new ArrayList<>();
            admizeAdSizeList.add(ADMIZE_AD_SIZE.SMALL_BANNER); // 320x50
            //admizeAdSizeList.add(ADMIZE_AD_SIZE.MEDIUM_RECTANGLE_BANNER); // 320x100
            //admizeAdSizeList.add(ADMIZE_AD_SIZE.LARGE_BANNER); // 300x250

            // 3. AdmizeAdRequest 생성
            AdmizeAdRequest admizeAdRequest =
                    new AdmizeAdRequest.Builder()
                            .admizeAdType(ADMIZE_AD_TYPE.BANNER)
                            .mediaUid("4e67c0824b9039a2b6047d8a5d60cb1c8470f4a5")
                            .publisherUid("666fe91f-4a46-4f9a-95b4-a8255603da69")
                            .placementUid("1e0a5c9c14b38280c6a53d27b3ada5303c793853")
                            .admizeMultiBidsList(admizeAdSizeList)
                            .coppaEnabled(true) // 아동 대상 서비스 취급용 광고 콘텐츠 설정. true이면 아동대상으로만 설정, false이면 미설정. 기본값은 true.
                            .setTest(true)
                            .build();

            // 4. AdmizeAdRequest를 이용, AdmizeAdView 생성.
            admizeAdView = new AdmizeAdView(this);
            admizeAdView.setAdmizeAdRequest(admizeAdRequest);

            RelativeLayout rootView = (RelativeLayout) findViewById(R.id.root_view);
            // 예시 : 화면 하단에 배너 부착
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            rootView.addView(admizeAdView, params);

            // 5. AdmizeAdView 바인딩 및 처리 결과 통지 받을 리스너(AdListener) 등록
            admizeAdView.setAdmizeAdListener(new AdmizeAdListener() {
                @Override
                public void onAdLoaded(String message) {
                    AdmizeLog.d("onAdLoaded()");
                }

                @Override
                public void onAdFailedToLoad(int statusCode, String message) {
                    AdmizeLog.d("onAdFailedToLoad() with statusCode: " + statusCode + ", message: "+ message);
                }

                @Override
                public void onAdOpened() {
                    AdmizeLog.d("onAdOpened()");
                }

                @Override
                public void onAdClicked() {
                    AdmizeLog.d("onAdClicked()");
                }

                @Override
                public void onAdClosed() {
                    AdmizeLog.d("onAdClosed()");
                }
            });

            // 6. AdView 로드
            admizeAdView.loadAd(this);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 반드시 호출필요
        if(admizeAdView != null){
            admizeAdView.clearView();
        }
    }
}