#================== Admize Proguard for Release 적용 코드 시작 ==================
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
#================== Admize Proguard for Release 적용 코드 끝 ==================