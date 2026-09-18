-keep class com.ecoloop.** { *; }
-keep class com.ecoloop.data.** { *; }
-keepnames class com.ecoloop.**
-dontwarn com.ecoloop.**

# Retrofit
-dontwarn retrofit2.**
-dontwarn okio.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**

# Coroutines
-dontwarn kotlinx.coroutines.**

# Hilt
-dontwarn dagger.hilt.**
-dontwarn dagger.**

# Serialization
-keepclassmembers,allowObfuscation,allowShrinking class kotlinx.** (*), <init>(...)
-keepclassmembers class com.ecoloop.data.remote.dto.** { *; }
-keepclassmembers class com.ecoloop.data.local.entity.** { *; }

# Room
-keep class * extends androidx.room.RoomDatabase
-keep class androidx.room.** { *; }
-dontwarn androidx.room.paging.**

# Gson/Jackson (if any reflection-based JSON is used)
-keepattributes Signature
-keepattributes *Annotation*
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# OkHttp platform
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# Coil
-dontwarn coil.**

# Crash reporting
-dontwarn com.google.firebase.crashlytics.**
