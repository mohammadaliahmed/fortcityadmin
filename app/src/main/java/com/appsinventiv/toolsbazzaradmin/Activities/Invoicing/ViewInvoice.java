package com.appsinventiv.toolsbazzaradmin.Activities.Invoicing;

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

import com.appsinventiv.toolsbazzaradmin.Adapters.InvoiceAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.CompanyDetailsModel;
import com.appsinventiv.toolsbazzaradmin.Models.Customer;
import com.appsinventiv.toolsbazzaradmin.Models.InvoiceModel;
import com.appsinventiv.toolsbazzaradmin.Models.LocationAndChargesModel;
import com.appsinventiv.toolsbazzaradmin.Models.ProductCountModel;
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

public class ViewInvoice extends AppCompatActivity {
    long invoiceNumber;
    DatabaseReference mDatabase;
    ArrayList<ProductCountModel> allProductsInOneOrderList = new ArrayList<>();
    ArrayList<ProductCountModel> availableProductsInOneOrderList = new ArrayList<>();
    TextView invoiceNumberText, date, address, delivery, total, storeAddress, orderNumber, grandTotal, shipping;
    RecyclerView recyclerView;
    InvoiceAdapter adapter;
    RelativeLayout wholeLayout;
    InvoiceModel model;
    RelativeLayout ll_linear;
    LocationAndChargesModel locationAndChargesModel;
    String locationId;
    String path;
    float totalPrice = 0;
    String from;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_invoice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true); getSupportActionBar().setElevation(0);
        }
        ll_linear = findViewById(R.id.ll_linear);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        invoiceNumberText = findViewById(R.id.poNumber);
        date = findViewById(R.id.date);
        address = findViewById(R.id.address);
        delivery = findViewById(R.id.delivery);
        total = findViewById(R.id.total);
        recyclerView = findViewById(R.id.recyclerview);
        wholeLayout = findViewById(R.id.wholeLayout);
        storeAddress = findViewById(R.id.storeAddress);
        orderNumber = findViewById(R.id.orderNumber);
        grandTotal = findViewById(R.id.grandTotal);
        shipping = findViewById(R.id.shipping);


        recyclerView.setNestedScrollingEnabled(false);

        Intent i = getIntent();
        invoiceNumber = i.getLongExtra("invoiceNumber", 0);
        path = i.getStringExtra("path");
        from = i.getStringExtra("from");
        this.setTitle("Invoice # " + invoiceNumber);


        setUpRecycler();
        if (from.equalsIgnoreCase("pending")) {
            getPendignInvoicesFromDB();
        } else {
            getInvoiceFromDb();
        }

        getAddressFromDb();


    }

    private void getPendignInvoicesFromDB() {
        mDatabase.child("Accounts/PendingInvoices/")
                .child("" + invoiceNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    model = dataSnapshot.getValue(InvoiceModel.class);
                    if (model != null) {

                        allProductsInOneOrderList = model.getCountModelArrayList();
                        availableProductsInOneOrderList = model.getNewCountModelArrayList();
                        invoiceNumberText.setText("Invoice # " + model.getId());
                        date.setText("" + CommonUtils.getFormattedDateOnly(model.getTime()));

                        address.setText("Name:  " + model.getCustomer().getName() + "\nPhone: " + model.getCustomer().getPhone() + "\nAddress:  " + model.getCustomer().getAddress()
                                + ", " + model.getCustomer().getCity()
                                + "\nCountry: " + model.getCustomer().getCountry());


                        locationAndChargesModel = new LocationAndChargesModel();
                        locationAndChargesModel.setCountryName(model.getCustomer().getCountry());
                        locationAndChargesModel.setCurrency(model.getCustomer().getCurrencySymbol());
                        locationAndChargesModel.setCurrencyRate(model.getCustomer().getCurrencyRate());


                        adapter = new InvoiceAdapter(ViewInvoice.this,
                                allProductsInOneOrderList,
                                availableProductsInOneOrderList,
                                model.getCustomer().getCustomerType(), locationAndChargesModel);
                        setUpLayout(model.getCustomer());
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

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

    private void setUpRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

    }

    private void getInvoiceFromDb() {
        mDatabase.child("Accounts/InvoicesFinalized/" + path)
                .child("" + invoiceNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    model = dataSnapshot.getValue(InvoiceModel.class);
                    if (model != null) {

                        allProductsInOneOrderList = model.getCountModelArrayList();
                        availableProductsInOneOrderList = model.getNewCountModelArrayList();
                        invoiceNumberText.setText("Invoice # " + model.getId());
                        date.setText("" + CommonUtils.getFormattedDateOnly(model.getTime()));

                        address.setText("Name:  " + model.getCustomer().getName() + "\nPhone: " + model.getCustomer().getPhone() + "\nAddress:  " + model.getCustomer().getAddress()
                                + ", " + model.getCustomer().getCity()
                                + "\nCountry: " + model.getCustomer().getCountry());


                        locationAndChargesModel = new LocationAndChargesModel();
                        locationAndChargesModel.setCountryName(model.getCustomer().getCountry());
                        locationAndChargesModel.setCurrency(model.getCustomer().getCurrencySymbol());
                        locationAndChargesModel.setCurrencyRate(model.getCustomer().getCurrencyRate());


                        adapter = new InvoiceAdapter(ViewInvoice.this,
                                allProductsInOneOrderList,
                                availableProductsInOneOrderList,
                                model.getCustomer().getCustomerType(), locationAndChargesModel);

                        setUpLayout(model.getCustomer());

                        recyclerView.setAdapter(adapter);
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

    private void setUpLayout(final Customer customer) {
        locationAndChargesModel = new LocationAndChargesModel();
        locationAndChargesModel.setCountryName(customer.getCountry());
        locationAndChargesModel.setCurrency(customer.getCurrencySymbol());
        locationAndChargesModel.setCurrencyRate(customer.getCurrencyRate());
        adapter.location(locationAndChargesModel);
//                        adapter.notifyDataSetChanged();
        for (int i = 0; i < model.getCountModelArrayList().size(); i++) {
            totalPrice = totalPrice + (((model.getCountModelArrayList().get(i).getProduct().getRetailPrice()
                    * model.getCountModelArrayList().get(i).getQuantity())
            ));

        }
        total.setText(locationAndChargesModel.getCurrency() + " " +
                CommonUtils.getFormattedPrice(totalPrice*model.getCustomer().getCurrencyRate()));


        delivery.setText(locationAndChargesModel.getCurrency() + " " +
                CommonUtils.getFormattedPrice(model.getDeliveryCharges() * model.getCustomer().getCurrencyRate()));


        shipping.setText(locationAndChargesModel.getCurrency() + " " +
                CommonUtils.getFormattedPrice(model.getShippingCharges() * model.getCustomer().getCurrencyRate()));

        grandTotal.setText(locationAndChargesModel.getCurrency() + " " +
                CommonUtils.getFormattedPrice(model.getGrandTotal() * model.getCustomer().getCurrencyRate()));
        orderNumber.setText("Order number: " + model.getOrderId());
        wholeLayout.setVisibility(View.GONE);


    }

    public void CalculateTotal() {

    }

    @Override
    public void onBackPressed() {

        finish();
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

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    public void saveBitmap(Bitmap bitmap) {
        String imgName = Long.toHexString(Double.doubleToLongBits(Math.random()));
        File imagePath = new File("/sdcard/" + "Invoice_Number_" + ".jpg");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.print_menu, menu);
        return true;
    }


}
