package com.fruitscale.ananas.login

import android.content.Context
import com.fruitscale.ananas.store.LoginStorage
import org.matrix.androidsdk.MXSession
import org.matrix.androidsdk.rest.model.login.Credentials

/**
 * Sessions should be managed with the [SessionManager]
 */
class SessionManager(val context: Context) {
    // UserId + Session
    private val sessions = hashMapOf<String, MXSession>()
    private var currentSession: MXSession? = null

    /**
     * Get all the sessions
     */
    fun getSessions() = sessions.values.toTypedArray()

    /**
     * Get the session for [userId]
     *
     * @param userId The user id
     *
     * @return The session for the user with this id
     */
    fun getSessionByUserId(userId: String) = sessions[userId]

    /**
     * Add or replace a [session] in the list of sessions
     *
     * @param session The session
     */
    fun addSession(session: MXSession) {
        sessions.put(session.credentials.userId, session)
    }

    /**
     * Load the sessions from the config into the list of sessions.
     *
     * @return The sessions from the config
     */
    fun loadSessionsFromConfig(): List<MXSession> {
        getSessionsFromConfig(context).let {
            it.forEach {
                addSession(it)
            }
            return it
        }
    }

    /**
     * Set the current selected session
     *
     * @param userId The user id of the selected session
     */
    fun setCurrentSession(userId: String) {
        currentSession = sessions[userId]
    }

    /**
     * Get the current selected session or the only session in the sessions map
     *
     * @return The current selected session or the only session in the sessions map, else null.
     */
    fun getCurrentSession() = currentSession ?: if(sessions.values.size == 1) sessions.values.firstOrNull() else null

    /**
     * Call this method when the token has been corrupted. This will refresh the token.
     *
     * @param credentials The credentials for the token
     */
    fun onTokenCorrupted(credentials: Credentials) {
        getSessionByUserId(credentials.userId)?.refreshToken()
    }

    /**
     * Get the sessions from the config. Please note that this will not add the sessions to the memory
     *
     * @param context The context
     *
     * @return The sessions from the config
     */
    fun getSessionsFromConfig(context: Context): List<MXSession> {
        val loginHandler = LoginHandler(context)

        return LoginStorage(context).getCredentials().map {
            loginHandler.newSessionWithCredentials(it, it.credentials, { onTokenCorrupted(it) })
        }
    }
}