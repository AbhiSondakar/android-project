package com.ecoloop.di

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class SessionInterceptor(
    private val sessionPrefs: DataStore<Preferences>,
    private val context: Context
) : Interceptor {

    companion object {
        private val KEY_SESSION_ID = stringPreferencesKey("session_id")
        private const val TAG = "SessionInterceptor"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requestBuilder = original.newBuilder()

        val storedSessionId = runBlocking {
            try {
                sessionPrefs.data.first()[KEY_SESSION_ID]
            } catch (e: Exception) {
                null
            }
        }
        if (!storedSessionId.isNullOrBlank()) {
            requestBuilder.addHeader("Cookie", "JSESSIONID=$storedSessionId")
        }

        val request = requestBuilder.build()
        val response = chain.proceed(request)

        val responseSessionId = response.header("X-Session-Id")
        if (!responseSessionId.isNullOrBlank()) {
            runBlocking {
                try {
                    sessionPrefs.edit { prefs ->
                        prefs[KEY_SESSION_ID] = responseSessionId
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to store session ID", e)
                }
            }
        }

        val setCookieHeaders = response.headers("Set-Cookie")
        for (cookie in setCookieHeaders) {
            if (cookie.startsWith("JSESSIONID=")) {
                val jsessionId = cookie.substringAfter("JSESSIONID=").substringBefore(";")
                if (jsessionId.isNotBlank()) {
                    runBlocking {
                        try {
                            sessionPrefs.edit { prefs ->
                                prefs[KEY_SESSION_ID] = jsessionId
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Failed to store JSESSIONID", e)
                        }
                    }
                    break
                }
            }
        }

        if (response.code == 401) {
            runBlocking {
                try {
                    sessionPrefs.edit { prefs ->
                        prefs.remove(KEY_SESSION_ID)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to clear session ID", e)
                }
            }
        }

        return response
    }
}
