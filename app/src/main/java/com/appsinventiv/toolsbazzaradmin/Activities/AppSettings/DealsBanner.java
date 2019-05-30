package com.appsinventiv.toolsbazzaradmin.Activities.AppSettings;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.appsinventiv.toolsbazzaradmin.Adapters.SelectedImagesAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.BannerPicsModel;
import com.appsinventiv.toolsbazzaradmin.Models.SelectedAdImages;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.appsinventiv.toolsbazzaradmin.Utils.CompressImage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DealsBanner extends AppCompatActivity {

    DatabaseReference mDatabase;
    StorageReference mStorageRef;
    private static final int REQUEST_CODE_CHOOSE = 23;
    SelectedImagesAdapter adapter;
    RecyclerView recyclerView;
    Bundle extras;
    RecyclerView recyclerviewPics;


    List<Uri> mSelected;
    ArrayList<String> imageUrl = new ArrayList<>();
    ArrayList<SelectedAdImages> banners = new ArrayList<>();
    ArrayList<SelectedAdImages> selectedAdImages = new ArrayList<>();

    Button update, pick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deals_banner);
        this.setTitle("Edit Deals Banners");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true); getSupportActionBar().setElevation(0);
        }
        getPermissions();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        pick = findViewById(R.id.pick);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerviewPics = findViewById(R.id.recyclerviewPics);

        update = findViewById(R.id.update);
        showPickedPictures();
        setUpAlreadyPics();
        getPicsFromDb();


        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mSelected != null) {
                            mSelected.clear();
                            selectedAdImages.clear();
                            imageUrl.clear();
                        }
                        Matisse.from(DealsBanner.this)
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
            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = 0;
                if (mSelected == null) {
                    CommonUtils.showToast("Please choose banners");
                } else {
                    for (String img : imageUrl) {

                        putPictures(img, "" + "", count);
                        count++;

                    }
                    CommonUtils.showToast("Uploaded");
                    Intent i = new Intent(DealsBanner.this, Settings.class);
                    startActivity(i);
                }
            }
        });


    }

    private void showPickedPictures() {
        selectedAdImages = new ArrayList<>();
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(DealsBanner.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        adapter = new SelectedImagesAdapter(DealsBanner.this, selectedAdImages, new SelectedImagesAdapter.ChooseOption() {
            @Override
            public void onDeleteClicked(SelectedAdImages images, int position) {

            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void setUpAlreadyPics() {
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(DealsBanner.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerviewPics.setLayoutManager(horizontalLayoutManagaer);
        SelectedImagesAdapter adapter = new SelectedImagesAdapter(DealsBanner.this, banners, new SelectedImagesAdapter.ChooseOption() {
            @Override
            public void onDeleteClicked(SelectedAdImages images, int position) {

            }
        });
        recyclerviewPics.setAdapter(adapter);
    }


    private void getPicsFromDb() {
        mDatabase.child("Settings").child("DealsBanners").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null) {
                    recyclerviewPics.setVisibility(View.VISIBLE);
                    BannerPicsModel model = dataSnapshot.getValue(BannerPicsModel.class);
                    if (model != null) {
                        banners.add(new SelectedAdImages(model.getUrl()));
                        adapter.notifyDataSetChanged();
                    }


                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void putPictures(String path, final String key, final int count) {
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
                        mDatabase.child("Settings").child("DealsBanners").child("" + count)
                                .setValue(new BannerPicsModel("" + count, "" + downloadUrl, "", count)).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                        CommonUtils.showToast("There was some error uploading pic");

                    }
                });


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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        selectedAdImages.clear();
        if (data != null) {
            if (requestCode == REQUEST_CODE_CHOOSE) {
                recyclerView.setVisibility(View.VISIBLE);

                mSelected = Matisse.obtainResult(data);
                for (Uri img :
                        mSelected) {
                    selectedAdImages.add(new SelectedAdImages("" + img));
                    adapter.notifyDataSetChanged();
                    CompressImage compressImage = new CompressImage(DealsBanner.this);
                    imageUrl.add(compressImage.compressImage("" + img));
                }

            }

            super.onActivityResult(requestCode, resultCode, data);
        }
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
