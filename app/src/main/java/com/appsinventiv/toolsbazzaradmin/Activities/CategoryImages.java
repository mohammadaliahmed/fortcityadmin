package com.appsinventiv.toolsbazzaradmin.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.CategoryPackage.MainCategoryModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.appsinventiv.toolsbazzaradmin.Utils.CompressImageToThumnail;
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

public class CategoryImages extends AppCompatActivity {

    DatabaseReference mDatabase;
    ArrayList<MainCategoryModel> productArrayList = new ArrayList<>();

    Button download, upload, compress;
    TextView msg;
    StorageReference mStorageRef;
    String[] listOfFiles;
    ArrayList<CompressUrl2> imageUrl = new ArrayList<>();


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
        msg.setText("total files: " + (listOfFiles == null ? "0" : listOfFiles.length));


        compress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < listOfFiles.length; i++) {
                            CompressImageToThumnail compressImage = new CompressImageToThumnail(CategoryImages.this);
                            String name = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + listOfFiles[i];
                            imageUrl.add(new CompressUrl2(compressImage.compressImage("" + Uri.fromFile(new File(name))),
                                    listOfFiles[i].replace(".jpg", "")));
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
                            for (MainCategoryModel product : productArrayList) {
                                try {
                                    String name = product.getMainCategory() + ".jpg";
                                    DownloadFile.fromUrll(product.getUrl(), name);
                                } catch (Exception e) {

                                }

                            }
                        }
                    });
                    t.start();

                }
            }
        });

        mDatabase.child("Settings/Categories/MainCategories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        MainCategoryModel product = snapshot.getValue(MainCategoryModel.class);
                        if (product != null) {
                            productArrayList.add(product);

                        }
                    }
                    if (productArrayList.size() > 0) {
                        msg.setText("all categories here " + productArrayList.size());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
                        mDatabase.child("Settings/Categories/MainCategories").child(key).child("url").setValue("" + downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                count++;

                                msg.setText("uploaded: " + count);
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

    class CompressUrl2 {
        String imgUrl, name;

        public CompressUrl2(String imgUrl, String name) {
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

}
