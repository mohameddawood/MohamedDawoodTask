package com.task.football.fixtures.data.db

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.task.football.fixtures.data.models.MatchesItem
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


private val Context.dataStore by preferencesDataStore("settings")

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext val appContext: Context) {
    private val dataStore = appContext.dataStore


    suspend fun saveMatches(matchesItem: MatchesItem) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("${matchesItem.id}")] = Gson().toJson(matchesItem)
        }
    }
    suspend fun saveMatchToFavorites(matchesItem: MatchesItem) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("${matchesItem.id}")] = Gson().toJson(matchesItem)
        }
    }

    suspend fun removeMatchToFavorites(matchesItem: MatchesItem) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("${matchesItem.id}")] = Gson().toJson(matchesItem)
        }
    }

    fun isKeyStored(key: Preferences.Key<String>): Flow<Boolean>  =
        dataStore.data.map {
                preference -> preference.contains(key)
        }.catch {

        }

    fun getFavs() = dataStore.data
}