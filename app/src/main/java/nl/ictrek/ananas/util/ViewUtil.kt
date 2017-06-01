package nl.ictrek.ananas.util

import android.view.View
import android.widget.Toast
import nl.ictrek.ananas.R

/**
 * Created by wouter on 5/30/17.
 */
fun View.toastOnClick(message: String = context.getString(R.string.function_not_implemented)) {
    setOnClickListener {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}