package com.appsinventiv.toolsbazzaradmin.Activities.Vendors;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Models.VendorModel;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class AddVendors extends AppCompatActivity {
    private static final int REQUEST_CODE_CHOOSE = 23;
    Button addVendor;
    EditText name, phone, address, email;
    DatabaseReference mDatabase;
    String vendorId;
    TextView textName, textPhone;
    ImageView picPicture;
    CircleImageView image;
    StorageReference mStorageRef;
    private List<Uri> mSelected = new ArrayList<>();
    private ArrayList<String> imageUrl = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vendors);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        this.setTitle("Add Vendor");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();


        vendorId = getIntent().getStringExtra("vendorId");


        addVendor = findViewById(R.id.addVendor);
        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        textName = findViewById(R.id.textName);
        textPhone = findViewById(R.id.textPhone);
        picPicture = findViewById(R.id.picPicture);
        image = findViewById(R.id.image);

        if (vendorId != null) {
            getVendorFromDB();
        }

        picPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                initMatise();
            }
        });

        getPermissions();
        addVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().length() == 0) {
                    name.setError("Enter vendor name");
                } else if (phone.getText().length() == 0) {
                    phone.setError("Enter phone");
                } else if (address.getText().length() == 0) {
                    address.setError("Enter address");
                } else if (email.getText().length() == 0) {
                    email.setError("Enter email");
                } else {
                    if (vendorId != null) {
                        updateDataToDB();
                    } else {

                        sendDataToDB();
                    }


                }

            }
        });


    }

    private void updateDataToDB() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("vendorName", name.getText().toString());
        map.put("vendorPhone", phone.getText().toString());
        map.put("vendorAddress", address.getText().toString());
        map.put("vendorEmail", email.getText().toString());
        mDatabase.child("Vendors").child(vendorId).updateChildren(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Updated");
                        if (mSelected.size() > 0) {
                            putPictures(imageUrl.get(0));
                        }
                    }
                });
    }

    private void sendDataToDB() {
        vendorId = name.getText().toString();
        mDatabase.child("Vendors").child(vendorId).setValue(new VendorModel(
                vendorId,
                name.getText().toString(),
                phone.getText().toString(),
                address.getText().toString(),
                System.currentTimeMillis(),
                email.getText().toString(),
                true
        )).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Vendor Added");
                if (mSelected.size() > 0) {
                    putPictures(imageUrl.get(0));
                }
//                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                CommonUtils.showToast("Error " + e.getMessage());
            }
        });
    }


    private void initMatise() {
        mSelected.clear();
        imageUrl.clear();
        Matisse.from(AddVendors.this)
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
                Glide.with(AddVendors.this).load(mSelected.get(0)).into(image);
                for (Uri img : mSelected) {
                    CompressImage compressImage = new CompressImage(AddVendors.this);
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

    private void getVendorFromDB() {
        mDatabase.child("Vendors").child(vendorId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    VendorModel model = dataSnapshot.getValue(VendorModel.class);
                    if (model != null) {
                        name.setText(model.getVendorName());
                        phone.setText(model.getVendorPhone());
                        email.setText(model.getVendorEmail());
                        address.setText(model.getVendorAddress());
                        textName.setText(model.getVendorName());
                        textPhone.setText(model.getVendorPhone());
                        if (model.getPicUrl() != null) {
                            try {
                                Glide.with(AddVendors.this).load(model.getPicUrl()).into(image);

                            } catch (Exception e) {

                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void putPictures(String path) {
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
                        mDatabase.child("Vendors").child(vendorId).child("picUrl").setValue("" + downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
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
