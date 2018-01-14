package com.fruitscale.ananas.store

import android.content.Context
import com.fruitscale.ananas.exception.config.InvalidCredentialsJsonException
import com.fruitscale.ananas.util.toList
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.json.JSONArray
import org.json.JSONObject
import org.matrix.androidsdk.HomeserverConnectionConfig

/**
 * Use this class to retrieve and remove credentials from the preferences
 */
class LoginStorage(val context: Context): AnkoLogger {
    companion object {
        val PREFERENCES_LOGIN = "Ananas.LoginStorage"
        val PREFERENCES_KEY_CONNECTION_CONFIGS = "PREFERENCES_KEY_CONNECTION_CONFIGS"
    }

    private fun getPreferences() = context.getSharedPreferences(PREFERENCES_LOGIN, Context.MODE_PRIVATE)

    /**
     * Get all the credentials from the preferences
     *
     * @return A list containing all the credentials
     */
    fun getCredentials(): List<HomeserverConnectionConfig> {
        val jsonString = getPreferences().getString(PREFERENCES_KEY_CONNECTION_CONFIGS, null)
                ?: return arrayListOf()

        debug { "Received login json" }

        val jsonArray = JSONArray(jsonString)

        return jsonArray.toList().map {
            if(it is JSONObject) {
                HomeserverConnectionConfig.fromJson(it)
            } else {
                throw InvalidCredentialsJsonException()
            }
        }
    }

    /**
     * Save [credentials] to the preferences
     *
     * @param credentials The credentials
     */
    private fun putCredentials(credentials: List<HomeserverConnectionConfig>) {
        val serialized = JSONArray(credentials.map { it.toJson() }).toString()

        val editor = getPreferences().edit()

        editor.putString(PREFERENCES_KEY_CONNECTION_CONFIGS, serialized)
        editor.apply()
    }

    /**
     * Add credentials to the preferences
     *
     * @param config The credentials
     */
    fun addCredentials(config: HomeserverConnectionConfig) {
        if(config.credentials == null) {
            return
        }

        val credentials = getCredentials().map { it }.plus(config)

        putCredentials(credentials)
    }

    /**
     * remove credentials to the preferences
     *
     * @param config The credentials
     */
    fun removeCredentials(config: HomeserverConnectionConfig) {
        if(config.credentials == null) {
            return
        }

        val credentials = getCredentials().filter { it.credentials.userId != config.credentials.userId }

        putCredentials(credentials)
    }

    /**
     * Replace credentials in the preferences
     *
     * @param config The credentials
     */
    fun replaceCredentials(config: HomeserverConnectionConfig) {
        if(config.credentials == null) {
            return
        }

        val credentials = getCredentials().filter { it.credentials.userId != config.credentials.userId }.plus(config)

        putCredentials(credentials)
    }

    /**
     * Remove all the credentials from the preferences
     */
    fun removeAllCredentials() {
        val editor = getPreferences().edit()

        editor.remove(PREFERENCES_KEY_CONNECTION_CONFIGS)
        editor.apply()
    }
}