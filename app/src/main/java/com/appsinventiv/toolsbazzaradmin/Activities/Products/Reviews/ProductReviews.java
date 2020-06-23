package com.appsinventiv.toolsbazzaradmin.Activities.Products.Reviews;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.Customers.SellerModel;
import com.appsinventiv.toolsbazzaradmin.Activities.Orders.ViewOrder;
import com.appsinventiv.toolsbazzaradmin.Activities.Products.AddProduct;
import com.appsinventiv.toolsbazzaradmin.Models.Product;
import com.appsinventiv.toolsbazzaradmin.Models.VendorModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.appsinventiv.toolsbazzaradmin.Utils.Constants;
import com.appsinventiv.toolsbazzaradmin.Utils.NotificationAsync;
import com.appsinventiv.toolsbazzaradmin.Utils.NotificationObserver;
import com.appsinventiv.toolsbazzaradmin.Utils.SwipeControllerActions;
import com.appsinventiv.toolsbazzaradmin.Utils.SwipeToRejectCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


public class ProductReviews extends AppCompatActivity implements RejectCallbacks, NotificationObserver {
    RecyclerView recyclerView;
    ArrayList<Product> productArrayList = new ArrayList<>();
    ProductsReviewsAdapter adapter;
    DatabaseReference mDatabase;

    TextView positiveRatingText, positiveCount, neutralCount, negativeCount;
    ProgressBar positiveBar, neutralBar, negativeBar;

    float posCount, neutCount, negCount, totalReviewCount, totalPositiveCount;
    TextView totalReviews;

    String vendorId;
    String storeName;

    private SwipeToRejectCallback swipeController;

