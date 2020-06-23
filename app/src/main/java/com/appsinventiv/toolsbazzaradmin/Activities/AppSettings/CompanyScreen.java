package com.appsinventiv.toolsbazzaradmin.Activities.AppSettings;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.Orders.Orders;
import com.appsinventiv.toolsbazzaradmin.Activities.Products.AddProduct;
import com.appsinventiv.toolsbazzaradmin.Activities.Products.ListOfProducts;
import com.appsinventiv.toolsbazzaradmin.Models.AboutUsModel;
import com.appsinventiv.toolsbazzaradmin.Models.CompanyDetailsModel;
import com.appsinventiv.toolsbazzaradmin.Models.VendorModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.appsinventiv.toolsbazzaradmin.Utils.CompressImage;
import com.appsinventiv.toolsbazzaradmin.Utils.Constants;
import com.appsinventiv.toolsbazzaradmin.Utils.SharedPrefs;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class CompanyScreen extends AppCompatActivity {
    private static final int REQUEST_CODE_CHOOSE = 24;
    private static final int REQUEST_CODE_CHOOSE_COVER = 25;
    public static String language;
    Toolbar toolbar;
    //    ImageView back;
    ImageView storeCover;
    CircleImageView profilePic;
    Button changeCover;
    StorageReference mStorageRef;
    List<Uri> mSelected1 = new ArrayList<>();
    List<Uri> mSelected2 = new ArrayList<>();

    ArrayList<String> imageUrl1 = new ArrayList<>();
    ArrayList<String> imageUrl2 = new ArrayList<>();
    TextView name;

    DatabaseReference mDatabase;
    RelativeLayout myOrders;
    //    ProgressBar coverProgress, picProgress;
    LinearLayout addProducts, myProducts, mySales;
    RelativeLayout bankAccount, selectCountry, selectLanguage, myReviews, otherStores, contactUs, aboutUs, termsConditions;
    TextView address;

    CollapsingToolbarLayout collapsing_toolbar;

    AppBarLayout app_bar_layout;
    private CompanyDetailsModel model;
    ImageView accountSettings, backImage;
    NestedScrollView scrollview;
    TextView toolbarTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comapny_screen);

        toolbar=findViewById(R.id.toolbar);

        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        toolbarTitle = findViewById(R.id.toolbarTitle);
        scrollview = findViewById(R.id.scrollview);
        backImage = findViewById(R.id.backImage);
//        editIcon = findViewById(R.id.editIcon);
        app_bar_layout = findViewById(R.id.app_bar_layout);
        collapsing_toolbar = findViewById(R.id.collapsing_toolbar);
        accountSettings = findViewById(R.id.accountSettings);
        bankAccount = findViewById(R.id.bankAccount);
        selectCountry = findViewById(R.id.selectCountry);
        selectLanguage = findViewById(R.id.selectLanguage);
        myReviews = findViewById(R.id.myReviews);
        otherStores = findViewById(R.id.otherStores);
        contactUs = findViewById(R.id.contactUs);
        aboutUs = findViewById(R.id.aboutUs);
        termsConditions = findViewById(R.id.termsConditions);
        address = findViewById(R.id.address);


        mySales = findViewById(R.id.mySales);
        myProducts = findViewById(R.id.myProducts);
        addProducts = findViewById(R.id.addProducts);
//        back = findViewById(R.id.back);
        changeCover = findViewById(R.id.changeCover);
        profilePic = findViewById(R.id.profilePic);
        storeCover = findViewById(R.id.storeCover);
//        coverProgress = findViewById(R.id.coverProgress);
//        picProgress = findViewById(R.id.picProgress);
        name = findViewById(R.id.name);
        myOrders = findViewById(R.id.myOrders);
        getPermissions();


        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        app_bar_layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == -collapsing_toolbar.getHeight() + toolbar.getHeight()) {
                    //toolbar is collapsed here
                    //write your code here
                    collapsing_toolbar.setTitle(model.getName());
                    collapsing_toolbar.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                    collapsing_toolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.colorBlack));


                } else {
                    collapsing_toolbar.setTitle("");
                }

            }
        });


        accountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(CompanyScreen.this, CompanySettings.class));

            }
        });
        bankAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                startActivity(new Intent(CompanyScreen.this, SellerAddBankAccount.class));

            }
        });
        selectCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(CompanyScreen.this, ChooseCountry.class);
//                i.putExtra("from", 1);
//                startActivity(i);

            }
        });
        selectLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent i = new Intent(CompanyScreen.this, ChooseLanguage.class);
//                startActivity(i);

            }
        });
        otherStores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });


        myReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(CompanyScreen.this, SellerProductReviews.class));

            }
        });
        otherStores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                startActivity(new Intent(CompanyScreen.this, ListOfOtherStores.class));
            }
        });


        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+94 775292313"));
                startActivity(i);

            }
        });


        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(CompanyScreen.this, AboutUs.class));


            }
        });


        termsConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CompanyScreen.this, ViewTerms.class));

            }
        });
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });


        mySales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CompanyScreen.this, Orders.class));

            }
        });
        myProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CompanyScreen.this, ListOfProducts.class));

            }
        });
        addProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.ADDING_PRODUCT_BACK = false;

                startActivity(new Intent(CompanyScreen.this, AddProduct.class));

            }
        });


        myOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CompanyScreen.this, Orders.class));
            }
        });


        mDatabase = FirebaseDatabase.getInstance().getReference();
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
        changeCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    initCoverMatisse();

                } catch (Exception e) {
                    CommonUtils.showToast(e.getMessage());
                }
            }
        });

        getVendorDataFromDB();
    }


    private void getVendorDataFromDB() {
        mDatabase.child("Settings").child("CompanyDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    model = dataSnapshot.getValue(CompanyDetailsModel.class);
                    if (model != null) {
//                        coverProgress.setVisibility(View.GONE);
//                        picProgress.setVisibility(View.GONE);

                        name.setText(model.getName());
                        address.setText(model.getAddress() + " " + model.getPhone() + " " + model.getEmail());

                        if (model.getCoverPicUrl() != null) {
                            try {
                                Glide.with(CompanyScreen.this).load(model.getCoverPicUrl()).into(storeCover);


                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } finally {
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

    private void initProfileMatisse() {
        Matisse.from(CompanyScreen.this)
                .choose(MimeType.allOf())
                .countable(true)
                .maxSelectable(1)
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    private void initCoverMatisse() {
        Matisse.from(CompanyScreen.this)
                .choose(MimeType.allOf())
                .countable(true)
                .maxSelectable(1)
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE_COVER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {

            if (requestCode == REQUEST_CODE_CHOOSE_COVER) {

                mSelected2 = Matisse.obtainResult(data);
                for (Uri img : mSelected2) {
                    CompressImage compressImage = new CompressImage(CompanyScreen.this);
                    imageUrl2.add(compressImage.compressImage("" + img));
                }
                putCoverPic(imageUrl2.get(0));

            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }


    public void putCoverPic(String path) {
        storeCover.setVisibility(View.VISIBLE);
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
                        mDatabase.child("Settings").child("CompanyDetails").child("coverPicUrl").setValue("" + downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                CommonUtils.showToast("Cover changed successfully");
                                storeCover.setVisibility(View.GONE);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }




}
