package com.fruitscale.ananas.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.fruitscale.ananas.login.SessionManager

/**
 * Service for handling sessions
 */
class SessionService : Service() {
    private val mBinder = SessionBinder()
    /**
     * The session manager. Use this to retrieve and store sessions.
     */
    val mSessionManager = SessionManager(this)

    override fun onBind(intent: Intent): IBinder = mBinder

    inner class SessionBinder: Binder() {
        internal
        val service: SessionService
            get() = this@SessionService
    }
}