package nl.ictrek.ananas

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.LinearLayout
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.CredentialRequest
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.GoogleApiClient
import kotlinx.android.synthetic.main.activity_login.*
import nl.ictrek.ananas.user.CredentialsManager
import nl.ictrek.ananas.util.showToast


/**
 * Created by jelle on 15/04/2017.
 *
 * Login activity.
 */
class LoginActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    var signInEnabled = true

    private var mDropoutIsOpen: Boolean = false

    private lateinit var mCredentialsApiClient: GoogleApiClient

    private val mCredentialRequest = CredentialRequest.Builder()
            .setPasswordLoginSupported(true)
            .setAccountTypes("matrix")
            .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if(CredentialsManager.isLoggedIn(this))
            moveToMainScreen()

        mCredentialsApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .addApi(Auth.CREDENTIALS_API)
                .build()

        val rotateUp = AnimatorSet()
        rotateUp.play(ObjectAnimator.ofFloat(dropout_icon, "rotation", 0f, 180f))

        val rotateDown = AnimatorSet()
        rotateDown.play(ObjectAnimator.ofFloat(dropout_icon, "rotation", 180f, 0f))

        mDropoutIsOpen = false

        advanced_options.setOnClickListener {
            if (mDropoutIsOpen) {
                rotateDown.start()
                collapseView(server_options)
            } else {
                rotateUp.start()
                expandView(server_options)
            }
            mDropoutIsOpen = !mDropoutIsOpen
        }

        email_sign_in_button.setOnClickListener {
            if(signInEnabled) {
                // verify that all fields are set
                if (email.text.isNotBlank() && password.text.isNotBlank() && server.text.isNotBlank()) {
                    signInEnabled = false
                    // email, password and server are not blank or null.

                    CredentialsManager.signIn(email.text.toString(), password.text.toString(), CredentialsManager.getMatrixHomeServerUrl(this), this) { success, credentials ->
                        signInEnabled = true
                        if(success)
                            moveToMainScreen()
                    }
                } else {
                    if (email.text.isBlank())
                        showToast("Email cannot be empty")
                    else if (password.text.isBlank())
                        showToast("Password cannot be empty")
                    else if (server.text.isBlank())
                        showToast("Matrix server url cannot be empty")
                }
            } else {
                showToast("Please wait for the previous sign in to complete")
            }
        }

        // TODO: Pre-fill the login form
    }

    fun moveToMainScreen() {
        startActivity(
                Intent(this, MainActivity::class.java)
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == CredentialsManager.RC_READ){
            if(resultCode == RESULT_OK) {
                val credential: Credential = data.getParcelableExtra(Credential.EXTRA_KEY)

                signInEnabled = false
                CredentialsManager.signIn(credential.id,
                        credential.password ?: "",
                        CredentialsManager.getMatrixHomeServerUrl(this),
                        this
                        ) { success, credentials ->
                    signInEnabled = true
                    moveToMainScreen()
                }
            } else {
                Log.e(TAG, "Credential Read: NOT OK")
                showToast("Credential read failed")
            }
        }
    }

    override fun onConnectionFailed(result: ConnectionResult) {
        showToast("Connection failed")
    }

    override fun onConnected(bundle: Bundle?) {
        Log.d(TAG, "onConnected")

        signInEnabled = false

        Auth.CredentialsApi.request(mCredentialsApiClient, mCredentialRequest).setResultCallback { credentialRequestResult ->
            if(credentialRequestResult.status.isSuccess) {
                Log.d(TAG, "Got credentials")
                CredentialsManager.signIn(credentialRequestResult.credential.id,
                        credentialRequestResult.credential.password ?: "",
                        CredentialsManager.getMatrixHomeServerUrl(this),
                        this) { success, credentials ->
                    signInEnabled = true
                    if(success)
                        moveToMainScreen()
                }
            } else if(credentialRequestResult.status.statusCode == CommonStatusCodes.SIGN_IN_REQUIRED) {
                // This is most likely the case where the user does not currently
                // have any saved credentials and thus needs to provide a username
                // and password to sign in.
                Log.d(TAG, "Did not receive credentials")
                signInEnabled = true
            } else {
                Log.w(TAG, "Unrecognized status code: " + credentialRequestResult.status.statusCode);
                signInEnabled = true
            }
        }

    }

    override fun onConnectionSuspended(cause: Int) {
        Log.d(TAG, "onConnectionSuspended: " + cause)
    }

    companion object {
        private val TAG = "LoginActivity"

        val INTERPOLATED_TIME_END = 1

        fun expandView(view: View) {
            view.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            val targetHeight = view.measuredHeight

            // Older versions of android (pre API 21) cancel animations for views with a height of 0.
            view.layoutParams.height = 1
            view.visibility = View.VISIBLE
            val animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                    view.layoutParams.height = if (interpolatedTime == 1f) {
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    } else {
                        (targetHeight * interpolatedTime).toInt()
                    }
                    view.requestLayout()
                }

                override fun willChangeBounds(): Boolean = true
            }

            // 1dp/ms
            animation.duration = (targetHeight / view.context.resources.displayMetrics.density).toLong()
            view.startAnimation(animation)
        }

        fun collapseView(view: View) {
            val initialHeight = view.measuredHeight

            val animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                    // interpolatedTime - The value of the normalized time (0.0 to 1.0) after it has
                    // been run through the interpolation function.
                    if (interpolatedTime == INTERPOLATED_TIME_END.toFloat()) {
                        view.visibility = View.GONE
                    } else {
                        view.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                        view.requestLayout()
                    }
                }

                override fun willChangeBounds(): Boolean = true
            }

            // 1dp/ms
            animation.duration = (initialHeight / view.context.resources.displayMetrics.density).toInt().toLong()
            view.startAnimation(animation)
        }
    }
}
