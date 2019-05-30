package com.appsinventiv.toolsbazzaradmin.Activities.Accounts.ExpensesAndRevenue;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.appsinventiv.toolsbazzaradmin.Adapters.SalariesAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.Employee;
import com.appsinventiv.toolsbazzaradmin.Models.SalaryModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Salaries extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Employee> itemList = new ArrayList<>();
    ArrayList<SalaryModel> salaryList = new ArrayList<>();
    Button save;
    SalariesAdapter adapter;
    DatabaseReference mDatabase;
    float total;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salaries);
        recyclerView = findViewById(R.id.recyclerView);
        save = findViewById(R.id.save);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true); getSupportActionBar().setElevation(0);
        }
        this.setTitle("Salaries");
        Intent i = getIntent();
        path = i.getStringExtra("path");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SalariesAdapter(Salaries.this, itemList, salaryList,path, new SalariesAdapter.SalaryInterface() {
            @Override
            public void values(ArrayList<SalaryModel> salaryModelList, ArrayList<Employee> employeeList) {
                for (int i = 0; i < employeeList.size(); i++) {
                    total = total+ ((salaryModelList.get(i).getBasicSalary()
                            + salaryModelList.get(i).getBonus()
                            + salaryModelList.get(i).getOverTime()) - (salaryModelList.get(i).getDeduction() + salaryModelList.get(i).getETFandEPF())
                    );
                    sendDataToServer(salaryModelList.get(i), employeeList.get(i), i);

                }

            }


        });
        recyclerView.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Admin").child("Employees").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getValue() != null) {
                    Employee model = dataSnapshot.getValue(Employee.class);
                    if (model != null) {
                        itemList.add(model);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        getDataFromServer(path);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.setValues();
            }
        });
    }

    private void getDataFromServer(String path) {
        mDatabase.child("Accounts").child("ExpensesAndRevenue").child(path).child("Salaries").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (!snapshot.getKey().equalsIgnoreCase("total")) {
                            SalaryModel model = snapshot.getValue(SalaryModel.class);
                            if (model != null) {
                                salaryList.add(model);
                            }
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

    private void sendDataToServer(SalaryModel salaryModel, Employee employee, int position) {
        long time = System.currentTimeMillis();
        mDatabase.child("Accounts").child("ExpensesAndRevenue")
                .child(path)
                .child("Salaries").child("" + position).setValue(new SalaryModel(
                "" + position,
                salaryModel.getBasicSalary(),
                salaryModel.getOverTime(),
                salaryModel.getBonus(),
                salaryModel.getDeduction(),
                salaryModel.getReason(),
                time,
                employee.getUsername(),
                salaryModel.getETFandEPF()
        )).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Updated");
                long time = System.currentTimeMillis();
                mDatabase.child("Accounts").child("ExpensesAndRevenue")
                        .child(CommonUtils.getYear(time))
                        .child(CommonUtils.getMonth(time))
                        .child("Salaries").child("total").setValue(calculateTotal());
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                CommonUtils.showToast(e.getMessage());
            }
        });
    }

    private float calculateTotal() {

        return total;
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

}
