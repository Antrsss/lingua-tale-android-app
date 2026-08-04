package com.example.linguatale.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class DataStoreNames {
    AUTH, ACCESS_TOKEN, REFRESH_TOKEN
}

@Singleton
class TokenDataStore @Inject constructor(
    private val context: Context
) {
    private val Context.dataStore by preferencesDataStore(DataStoreNames.AUTH.name)

    companion object {
        val ACCESS_TOKEN = stringPreferencesKey(DataStoreNames.ACCESS_TOKEN.name)
        val REFRESH_TOKEN = stringPreferencesKey(DataStoreNames.REFRESH_TOKEN.name)
    }

    val accessToken: Flow<String?> = context.dataStore.data
        .map { it[ACCESS_TOKEN] }

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        context.dataStore.edit {
            it[ACCESS_TOKEN] = accessToken
            it[REFRESH_TOKEN] = refreshToken
        }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}