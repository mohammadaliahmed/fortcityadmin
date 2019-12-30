package com.appsinventiv.toolsbazzaradmin.Activities.Employees;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.appsinventiv.toolsbazzaradmin.Activities.Login.Login;
import com.appsinventiv.toolsbazzaradmin.Activities.Login.Splash;
import com.appsinventiv.toolsbazzaradmin.Activities.MainPage.MainActivity;
import com.appsinventiv.toolsbazzaradmin.Models.Employee;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.appsinventiv.toolsbazzaradmin.Utils.SharedPrefs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PendingApproval extends AppCompatActivity {
    Button exit;

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Pending Approval");
        setContentView(R.layout.activity_pending_approval);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);

        }
        mDatabase = FirebaseDatabase.getInstance().getReference();

        exit = findViewById(R.id.exit);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PendingApproval.this, Login.class));
                finish();
            }
        });

        if (SharedPrefs.getEmployee() != null) {
            mDatabase.child("Admin").child(SharedPrefs.getEmployee().getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        Employee employee = dataSnapshot.getValue(Employee.class);
                        if (employee != null) {
                            if (!employee.isActive()) {
                                CommonUtils.showToast("Account approval pending");
                            } else {
                                CommonUtils.showToast("Your account is approved");
                                SharedPrefs.setEmployee(employee);
                                startActivity(new Intent(PendingApproval.this, MainActivity.class));
                                finish();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PendingApproval.this, Login.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(PendingApproval.this, Login.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

}
