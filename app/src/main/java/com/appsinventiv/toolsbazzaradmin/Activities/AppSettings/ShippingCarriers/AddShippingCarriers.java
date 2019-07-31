package com.appsinventiv.toolsbazzaradmin.Activities.AppSettings.ShippingCarriers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.Products.EditProduct;
import com.appsinventiv.toolsbazzaradmin.Models.LocationAndChargesModel;
import com.appsinventiv.toolsbazzaradmin.Models.SelectedAdImages;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.appsinventiv.toolsbazzaradmin.Utils.CompressImage;
import com.bumptech.glide.Glide;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddShippingCarriers extends AppCompatActivity {
    private static final int REQUEST_CODE_CHOOSE = 23;
    EditText name, telephone, address, email;
    Button update;
    DatabaseReference mDatabase;
    TextView textName, textPhone;
    ImageView picPicture;

    CircleImageView shippingImage;
    StorageReference mStorageRef;
    private List<Uri> mSelected = new ArrayList<>();
    private ArrayList<String> imageUrl = new ArrayList<>();
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shipping_carrier);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        getPermissions();
        this.setTitle("Create shipping agent");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        textName = findViewById(R.id.textName);
        textPhone = findViewById(R.id.textPhone);
        picPicture = findViewById(R.id.picPicture);
        name = findViewById(R.id.name);
        telephone = findViewById(R.id.telephone);
        address = findViewById(R.id.address);
        shippingImage = findViewById(R.id.shippingImage);
        email = findViewById(R.id.email);
        update = findViewById(R.id.update);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        id = getIntent().getStringExtra("shippingId");
        if (id != null) {
            getDataFromDB();
        } else {

        }

        picPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initMatise();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().length() == 0) {
                    name.setError("Enter name");
                } else if (telephone.getText().length() == 0) {
                    telephone.setError("Enter telephone");
                } else if (address.getText().length() == 0) {
                    address.setError("Enter address");
                } else if (email.getText().length() == 0) {
                    email.setError("Enter email");
                } else {
                    if (id != null) {
                        updateDataToDB();
                    } else {
                        sendDataToDb();
                    }
                }

            }
        });


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

    private void updateDataToDB() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("name", name.getText().toString());
        map.put("telephone", telephone.getText().toString());
        map.put("address", address.getText().toString());
        map.put("email", email.getText().toString());
        mDatabase.child("Settings").child("ShippingCompanies").child(id).updateChildren(map)
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

    private void getDataFromDB() {
        mDatabase.child("Settings").child("ShippingCompanies").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    ShippingCompanyModel model = dataSnapshot.getValue(ShippingCompanyModel.class);
                    if (model != null) {
                        name.setText(model.getName());
                        telephone.setText(model.getTelephone());
                        email.setText(model.getEmail());
                        address.setText(model.getAddress());
                        textPhone.setText(model.getTelephone());
                        textName.setText(model.getName());
                        if (!model.getPicUrl().equalsIgnoreCase("")) {
                            Glide.with(AddShippingCarriers.this).load(model.getPicUrl()).into(shippingImage);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initMatise() {
        mSelected.clear();
        imageUrl.clear();
        Matisse.from(AddShippingCarriers.this)
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
                Glide.with(AddShippingCarriers.this).load(mSelected.get(0)).into(shippingImage);
                for (Uri img : mSelected) {
                    CompressImage compressImage = new CompressImage(AddShippingCarriers.this);
                    imageUrl.add(compressImage.compressImage("" + img));
                }

            }

            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void sendDataToDb() {
        id = name.getText().toString();
        ShippingCompanyModel model = new ShippingCompanyModel(
                name.getText().toString(),
                name.getText().toString(),
                telephone.getText().toString(),
                address.getText().toString(),
                email.getText().toString(),
                ""
        );
        mDatabase.child("Settings").child("ShippingCompanies").child(name.getText().toString()).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Company Added");
                if (mSelected.size() > 0) {
                    putPictures(imageUrl.get(0));
                }

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
                        mDatabase.child("Settings").child("ShippingCompanies").child(id).child("picUrl").setValue("" + downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
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
