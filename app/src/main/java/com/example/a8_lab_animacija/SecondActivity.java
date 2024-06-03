package com.example.a8_lab_animacija;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.transition.TransitionManager;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {
    private AnimatorSet animSetXY;
    private ObjectAnimator translateX;
    private ObjectAnimator translateY;
    private ObjectAnimator scaleX;
    private ObjectAnimator scaleY;
    private View button;
    private ViewGroup layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        button = findViewById(R.id.btnAnimation);
        layout = (ViewGroup) findViewById(R.id.layout);

        layout.setOnTouchListener(onTouchView);

        animSetXY = new AnimatorSet();
        button.setBackgroundColor(Color.BLUE);
    }

    private final Animator.AnimatorListener animSetXYListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(@NonNull Animator animation) {
            button.setBackgroundColor(Color.BLACK);
        }

        @Override
        public void onAnimationEnd(@NonNull Animator animation) {
            button.setBackgroundColor(Color.BLUE);
            button.setEnabled(false);
        }

        @Override
        public void onAnimationCancel(@NonNull Animator animation) { }

        @Override
        public void onAnimationRepeat(@NonNull Animator animation) { }
    };


//    public void moveButton(View view){
//        button = findViewById(R.id.btnAnimation);
//
//
//        //change position
//        RelativeLayout.LayoutParams positionRules =
//                new RelativeLayout.LayoutParams(
//                        RelativeLayout.LayoutParams.WRAP_CONTENT,
//                RelativeLayout.LayoutParams.WRAP_CONTENT);
////
////        positionRules.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//        button.setX(view.getWidth() - 500);
//        button.setY(view.getHeight() - 400);
//      //  button.setLayoutParams(positionRules);
//
//
//        //change size
//        ViewGroup.LayoutParams sizeRules = button.getLayoutParams();
//        sizeRules.width = 450;
//        sizeRules.height = 300;
//        button.setLayoutParams(sizeRules);
//    }

    private final View.OnTouchListener onTouchView = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (!animSetXY.isRunning()) {

                    int centerX = (int) (layout.getWidth() / 2 - button.getX() - button.getWidth()/2);
                    int centerY = (int) (layout.getHeight() / 2 - button.getHeight()/2 - button.getY());
                    animSetXY = new AnimatorSet();

                    translateX = ObjectAnimator.ofFloat(button, "translationX", centerX);
                    translateY = ObjectAnimator.ofFloat(button, "translationY", centerY);

                    scaleX = ObjectAnimator.ofFloat(button, "scaleX", 2f);
                    scaleY = ObjectAnimator.ofFloat(button, "scaleY", 2f);

                    animSetXY.playTogether(translateX, translateY, scaleX, scaleY);
                    animSetXY.setInterpolator(new AccelerateDecelerateInterpolator());

                    animSetXY.addListener(animSetXYListener);
                    animSetXY.setDuration(1000);
                    animSetXY.start();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Animation in progress!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            return true;  // Indicate that the touch event has been handled
        }
    };

}