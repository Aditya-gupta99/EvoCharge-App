package com.sparklead.evocharge.ui.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.sparklead.evocharge.models.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PrefManager @Inject constructor(@ApplicationContext context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "EvoCharge")
    private val dataStore = context.dataStore
    private val gson = Gson()

    suspend fun saveBooleanValue(key: String, value: Boolean) {
        val dataStoreKey = booleanPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[dataStoreKey] = value
        }
    }

    fun readBooleanValue(key: String): Flow<Boolean> {
        val dataStoreKey = booleanPreferencesKey(key)
        return dataStore.data
            .catch {
                when (it) {
                    is IOException -> {
                        emit(emptyPreferences())
                    }

                    else -> throw it
                }
            }
            .map { preferences ->
                preferences[dataStoreKey] ?: false
            }
    }

    suspend fun saveStringValue(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[dataStoreKey] = value
        }
    }

    fun readStringValue(key: String): Flow<String> {
        val dataStoreKey = stringPreferencesKey(key)
        return dataStore.data
            .catch {
                when (it) {
                    is IOException -> {
                        emit(emptyPreferences())
                    }

                    else -> throw it
                }
            }
            .map { preferences ->
                preferences[dataStoreKey] ?: ""
            }
    }

    suspend fun saveUser(user: User) {
        val dataStoreKey = stringPreferencesKey(Constants.USER)
        dataStore.edit { preferences ->
            preferences[dataStoreKey] = gson.toJson(user)
        }
    }

    suspend fun getSaveUser() : Flow<User>{
        val dataStoreKey = stringPreferencesKey(Constants.USER)
        return dataStore.data
            .catch {
                when (it) {
                    is IOException -> {
                        emit(emptyPreferences())
                    }

                    else -> throw it
                }
            }
            .map { preferences ->
                gson.fromJson(preferences[dataStoreKey],User::class.java) ?: User()
            }
    }
}