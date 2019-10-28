package com.appsinventiv.toolsbazzaradmin.Activities.AppSettings.BannersPackage;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.AppSettings.Settings;
import com.appsinventiv.toolsbazzaradmin.Activities.Customers.SellerModel;
import com.appsinventiv.toolsbazzaradmin.Adapters.PickedPicturesAdapter;
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

public class SellerBanners extends AppCompatActivity {


    DatabaseReference mDatabase;
    StorageReference mStorageRef;
    private static final int REQUEST_CODE_CHOOSE = 23;
    PickedPicturesAdapter adapter;
    RecyclerView recyclerView;
    Bundle extras;

    List<Uri> mSelected;
    ArrayList<String> imageUrl = new ArrayList<>();
    ArrayList<BannerPicsModel> banners = new ArrayList<>();
    ArrayList<String> selectedAdImages = new ArrayList<>();

    Button update, pick;
    RecyclerView recyclerviewPics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_settings);
        this.setTitle("Edit Banners");
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
                        Matisse.from(SellerBanners.this)
                                .choose(MimeType.allOf())
                                .countable(true)
                                .maxSelectable(20)
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
                    for (String img : selectedAdImages) {

                        putPictures(img, "" + "", count);
                        count++;

                    }
                    CommonUtils.showToast("Uploading..");

                }
            }
        });


    }

    private void setUpAlreadyPics() {
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(SellerBanners.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerviewPics.setLayoutManager(horizontalLayoutManagaer);
        SelectedImagesAdapter adapter = new SelectedImagesAdapter(SellerBanners.this, banners, new SelectedImagesAdapter.ChooseOption() {
            @Override
            public void onDeleteClicked(BannerPicsModel images, int position) {
                showDeleteAlert(images);
            }


        });
        recyclerviewPics.setAdapter(adapter);
    }

    private void showDeleteAlert(final BannerPicsModel bannerPicsModel) {

        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = layoutInflater.inflate(R.layout.alert_dialog_curved, null);

        dialog.setContentView(layout);

        TextView message = layout.findViewById(R.id.message);
        TextView no = layout.findViewById(R.id.no);
        TextView yes = layout.findViewById(R.id.yes);

        message.setText("Do you want to delete this banner? ");

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                mDatabase.child("Settings/SellerBanners").child("" + bannerPicsModel.getId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Deleted");
                    }
                });

            }
        });


        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });



        dialog.show();

    }

    private void getPicsFromDb() {
        mDatabase.child("Settings").child("SellerBanners").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    banners.clear();
                    recyclerviewPics.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        BannerPicsModel model = snapshot.getValue(BannerPicsModel.class);
                        if (model != null) {
                            banners.add(model);

                        }
                    }
                    adapter.notifyDataSetChanged();

                }
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
                        String key = mDatabase.push().getKey();
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        mDatabase.child("Settings").child("SellerBanners").child(key)
                                .setValue(new BannerPicsModel(key, "" + downloadUrl, "", count)).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        selectedAdImages.clear();
        if (data != null) {
            if (requestCode == REQUEST_CODE_CHOOSE) {
                recyclerView.setVisibility(View.VISIBLE);

                mSelected = Matisse.obtainResult(data);
                for (Uri img :
                        mSelected) {
//                    selectedAdImages.add(new BannerPicsModel());
                    adapter.notifyDataSetChanged();
                    CompressImage compressImage = new CompressImage(SellerBanners.this);
                    selectedAdImages.add(compressImage.compressImage("" + img));
                }
                adapter.notifyDataSetChanged();

            }

            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void showPickedPictures() {
        selectedAdImages = new ArrayList<>();
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(SellerBanners.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        adapter = new PickedPicturesAdapter(SellerBanners.this, selectedAdImages, new PickedPicturesAdapter.ChooseOption() {
            @Override
            public void onDeleteClicked(int position) {
                selectedAdImages.remove(position);
                adapter.notifyDataSetChanged();
            }


        });
        recyclerView.setAdapter(adapter);
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