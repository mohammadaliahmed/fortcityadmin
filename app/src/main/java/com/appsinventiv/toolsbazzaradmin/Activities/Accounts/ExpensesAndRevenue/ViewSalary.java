package com.appsinventiv.toolsbazzaradmin.Activities.Accounts.ExpensesAndRevenue;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Models.CompanyDetailsModel;
import com.appsinventiv.toolsbazzaradmin.Models.Employee;
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

public class ViewSalary extends AppCompatActivity {
    TextView date, total, storeAddress, salary, overTime, bonus, reason, deduction, employee,etfepf;
    DatabaseReference mDatabase;
    RelativeLayout ll_linear;
    float totalPrice = 0;
    String salaryId;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_salary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Intent i = getIntent();
        salaryId = i.getStringExtra("salaryId");
        path = i.getStringExtra("path");


        date = findViewById(R.id.date);
        total = findViewById(R.id.total);
        storeAddress = findViewById(R.id.storeAddress);
        ll_linear = findViewById(R.id.ll_linear);
        salary = findViewById(R.id.salary);
        overTime = findViewById(R.id.overTime);
        bonus = findViewById(R.id.bonus);
        reason = findViewById(R.id.reason);
        deduction = findViewById(R.id.deduction);
        employee = findViewById(R.id.employee);
        etfepf = findViewById(R.id.etfepf);

        getDataFromSever();

    }

    private void getDataFromSever() {
        mDatabase.child("Accounts").child("ExpensesAndRevenue").child(path).child("Salaries").child(salaryId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    SalaryModel model = dataSnapshot.getValue(SalaryModel.class);
                    if (model != null) {
                        CommonUtils.getFormattedDate(model.getTime());
                        salary.setText("Rs. " + model.getBasicSalary());
                        overTime.setText("Rs. " + model.getOverTime());
                        bonus.setText("Rs. " + model.getBonus());
                        reason.setText("5. Reason: " + model.getReason());
                        deduction.setText("-Rs. " + model.getDeduction());
                        etfepf.setText("-Rs. " + model.getETFandEPF());
                        total.setText("Total: Rs. " + model.getTotal());
                        getEmployeeFromDb(model.getUserId());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        getAddressFromDb();


    }

    private void getEmployeeFromDb(String userId) {
        mDatabase.child("Admin").child("Employees").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Employee employeeModel = dataSnapshot.getValue(Employee.class);
                    if (employeeModel != null) {
                        employee.setText(CommonUtils.rolesList[employeeModel.getRole()] + ": " + employeeModel.getName());
                        ViewSalary.this.setTitle("Salary Slip: " + employeeModel.getName());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {

        finish();
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
}
