package com.appsinventiv.toolsbazzaradmin.Activities.Accounts.ExpensesAndRevenue;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Adapters.SalariesReportAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.CompanyDetailsModel;
import com.appsinventiv.toolsbazzaradmin.Models.ExpensesAndRevenueModelMap;
import com.appsinventiv.toolsbazzaradmin.Models.InvoiceModel;
import com.appsinventiv.toolsbazzaradmin.Models.PurchaseOrderModel;
import com.appsinventiv.toolsbazzaradmin.Models.SalaryModel;
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

public class ViewExpensesReport extends AppCompatActivity {
    RelativeLayout ll_linear;
    RecyclerView recycler;
    ArrayList<SalaryModel> itemList = new ArrayList<>();
    SalariesReportAdapter adapter;
    private String path;
    DatabaseReference mDatabase;
    TextView which, leftText, rightText;
    float purchaseBanking = 0;
    float netProfitbanking = 0;
    float lost = 0;
    float sale = 0;
    float purchase = 0;
    float total = 0;

    float profit = 0;
    float cost = 0;
    private String leftTotal;
    private String rightTotal;

    TextView transportation, transportationTotal, rent, rentTotal, billsTotal, bills,
            stationaries, stationariesTotal, miscellaneous, miscellaneousTotal, remarks, shopAddress,totalSalaries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_expenses_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true); getSupportActionBar().setElevation(0);
        }
        this.setTitle("Expense Report");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Intent i = getIntent();
        path = i.getStringExtra("path");

        ll_linear = findViewById(R.id.ll_linear);
        recycler = findViewById(R.id.salariesRecycler);
        transportation = findViewById(R.id.transportation);
        transportationTotal = findViewById(R.id.transportationTotal);
        rent = findViewById(R.id.rent);
        rentTotal = findViewById(R.id.rentTotal);
        bills = findViewById(R.id.bills);
        billsTotal = findViewById(R.id.billsTotal);
        stationaries = findViewById(R.id.stationaries);
        stationariesTotal = findViewById(R.id.stationariesTotal);
        miscellaneous = findViewById(R.id.miscellaneous);
        miscellaneousTotal = findViewById(R.id.miscellaneousTotal);
        remarks = findViewById(R.id.remarks);
        shopAddress = findViewById(R.id.shopAddress);
        totalSalaries = findViewById(R.id.totalSalaries);
        which = findViewById(R.id.which);
        rightText = findViewById(R.id.rightText);
        leftText = findViewById(R.id.leftText);

        setUpSalariesRecycler();

        getAddressFromDb();

    }

    private void getSalesFromDb() {
        mDatabase.child("Accounts").child("InvoicesFinalized").child(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    for (DataSnapshot month : dataSnapshot.getChildren()) {
                        for (DataSnapshot daysSnapshot : month.getChildren()) {
                            InvoiceModel model = daysSnapshot.getValue(InvoiceModel.class);
                            if (model != null) {
                                sale = sale + model.getTotalPrice();
                                for (int i = 0; i < model.getCountModelArrayList().size(); i++) {
                                    profit = profit + (model.getShippingCharges() + model.getDeliveryCharges() + (model.getCountModelArrayList().get(i).getProduct().getRetailPrice() * model.getCountModelArrayList().get(i).getQuantity()) -
                                            (model.getCountModelArrayList().get(i).getProduct().getCostPrice() * model.getCountModelArrayList().get(i).getQuantity())
                                    );
                                }
                            }
                        }
                    }
                    leftTotal = "Sale: Rs " + sale + "\nPurchase: Rs" + purchase + "\nProfit: Rs " + profit + "\nCost: Rs " + cost;
                    leftText.setText(leftTotal);
                    rightTotal = "Expense: Rs " + total + "\nPurchase Banking: Rs" + purchase + "\nNet Profit: Rs " + (profit - total) + "\nLoss: Rs " + ((profit - total) < 0 ? (profit - total) : 0);
                    rightText.setText(rightTotal);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getPurchasesFromDb() {

        mDatabase.child("Accounts").child("PurchaseFinalized").child(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    for (DataSnapshot month : dataSnapshot.getChildren()) {
                        for (DataSnapshot daysSnapshot : month.getChildren()) {
                            PurchaseOrderModel model = daysSnapshot.getValue(PurchaseOrderModel.class);
                            if (model != null) {
                                cost = cost + model.getTotal();
                                purchase = purchase + model.getTotal();
                            }
                        }
                    }
                    leftTotal = "Sale: Rs " + sale + "\nPurchase: Rs" + purchase + "\nProfit: Rs " + profit + "\nCost: Rs " + cost;
                    leftText.setText(leftTotal);
                    float loss = ((profit - total) < 0 ? (profit - total) : 0);

                    rightTotal = "Expense: Rs " + total + "\nPurchase Banking: Rs" + purchase + "\nNet Profit: Rs " + (profit - total) + "\nLoss: Rs " + loss;
                    rightText.setText(rightTotal);

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
                    shopAddress.setText(model.getAddress()+" "+model.getPhone()+" "+model.getEmail());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setUpSalariesRecycler() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recycler.setLayoutManager(layoutManager);
        adapter = new SalariesReportAdapter(this, itemList);
        recycler.setAdapter(adapter);
        getSalariesDataFromDB();
        getOtherDataFromServer();
        getSalesFromDb();
        getPurchasesFromDb();
    }

    private void getOtherDataFromServer() {
        mDatabase.child("Accounts").child("ExpensesAndRevenue").child(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    ExpensesAndRevenueModelMap modelMap = dataSnapshot.getValue(ExpensesAndRevenueModelMap.class);
                    if (modelMap != null) {
                        if (modelMap.getTransportation() != null) {
                            transportation.setText("Rs " + modelMap.getTransportation().getOfficeTransportation() +
                                    "\nRs " + modelMap.getTransportation().getStaffTransportation() +
                                    "\nRs " + modelMap.getTransportation().getOfficeFuel() +
                                    "\nRs " + modelMap.getTransportation().getStaffFuel() +
                                    "\nRs " + modelMap.getTransportation().getShipping() +
                                    "\nRs " + modelMap.getTransportation().getDelivery());

                            transportationTotal.setText("Total Rs " + modelMap.getTransportation().getTotal());
                            total = total + modelMap.getTransportation().getTotal();

                        }
                        if (modelMap.getRent() != null) {

                            rent.setText("Rs " + modelMap.getRent().getOfficeRent() +
                                    "\nRs " + modelMap.getRent().getRentACar());
                            rentTotal.setText("Total Rs " + modelMap.getRent().getTotal());
                            total = total + modelMap.getRent().getTotal();
                        }

                        if (modelMap.getUtilityBills() != null) {

                            billsTotal.setText("Total Rs " + modelMap.getUtilityBills().getTotal());

                            bills.setText("Rs " + modelMap.getUtilityBills().getElectricCityBill() +
                                    "\nRs " + modelMap.getUtilityBills().getWaterBill() +
                                    "\nRs " + modelMap.getUtilityBills().getInternetBill() +
                                    "\nRs " + modelMap.getUtilityBills().getStaffInternetBill() +
                                    "\nRs " + modelMap.getUtilityBills().getOfficeTelephoneBill() +
                                    "\nRs " + modelMap.getUtilityBills().getStaffMobileBill() +
                                    "\nRs " + modelMap.getUtilityBills().getGovernmentTax());
                            total = total + modelMap.getUtilityBills().getTotal();

                        }
                        if (modelMap.getStationaries() != null) {

                            stationariesTotal.setText("Total Rs " + modelMap.getStationaries().getTotal());

                            stationaries.setText("Rs " + modelMap.getStationaries().getOfficeStationaries() +
                                    "\nRs " + modelMap.getStationaries().getStaffStationaries() +
                                    "\nRs " + modelMap.getStationaries().getAdvertising() +
                                    "\nRs " + modelMap.getStationaries().getBillBooksPrint() +
                                    "\nRs " + modelMap.getStationaries().getPackingMaterial() +
                                    "\nRs " + modelMap.getStationaries().getBusinessCardPrint());
                            total = total + modelMap.getStationaries().getTotal();


                        }
                        if (modelMap.getMiscellaneous() != null) {

                            miscellaneousTotal.setText("Total Rs " + modelMap.getMiscellaneous().getTotal());

                            remarks.setText(modelMap.getMiscellaneous().getRemarks1() + "\n" +
                                    modelMap.getMiscellaneous().getRemarks2() + "\n" +
                                    modelMap.getMiscellaneous().getRemarks3() + "\n" +
                                    modelMap.getMiscellaneous().getRemarks4() + "\n" +
                                    modelMap.getMiscellaneous().getRemarks5() + "\n"
                            );
                            miscellaneous.setText("Rs " + modelMap.getMiscellaneous().getCost1() +
                                    "\nRs " + modelMap.getMiscellaneous().getCost2() +
                                    "\nRs " + modelMap.getMiscellaneous().getCost3() +
                                    "\nRs " + modelMap.getMiscellaneous().getCost4() +
                                    "\nRs " + modelMap.getMiscellaneous().getCost5()
                            );
                            total = total + modelMap.getMiscellaneous().getTotal();



                        }
                        which.setText(path);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getSalariesDataFromDB() {
        mDatabase.child("Accounts").child("ExpensesAndRevenue").child(path).child("Salaries").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (!snapshot.getKey().equalsIgnoreCase("total")) {

                            SalaryModel model = snapshot.getValue(SalaryModel.class);
                            if (model != null) {
                                itemList.add(model);
                            }
                        }else if(snapshot.getKey().equalsIgnoreCase("total")){
                            total = total +snapshot.getValue(Float.class) ;
                            totalSalaries.setText("Total Rs "+snapshot.getValue(Float.class));
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

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    public void saveBitmap(Bitmap bitmap) {
        String imgName = Long.toHexString(Double.doubleToLongBits(Math.random()));
        File imagePath = new File("/sdcard/" + "Expenses_Report_" + ".jpg");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            CommonUtils.showToast("Expenses saved in gallery\nKindly view it");
            Log.e("ImageSave", "Saveimage");
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
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

    @Override
    public void onBackPressed() {
        finish();
    }
}
