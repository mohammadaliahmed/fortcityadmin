package com.appsinventiv.toolsbazzaradmin.Activities.AppSettings;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.AppSettings.ShippingCarriers.AddShippingCarriers;
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
import java.util.HashMap;
import java.util.List;

public class CompanySettings extends AppCompatActivity {
    EditText name, address, telephone, phone, email;
    Button update;
    DatabaseReference mDatabase;

    TextView textPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_settings);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        this.setTitle("Edit company details");
        textPhone = findViewById(R.id.textPhone);
        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        telephone = findViewById(R.id.telephone);
        phone = findViewById(R.id.mobile);
        update = findViewById(R.id.update);
        email = findViewById(R.id.email);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        getDataFromDb();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().length() == 0) {
                    name.setError("Cannot be null");
                } else if (address.getText().length() == 0) {
                    address.setError("Cannot be null");

                } else if (telephone.getText().length() == 0) {
                    telephone.setError("Cannot be null");

                } else if (phone.getText().length() == 0) {
                    phone.setError("Cannot be null");

                } else if (email.getText().length() == 0) {
                    email.setError("Cannot be null");

                } else {
                    sendDataToServer();
                }
            }
        });
    }


    private void getDataFromDb() {
        mDatabase.child("Settings").child("CompanyDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    CompanyDetailsModel model = dataSnapshot.getValue(CompanyDetailsModel.class);
                    if (model != null) {
                        name.setText(model.getName());
                        address.setText(model.getAddress());
                        telephone.setText(model.getTelephone());
                        phone.setText(model.getPhone());
                        email.setText(model.getEmail());
                        textPhone.setText(model.getPhone());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendDataToServer() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name.getText().toString());
        map.put("address", address.getText().toString());
        map.put("telephone", telephone.getText().toString());
        map.put("phone", phone.getText().toString());
        map.put("email", email.getText().toString());
        mDatabase.child("Settings").child("CompanyDetails").updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Updated");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                CommonUtils.showToast("Error: " + e.getMessage());
            }
        });
    }


    @Override
    public void onBackPressed() {

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {

            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
