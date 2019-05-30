package com.appsinventiv.toolsbazzaradmin.Activities.Purchases;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Adapters.PurchaseOrderAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.CompanyDetailsModel;
import com.appsinventiv.toolsbazzaradmin.Models.ProductCountModel;
import com.appsinventiv.toolsbazzaradmin.Models.PurchaseOrderModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ViewPurchaseOrder extends AppCompatActivity {
    String purchaseOrder;
    DatabaseReference mDatabase;
    ArrayList<ProductCountModel> orderList = new ArrayList<>();
    TextView poNumber, date, address, total, storeAddress;
    RecyclerView recyclerview;
    RelativeLayout wholeLayout;
    PurchaseOrderModel model;
    PurchaseOrderAdapter adapter;
    RelativeLayout ll_linear;
    TextView employee;
    String path, from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_purchase_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true); getSupportActionBar().setElevation(0);
        }

        Intent i = getIntent();
        purchaseOrder = i.getStringExtra("po");
        path = i.getStringExtra("path");
        from = i.getStringExtra("from");
        ll_linear = findViewById(R.id.ll_linear);

        poNumber = findViewById(R.id.poNumber);
        date = findViewById(R.id.date);
        address = findViewById(R.id.address);
        total = findViewById(R.id.total);
        storeAddress = findViewById(R.id.storeAddress);
        recyclerview = findViewById(R.id.recyclerview);
        wholeLayout = findViewById(R.id.wholeLayout);
        employee = findViewById(R.id.employee);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(layoutManager);

        if (from.equalsIgnoreCase("final")) {
            getDataFromDb();
        } else if (from.equalsIgnoreCase("completed")) {
            getCompletedDataFromDb();
        } else if (from.equalsIgnoreCase("pending")) {
            getPendingDataFromDb();
        }


        adapter = new PurchaseOrderAdapter(this, orderList);
        recyclerview.setAdapter(adapter);

        getAddressFromDb();

    }

    private void getPendingDataFromDb() {
        mDatabase.child(path).child(purchaseOrder).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    PurchaseOrderModel model = dataSnapshot.getValue(PurchaseOrderModel.class);
                    if (model != null) {
                        poNumber.setText("PO number: " + model.getPoNumber());
                        ViewPurchaseOrder.this.setTitle("PO #: " + model.getPoNumber());

                        date.setText("" + CommonUtils.getFormattedDateOnly(model.getTime()));
                        total.setText("Total        Rs: " + model.getTotal());
                        employee.setText("Staff name: " + model.getEmployeeName());
                        orderList = model.getProductsList();
                        address.setText("Name: " + model.getVendor().getVendorName()
                                + "\nPhone: " + model.getVendor().getVendorPhone() + "\nAddress: " + model.getVendor().getVendorAddress());
                        adapter = new PurchaseOrderAdapter(ViewPurchaseOrder.this, orderList);
                        recyclerview.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        wholeLayout.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getCompletedDataFromDb() {
        mDatabase.child(path).child(purchaseOrder).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    PurchaseOrderModel model = dataSnapshot.getValue(PurchaseOrderModel.class);
                    if (model != null) {
                        poNumber.setText("PO number: " + model.getPoNumber());
                        ViewPurchaseOrder.this.setTitle("PO #: " + model.getPoNumber());

                        date.setText("" + CommonUtils.getFormattedDateOnly(model.getTime()));
                        total.setText("Total        Rs: " + model.getTotal());
                        employee.setText("Staff name: " + model.getEmployeeName());
                        orderList = model.getProductsList();
                        address.setText("Name: " + model.getVendor().getVendorName()
                                + "\nPhone: " + model.getVendor().getVendorPhone() + "\nAddress: " + model.getVendor().getVendorAddress());
                        adapter = new PurchaseOrderAdapter(ViewPurchaseOrder.this, orderList);
                        recyclerview.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        wholeLayout.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getAddressFromDb() {
        mDatabase.child("Settings").child("CompanyDetails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    CompanyDetailsModel model=dataSnapshot.getValue(CompanyDetailsModel.class);
                    storeAddress.setText(model.getAddress()+" "+model.getPhone()+" "+model.getEmail());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void getDataFromDb() {
        mDatabase.child("Accounts").child("PurchaseFinalized").child(path).child(purchaseOrder).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    PurchaseOrderModel model = dataSnapshot.getValue(PurchaseOrderModel.class);
                    if (model != null) {
                        poNumber.setText("PO number: " + model.getPoNumber());
                        ViewPurchaseOrder.this.setTitle("PO #: " + model.getPoNumber());

                        date.setText("" + CommonUtils.getFormattedDateOnly(model.getTime()));
                        total.setText("Total        Rs: " + model.getTotal());
                        employee.setText("Staff name: " + model.getEmployeeName());
                        orderList = model.getProductsList();
                        address.setText("Name: " + model.getVendor().getVendorName()
                                + "\nPhone: " + model.getVendor().getVendorPhone() + "\nAddress: " + model.getVendor().getVendorAddress());
                        adapter = new PurchaseOrderAdapter(ViewPurchaseOrder.this, orderList);
                        recyclerview.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        wholeLayout.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    public void saveBitmap(Bitmap bitmap) {
        String imgName = Long.toHexString(Double.doubleToLongBits(Math.random()));
        File imagePath = new File("/sdcard/" + "Purchase_Order_" + ".jpg");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            CommonUtils.showToast("Invoice saved in gallery\nKindly view it");
            Log.e("ImageSave", "Saveimage");
        } catch (FileNotFoundException e) {
            getPermissions();
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            getPermissions();
            Log.e("GREC", e.getMessage(), e);
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
    public void onBackPressed() {

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.print_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {

            finish();
        }
        if (id == R.id.action_print) {
            Bitmap bitmap1 = loadBitmapFromView(ll_linear, ll_linear.getWidth(), ll_linear.getHeight());
            saveBitmap(bitmap1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
