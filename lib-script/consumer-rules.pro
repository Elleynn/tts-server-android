-keepclassmembers class * {
    @com.elleynn.script.annotation.ScriptInterface <methods>;
}

-keep class com.elleynn.script.runtime.**{ *;}
-keep class com.elleynn.script.simple.**{ *;}

-keep class org.mozilla.javascript.**  { *; }