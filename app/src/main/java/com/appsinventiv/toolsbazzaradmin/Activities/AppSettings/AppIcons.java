package com.appsinventiv.toolsbazzaradmin.Activities.AppSettings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.Orders.Orders;
import com.appsinventiv.toolsbazzaradmin.Activities.Products.AddProduct;
import com.appsinventiv.toolsbazzaradmin.Activities.Products.ListOfProducts;
import com.appsinventiv.toolsbazzaradmin.Models.CompanyDetailsModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.appsinventiv.toolsbazzaradmin.Utils.CompressImage;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AppIcons extends AppCompatActivity {


    RelativeLayout defaultIcon, eidUlFitr, eidUlAdha, christmas, sinhalaNewYear, diwali, easter;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_icons);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        this.setTitle("Change App Icon");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        defaultIcon = findViewById(R.id.defaultIcon);
        eidUlFitr = findViewById(R.id.eidUlFitr);
        eidUlAdha = findViewById(R.id.eidUlAdha);
        christmas = findViewById(R.id.christmas);
        sinhalaNewYear = findViewById(R.id.sinhalaNewYear);
        diwali = findViewById(R.id.diwali);
        easter = findViewById(R.id.easter);


        defaultIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert("defaultIcon");
            }
        });
        eidUlFitr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert("eidUlFitr");
            }
        });
        eidUlAdha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert("eidUlAdha");
            }
        });
        christmas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert("christmas");
            }
        });

        sinhalaNewYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert("sinhalaNewYear");
            }
        });
        diwali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert("diwali");
            }
        });
        easter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert("easter");
            }
        });

    }

    private void showAlert(final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Set " + key + " as app icon?");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabase.child("Settings").child("appIcon").setValue(key).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("App Icon Changed");
                    }
                });

            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {


            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
