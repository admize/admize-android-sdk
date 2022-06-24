# 목차
1. [Admize 시작하기](#1-Admize-시작하기)
    * [Admize SDK 추가](#admize-sdk-추가)   
    * [AndroidManifest.xml 속성 지정](#androidmanifestxml-속성-지정)
    * [proguard 설정](#proguard-설정-Admize-SDK-포함된-Class는-난독화-시키지-않도록-주의)
    * [AndroidX 설정](#androidx-사용하는-경우)
2. [배너 광고 추가하기](#2-배너-광고-추가하기)
    * [JAVA 방식 base](#자세한-내용은-'AdmizeSample'-참조)
    * [XML 방식](#xml-방식--설정하지-않은-항목들은-기본값으로-설정됩니다)
    * [AdmizeAdRequest 설정방법](#AdmizeAdRequest-설정방법)
3. [전면 광고 추가하기](#3-전면-광고-추가하기)
    * [전면광고 fullScreen Type](#전면광고-fullscreen-type)   
4. [지면 자동생성](#4-지면-자동생성)
5. [아동 대상 서비스 취급용 '태그' 설정](#5-아동-대상-서비스-취급용-태그-설정)
6. [Class Reference](#6-class-reference)

- - - 
# 1. Admize 시작하기


### Admize SDK 추가
	
- 최상위 level build.gradle 에  maven repository 추가

	```clojure
	allprojects {
	    repositories {
        	google()
	        jcenter()
			maven {
				url "s3://repo.admize.io/releases"
				credentials(AwsCredentials) {
					accessKey "AKIA5XG4MQG6UOZLGC4Q"
					secretKey "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"
				}
			}
		}
	    }
	}
	```
	
- app level build.gradle 에 'dependencies'  추가

	```clojure
 	dependencies {
	implementation 'com.google.android.gms:play-services-ads-identifier:17.0.0'
	implementation 'com.google.android.gms:play-services-appset:16.0.0'
    implementation 'io.admize.sdk:admize-sdk:1.0.0'
    }
	```



### AndroidManifest.xml 속성 지정

#### 필수 퍼미션 추가
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="com.google.android.gms.permission.AD_ID"/>
```

#### 네트워크 보안 설정 (targetSdkVersion 28 이상)

`광고 노출 및 클릭이 정상적으로 동작하기 위해서 cleartext 네트워크 설정 필요`

```xml
<application android:usesCleartextTraffic="true" />
```	


#### Activity orientation
- Activity 형식의 전체 화면 랜딩을 지원하기 위해선 아래의 설정으로 추가한다.
- 만약, 설정하지 않으면 화면 전환 시 마다 광고view 가 `초기화` 됩니다.

```xml
<activity
    android:name=".MainActivity"
    android:configChanges="keyboard|keyboardHidden|orientation|screenSize">
</activity>
```

#### Media Uid 설정
- APP 등록 후 부여 받은 media uid를 아래의 설정으로 추가한다.
- 만약, 설정하지 않으면 광고가 표시가 되지 않습니다. AndroidManifest.xml에서 아래와 같이 설정을 하거나, AdmizeAdRequest의 mediaUid() 둘 중 한 곳에 media uid가 반드시 선언되어야 합니다.
```xml
<meta-data
	android:name="io.admize.sdk.android.ads.MEDIA_UID"
	android:value="abc" />
```


### proguard 설정 Admize SDK 포함된 Class는 난독화 시키지 않도록 주의

```clojure
proguard-rules.pro ::
-keep class io.admize.sdk.android.cores.http.admize.response.** {*;}
-keep interface io.admize.sdk.android.cores.http.admize.response.** {*;}
-keep class io.admize.sdk.android.cores.http.admize.request.** {*;}
-keep interface io.admize.sdk.android.cores.http.admize.request.** {*;}

-keep class io.admize.sdk.android.ads.ADMIZE_AD_SIZE {*;}
-keep class io.admize.sdk.android.ads.ADMIZE_AD_TYPE {*;}
-keep class io.admize.sdk.android.ads.AdmizeLog$LogLevel{*;}

-keepclasseswithmembers class io.admize.sdk.android.ads.AdmizeAds {
  public static *** initialize(***);
  public static *** initialize(***, ***);
}

-keepclasseswithmembers class io.admize.sdk.android.ads.AdmizeAdView {
    public <init>(...);
    public *** loadAd(***);
    public *** loadAd(***, ***);
    public *** setAdmizeAdListener(***);
    public *** clearView();
}

-keepclasseswithmembers class io.admize.sdk.android.ads.AdmizeLog {
    public static *** setLogLevel(***);
    public static *** getLogLevel();
    public static *** d(***);
    public static *** i(***);
    public static *** w(***);
    public static *** e(java.lang.String);
    public static *** e(java.lang.Exception);
    public static *** e(java.lang.String, java.lang.Exception);
    public static *** e(java.lang.String, java.lang.Throwable);
}

-keepclasseswithmembers class io.admize.sdk.android.ads.AdmizeAdRequest$Builder {
    public <init>(...);
    public *** admizeAdType(***);
    public *** publisherUid(***);
    public *** placementUid(***);
    public *** mediaUid(***);
    public *** admizeMultiBidsList(***);
    public *** setTest(***);
    public *** coppaEnabled(***);
    public *** build();
}

-keepclasseswithmembers class io.admize.sdk.android.ads.AdmizeInterstitialAd {
    public static *** loadAd(***, ***, ***);
    public *** show(***);
    public *** setAdmizeInterstitialAdListener(***);
}

-keep interface io.admize.sdk.android.ads.AdmizeInterstitialAdListener {*;}
-keep interface io.admize.sdk.android.ads.AdmizeAdListener {*;}
-keep interface io.admize.sdk.android.ads.init.AdmizeOnInitializationCompleteListener {*;}
```
	
### AndroidX 사용하는 경우
```xml
gradle.properties ::
 * android.useAndroidX=true
 * android.enableJetifier=true
```

- 참고 : https://developer.android.com/jetpack/androidx/migrate

# 2. 배너 광고 추가하기

광고를 삽입하고 싶은 layout에 광고를 소스를 삽입(두 가지 방식 제공 : XML 방식, JAVA 방식)

#### `JAVA 방식 base` [자세한 내용은 ‘AdmizeSample’ 참조]
``` java
   private AdmizeAdView admizeAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java);

        // 0. Admize 로그 수준 지정 : 로그의 상세함 순서는 다음과 같다.
        // LogLevel.Verbose > LogLevel.Debug > LogLevel.Info > LogLevel.Warn > LogLevel.Error > LogLevel.None
        AdmizeLog.setLogLevel(LogLevel.Debug);
		
        // 1. 초기화(디바이스 정보, Media ID 가져오기)
        AdmizeAds.initialize(this, new AdmizeOnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(int statusCode, String message) {
                 AdmizeLog.d("statusCode: " + statusCode + ", message: " + message);
            }
        });
	
        // 2. Bid size(SingleBid 또는 MultiBids)
	// 하나의 지면에서 다양한 사이즈를 사용할 때 멀티비즈 요청을 하고자할 때는 다음과 같이 입력합니다.
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
    }
 
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 반드시 호출필요
        admizeAdView.clearView();
    }
```

#### ‘광고 삽입할 부분’.xml 

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:id="@+id/root_view">

</RelativeLayout>
```

#### `XML 방식` : 설정하지 않은 항목들은 기본값으로 설정됩니다.
- banner size는 320x50, 320x100, 300x250만 지원합니다. 
- 
- 
```xml
    <io.admize.sdk.android.ads.AdmizeAdView
        xmlns:ads="http://schemas.android.com/apk/res-auto"
        android:id="@+id/admize_adView_banner"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        ads:textColor="@color/black"
        ads:admizeAdType="BANNER"
        ads:bannerSize="BANNER320x50" // 하나의 지면에서 다양한 사이즈를 사용할 때 멀티비즈 요청을 하고자할 때는 다음과 같이 입력합니다. 예시) 320x50과 320x100 2가지 사이즈로 멀티비즈를 요청하는 경우에 대한 사이즈 설정은 ads:bannerSize="BANNER320x50|BANNER320x100"
        ads:placementUid="1e0a5c9c14b38280c6a53d27b3ada5303c793853"
        ads:publisherUid="666fe91f-4a46-4f9a-95b4-a8255603da69"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent" />
```

#### 주의사항  

- Lifecycle에 따라 AdmizeAdView의 pause/resume/destroy API를 호출하지 않을 경우, 광고 수신에 불이익을 받을 수 있습니다.

### `AdmizeAdRequest 설정방법`

Adinfo|설 명
---|---
admizeAdType()|광고 종류를 설정합니다. 필수 값이며 "BANNER", "INTERSTITIAL"을 설정할 수 있습니다.
publisherUid()|APP 등록 후 APP 소유자에게 발급되는 고유 ID입니다. 필수 값이며 발급 관련한 자세한 내용은 <ops_admize@fsn.co.kr>로 문의바랍니다.
placementUid()|지면 ID로 게재할 광고의 위치에 부여되는 고유 ID입니다. 필수 값이며 발급 관련한 자세한 내용은 <ops_admize@fsn.co.kr>로 문의바랍니다.
mediaUid()|APP 등록 후 부여 받은 media uid 입력합니다. 필수 값이며 만약, 설정하지 않으면 광고가 표시가 되지 않습니다. AndroidManifest.xml에서 아래와 같이 설정을 하거나, AdmizeAdRequest의 mediaUid() 둘 중 한 곳에 media uid가 반드시 선언되어야 합니다.
admizeMultiBidsList()|지원하는 배너 사이즈입니다. admizeAdType을 "BANNER"로 지정한 경우 필수 값이며 "BANNER320x50", "BANNER320x100", "BANNER300x250"을 지원합니다.
setTest()|테스트 모드를 지원합니다. 옵션값이며 true일 경우 테스트 광고가 보여지고, false일 경우 실제 광고가 보여집니다.테스트 광고와 관련한 자세한 내용은 <ops_admize@fsn.co.kr>로 문의바랍니다.

# 3. 전면 광고 추가하기

#### 전면광고 fullScreen Type

```java
    private AdmizeInterstitialAd mAdmizeInterstitialAd;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
	// 0. Admize 로그 수준 지정 : 로그의 상세함 순서는 다음과 같다.
        // LogLevel.Verbose > LogLevel.Debug > LogLevel.Info > LogLevel.Warn > LogLevel.Error > LogLevel.None
        AdmizeLog.setLogLevel(LogLevel.Debug);
	
        // 1. 초기화(디바이스 정보, Media ID 가져오기)
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
 
        // 3. AdmizeInterstitalAd 로드 및 결과 통지 받을 리스너(AdmizeInterstitialAdListener) 등록
        AdmizeInterstitialAd.loadAd(this, admizeAdRequest, new AdmizeInterstitialAdListener(){
            @Override
            public void onAdLoaded(@NonNull AdmizeInterstitialAd admizeInterstitialAd, String message) {
                mAdmizeInterstitialAd = admizeInterstitialAd;
                Toast.makeText(getApplicationContext(), getClass().getSimpleName() + ".onAdLoaded()", Toast.LENGTH_LONG).show();
                if(mAdmizeInterstitialAd != null){
                    mAdmizeInterstitialAd.show(MainActivity.this);
                }
            }
 
            @Override
            public void onAdFailedToLoad(int statusCode, String message) {
                AdmizeLog.d("onAdFailedToLoad() with statusCode: " + statusCode + ", message: "+ message);
                Toast.makeText(getApplicationContext(), getClass().getSimpleName() + ".onAdFailedToLoad() with statusCode: " + statusCode + ", message: "+ message, Toast.LENGTH_LONG).show();
            }
        });
    }
```

#### 주의사항

Lifecycle에 따라 pause/resume/destroy API를 호출하지 않을 경우, 광고 수신에 불이익을 받을 수 있습니다.

# 4. 지면 자동생성

새로운 지면의 추가를 원하시는 경우 아래의 두 가지 방법 중 선택합니다.
1) Admize 담당자에게 새로운 지면에 적용할 placement uid 추가 발급 요청
2) 추가할 지면에 placement uid를 직접 생성 후 요청 시 지면 자동 생성
- 새로운 지면에서 요청 발생 시 자동으로 지면이 신규 생성됩니다.
- placement uid 생성 기준은 8자리 이내의 숫자로 생성합니다. (예시 : 1, 12345, 10000001)
- 다른 지면과 placement uid가 중복되지 않도록 주의

# 5. 아동 대상 서비스 취급용 '태그' 설정

아동대상 콘텐츠로 지정한 경우 관심 기반 광고 및 리마케팅 광고 등이 필터링 됩니다.
		
- google families policy : https://play.google.com/about/families/#!?zippy_activeEl=designed-for-families#designed-for-families
- coppa : https://www.ftc.gov/tips-advice/business-center/privacy-and-security/children's-privacy

##### COPPA에 따라 콘텐츠를 아동 대상으로 지정하려면 'coppaEnabled(true)' 로 호출 한다.

```java
AdmizeAdRequest admizeAdRequest =
		new AdmizeAdRequest.Builder()
				.coppaEnabled(true)
				.build();
```
##### COPPA에 따라 콘텐츠를 아동 대상으로 지정하지 않으려면 'coppaEnabled(false)' 로 호출 한다.
```java
AdmizeAdRequest admizeAdRequest =
		new AdmizeAdRequest.Builder()
				.coppaEnabled(false)
				.build();
```
 \* coppaEnabled를 호출하지 않으면 아동 대상 콘텐츠 인 것으로 간주합니다.

# 6. Class Reference

AdmizeLog 로그 생성 클래스
------------------------------
데이터 형||
---|---
LogLevel|로그 수준. enum Verbose, Debug, Info, Warn, Error, None

메소드||
---|---
setLogLevel(LogLevel)|로그 수준 지정
getLogLevel()	|현재 로그 수준
d(String message)|Debug 메시지 출력. LogLevel이 Debug이상일 때만 출력된다.
i(String message)|Info 메시지 출력. LogLevel이 Info이상일 때만 출력된다.
w(String message)|Warn 메시지 출력. LogLevel이 Warn이상일 때만 출력된다.
e(String message)|Error 메시지 출력. LogLevel이 Error일 때만 출력된다.
e(String message, Exception e)|Error 메시지 출력. LogLevel이 Error일 때만 출력된다.
e(String message, Throwable t)|Error 메시지 출력. LogLevel이 Error일 때만 출력된다.


배너 광고
------------------------------
AdmizeAdView[광고 뷰 클래스]||
---|---
setAdmizeAdRequest(AdmizeAdRequest)	|광고 정보 설정
setAdmizeAdListener(AdmizeAdListener)	|AdmizeAdListener 지정
setShowPreExpandableAd(boolean)	|P/E 광고 허용 여부 설정
loadAd(Context)	|광고 요청
clearView()	|광고 소멸

AdmizeAdListener||
---|---
onAdLoaded(String message)    |광고 노출 성공 시 호출됨. 
onAdFailedToLoad(int statusCode, String message)    |광고 노출 실패 시 호출됨. 오류 코드와 내용이 statusCode, message 변수에 설정됨
onAdOpened()    |webView를 통해 랜딩 페이지가 열린 경우 호출됨
onAdClicked()   |광고가 클릭되었을 때 호출됨.
onAdClosed()|광고를 닫았을 때 호출됨.

전면 광고_풀스크린형
---------------------------------
AdmizeInterstitialAd||
---|---
loadAd(Context, AdmizeAdRequest, AdmizeInterstitialAdListener)	|광고 정보 설정
show()	|수신한 전면 광고를 노출

AdmizeInterstitialAdListener||
---|---
onAdLoaded(AdmizeInterstitialAd, String message)	|광고 노출 성공 시 호출됨.
onAdFailedToLoad(int statusCode, String message)	|광고 노출 실패 시 호출됨. 오류 코드와 내용이 statusCode, message 변수에 설정됨

> admize SDK 설치 관련하여 문의 사항은 고객센터 **1544-8867**
> 또는 ops_admize@fsn.co.kr 로 문의주시기 바랍니다.
