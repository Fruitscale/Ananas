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
    private boolean dropoutRotatedUp;
    private View dropoutButton;
    private ImageView dropoutIcon;
    private View dropoutMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dropoutButton = findViewById(R.id.advanced_options);
        dropoutIcon = (ImageView) findViewById(R.id.dropout_icon);
        dropoutMenu = findViewById(R.id.server_options);

        final AnimatorSet rotateUp = new AnimatorSet();
        rotateUp.play(ObjectAnimator.ofFloat(dropoutIcon, "rotation", 0, 180));

        final AnimatorSet rotateDown = new AnimatorSet();
        rotateDown.play(ObjectAnimator.ofFloat(dropoutIcon, "rotation", 180, 0));

        dropoutRotatedUp = false;

        dropoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dropoutRotatedUp) {
                    rotateDown.start();
                    dropoutRotatedUp = false;
                    collapseView(dropoutMenu);
                } else {
                    rotateUp.start();
                    dropoutRotatedUp = true;
                    expandView(dropoutMenu);
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
                if (interpolatedTime == 1) {
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
