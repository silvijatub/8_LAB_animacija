package com.example.a8_lab_animacija;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ImageView rocket, rockRed, rockTeal, rockPink;
    private View root;

    private AnimatorSet animSetXY;
    private ObjectAnimator translateX;
    private ObjectAnimator translateY;
    private ObjectAnimator rotation;
    private ObjectAnimator alphaAnimation;
    private ObjectAnimator scaleX;
    private ObjectAnimator scaleY;

    private float deltaX = 0f;
    private float deltaY = 0f;
    private double rocketDirection = 0.0;
    Button second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Request the window feature and full screen before setting content view
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_main);

        second = (Button) findViewById(R.id.button);


        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });


        // Hide the action bar if it's already created
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        rocket = findViewById(R.id.imageView);
        root = findViewById(R.id.rootView);
        rockRed = findViewById(R.id.rock_red);
        rockTeal = findViewById(R.id.rock_teal);
        rockPink = findViewById(R.id.rock_pink);

        root.setOnTouchListener(onTouchView);
        root.setBackgroundColor(Color.BLACK);

        rotation = new ObjectAnimator();
        animSetXY = new AnimatorSet();

        // Start animating rocks
        startRockAnimation(rockTeal);
        startRockAnimation(rockRed);
        startRockAnimation(rockPink);
        startRockAnimation(rockTeal);
        startRockAnimation(rockRed);
        startRockAnimation(rockPink);

    }

    private final Animator.AnimatorListener animSetXYListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(@NonNull Animator animation) {
            rocket.setImageDrawable(getDrawable(R.drawable.rocket_1));
        }

        @Override
        public void onAnimationEnd(@NonNull Animator animation) {
            rocket.setImageDrawable(getDrawable(R.drawable.rocket_0));
            checkCollision();
        }

        @Override
        public void onAnimationCancel(@NonNull Animator animation) { }

        @Override
        public void onAnimationRepeat(@NonNull Animator animation) { }
    };

    private final Animator.AnimatorListener rotationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(@NonNull Animator animation) { }

        @Override
        public void onAnimationEnd(@NonNull Animator animation) {
            animSetXY.start();
        }

        @Override
        public void onAnimationCancel(@NonNull Animator animation) { }

        @Override
        public void onAnimationRepeat(@NonNull Animator animation) { }
    };

    private final View.OnTouchListener onTouchView = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (!rotation.isRunning() && !animSetXY.isRunning()) {
                    deltaX = event.getX() - (rocket.getWidth() / 2);
                    deltaY = event.getY() - (rocket.getHeight() / 2);

                    rocketDirection = Math.atan2(deltaY - rocket.getY(),
                            deltaX - rocket.getX()) * 180 / Math.PI;

                    rotation = ObjectAnimator.ofFloat(rocket, "rotation", (float) (rocketDirection));
                    rotation.addListener(rotationListener);
                    rotation.setDuration(800);
                    rotation.setInterpolator(new AccelerateDecelerateInterpolator());
                    rotation.start();

                    animSetXY = new AnimatorSet();
                    translateX = ObjectAnimator.ofFloat(rocket, "translationX", deltaX);
                    translateY = ObjectAnimator.ofFloat(rocket, "translationY", deltaY);
                    alphaAnimation = ObjectAnimator.ofFloat(rocket, "alpha", 1f, 0.88f, 1f);
                    scaleX = ObjectAnimator.ofFloat(rocket, "scaleX", 1f, 1.06f, 1f);
                    scaleY = ObjectAnimator.ofFloat(rocket, "scaleY", 1f, 1.06f, 1f);
                    animSetXY.playTogether(translateX, translateY, alphaAnimation, scaleX, scaleY);
                    animSetXY.setInterpolator(new AccelerateDecelerateInterpolator());
                    animSetXY.addListener(animSetXYListener);
                    animSetXY.setDuration(1000);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "We're flying!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            return true;  // Indicate that the touch event has been handled
        }
    };

    private void startRockAnimation(final ImageView rock) {
       // final int duration = 1000;
        Random random = new Random();
        float startX = 0;
        int startY = 4000;

        int duration = getRandomNumber(3000, 6000);
        //int duration = random.; //the duration of the animation in ms

       // double direction = Math.random() * 2 * Math.PI;

        ObjectAnimator translateX = ObjectAnimator.ofFloat(rock, "translationX", random.nextInt(1000), random.nextInt(1000));
        ObjectAnimator translateY = ObjectAnimator.ofFloat(rock, "translationY", startY, -200);


        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.playTogether(translateX, translateY);
        animSetXY.setInterpolator(new AccelerateDecelerateInterpolator());
        animSetXY.setDuration(duration);

        animSetXY.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) { }

            @Override
            public void onAnimationEnd(Animator animator) {
                startRockAnimation(rock);  // Recursively call to continue animation
            }

            @Override
            public void onAnimationCancel(Animator animator) { }

            @Override
            public void onAnimationRepeat(Animator animator) { }
        });

     animSetXY.start();
    }

    private void checkCollision() {
        if (isColliding(rocket, rockRed)) {
            rockRed.setVisibility(View.INVISIBLE);
        }
        if (isColliding(rocket, rockTeal)) {
            rockTeal.setVisibility(View.INVISIBLE);
        }
        if (isColliding(rocket, rockPink)) {
            rockPink.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isColliding(View v1, View v2) {
        int[] location1 = new int[2];
        int[] location2 = new int[2];

        v1.getLocationOnScreen(location1);
        v2.getLocationOnScreen(location2);

        int v1_x = location1[0];
        int v1_y = location1[1];
        int v2_x = location2[0];
        int v2_y = location2[1];

        int v1_w = v1.getWidth();
        int v1_h = v1.getHeight();
        int v2_w = v2.getWidth();
        int v2_h = v2.getHeight();

        return v1_x < v2_x + v2_w &&
                v1_x + v1_w > v2_x &&
                v1_y < v2_y + v2_h &&
                v1_y + v1_h > v2_y;
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

}
