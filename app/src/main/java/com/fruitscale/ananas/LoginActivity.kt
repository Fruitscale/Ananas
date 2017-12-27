package com.fruitscale.ananas

import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.EditorInfo
import com.fruitscale.ananas.callback.SimplerCallback
import com.fruitscale.ananas.login.LoginHandler
import com.fruitscale.ananas.service.SessionService
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.*
import org.jetbrains.anko.design.longSnackbar
import org.matrix.androidsdk.HomeserverConnectionConfig

/**
 * Login activity for logging in to your account
 *
 * TODO: Add username and phone number login options
 */
class LoginActivity : AppCompatActivity(), ServiceConnection, AnkoLogger {
    var mLoggingIn = false
    var mProgressDialog: AlertDialog? = null
    var mSessionService: SessionService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // set up session service
        bindService(Intent(this, SessionService::class.java), this, Context.BIND_AUTO_CREATE)

        mLoggingIn = false
        sign_in_button.setOnClickListener {
            loginWithFormData()
        }
        password.setOnEditorActionListener { textView, actionId, keyEvent ->
            if(actionId == EditorInfo.IME_ACTION_GO)
                loginWithFormData()
            true
        }

    }

    override fun onStop() {
        super.onStop()
        unbindService(this)
    }

    fun loginWithFormData() {
        login(server.text.toString(), email.text.toString(), password.text.toString())
    }

    fun login(homeserverUrl: String, username: String, password: String) {
        if(mLoggingIn)
            return
        mLoggingIn = true

        mProgressDialog = indeterminateProgressDialog(R.string.logging_in, R.string.please_wait).apply { setCancelable(false) }

        LoginHandler(this).newSessionWithMail(HomeserverConnectionConfig(Uri.parse(homeserverUrl)), username, password, { credentials -> mSessionService?.mSessionManager?.onTokenCorrupted(credentials) },
                SimplerCallback(
                        {
                            // on matrix error
                            info { "Matrix error with: $it" }

                            doneLoggingIn()
                            showRetrySnackbar()
                        },
                        {
                            // on network error
                            info { "Network error with: $it" }

                            doneLoggingIn()
                            showRetrySnackbar()
                        },
                        {
                            // on unexpected error
                            info { "Unexpected error with: $it" }

                            doneLoggingIn()
                            showRetrySnackbar()

                        },
                        { session ->
                            // on success
                            info { "Successful log in." }

                            doneLoggingIn()

                            //longToast("Welcome, ${session.myUserId}")

                            mSessionService?.mSessionManager?.let {
                                it.addSession(session)
                                it.setCurrentSession(session.myUserId)
                            }

                            startActivity(intentFor<MainActivity>())

                            finish()
                        }
                ))
    }

    fun doneLoggingIn() {
        mLoggingIn = false
        mProgressDialog?.dismiss()
    }

    fun showRetrySnackbar() {
        longSnackbar(contentView!!, R.string.log_in_failed, R.string.retry) {
            loginWithFormData()
        }
    }

    override fun onServiceDisconnected(name: ComponentName) {
        mSessionService = null
    }

    override fun onServiceConnected(name: ComponentName, binder: IBinder) {
        mSessionService = (binder as SessionService.SessionBinder).service
    }
}
