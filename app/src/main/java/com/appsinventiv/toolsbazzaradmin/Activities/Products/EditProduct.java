package com.appsinventiv.toolsbazzaradmin.Activities.Products;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.CategoryPackage.ChooseMainCategory;
import com.appsinventiv.toolsbazzaradmin.Activities.CategoryPackage.ChooseOtherMainCategory;
import com.appsinventiv.toolsbazzaradmin.Activities.Orders.BottomAdapter;
import com.appsinventiv.toolsbazzaradmin.Activities.Orders.BottomDialogModel;
import com.appsinventiv.toolsbazzaradmin.Activities.Products.ChooseOptions.ChooseAttributes;
import com.appsinventiv.toolsbazzaradmin.Activities.Products.ChooseOptions.ChooseAttributesAgain;
import com.appsinventiv.toolsbazzaradmin.Activities.Products.ChooseOptions.ChooseWarrenty;
import com.appsinventiv.toolsbazzaradmin.Activities.Products.ProductVariation.ChooseProductVariation;
import com.appsinventiv.toolsbazzaradmin.Activities.Products.ProductVariation.EditProductVariation;
import com.appsinventiv.toolsbazzaradmin.Activities.Products.ProductVariation.EditProductVariation;
import com.appsinventiv.toolsbazzaradmin.Adapters.PickedPicturesAdapter;
import com.appsinventiv.toolsbazzaradmin.Interfaces.ProductObserver;
import com.appsinventiv.toolsbazzaradmin.Models.NewProductModel;
import com.appsinventiv.toolsbazzaradmin.Models.Product;
import com.appsinventiv.toolsbazzaradmin.Models.SelectedAdImages;
import com.appsinventiv.toolsbazzaradmin.Models.VendorModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.appsinventiv.toolsbazzaradmin.Utils.CompressImage;
import com.appsinventiv.toolsbazzaradmin.Utils.Constants;
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
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditProduct extends AppCompatActivity implements ProductObserver {
    TextView categoryChoosen;
    StorageReference mStorageRef;
    DatabaseReference mDatabase;
    Button pick, upload;
    private static final int REQUEST_CODE_CHOOSE = 23;
    List<Uri> mSelected;
    RecyclerView recyclerView;
    PickedPicturesAdapter adapter;
    Bundle extras;
    ArrayList<SelectedAdImages> selectedAdImages = new ArrayList<>();
    ArrayList<String> imageUrl = new ArrayList<>();
    EditText e_title, e_sku, e_subtitle, e_costPrice, e_wholesalePrice,
            e_retailPrice, e_minOrderQty, e_measurement, e_sizes, e_colors, e_description,
            e_oldRetailPrice, e_oldWholesalePrice, quantityAvailable;
    String productId;
    ProgressBar progressBar;
    ArrayList<VendorModel> vendorModelArrayList = new ArrayList<>();
    VendorModel vendor;
    ProductObserver observer;
    int newSku = 10001;
    RadioGroup radioGroup;
    RadioButton selected;
    Product product;
    public static ArrayList<String> categoryList = new ArrayList<>();
    EditText brandName, productContents;
    TextView warrantyChosen, weightChosen;
    private String whichWarranty;
    public static String productWeight, dimens;
    TextView productIdd;
    public static int fromWhere = 0;

    private ArrayList<BottomDialogModel> vendrs = new ArrayList<>();
    TextView chooseVendor;


    int sellingTo = 1;

    RadioButton both, wholesale, retail;
    LinearLayout retailArea, wholesaleArea;
    private String localThumbnail;
    EditText warrantyPolicy;
    TextView dangerousGoodsTv;
    TextView productVariation;
    TextView productVariationSubtitle;
    public static AddProduct activity;
    EditText productModel;
    CardView cardAttr;
    TextView warrantyPeriodTv;

    public static String warrantyPeriod, dangerousGoods;


    @Override
    protected void onResume() {
        super.onResume();
//        if (productWeight != null) {
//            weightChosen.setText("Weight: " + productWeight + "Kg");
//        }
        Constants.RE_ATTRIBUTES = false;
        if (productWeight != null) {
            weightChosen.setText("Weight: " + productWeight + "Kg");
        }
        if (AddProduct.productAttributesMap != null && AddProduct.productAttributesMap.size() > 0) {
            setupAttributesLayout();

        }
        if (categoryList.size() > 0) {
            categoryChoosen.setText("Category: " + categoryList);

        }
        if (whichWarranty != null) {
            warrantyChosen.setText("Warranty: " + whichWarranty);
        }
        if (warrantyPeriod != null) {
            warrantyPeriodTv.setText("Period:" + warrantyPeriod);
        }
        if (dangerousGoods != null) {
            dangerousGoodsTv.setText("Dangerous:" + dangerousGoods);
        }

        if (categoryList != null && categoryList.size() > 0) {
            Constants.ADDING_PRODUCT = false;
        } else {
//            Constants.ADDING_PRODUCT = true;
//            Intent i = new Intent(AddProduct.this, ChooseMainCategory.class);
//            categoryList.clear();
//            startActivityForResult(i, 1);
            Constants.EDITING_PRODUCT = true;

            if (!Constants.ADDING_PRODUCT_BACK) {
                Constants.ADDING_PRODUCT = true;
                sellingTo = 1;
            } else {
                finish();
            }
        }
        if (EditProductVariation.hashMapHashMap != null && EditProductVariation.hashMapHashMap.size() > 0) {
            productVariationSubtitle.setText("Color and size selected");
        } else {

        }
    }

    private void setupAttributesLayout() {
        final LinearLayout options_layout = (LinearLayout) findViewById(R.id.layout);
        options_layout.removeAllViews();
        for (final Map.Entry<String, Object> entry : AddProduct.productAttributesMap.entrySet()) {
            final String key = entry.getKey();
            String value = entry.getValue().toString();
            LayoutInflater inflater = LayoutInflater.from(this);
            final View to_add = inflater.inflate(R.layout.product_attributes_layout,
                    options_layout, false);
//                ImageView  delete = to_add.findViewById(R.id.delete);
            TextInputEditText subtitle = to_add.findViewById(R.id.subtitle);
            TextInputLayout keu = to_add.findViewById(R.id.TextInputLayout);
            ImageView delete = to_add.findViewById(R.id.delete);
            ImageView edit = to_add.findViewById(R.id.edit);
            keu.setHint(key);
            subtitle.setText(value);
            options_layout.addView(to_add);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    options_layout.removeView(to_add);
                    AddProduct.productAttributesMap.remove(entry.getKey());
                }
            });
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(EditProduct.this, ChooseAttributesAgain.class);
                    i.putExtra("attribute", key);
                    startActivity(i);
                }
            });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }
        Constants.ADDING_PRODUCT = true;
        Constants.EDITING_PRODUCT = true;
        this.setTitle("Edit Product");
        Intent i = getIntent();
        productId = i.getStringExtra("productId");

        observer = EditProduct.this;

        getPermissions();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        categoryChoosen = findViewById(R.id.categoryChoosen);
        categoryChoosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromWhere = 1;
                Constants.ADDING_PRODUCT = true;
                Intent i = new Intent(EditProduct.this, ChooseMainCategory.class);
                categoryList.clear();
                startActivityForResult(i, 1);

            }
        });
        chooseVendor = findViewById(R.id.chooseVendor);

        pick = findViewById(R.id.pick);
        productIdd = findViewById(R.id.productId);
        upload = findViewById(R.id.upload);
        recyclerView = findViewById(R.id.recyclerview);
        e_title = findViewById(R.id.title);
        e_subtitle = findViewById(R.id.subtitle);
        e_costPrice = findViewById(R.id.costPrice);
        e_wholesalePrice = findViewById(R.id.wholeSalePrice);
        e_retailPrice = findViewById(R.id.retailPrice);
        e_minOrderQty = findViewById(R.id.minOrder);
        e_measurement = findViewById(R.id.measurement);
        e_sku = findViewById(R.id.productSku);
        e_description = findViewById(R.id.description);
        e_sizes = findViewById(R.id.size);
        e_colors = findViewById(R.id.color);
        e_oldWholesalePrice = findViewById(R.id.oldWholeSalePrice);
        e_oldRetailPrice = findViewById(R.id.oldRetailPrice);
        progressBar = findViewById(R.id.prgress);
        radioGroup = findViewById(R.id.radioGroup);
        quantityAvailable = findViewById(R.id.quantityAvailable);
        brandName = findViewById(R.id.brandName);
        productContents = findViewById(R.id.productContents);
        weightChosen = findViewById(R.id.weightChosen);
        warrantyChosen = findViewById(R.id.warrantyChosen);


        productVariationSubtitle = findViewById(R.id.productVariationSubtitle);

        productModel = findViewById(R.id.productModel);
        productVariation = findViewById(R.id.productVariation);
        warrantyPeriodTv = findViewById(R.id.warrantyPeriod);
        cardAttr = findViewById(R.id.cardAttr);


        cardAttr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.RE_ATTRIBUTES = true;
                if (ChooseOtherMainCategory.activity != null) {
                    ChooseOtherMainCategory.activity.finish();
                }
                Intent i = new Intent(EditProduct.this, ChooseAttributes.class);
                startActivity(i);

                finish();
            }
        });

        productIdd.setText("Product Id: " + productId);


        chooseVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog(vendrs);
            }
        });

        warrantyChosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showWarrantyAlert();
