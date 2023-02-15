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

#do not ignore Nonpublic libs
#产生混淆映射文件
-verbose

#不做预校验
#-dontpreverify

#抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable

#指定混淆算法
-optimizations !code/simplification/cast,!field/*,!class/merging/*

#保留四大组件
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.content.ContentValues
#-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View


#保留support下面所有类
-keep class android.support.**{*;}
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**

#保留R下面的资源
-keep class **.R$*{*;}

#保留枚举类不被混淆
-keepclassmembers enum *{
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
# 保留我们自定义控件（继承自View）不被混淆
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    }

# 保留Parcelable序列化类不被混淆
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
    }

# 保留Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
    }

# 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
    }


##----webview处理----



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
# 注意这里只是谷歌的一个示例，具体需要替换成你自己的各种data、entity、bean等类
-keep class com.hazukie.scheduleviews.iJBridges.** { *; }
-keep class com.hazukie.scheduleviews.net.**{*;}
-keep class com.hazukie.scheduleviews.models.**{*;}

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

##---------------End: proguard configuration for Gson  ----------

##----okio and okhttp ---##
# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*
-dontwarn org.bouncycastle.jsse.BCSSLParameters*
-dontwarn org.bouncycastle.jsse.BCSSLSocket*
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider*
-dontwarn org.conscrypt.Conscrypt$Version*
-dontwarn org.conscrypt.Conscrypt*
-dontwarn org.conscrypt.ConscryptHostnameVerifier*
-dontwarn org.openjsse.javax.net.ssl.SSLParameters*
-dontwarn org.openjsse.javax.net.ssl.SSLSocket*
-dontwarn org.openjsse.net.ssl.OpenJSSE*