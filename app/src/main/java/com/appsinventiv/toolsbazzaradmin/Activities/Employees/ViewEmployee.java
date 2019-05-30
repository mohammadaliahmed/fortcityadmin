package com.appsinventiv.toolsbazzaradmin.Activities.Employees;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Models.Employee;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewEmployee extends AppCompatActivity {
    String username;
    DatabaseReference mDatabase;
    TextView name, role, employeeRole, phone, email, user_name, password;
    Button update, assignRole;
    String number;
    Spinner spinner;
    int roleId;
    String[] rolesList = new String[]{"Select Role", "Admin", "Sales & Marketing",
            "Accountant/Cashier", "Purchasing Officer", "Operation Executive", "Delivery Boy", "Family"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_employee);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true); getSupportActionBar().setElevation(0);
        }
        Intent i = getIntent();
        username = i.getStringExtra("username");
        this.setTitle("Employee: " + username);

        name = findViewById(R.id.name);
        employeeRole = findViewById(R.id.employeeRole);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        update = findViewById(R.id.update);
        spinner = findViewById(R.id.roles);
        assignRole = findViewById(R.id.assignRole);
        user_name = findViewById(R.id.username);
        password = findViewById(R.id.password);


        setUpSpinner();

        assignRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNewRole();
            }
        });


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Admin").child("Employees").child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Employee employee = dataSnapshot.getValue(Employee.class);
                    if (employee != null) {
                        name.setText(employee.getName());
                        phone.setText(employee.getPhone());
                        email.setText(employee.getEmail());
                        number = employee.getPhone();
                        user_name.setText(employee.getUsername());
                        password.setText(employee.getPassword());
                        if (employee.getRole() == 0) {
                            employeeRole.setText("No role assigned");
                        } else {
                            employeeRole.setText(rolesList[employee.getRole()]);

                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             finish();
            }
        });
    }

    private void updateNewRole() {
        mDatabase.child("Admin").child("Employees").child(username).child("role").setValue(roleId)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (roleId != 0) {
                            CommonUtils.showToast("New role assigned to " + username + "  as " + rolesList[roleId]);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                CommonUtils.showToast("Error " + e.getMessage());
            }
        });
    }

    private void setUpSpinner() {
        final String[] items = new String[]{"Disable", "Admin", "Sales & Marketing",
                "Accountant/Cashier", "Purchasing Officer", "Operation Executive", "Delivery Boy", "Family"};


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                roleId = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
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
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
