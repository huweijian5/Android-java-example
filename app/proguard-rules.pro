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

#打印混淆信息
#-verbose
#
#-dontwarn android.support.annotation.Keep
##保留注解，如果不添加改行会导致我们的@Keep注解失效
#-keepattributes *Annotation*
#-keep @android.support.annotation.Keep class **{
#@android.support.annotation.Keep <fields>;
#}