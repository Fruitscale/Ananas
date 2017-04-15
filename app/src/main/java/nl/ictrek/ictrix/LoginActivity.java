package nl.ictrek.ictrix;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by jelle on 15/04/2017.
 */

public class LoginActivity extends AppCompatActivity {
    public static final int INTERPOLATED_TIME_END = 1;

    private boolean mDropoutRotatedUp;
    private View mDropoutButton;
    private ImageView mDropoutIcon;
    private View mDropoutMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mDropoutButton = findViewById(R.id.advanced_options);
        mDropoutIcon = (ImageView) findViewById(R.id.dropout_icon);
        mDropoutMenu = findViewById(R.id.server_options);

        final AnimatorSet rotateUp = new AnimatorSet();
        rotateUp.play(ObjectAnimator.ofFloat(mDropoutIcon, "rotation", 0, 180));

        final AnimatorSet rotateDown = new AnimatorSet();
        rotateDown.play(ObjectAnimator.ofFloat(mDropoutIcon, "rotation", 180, 0));

        mDropoutRotatedUp = false;

        mDropoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDropoutRotatedUp = !mDropoutRotatedUp;
                if (mDropoutRotatedUp) {
                    rotateDown.start();
                    collapseView(mDropoutMenu);
                } else {
                    rotateUp.start();
                    expandView(mDropoutMenu);
                }
            }
        });
    }

    public static void expandView(final View view) {
        view.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = view.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        view.getLayoutParams().height = 1;
        view.setVisibility(View.VISIBLE);
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                view.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                view.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        animation.setDuration((int) (targetHeight / view.getContext().getResources().getDisplayMetrics().density));
        view.startAnimation(animation);
    }

    public static void collapseView(final View view) {
        final int initialHeight = view.getMeasuredHeight();

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                // interpolatedTime - The value of the normalized time (0.0 to 1.0) after it has
                // been run through the interpolation function.
                if (interpolatedTime == INTERPOLATED_TIME_END) {
                    view.setVisibility(View.GONE);
                } else {
                    view.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    view.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        animation.setDuration((int) (initialHeight / view.getContext().getResources().getDisplayMetrics().density));
        view.startAnimation(animation);
    }
}
