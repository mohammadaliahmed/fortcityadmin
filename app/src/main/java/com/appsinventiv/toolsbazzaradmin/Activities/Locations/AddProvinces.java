package com.appsinventiv.toolsbazzaradmin.Activities.Locations;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.Products.AddMainCategories;
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
import java.util.Arrays;
import java.util.List;

public class AddProvinces extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText provinces;
    Button create, update;
    DatabaseReference mDatabase;
    String country;
    boolean type;
    String provincesList = "";
    ArrayList<String> itemList = new ArrayList<>();
    AddProvinceAdapter adapter;
    CountryModel model;

    ImageView pickFlag, flag;
    StorageReference mStorageRef;
    private static final int REQUEST_CODE_CHOOSE = 23;
    List<Uri> mSelected = new ArrayList<>();
    ArrayList<String> imageUrl = new ArrayList<>();
    TextView shipping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_provinces);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        recyclerView = findViewById(R.id.recyclerView);
        provinces = findViewById(R.id.provinces);
        pickFlag = findViewById(R.id.pickFlag);
        flag = findViewById(R.id.flag);

        create = findViewById(R.id.create);
        update = findViewById(R.id.update);
        shipping = findViewById(R.id.shipping);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        country = getIntent().getStringExtra("country");
        type = getIntent().getBooleanExtra("type", false);
        this.setTitle("Country: " + country);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new AddProvinceAdapter(this, itemList);

        recyclerView.setAdapter(adapter);

        pickFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelected.clear();
                Matisse.from(AddProvinces.this)
                        .choose(MimeType.allOf())
                        .countable(true)
                        .maxSelectable(1)
                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .forResult(REQUEST_CODE_CHOOSE);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (provinces.getText().length() == 0) {
                    provinces.setError("Enter data");
                } else {
                    sendDataToDB();
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (country != null) {
            getDataFromDB();
        }

    }

    private void getDataFromDB() {
        mDatabase.child("Settings").child("Locations").child("Countries").child(type?"international":"local")
                .child(country).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    itemList.clear();
                    provincesList = "";

                    model = dataSnapshot.getValue(CountryModel.class);
                    shipping.setText(model.isShippingCountry() ? "International shipping country" : "Local shipping country");
                    adapter.canGoNext(model.isShippingCountry() ? true : false);
                    try {
                        Glide.with(AddProvinces.this).load(model.getImageUrl()).into(flag);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                    if (model != null) {

                        for (DataSnapshot snapshot : dataSnapshot.child("provinces").getChildren()) {
                            String value = snapshot.getValue(String.class);
                            itemList.add(value);

                            provincesList = provincesList + value + "\n";
                            provinces.setText(provincesList);
                        }

                        adapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void putPictures(String path, final String key) {
        String imgName = Long.toHexString(Double.doubleToLongBits(Math.random()));

        Uri file = Uri.fromFile(new File(path));

        mStorageRef = FirebaseStorage.getInstance().getReference();

        StorageReference riversRef = mStorageRef.child("Photos").child(imgName);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    @SuppressWarnings("VisibleForTests")
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content

                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        mDatabase.child("Settings").child("Locations").child("Countries").child(type ? "international" : "local").child(country).child("imageUrl").setValue("" + downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Glide.with(AddProvinces.this).load(R.drawable.photo).into(pickFlag);

                                adapter.notifyDataSetChanged();
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

    private void sendDataToDB() {
        List<String> provincesList = new ArrayList<String>(Arrays.asList(provinces.getText().toString().split("\n")));

        mDatabase.child("Settings").child("Locations").child("Countries").child(type?"international":"local").child(country)
                .setValue(new CountryModel(
                        model.getCountryName(),
                        model.currencySymbol,
                        model.getCurrencyRate(),
                        model.getMobileCode(),
                        provincesList
                        , model.getImageUrl(),
                        ListOfCountries.internationalShipping
                )).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                mDatabase.child("Settings").child("Locations").child("Countries").child(country).child("isShippingCountry").setValue(ListOfCountries.internationalShipping);
                if (imageUrl.size() > 0) {
                    for (String img : imageUrl) {
                        putPictures(img, "");
                    }
                }
                CommonUtils.showToast("Provinces added");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == REQUEST_CODE_CHOOSE) {

                mSelected = Matisse.obtainResult(data);
                Glide.with(AddProvinces.this).load(mSelected.get(0)).into(pickFlag);
                for (Uri img : mSelected) {
                    CompressImage compressImage = new CompressImage(AddProvinces.this);
                    imageUrl.add(compressImage.compressImage("" + img));
                }

            }

            super.onActivityResult(requestCode, resultCode, data);
        }
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

    @Override
    public void onBackPressed() {
        finish();
    }
}
