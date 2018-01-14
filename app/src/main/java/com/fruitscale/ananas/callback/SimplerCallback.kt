package com.fruitscale.ananas.callback

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.matrix.androidsdk.rest.callback.SimpleApiCallback
import org.matrix.androidsdk.rest.model.MatrixError
import java.lang.Exception

/**
 * A simple callback
 *
 * If an onError callback is not given it will just log the error and do nothing
 *
 * @param onMatrixError What to do on a Matrix error
 * @param onNetworkError What do on a network error
 * @param onUnexpectedError What do on an unexpected error
 * @param onSuccess What to do on success
 */
class SimplerCallback<T>(private val onMatrixError: ((MatrixError) -> Unit)? = null,
                         private val onNetworkError: ((Exception) -> Unit)? = null,
                         private val onUnexpectedError: ((Exception) -> Unit)? = null,
                         private val onSuccess: (T) -> Unit): SimpleApiCallback<T>(), AnkoLogger {
    override fun onMatrixError(e: MatrixError) {
        if(onMatrixError == null) {
            error { "Matrix error: $e" }
        } else {
            onMatrixError.invoke(e)
        }
    }

    override fun onNetworkError(e: Exception) {
        if(onNetworkError == null) {
            error { "Network error: $e" }
        } else {
            onNetworkError.invoke(e)
        }
    }

    override fun onUnexpectedError(e: Exception) {
        if(onUnexpectedError == null) {
            error { "Unexpected error: $e" }
        } else {
            onUnexpectedError.invoke(e)
        }
    }

    override fun onSuccess(info: T) {
        onSuccess.invoke(info)
    }

    fun <U> copyOnErrors(onSuccess: (U) -> Unit) = SimplerCallback(onMatrixError, onNetworkError, onUnexpectedError, onSuccess)
}