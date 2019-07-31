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
import java.util.List;

public class CompanySettings extends AppCompatActivity {
    private static final int REQUEST_CODE_CHOOSE = 23;
    EditText name, address, telephone, phone, email;
    Button update;
    DatabaseReference mDatabase;
    CollapsingToolbarLayout collapsing_toolbar;

    AppBarLayout app_bar_layout;
    Button changeCover;
    ImageView coverImage;
    private List<Uri> mSelected = new ArrayList<>();
    private ArrayList<String> imageUrl = new ArrayList<>();
    StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_settings);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getPermissions();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        mStorageRef = FirebaseStorage.getInstance().getReference();


        this.setTitle("Edit company details");
        changeCover = findViewById(R.id.changeCover);
        coverImage = findViewById(R.id.coverImage);
        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        telephone = findViewById(R.id.telephone);
        phone = findViewById(R.id.mobile);
        update = findViewById(R.id.update);
        email = findViewById(R.id.email);
        app_bar_layout = findViewById(R.id.app_bar_layout);
        collapsing_toolbar = findViewById(R.id.collapsing_toolbar);


        changeCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initMatise();
            }
        });


        app_bar_layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == -collapsing_toolbar.getHeight() + toolbar.getHeight()) {
                    //toolbar is collapsed here
                    //write your code here
                    collapsing_toolbar.setTitle("Settings");
                    collapsing_toolbar.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                    collapsing_toolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.colorBlack));


                } else {
                    collapsing_toolbar.setTitle("");
                }

            }
        });


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

    private void initMatise() {
        mSelected.clear();
        imageUrl.clear();
        Matisse.from(CompanySettings.this)
                .choose(MimeType.allOf())
                .countable(true)
                .maxSelectable(1)
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null) {
            if (requestCode == REQUEST_CODE_CHOOSE) {

                mSelected = Matisse.obtainResult(data);
                Glide.with(CompanySettings.this).load(mSelected.get(0)).into(coverImage);
                for (Uri img : mSelected) {
                    CompressImage compressImage = new CompressImage(CompanySettings.this);
                    imageUrl.add(compressImage.compressImage("" + img));
                }

            }

            super.onActivityResult(requestCode, resultCode, data);
        }
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
                        try {
                            Glide.with(CompanySettings.this).load(model.getCoverPicUrl()).into(coverImage);
                        } catch (Exception e) {

                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendDataToServer() {
        mDatabase.child("Settings").child("CompanyDetails").setValue(new CompanyDetailsModel(
                name.getText().toString(),
                address.getText().toString(),
                telephone.getText().toString(),
                phone.getText().toString(),
                email.getText().toString()

        )).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Updated");
                if (mSelected.size() > 0) {
                    putPictures(imageUrl.get(0));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                CommonUtils.showToast("Error: " + e.getMessage());
            }
        });
    }

    public void putPictures(String path) {
        CommonUtils.showToast("Uploading picture..");
        String imgName = Long.toHexString(Double.doubleToLongBits(Math.random()));

        Uri file = Uri.fromFile(new File(path));


        StorageReference riversRef = mStorageRef.child("Photos").child(imgName);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    @SuppressWarnings("VisibleForTests")
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content

                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        mDatabase.child("Settings").child("CompanyDetails").child("coverPicUrl").setValue("" + downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                CommonUtils.showToast("Picture Uploaded");
                            }
                        });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        CommonUtils.showToast("There was some error uploading pic");
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

    private void getPermissions() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}
