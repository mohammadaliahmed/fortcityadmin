package com.appsinventiv.toolsbazzaradmin.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.Employees.PendingApproval;
import com.appsinventiv.toolsbazzaradmin.Activities.MainPage.MainActivity;
import com.appsinventiv.toolsbazzaradmin.Activities.Products.AddProduct;
import com.appsinventiv.toolsbazzaradmin.Models.Product;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.appsinventiv.toolsbazzaradmin.Utils.CompressImage;
import com.appsinventiv.toolsbazzaradmin.Utils.SharedPrefs;
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

import java.io.File;
import java.util.ArrayList;

public class ProductImages extends AppCompatActivity {

    DatabaseReference mDatabase;
    ArrayList<Product> productArrayList = new ArrayList<>();

    Button download, upload, compress;
    TextView msg;
    StorageReference mStorageRef;
    String[] listOfFiles;
    ArrayList<CompressUrl> imageUrl = new ArrayList<>();


    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);

        download = findViewById(R.id.download);
        msg = findViewById(R.id.msg);
        upload = findViewById(R.id.upload);
        compress = findViewById(R.id.compress);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mStorageRef = FirebaseStorage.getInstance().getReference();

        listOfFiles = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).list();
        msg.setText("total files: " + (listOfFiles==null?"0":listOfFiles.length));


        compress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < listOfFiles.length; i++) {
                            CompressImage compressImage = new CompressImage(ProductImages.this);
                            String name = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + listOfFiles[i];
                            imageUrl.add(new CompressUrl(compressImage.compressImage("" + Uri.fromFile(new File(name))), listOfFiles[i].replace(".jpg", "")));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    msg.setText("commpressed " + imageUrl.size());
                                }
                            });
                        }
                    }
                });
                t.start();

            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                putPictures(imageUrl.get(0).getImgUrl(), imageUrl.get(0).getName());


            }
        });


        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (productArrayList.size() > 0) {
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (Product product : productArrayList) {
                                try {
                                    String name = product.getId() + ".jpg";
                                    DownloadFile.fromUrll(product.getThumbnailUrl(), name);
                                } catch (Exception e) {

                                }

                            }
                        }
                    });
                    t.start();

                }
            }
        });

//        mDatabase.child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.getValue() != null) {
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        Product product = snapshot.getValue(Product.class);
//                        if (product != null) {
//                            productArrayList.add(product);
//
//                        }
//                    }
//                    if (productArrayList.size() > 0) {
//                        msg.setText("all products here " + productArrayList.size());
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//
    }

    public void putPictures(String path, final String key) {
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
                        mDatabase.child("Products").child(key).child("thumbnailUrl").setValue("" + downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                ProductImages.this.count++;

                                msg.setText("uploaded: " + ProductImages.this.count);
                                if (count < imageUrl.size()) {
                                    putPictures(imageUrl.get(count).getImgUrl(), imageUrl.get(count).getName());
                                } else {
                                    CommonUtils.showToast("Uplaoded all");
                                }
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


}

class CompressUrl {
    String imgUrl, name;

    public CompressUrl(String imgUrl, String name) {
        this.imgUrl = imgUrl;
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
