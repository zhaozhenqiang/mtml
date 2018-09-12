# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontwarn **

#webview
-keepclassmembers class * extends android.webkit.WebChromeClient {
    public void openFileChooser(...);
    public void onShowFileChooser(...);
 }

-keep class * extends java.lang.annotation.Annotation { *; }

# 实体类，可能需要fastJson反射
#-keep com.mutoumulao.expo.redwood.entity.**{*;}

# R文件可能被第三方lib通过发射调用
-keepclassmembers class **.R$* {
    public static <fields>;
}

# fastjson
-keep class com.alibaba.fastjson.** { *; }
-keep class sun.misc.Unsafe { *; }
-keepattributes Signature,*Annotation*,InnerClasses
#避免混淆泛型
-keepattributes Singature
# 不混淆注释
-keepattributes *Annotation

#eventbus
-keepclassmembers class ** {
    public void onEvent*(**);
}

# shareSDK
#友盟
-keep public class * extends com.umeng.**
-keep class com.umeng.** { *; }
-keep class com.tencent.**{*;}
-keep class m.framework.**{*;}

-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-keep class com.mob.**{*;}
#-dontwarn com.mob.**
#-dontwarn cn.sharesdk.**
#-dontwarn **.R$*

#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# banner 的混淆代码
-keep class com.youth.banner.** {
    *;
 }


-keep class com.alipay.**{*;}
-keep class org.json.alipay.**{*;}
-keep class com.ta.utdid2.**{*;}
-keep class com.ut.device.**{*;}
-keep class com.alibaba.fastjson.**{*;}


#hx
-keep class com.hyphenate.** {*;}
#-dontwarn  com.hyphenate.**
#另外，demo中发送表情的时候使用到反射，需要keep SmileUtils,注意前面的包名，
#-keep com.mutoumulao.expo.redwood.hx {*;}

# weichatpay
#-libraryjars libs/libammsdk.jar
-keep class com.tencent.** { *;}

# ButterKnife 8.8.1
-keep public class * implements butterknife.Unbinder { public <init>(**, android.view.View); }

-keep class butterknife.*
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

##---------------End: proguard configuration for Gson  ----------

# okhttp
#-dontwarn okhttp3.**
-keep class okhttp3.**{*;}

# okio
#-dontwarn okio.**
-keep class okio.**{*;}
