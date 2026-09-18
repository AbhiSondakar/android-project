package com.ecoloop.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        private val KEY_SESSION_ID = stringPreferencesKey("session_id")
        private val KEY_USER_ID = stringPreferencesKey("user_id")
        private val KEY_USER_ROLE = stringPreferencesKey("user_role")
    }

    val sessionId: Flow<String?> = dataStore.data.map { prefs ->
        prefs[KEY_SESSION_ID]
    }

    val userId: Flow<String?> = dataStore.data.map { prefs ->
        prefs[KEY_USER_ID]
    }

    val userRole: Flow<String?> = dataStore.data.map { prefs ->
        prefs[KEY_USER_ROLE]
    }

    val isLoggedIn: Flow<Boolean> = sessionId.map { it != null && it.isNotBlank() }

    suspend fun saveSession(sessionId: String, userId: String, role: String) {
        dataStore.edit { prefs ->
            prefs[KEY_SESSION_ID] = sessionId
            prefs[KEY_USER_ID] = userId
            prefs[KEY_USER_ROLE] = role
        }
    }

    suspend fun clearSession() {
        dataStore.edit { prefs ->
            prefs.clear()
        }
    }

    suspend fun clearIfUnauthorized() {
        dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
