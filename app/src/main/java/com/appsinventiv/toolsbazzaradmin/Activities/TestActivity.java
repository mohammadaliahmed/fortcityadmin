package com.appsinventiv.toolsbazzaradmin.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.appsinventiv.toolsbazzaradmin.Activities.Login.Login;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.PrefManager;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;


public class TestActivity extends AppCompatActivity {
    EditText text;
    TextInputLayout adasd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_activity);
//        text = findViewById(R.id.text);
//        adasd = findViewById(R.id.adasd);
//        adasd.setHint("fuck ya");
//
//        text.setHint("sdfsdfsdfsdfsd");
//        text.setText("hi all");
    }
}
