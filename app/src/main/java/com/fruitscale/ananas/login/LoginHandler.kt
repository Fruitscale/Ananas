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

    private fun loginWithMail(hsConfig: HomeserverConnectionConfig,
                              mail: String,
                              password: String,
                              callback: SimplerCallback<Credentials>) {
        LoginRestClient(hsConfig).loginWith3Pid("email", mail, password, callback)
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
     * Create a new session with [mail] and [password]
     *
     * @param hsConfig The homeserver config
     * @param mail The mail-address
     * @param password The password
     * @param onTokenCorrupted What to do when the token is corrupted
     * @param callback The callback
     */
    fun newSessionWithMail(hsConfig: HomeserverConnectionConfig,
                           mail: String,
                           password: String,
                           onTokenCorrupted: (Credentials) -> Unit,
                           callback: SimplerCallback<MXSession>) {
        loginWithMail(hsConfig, mail, password, callback.copyOnErrors { credentials ->
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