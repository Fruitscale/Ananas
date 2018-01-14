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
import android.text.InputType
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.view.inputmethod.EditorInfo
import com.fruitscale.ananas.callback.SimplerCallback
import com.fruitscale.ananas.login.AuthenticationType
import com.fruitscale.ananas.login.LoginHandler
import com.fruitscale.ananas.service.SessionService
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import kotlinx.android.synthetic.main.activity_chat.view.*
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.*
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.sdk25.coroutines.onCheckedChange
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.matrix.androidsdk.HomeserverConnectionConfig
import org.matrix.androidsdk.MXSession

/**
 * Login activity for logging in to your account
 *
 * TODO: Add username and phone number login options
 */
class LoginActivity : AppCompatActivity(), ServiceConnection, AnkoLogger {
    var mLoggingIn = false
    var mProgressDialog: AlertDialog? = null
    var mSessionService: SessionService? = null
    var mAdvancedOptionsOpen = false
    var mAuthenticationType = AuthenticationType.EMAIL

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
            if(actionId == EditorInfo.IME_ACTION_GO) {
                loginWithFormData()
            }
            true
        }

        advanced_options.onClick {
            val rotate = RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
                duration = 100
                interpolator = LinearInterpolator()
            }

            if(mAdvancedOptionsOpen) {
                mAdvancedOptionsOpen = false
                server_options.visibility = View.GONE
                dropout_icon.rotation = 0f
                dropout_icon.startAnimation(rotate)
            } else {
                mAdvancedOptionsOpen = true
                server_options.visibility = View.VISIBLE
                dropout_icon.rotation = 180f
                dropout_icon.startAnimation(rotate)
            }
        }

        handleAuthenticationType()

        select_identification_method.onCheckedChange { group, checkedId ->
            handleAuthenticationType(checkedId)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(this)
    }

    fun handleAuthenticationType(checkId: Int = select_identification_method.checkedRadioButtonId) {
        mAuthenticationType = when(checkId) {
            R.id.select_email -> AuthenticationType.EMAIL
            R.id.select_username -> AuthenticationType.USERNAME
            R.id.select_pid3 -> AuthenticationType.PID3
            R.id.select_phone -> AuthenticationType.PHONE
            else -> AuthenticationType.EMAIL
        }

        identification_pid3_container.visibility = View.GONE

        when(mAuthenticationType) {
            AuthenticationType.USERNAME -> {
                identification_main.setHint(R.string.username_hint)
                identification_main.inputType = InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE
            }
            AuthenticationType.EMAIL -> {
                identification_main.setHint(R.string.email_hint)
                identification_main.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            }
            AuthenticationType.PHONE -> {
                identification_main.setHint(R.string.phone_hint)
                identification_main.inputType = InputType.TYPE_CLASS_PHONE
            }
            AuthenticationType.PID3 -> {
                identification_main.setHint(R.string.pid3_hint)
                identification_main.inputType = InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE
                identification_pid3_container.visibility = View.VISIBLE
                identification_pid3_type.requestFocus()
            }
        }
    }

    fun loginWithFormData() {
        login(server.text.toString(), identification_main.text.toString(), password.text.toString())
    }

    fun login(homeserverUrl: String, identification: String, password: String) {
        if(mLoggingIn) {
            return
        }

        mLoggingIn = true

        mProgressDialog = indeterminateProgressDialog(R.string.logging_in, R.string.please_wait).apply { setCancelable(false) }

        val callback =
                SimplerCallback<MXSession>(
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
                )

        when(mAuthenticationType) {
            AuthenticationType.USERNAME -> {
                LoginHandler(this).newSessionWithUsername(HomeserverConnectionConfig(Uri.parse(homeserverUrl)), identification, password, { credentials -> mSessionService?.mSessionManager?.onTokenCorrupted(credentials) }, callback)
            }
            AuthenticationType.EMAIL -> {
                LoginHandler(this).newSessionWith3Pid(HomeserverConnectionConfig(Uri.parse(homeserverUrl)), "email", identification, password, { credentials -> mSessionService?.mSessionManager?.onTokenCorrupted(credentials) }, callback)
            }
            AuthenticationType.PHONE -> {
                // splitting phone number from: https://stackoverflow.com/a/16835306

                val phoneUtil = PhoneNumberUtil.getInstance()
                try {
                    val number = phoneUtil.parse(identification, "")
                    val countryCode = "+${number.countryCode}"
                    val phone = "${number.nationalNumber}"

                    LoginHandler(this).newSessionWithPhone(HomeserverConnectionConfig(Uri.parse(homeserverUrl)), phone, countryCode, password, { credentials -> mSessionService?.mSessionManager?.onTokenCorrupted(credentials) }, callback)
                } catch (e: NumberParseException) {
                    // phone number incorrect
                    longSnackbar(contentView!!, R.string.phone_number_invalid)
                    doneLoggingIn()
                }
            }
            AuthenticationType.PID3 -> {
                LoginHandler(this).newSessionWith3Pid(HomeserverConnectionConfig(Uri.parse(homeserverUrl)), identification_pid3_type.editText.toString(), identification, password, { credentials -> mSessionService?.mSessionManager?.onTokenCorrupted(credentials) }, callback)
            }
        }
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