//                showWarrantyAlert();
                Intent i = new Intent(EditProduct.this, ChooseWarrenty.class);
                i.putExtra("addingProduct", "yes");
                startActivity(i);
            }
        });

        weightChosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EditProduct.this, AddProductWeight.class);
                startActivity(i);
            }
        });

        productVariation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditProduct.this, EditProductVariation.class));
            }
        });

        showPickedPictures();

        getDataFromServer();
        getVendorsFromDb();
        e_wholesalePrice.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    /* Write your logic here that will be executed when user taps next button */
                    e_oldWholesalePrice.requestFocus();

                    handled = true;
                }

                return handled;
            }
        });
        e_oldWholesalePrice.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    /* Write your logic here that will be executed when user taps next button */
                    e_retailPrice.requestFocus();

                    handled = true;
                }

                return handled;
            }
        });
        e_retailPrice.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    /* Write your logic here that will be executed when user taps next button */
                    e_oldRetailPrice.requestFocus();

                    handled = true;
                }

                return handled;
            }
        });


        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelected != null) {
                    mSelected.clear();
                    selectedAdImages.clear();
                    imageUrl.clear();
                }
                Matisse.from(EditProduct.this)
                        .choose(MimeType.allOf())
                        .countable(true)
                        .maxSelectable(5)
                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .forResult(REQUEST_CODE_CHOOSE);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (product.getCategory() == null && categoryList.size() == 0) {
                    CommonUtils.showToast("Please select category");
                } else if (e_title.getText().length() == 0) {
                    e_title.setError("Enter title");
                } else if (e_subtitle.getText().length() == 0) {
                    e_subtitle.setError("Enter subtitle");
                } else if (e_costPrice.getText().length() == 0) {
                    e_costPrice.setError("Enter price");
                } else if (productWeight != null) {
                    CommonUtils.showToast("Enter product weight");
                } else {
                    List<String> container = new ArrayList<>();
                    if (e_sizes.getText().length() > 0) {
                        String[] sizes = e_sizes.getText().toString().replace("[", "").replace("]", "").replace(" ", "").split(",");
                        container = Arrays.asList(sizes);

                    }
                    List<String> container1 = new ArrayList<>();

                    if (e_colors.getText().length() > 0) {
                        String[] colors = e_colors.getText().toString().replace("[", "").replace("]", "").replace(" ", "").split(",");
                        container1 = Arrays.asList(colors);

                    }
                    progressBar.setVisibility(View.VISIBLE);
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    selected = findViewById(selectedId);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("title", e_title.getText().toString());
                    map.put("subtitle", e_subtitle.getText().toString());
                    map.put("sku", newSku);
                    map.put("costPrice", Float.parseFloat(e_costPrice.getText().toString()));
                    map.put("wholeSalePrice", Float.parseFloat(e_wholesalePrice.getText().toString()));
                    map.put("retailPrice", Float.parseFloat(e_retailPrice.getText().toString()));
                    map.put("minOrderQuantity", Long.parseLong(e_minOrderQty.getText().toString()));
                    map.put("measurement", e_measurement.getText().toString());
                    map.put("description", e_description.getText().toString());
                    map.put("sizeList", container);
                    map.put("colorList", container1);
                    map.put("oldWholeSalePrice", Float.parseFloat(e_oldWholesalePrice.getText().toString()));
                    map.put("oldRetailPrice", Float.parseFloat(e_oldRetailPrice.getText().toString()));
                    map.put("category", categoryList);
                    map.put("quantityAvailable", Integer.parseInt(quantityAvailable.getText().toString()));
                    map.put("brandName", brandName.getText().toString());
                    map.put("model", productModel.getText().toString());
                    map.put("productContents", productContents.getText().toString());
                    map.put("warrantyType", whichWarranty);
                    map.put("productWeight", productWeight);
                    map.put("dimen", dimens);
                    map.put("productAttributes", product.getProductAttributes());
                    map.put("attributesWithPics", EditProductVariation.uploadedMap);
                    map.put("newAttributes", EditProductVariation.hashMapHashMap);

                    mDatabase.child("Products").child(productId).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            int count = 0;
                            progressBar.setVisibility(View.GONE);
                            if (imageUrl.size() != 0) {

                                for (String img : imageUrl) {
                                    if (!img.contains("firebasestorage")) {
                                        putPictures(img, "" + productId, count);
                                        count++;
                                        observer.onUploaded(count, imageUrl.size());
                                    }

                                }
                            } else {

//                                putPicturesBack();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }


            }
        });


    }

    @SuppressLint("WrongConstant")
    private void showBottomDialog(ArrayList<BottomDialogModel> list) {


        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.bottom_option, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        dialog.setContentView(customView);
        RecyclerView recyclerview = customView.findViewById(R.id.recyclerview);


        BottomAdapter adapter = new BottomAdapter(this, list, new BottomAdapter.ShareMessageFriendsAdapterCallbacks() {
            @Override
            public void onChoose(int position) {
                vendor = vendorModelArrayList.get(position);
                dialog.dismiss();
                chooseVendor.setText(vendor.getVendorName());
            }
        });

//        dialog.dismiss();


        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerview.setAdapter(adapter);

        dialog.show();


    }


    private void showWarrantyAlert() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(EditProduct.this);
        builderSingle.setTitle("Select warranty type");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(EditProduct.this, android.R.layout.simple_list_item_1);
        arrayAdapter.add("International Manufacture Warranty");
        arrayAdapter.add("International Seller Warranty");
        arrayAdapter.add("Local Seller Warranty");
        arrayAdapter.add("No Warranty");
        arrayAdapter.add("Non-local Warranty");

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                warrantyChosen.setText("Warranty chosen: " + arrayAdapter.getItem(which));
                whichWarranty = arrayAdapter.getItem(which);

            }
        });
        builderSingle.show();
    }

    private void putPicturesBack() {

        mDatabase.child("Products").child(productId).child("pictures")
                .setValue(product.getPictures()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Product updated");
                finish();
            }
        });
    }

    private void getDataFromServer() {
        mDatabase.child("Products").child(Constants.PRODUCT_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    product = dataSnapshot.getValue(Product.class);
                    if (product != null) {
                        if (product.getAttributesWithPics() != null && dataSnapshot.child("newAttributes").getValue() != null) {
                            HashMap<String, ArrayList<NewProductModel>> newMap = new HashMap<>();
                            for (DataSnapshot color : dataSnapshot.child("newAttributes").getChildren()) {
                                ArrayList<NewProductModel> newProductModelArrayList = new ArrayList<>();
                                for (DataSnapshot size : color.getChildren()) {
                                    NewProductModel countModel = size.getValue(NewProductModel.class);
                                    if (countModel != null) {
                                        newProductModelArrayList.add(countModel);
                                    }
                                    newMap.put(color.getKey(), newProductModelArrayList);
                                }

                            }
                            product.setProductCountHashmap(newMap);

                        }
                        SharedPrefs.setProduct(product);

                        e_title.setText(product.getTitle());
                        e_subtitle.setText(product.getSubtitle() + "");
                        e_costPrice.setText(product.getCostPrice() + "");
                        e_wholesalePrice.setText(product.getWholeSalePrice() + "");
                        e_retailPrice.setText(product.getRetailPrice() + "");
                        e_minOrderQty.setText("" + product.getMinOrderQuantity());
                        e_measurement.setText(product.getMeasurement());
                        e_sku.setText("" + product.getSku());
                        e_description.setText(product.getDescription());
                        quantityAvailable.setText("" + product.getQuantityAvailable());
                        if (product.getSizeList() != null) {
                            e_sizes.setText("" + product.getSizeList());
                        }
                        if (product.getColorList() != null) {
                            e_colors.setText("" + product.getColorList());
                        }

                        e_oldWholesalePrice.setText("" + product.getOldWholeSalePrice());
                        e_oldRetailPrice.setText("" + product.getOldRetailPrice());
                        newSku = product.getSku();
                        AddProduct.categoryList = product.getCategory();
                        if (product.getProductAttributes() != null && product.getProductAttributes().size() > 0) {
                            AddProduct.productAttributesMap = product.getProductAttributes();
                            setupAttributesLayout();
                        }
//                        if (product.getPictures() != null) {
//                            for (int i = 0; i < product.getPictures().size(); i++) {
//                                selectedAdImages.add(new SelectedAdImages(product.getPictures().get(i)));
//                                imageUrl.add(product.getPictures())
//                            }
//                        }
                        imageUrl = product.getPictures();
                        recyclerView.setVisibility(View.VISIBLE);
//                        adapter.notifyDataSetChanged();
                        adapter.setMobileAds(imageUrl);
                        warrantyChosen.setText("Warranty chosen: " + product.getWarrantyType() == null ? "" : product.getWarrantyType());
                        whichWarranty = product.getWarrantyType();
                        productContents.setText(product.getProductContents());
                        brandName.setText(product.getBrandName());
                        chooseVendor.setText(product.getVendor().getStoreName() == null ? product.getVendor().getVendorName() : product.getVendor().getStoreName());
                        weightChosen.setText(product.getProductWeight() == null ? "Choose weight" : "Product Weight: " + product.getProductWeight());
                        if (product.getCategory() != null) {
                            categoryList = product.getCategory();
                            categoryChoosen.setText("Category: " + product.getCategory());
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void showPickedPictures() {
        selectedAdImages = new ArrayList<>();
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(EditProduct.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        adapter = new PickedPicturesAdapter(EditProduct.this, imageUrl, new PickedPicturesAdapter.ChooseOption() {
            @Override
            public void onDeleteClicked(int position) {
                product.getPictures().remove(position);
                imageUrl.remove(position);
                adapter.notifyDataSetChanged();
            }

        });
        recyclerView.setAdapter(adapter);
    }

    private void getVendorsFromDb() {
        mDatabase.child("Vendors").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot != null) {
                            VendorModel model = snapshot.getValue(VendorModel.class);
                            if (model != null) {
                                vendorModelArrayList.add(model);
                                setUpVendors();
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


    private void setUpVendors() {
        vendrs = new ArrayList<>();
        for (int i = 0; i < vendorModelArrayList.size(); i++) {
            vendrs.add(new BottomDialogModel(
                    vendorModelArrayList.get(i).getUsername(),
                    vendorModelArrayList.get(i).getVendorName()
                    , vendorModelArrayList.get(i).getPhone(),
                    vendorModelArrayList.get(i).getPicUrl()));

        }


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
                        observer.putThumbnailUrl(count, "" + downloadUrl);
                        mDatabase.child("Products").child(productId).child("pictures").child("" + count).setValue("" + downloadUrl);


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
        CommonUtils.showToast(categoryList + "");
        categoryChoosen.setText("Category: " + categoryList);
        selectedAdImages.clear();
        if (data != null) {
            if (requestCode == REQUEST_CODE_CHOOSE) {
                recyclerView.setVisibility(View.VISIBLE);

                mSelected = Matisse.obtainResult(data);
                for (Uri img : mSelected) {
                    selectedAdImages.add(new SelectedAdImages("" + img));
                    adapter.notifyDataSetChanged();
                    CompressImage compressImage = new CompressImage(EditProduct.this);
                    imageUrl.add(compressImage.compressImage("" + img));
                }

            }
            if (requestCode == 1) {
                extras = data.getExtras();
                if (extras.getString("subCategory") != null) {
                    categoryChoosen.setText("Category: " + extras.getString("subCategory"));

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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            Constants.ADDING_PRODUCT = false;
            Constants.EDITING_PRODUCT = false;

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Constants.ADDING_PRODUCT = false;
        Constants.EDITING_PRODUCT = false;

    }

    @Override
    public void onUploaded(int count, int arraySize) {
        if (count == arraySize) {
            Constants.ADDING_PRODUCT = false;
            Constants.EDITING_PRODUCT = false;
            Intent i = new Intent(EditProduct.this, ProductUploaded.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void putThumbnailUrl(int count, String url) {
        if (count == 0) {
            mDatabase.child("Products").child(productId).child("thumbnailUrl").setValue(url);
        }
    }
}
