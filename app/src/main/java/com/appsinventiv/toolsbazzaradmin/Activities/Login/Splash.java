package com.appsinventiv.toolsbazzaradmin.Activities.Login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.appsinventiv.toolsbazzaradmin.Activities.CategoryImages;
import com.appsinventiv.toolsbazzaradmin.Activities.Employees.PendingApproval;
import com.appsinventiv.toolsbazzaradmin.Activities.MainPage.MainActivity;
import com.appsinventiv.toolsbazzaradmin.Activities.ProductImages;
import com.appsinventiv.toolsbazzaradmin.Activities.TestActivity;
import com.appsinventiv.toolsbazzaradmin.Activities.Welcome;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.SharedPrefs;

public class Splash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }


        ImageView myImageView = (ImageView) findViewById(R.id.logo);
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        myImageView.startAnimation(myFadeInAnimation);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if (SharedPrefs.getEmployee() != null) {
                    if (!SharedPrefs.getEmployee().isApproved()) {
                        Intent i = new Intent(Splash.this, PendingApproval.class);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(Splash.this, MainActivity.class);
//                        Intent i = new Intent(Splash.this, CategoryImages.class);
                        startActivity(i);
                        finish();
                    }
                } else {
//                Intent i = new Intent(Splash.this, TestActivity.class);
                    Intent i = new Intent(Splash.this, Welcome.class);
                    startActivity(i);


                    // close this activity
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}