    int postion;
    ArrayList<String> ite = new ArrayList<>();
    SellerModel vendorModel;
    FloatingActionButton addProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_revieiws);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }


        mDatabase = FirebaseDatabase.getInstance().getReference();

        vendorId = getIntent().getStringExtra("vendorId");
        storeName = getIntent().getStringExtra("storeName");

        getSellerDetailsFromDB();
        this.setTitle(storeName);
        addProduct = findViewById(R.id.addProduct);
        positiveCount = findViewById(R.id.positiveCount);
        neutralCount = findViewById(R.id.neutralCount);
        negativeCount = findViewById(R.id.negativeCount);
        totalReviews = findViewById(R.id.totalReviews);
        positiveBar = findViewById(R.id.positiveBar);
        neutralBar = findViewById(R.id.neutralBar);
        negativeBar = findViewById(R.id.negativeBar);
        positiveRatingText = findViewById(R.id.positiveRatingText);

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.ADDING_PRODUCT_BACK = false;

                startActivity(new Intent(ProductReviews.this, AddProduct.class));
            }
        });

        recyclerView = findViewById(R.id.recycler_view_products);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new ProductsReviewsAdapter(this, productArrayList);
        adapter.setCallbacks(new ProductsReviewsAdapter.SellerProductsAdapterCallbacks() {
            @Override
            public void onStatusChange(Product product, final String status) {
                if (!vendorId.equalsIgnoreCase("Fort City")) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("sellerProductStatus", status);
                    map.put("vendor", vendorModel);
                    mDatabase.child("Products").child(product.getId()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            CommonUtils.showToast(status);
                        }
                    });

                } else {
                    mDatabase.child("Products").child(product.getId())
                            .child("sellerProductStatus").setValue(status).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            CommonUtils.showToast(status);
                        }
                    });
                }
            }

            @Override
            public void onActiveStatusChange(Product product, final boolean status) {
                mDatabase.child("Products").child(product.getId())
                        .child("active").setValue(status).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast(status ? "Active" : "Inactive");
                    }
                });
            }

            @Override
            public void onReject(Product product) {


            }
        });
        recyclerView.setAdapter(adapter);
        swipeController = new SwipeToRejectCallback(new SwipeControllerActions() {
            @Override
            public void onRightClicked(final int position) {
                postion = position;
//                rejectProduct(productArrayList.get(position).getId());
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                // Create and show the dialog.
                TabbedDialog dialogFragment = new TabbedDialog();
                dialogFragment.show(ft, "dialog");

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
        if (vendorId != null) {
            if (vendorId.equalsIgnoreCase("Fort city")) {
                gettFortCityProductsFromDB();
            } else {
                getProductsFromDB();

            }
        }
    }

    private void getSellerDetailsFromDB() {
        mDatabase.child("Sellers").child(vendorId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    vendorModel = dataSnapshot.getValue(SellerModel.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void gettFortCityProductsFromDB() {
        mDatabase.child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    productArrayList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Product product = snapshot.getValue(Product.class);
                        if (product != null) {
                            if (Constants.PRODUCT_STATUS.equalsIgnoreCase("Out of stock")) {
                                if (product.getQuantityAvailable() < 1) {
                                    productArrayList.add(product);
                                    performCalculations();
                                }
                            } else {
                                if (product.getSellerProductStatus() != null && product.getSellerProductStatus().equalsIgnoreCase(Constants.PRODUCT_STATUS)) {
                                    if (product.getUploadedBy() == null) {


                                        productArrayList.add(product);
//                                performCalculations();


                                    } else if (product.getUploadedBy().equalsIgnoreCase("admin")) {
                                        productArrayList.add(product);

                                    }
                                    performCalculations();
                                }
                            }
                        }

                    }
//                    ArrayList<String> st=new ArrayList<>();
//                    if(productArrayList.size()>0){
//                        for(Product p:productArrayList){
//                            st.add(p.getId());
//                        }
//                    }
                    Collections.sort(productArrayList, new Comparator<Product>() {
                        @Override
                        public int compare(Product listData, Product t1) {
                            Long ob1 = listData.getTime();
                            Long ob2 = t1.getTime();

                            return ob2.compareTo(ob1);
                        }
                    });
                    adapter.notifyDataSetChanged();
                } else {
                    CommonUtils.showToast("No Data");
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void rejectProduct(final Product product, final String rejectReason) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("sellerProductStatus", "Rejected");
        map.put("rejectReason", rejectReason);

        mDatabase.child("Products").child(product.getId()).updateChildren(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Product rejected");
                        NotificationAsync notificationAsync = new NotificationAsync(ProductReviews.this);
                        String notification_title = "Rejected: " + (product.getTitle().length() > 20 ? product.getTitle().substring(20) : product.getTitle());
                        String notification_message = "Reason: " + rejectReason;
                        notificationAsync.execute("ali", product.getVendor().getFcmKey(), notification_title, notification_message, "productRejected", "abc");

                        finish();
                    }
                });
    }

    private void getProductsFromDB() {

        mDatabase.child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    productArrayList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Product product = snapshot.getValue(Product.class);
                        if (product != null) {
                            if (product.getVendor() != null) {
                                if (product.getVendor() != null && product.getVendor().getVendorId() != null) {
                                    if (product.getVendor().getVendorId().equalsIgnoreCase(vendorId)) {
                                        if (Constants.PRODUCT_STATUS.equalsIgnoreCase("Out of stock")) {
                                            if (product.getQuantityAvailable() < 1) {
                                                productArrayList.add(product);
                                                performCalculations();
                                            }
                                        } else {
                                            if (product.getSellerProductStatus().equalsIgnoreCase(Constants.PRODUCT_STATUS)) {
                                                productArrayList.add(product);
                                                performCalculations();
                                            }
                                        }


                                    }
//
                                }
                            }
                        }

                    }
                    Collections.sort(productArrayList, new Comparator<Product>() {
                        @Override
                        public int compare(Product listData, Product t1) {
                            Long ob1 = listData.getTime();
                            Long ob2 = t1.getTime();

                            return ob2.compareTo(ob1);
                        }
                    });
                    adapter.notifyDataSetChanged();
                } else {
                    CommonUtils.showToast("No Data");
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void performCalculations() {
        totalReviewCount = 0;
        totalPositiveCount = 0;
        negCount = 0;
        posCount = 0;
        neutCount = 0;
        int personRated = 0;
        for (Product product : productArrayList) {
            totalReviewCount = totalReviewCount + product.getRatingCount();
            if (product.getRating() >= 3) {
                posCount = posCount + product.getPositiveCount();
                totalPositiveCount = totalPositiveCount + 1;
            } else if (product.getRating() > 2 && product.getRating() < 3) {
                neutCount = neutCount + product.getNeutralCount();
            } else {
                negCount = negCount + product.getNegativeCount();
            }
        }

        positiveCount.setText("" + posCount);
        negativeCount.setText("" + negCount);
        neutralCount.setText("" + neutCount);
        posCount = ((float) posCount / (float) totalReviewCount) * 100;
        neutCount = (neutCount / totalReviewCount) * 100;
        negCount = (negCount / totalReviewCount) * 100;
        positiveBar.setProgress((int) posCount);
        neutralBar.setProgress((int) neutCount);
        negativeBar.setProgress((int) negCount);
        float tt = ((float) totalPositiveCount / (float) totalReviewCount) * 100;
        positiveRatingText.setText(String.format("%.2f", tt) + "%");

        totalReviews.setText("" + totalReviewCount + " Customer reviews");

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
    public void onOptionSelected(String value) {
//        CommonUtils.showToast(value);
        rejectProduct(productArrayList.get(postion), value);

    }

    @Override
    public void onSuccess(String chatId) {

    }

    @Override
    public void onFailure() {

    }
}
