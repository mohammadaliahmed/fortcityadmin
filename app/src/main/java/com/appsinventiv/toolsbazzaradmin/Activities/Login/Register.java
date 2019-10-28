package com.appsinventiv.toolsbazzaradmin.Activities.Login;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appsinventiv.toolsbazzaradmin.Activities.AppSettings.ViewTerms;
import com.appsinventiv.toolsbazzaradmin.Activities.Employees.PendingApproval;
import com.appsinventiv.toolsbazzaradmin.Activities.Welcome;
import com.appsinventiv.toolsbazzaradmin.Models.Customer;
import com.appsinventiv.toolsbazzaradmin.Models.Employee;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.appsinventiv.toolsbazzaradmin.Utils.PrefManager;
import com.appsinventiv.toolsbazzaradmin.Utils.SharedPrefs;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Register extends AppCompatActivity {
    Button signup;
    TextView login;
    DatabaseReference mDatabase;
    private PrefManager prefManager;
    ArrayList<String> userslist = new ArrayList<String>();
    EditText e_fullname, e_username, e_email, e_password, e_phone;
    String fullname, username, email, password, phone, address, city;
    long time;
    TextView viewTerms;
    private View systemUIView;

    HashMap<String, Employee> emailMap = new HashMap<>();
    HashMap<String, Employee> usernameMap = new HashMap<>();
    HashMap<String, Employee> phoneMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        this.setTitle("Register");

        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();


        getEmployessFromDB();

        viewTerms = findViewById(R.id.viewTerms);
        viewTerms.setPaintFlags(viewTerms.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        e_fullname = findViewById(R.id.name);
        e_username = findViewById(R.id.username);
        e_email = findViewById(R.id.email);
        e_password = findViewById(R.id.password);
        e_phone = findViewById(R.id.phone);

        signup = findViewById(R.id.signup);
        login = findViewById(R.id.signin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Register.this, Login.class);
                startActivity(i);
                finish();
            }
        });


        viewTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Register.this, ViewTerms.class);
                startActivity(i);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (e_fullname.getText().toString().length() == 0) {
                    e_fullname.setError("Cannot be null");
                } else if (e_username.getText().toString().length() == 0) {
                    e_username.setError("Cannot be null");
                } else if (e_email.getText().toString().length() == 0) {
                    e_email.setError("Cannot be null");
                } else if (e_password.getText().toString().length() == 0) {
                    e_password.setError("Cannot be null");
                } else if (e_phone.getText().toString().length() == 0) {
                    e_phone.setError("Cannot be null");
                } else {
                    fullname = e_fullname.getText().toString();
                    username = e_username.getText().toString();
                    email = e_email.getText().toString();
                    password = e_password.getText().toString();
                    phone = e_phone.getText().toString();


                    if (phoneMap.containsKey(phone)) {
                        CommonUtils.showToast("We found an Existing Account under same phone number. Try to Sign in using your Password");

                    } else if (emailMap.containsKey(email)) {
                        CommonUtils.showToast("We found an Existing Account under same Email.Try to Sign in using your Password");
                    } else if (usernameMap.containsKey(username)) {
                        CommonUtils.showToast("Username is already taken\nPlease choose another");
                    } else {

                        time = System.currentTimeMillis();
                        int randomPIN = (int) (Math.random() * 900000) + 100000;
                        final Employee employee = new Employee(username, fullname, email, password, phone,
                                SharedPrefs.getFcmKey(), "", System.currentTimeMillis(),
                                randomPIN,
                                false, false, false, false

                        );
                        mDatabase.child("Admin").child("Employees")
                                .child(username)
                                .setValue(employee
                                )
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Register.this, "Thank you for registering", Toast.LENGTH_SHORT).show();
                                        SharedPrefs.setEmployee(employee);
                                        SharedPrefs.setUsername(username);
                                        SharedPrefs.setFullName(fullname);
                                        SharedPrefs.setIsLoggedIn("yes");
                                        launchHomeScreen();

//                                        startActivity(new Intent(Register.this, EmployeeVerficiation.class));

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Register.this, "There was some error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                }
            }
        });


    }

    private void getEmployessFromDB() {
        mDatabase.child("Admin").child("Employees").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Employee customer = snapshot.getValue(Employee.class);
                        if (customer != null && customer.getUsername() != null) {
                            emailMap.put(customer.getEmail(), customer);
                            usernameMap.put(customer.getUsername(), customer);
                            phoneMap.put(customer.getPhone(), customer);
                        }
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
        startActivity(new Intent(Register.this, Login.class));
        finish();
    }

    private void launchHomeScreen() {
//        prefManager.setFirstTimeLaunch(false);
//        prefManager.setIsFirstTimeLaunchWelcome(false);

        startActivity(new Intent(Register.this, PendingApproval.class));

        finish();
    }


}
