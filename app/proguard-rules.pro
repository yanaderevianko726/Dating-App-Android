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
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class io.agora.**{*;}
-keep class * implements com.android.vending.billing.IInAppBillingService { *; }
-keepclasseswithmembers public class com.flutterwave.raveandroid.** { *; }
-dontwarn com.flutterwave.raveandroid.card.CardFragment
-dontwarn java.lang.instrument.ClassFileTransformer
-dontwarn sun.misc.SignalHandler

## don't waring Kotlin
-dontwarn kotlin.jvm.internal.**
-dontwarn kotlin.jvm.functions.**
-dontwarn kotlinx.coroutines.**
-dontwarn kotlin.coroutines.**