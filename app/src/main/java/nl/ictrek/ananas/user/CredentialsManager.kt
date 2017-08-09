package nl.ictrek.ananas.user

import android.content.Context
import android.content.IntentSender
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import nl.ictrek.ananas.util.showToast
import org.matrix.androidsdk.HomeserverConnectionConfig
import org.matrix.androidsdk.rest.callback.SimpleApiCallback
import org.matrix.androidsdk.rest.client.LoginRestClient
import org.matrix.androidsdk.rest.model.MatrixError
import org.matrix.androidsdk.rest.model.login.Credentials
import java.lang.Exception


/**
 * Object for managing Matrix credentials
 */
object CredentialsManager : GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    val RC_SAVE = 1
    val RC_HINT = 2
    val RC_READ = 3
    private val TAG = "CredentialsManager"
    private val PREFS_FILE = "Credentials"

    override fun onConnectionFailed(result: ConnectionResult) {

    }

    override fun onConnected(bundle: Bundle?) {

    }

    override fun onConnectionSuspended(result: Int) {

    }

    /**
     * Sign in to Matrix
     *
     * @param email User mailaddress
     * @param password User password
     * @param hsConfig The homeserverconnection config
     * @param callback The callback class.
     *
     * @return Returns true on successful sign-in and false on non-successful sign-in.
     */
    fun signIn(email: String, password: String, homeServerUrl: String, activity: FragmentActivity, saveInSmartLock: Boolean = true, callback: ((success: Boolean, credentials: Credentials?) -> Unit)? = null) {
        LoginRestClient(HomeserverConnectionConfig(Uri.parse(homeServerUrl))).loginWithUser(email, password, object : SimpleApiCallback<Credentials>() {
            override fun onSuccess(info: Credentials) {
                if (saveInSmartLock) {
                    val credentialsApiClient = GoogleApiClient.Builder(activity)
                            .addConnectionCallbacks(this@CredentialsManager)
                            .enableAutoManage(activity, this@CredentialsManager)
                            .addApi(Auth.CREDENTIALS_API)
                            .build()

                    // save the credentials in Google Smart Lock
                    Auth.CredentialsApi.save(
                            credentialsApiClient,
                            Credential.Builder(email)
                                    .setAccountType("matrix")
                                    .setPassword(password)
                                    .build()
                    ).setResultCallback { result ->
                        val status = result.status
                        if (status.isSuccess) {
                            Log.d(TAG, "SAVE: OK")
                            activity.showToast("Credentials saved")
                        } else {
                            if (status.hasResolution()) {
                                // Try to resolve the save request. This will prompt the user if
                                // the credential is new.
                                try {
                                    status.startResolutionForResult(activity, RC_SAVE)
                                } catch (e: IntentSender.SendIntentException) {
                                    // Could not resolve the request
                                    Log.e(TAG, "STATUS: Failed to send resolution.", e)
                                    activity.showToast("Save failed")
                                }

                            } else {
                                // Request has no resolution
                                activity.showToast("Save failed")
                            }
                        }
                    }
                }

                // TODO: Store auth tokens etc.
                getSharedPreferences(activity).edit().apply{
                    putString("matrix_url", homeServerUrl)
                    putBoolean("logged_in", true)
                }.apply()

                // invoke the callback
                callback?.invoke(true, info)
            }

            override fun onMatrixError(e: MatrixError) {
                activity.showToast(e.localizedMessage)
                callback?.invoke(false, null)
            }

            override fun onNetworkError(e: Exception) {
                activity.showToast("Network error")
                callback?.invoke(false, null)
            }

            override fun onUnexpectedError(e: Exception) {
                activity.showToast(e.localizedMessage)
                callback?.invoke(false, null)
            }
        })
    }

    private fun getSharedPreferences(context: Context): SharedPreferences = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)

    fun getMatrixHomeServerUrl(context: Context): String = getSharedPreferences(context).getString("matrix_url" ,"https://matrix.org/")

    fun isLoggedIn(context: Context): Boolean = getSharedPreferences(context).getBoolean("logged_in", false)
}