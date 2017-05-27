package nl.ictrek.ananas

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Created by jelle on 15/04/2017.
 *
 * Login activity.
 */
class LoginActivity : AppCompatActivity() {

    private var mDropoutIsOpen: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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
    }

    companion object {
        val INTERPOLATED_TIME_END = 1

        fun expandView(view: View) {
            view.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            val targetHeight = view.measuredHeight

            // Older versions of android (pre API 21) cancel animations for views with a height of 0.
            view.layoutParams.height = 1
            view.visibility = View.VISIBLE
            val animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                    view.layoutParams.height = if (interpolatedTime == 1f)
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    else
                        (targetHeight * interpolatedTime).toInt()
                    view.requestLayout()
                }

                override fun willChangeBounds(): Boolean = true
            }

            // 1dp/ms
            animation.duration = (targetHeight / view.context.resources.displayMetrics.density).toInt().toLong()
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
