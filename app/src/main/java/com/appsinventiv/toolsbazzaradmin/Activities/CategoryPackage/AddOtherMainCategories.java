package com.appsinventiv.toolsbazzaradmin.Activities.CategoryPackage;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.appsinventiv.toolsbazzaradmin.Utils.CompressImage;
import com.appsinventiv.toolsbazzaradmin.Utils.SwipeControllerActions;
import com.appsinventiv.toolsbazzaradmin.Utils.SwipeToDeleteCallback;
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

public class AddOtherMainCategories extends AppCompatActivity {
    DatabaseReference mDatabase;
    EditText mainCategories, subCategories;
    ImageView pickImage;
    Button update;
    List<Uri> mSelected = new ArrayList<>();
    ArrayList<String> imageUrl = new ArrayList<>();
    private static final int REQUEST_CODE_CHOOSE = 23;
    RecyclerView recyclerView;
    OtherMainCategoryAdapter adapter;
    private ArrayList<MainCategoryModel> itemList = new ArrayList<>();
    StorageReference mStorageRef;
    String mainCategory;
    private SwipeToDeleteCallback swipeController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_main_categories);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mainCategory = getIntent().getStringExtra("mainCategory");
        this.setTitle(mainCategory);
        pickImage = findViewById(R.id.pickImage);
        update = findViewById(R.id.update);
        mainCategories = findViewById(R.id.mainCategories);
        recyclerView = findViewById(R.id.recyclerView);
        subCategories = findViewById(R.id.subCategories);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new OtherMainCategoryAdapter(this, itemList, 1, new OtherMainCategoryAdapter.MainCategoryCallBacks() {
            @Override
            public void deleteCategory(MainCategoryModel model) {
                showAlert(model);
            }
        });
        recyclerView.setAdapter(adapter);

        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelected.clear();
                Matisse.from(AddOtherMainCategories.this)
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

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mainCategories.getText().length() == 0) {
                    mainCategories.setError("Enter value");
                } else if (subCategories.getText().length() == 0) {
                    subCategories.setError("Enter categories");
                } else if (mSelected.size() == 0) {
                    CommonUtils.showToast("Please pick icon");
                } else {
                    uploadData();
                }
            }
        });

        swipeController = new SwipeToDeleteCallback(new SwipeControllerActions() {
            @Override
            public void onRightClicked(final int position) {
                showAlert(itemList.get(position));

            }
        });
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });

        getMainCategoriesFromDB();

    }

    private void showAlert(final MainCategoryModel model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddOtherMainCategories.this);
        builder.setTitle("Alert");
        builder.setMessage("Do you want to delete this category?");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteCategory(model);
            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteCategory(MainCategoryModel model) {
        mDatabase.child("Settings/Categories/OtherMainCategories/" + mainCategory).child(model.getMainCategory()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Category Deleted");
            }
        });
    }

    private void getMainCategoriesFromDB() {
        mDatabase.child("Settings/Categories/OtherMainCategories/" + mainCategory).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    itemList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        MainCategoryModel model = snapshot.getValue(MainCategoryModel.class);
                        if (model != null) {
                            itemList.add(model);
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

    private void uploadData() {
        mDatabase.child("Settings/Categories/OtherMainCategories/" + mainCategory).child(mainCategories.getText().toString())
                .setValue(new MainCategoryModel(mainCategories.getText().toString(), "", subCategories.getText().toString())).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                for (String img : imageUrl) {
                    putPictures(img, "" + mainCategories.getText().toString());
                }
                adapter.notifyDataSetChanged();
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
                        mDatabase.child("Settings/Categories/OtherMainCategories/" + mainCategory).child(key).child("url").setValue("" + downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mainCategories.setText("");
                                subCategories.setText("");
                                Glide.with(AddOtherMainCategories.this).load(R.drawable.photo).into(pickImage);

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == REQUEST_CODE_CHOOSE) {

                mSelected = Matisse.obtainResult(data);
                Glide.with(AddOtherMainCategories.this).load(mSelected.get(0)).into(pickImage);
                for (Uri img : mSelected) {
                    CompressImage compressImage = new CompressImage(AddOtherMainCategories.this);
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
