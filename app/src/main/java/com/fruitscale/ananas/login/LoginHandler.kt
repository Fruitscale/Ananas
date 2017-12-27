package com.fruitscale.ananas.login

import android.content.Context
import com.fruitscale.ananas.callback.SimplerCallback
import org.matrix.androidsdk.HomeserverConnectionConfig
import org.matrix.androidsdk.MXDataHandler
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.data.store.MXMemoryStore
import org.matrix.androidsdk.rest.client.LoginRestClient
import org.matrix.androidsdk.rest.model.login.Credentials

/**
 * Tool for more easily handling logins
 */
class LoginHandler(val context: Context) {
    private fun loginWithUsername(hsConfig: HomeserverConnectionConfig,
                                  username: String,
                                  password: String,
                                  callback: SimplerCallback<Credentials>) {
        LoginRestClient(hsConfig).loginWithUser(username, password, callback)
    }

    private fun loginWithPhone(hsConfig: HomeserverConnectionConfig,
                               phone: String,
                               countryCode: String,
                               password: String,
                               callback: SimplerCallback<Credentials>) {
        LoginRestClient(hsConfig).loginWithPhoneNumber(phone, countryCode, password, callback)
    }

    private fun loginWith3Pid(hsConfig: HomeserverConnectionConfig,
                              type: String,
                              identification: String,
                              password: String,
                              callback: SimplerCallback<Credentials>) {
        LoginRestClient(hsConfig).loginWith3Pid(type, identification, password, callback)
    }

    /**
     * Create a new session with [username] and [password]
     *
     * @param hsConfig The homeserver config
     * @param username The username
     * @param password The password
     * @param onTokenCorrupted What to do when the token is corrupted
     * @param callback The callback
     */
    fun newSessionWithUsername(hsConfig: HomeserverConnectionConfig,
                               username: String,
                               password: String,
                               onTokenCorrupted: (Credentials) -> Unit,
                               callback: SimplerCallback<MXSession>) {
        loginWithUsername(hsConfig, username, password, callback.copyOnErrors { credentials ->
            callback.onSuccess(newSessionWithCredentials(hsConfig.apply { this.credentials = credentials }, credentials, { onTokenCorrupted(credentials) }))
        })
    }

    /**
     * Create a new session with [identification] and [password] for pid3 [type]
     *
     * @param hsConfig The homeserver config
     * @param type The 3Pid type
     * @param identification The identification
     * @param password The password
     * @param onTokenCorrupted What to do when the token is corrupted
     * @param callback The callback
     */
    fun newSessionWith3Pid(hsConfig: HomeserverConnectionConfig,
                           type: String,
                           identification: String,
                           password: String,
                           onTokenCorrupted: (Credentials) -> Unit,
                           callback: SimplerCallback<MXSession>) {
        loginWith3Pid(hsConfig, type, identification, password, callback.copyOnErrors { credentials ->
            callback.onSuccess(newSessionWithCredentials(hsConfig.apply { this.credentials = credentials }, credentials, { onTokenCorrupted(credentials) }))
        })
    }

    /**
     * Create a new session with [phone], [countryCode] and [password]
     *
     * @param hsConfig The homeserver config
     * @param phone The phone number
     * @param countryCode The country code
     * @param password The password
     * @param onTokenCorrupted What to do when the token is corrupted
     * @param callback The callback
     */
    fun newSessionWithPhone(hsConfig: HomeserverConnectionConfig,
                            phone: String,
                            countryCode: String,
                            password: String,
                            onTokenCorrupted: (Credentials) -> Unit,
                            callback: SimplerCallback<MXSession>) {
        loginWithPhone(hsConfig, phone, countryCode, password, callback.copyOnErrors { credentials ->
            callback.onSuccess(newSessionWithCredentials(hsConfig.apply { this.credentials = credentials }, credentials, { onTokenCorrupted(credentials) }))
        })
    }

    /**
     * Create a new session with [credentials]
     *
     * @param hsConfig The homeserver config
     * @param credentials The credentials
     * @param onTokenCorrupted What to do when the token is corrupted
     */
    fun newSessionWithCredentials(hsConfig: HomeserverConnectionConfig,
                                  credentials: Credentials,
                                  onTokenCorrupted: (Credentials) -> Unit) =
            MXSession(
                hsConfig,
                MXDataHandler(
                        MXMemoryStore(),
                        credentials,
                        { onTokenCorrupted(credentials) }
                ),
                context.applicationContext
            )
}