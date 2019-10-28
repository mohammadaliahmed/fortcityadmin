package com.appsinventiv.toolsbazzaradmin.Activities.CategoryPackage.Attributes;

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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.CategoryPackage.MainCategoryModel;
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
import java.util.Arrays;
import java.util.List;

public class AddSubAttributes extends AppCompatActivity {
    DatabaseReference mDatabase;
    EditText mainCategories;
    ImageView pickImage;
    Button update;
    List<Uri> mSelected = new ArrayList<>();
    ArrayList<String> imageUrl = new ArrayList<>();
    private static final int REQUEST_CODE_CHOOSE = 23;
    RecyclerView recyclerView;
    SubAttributesAdapter adapter;
    private ArrayList<SubAttributeModel> itemList = new ArrayList<>();
    StorageReference mStorageRef;
    String selection = "single";
    RadioButton single, multiple, userInput;
    String mainAttribute;
    SwipeToDeleteCallback swipeController;
    TextView optionTv;
    String option, alaw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub_attributes);
        this.setTitle("Sub Attributes");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        pickImage = findViewById(R.id.pickImage);
        update = findViewById(R.id.update);
        mainCategories = findViewById(R.id.mainCategories);
        recyclerView = findViewById(R.id.recyclerView);
        single = findViewById(R.id.single);
        userInput = findViewById(R.id.userInput);
        multiple = findViewById(R.id.multiple);
        optionTv = findViewById(R.id.option);
        mainAttribute = getIntent().getStringExtra("mainAttribute");
        option = getIntent().getStringExtra("option");
        alaw = getIntent().getStringExtra("alaw");

        optionTv.setText("Option: " + alaw);


        single.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isPressed()) {
                    selection = "single";
                }
            }
        });
        multiple.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isPressed()) {
                    selection = "multiple";
                }
            }
        });
        userInput.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isPressed()) {
                    selection = "userInput";
                }
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new SubAttributesAdapter(this, itemList, 1, new SubAttributesAdapter.SubAttributesCallBacks() {
            @Override
            public void deleteCategory(SubAttributeModel model) {
                showAlert(model);
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
        recyclerView.setAdapter(adapter);

        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelected.clear();
//                Matisse.from(AddSubAttributes.this)
//                        .choose(MimeType.allOf())
//                        .countable(true)
//                        .maxSelectable(1)
//                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
//                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
//                        .thumbnailScale(0.85f)
//                        .imageEngine(new GlideEngine())
//                        .forResult(REQUEST_CODE_CHOOSE);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mainCategories.getText().length() == 0) {
                    mainCategories.setError("Enter value");
//                } else if (mSelected.size() == 0) {
//                    CommonUtils.showToast("Please pick icon");
                } else {
                    uploadData();
                }
            }
        });

        getMainCategoriesFromDB();

    }

    private void showAlert(final SubAttributeModel model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddSubAttributes.this);
        builder.setTitle("Alert");
        builder.setMessage("Do you want to delete this attribute?");

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

    private void deleteCategory(SubAttributeModel model) {
        mDatabase.child("Settings/Attributes/SubAttributes").child(mainAttribute).child(model.getMainCategory()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Attribute Deleted");
            }
        });
    }

    private void getMainCategoriesFromDB() {
        mDatabase.child("Settings/Attributes/SubAttributes").child(mainAttribute).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    itemList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        SubAttributeModel model = snapshot.getValue(SubAttributeModel.class);
                        if (model != null) {
                            itemList.add(model);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    itemList.clear();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void uploadData() {
        List<String> container = new ArrayList<>();

        String[] sizes = mainCategories.getText().toString().split("\n");
        container = Arrays.asList(sizes);

        for (String item : container) {
            mDatabase.child("Settings/Attributes/SubAttributes").child(mainAttribute).child(item)
                    .setValue(new SubAttributeModel(item, "", option)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                }
            });
        }
        mainCategories.setText("");
        CommonUtils.showToast("Updated");


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
                        mDatabase.child("Settings/Attributes/SubAttributes").child(mainAttribute).child(key).child("url").setValue("" + downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mainCategories.setText("");
                                Glide.with(AddSubAttributes.this).load(R.drawable.photo).into(pickImage);

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
                Glide.with(AddSubAttributes.this).load(mSelected.get(0)).into(pickImage);
                for (Uri img : mSelected) {
                    CompressImage compressImage = new CompressImage(AddSubAttributes.this);
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
