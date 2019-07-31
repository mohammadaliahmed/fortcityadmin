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
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.MainPage.MainActivity;
import com.appsinventiv.toolsbazzaradmin.Models.Employee;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

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
    String[] newRoleList = new String[]{"CEO", "IT Department", "Accounts Manager", "Accounting Team Leader",
            "Accounting Supervisor", "Senior Accountant", "Accountant", "Sales & Marketing Manager", "Sales & Marketing Leader"
            , "Sales & Marketing Supervisor", "Senior Sales & Marketing Executive", "Sales Executive", "Customer Care Manager",
            "Customer Care Leader", "Customer Care Supervisor", "Senior Customer Care Executive", "Customer Care Executive",
            "Call Center Executive", "Operations Manager", "Operations Leader", "Operations Supervisor", "Senior Operations Executive",
            "Operations Executive III", "Operations Executive II", "Operations Executive I", "Delivery Boy", "Procurement Manager",
            "Procurement Leader", "Procurement Supervisor", "Senior Procurement Executive",
            "Procurement Executive", "Purchasing Officer"};
    CircleImageView userImage;
    TextView textName, textRole;


    private Button btn;
    private ExpandableListView lvCategory;

    private ArrayList<DataItem> arCategory;
    private ArrayList<SubCategoryItem> arSubCategory;
    private ArrayList<ArrayList<SubCategoryItem>> arSubCategoryFinal;

    private ArrayList<HashMap<String, String>> parentItems;
    private ArrayList<ArrayList<HashMap<String, String>>> childItems;
    private MyCategoriesExpandableListAdapter myCategoriesExpandableListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_employee);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
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
        userImage = findViewById(R.id.userImage);
        textRole = findViewById(R.id.textRole);
        textName = findViewById(R.id.textName);


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
                        textName.setText(employee.getName());

                        if (employee.getPicUrl() != null) {
                            Glide.with(ViewEmployee.this).load(employee.getPicUrl()).into(userImage);
                        }
                        if (employee.getRole() == 0) {
                            employeeRole.setText("No role assigned");
                            textRole.setText("No role assigned");
                        } else {
                            employeeRole.setText(rolesList[employee.getRole()]);
                            textRole.setText(rolesList[employee.getRole()]);

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


        btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewEmployee.this, CheckedActivity.class);
                startActivity(intent);
            }
        });

        setupReferences();

    }

    private void setupReferences() {

        lvCategory = findViewById(R.id.lvCategory);
        arCategory = new ArrayList<>();
        arSubCategory = new ArrayList<>();
        parentItems = new ArrayList<>();
        childItems = new ArrayList<>();

        DataItem dataItem = new DataItem();
        dataItem.setCategoryId("1");
        dataItem.setCategoryName("Admin");

        arSubCategory = new ArrayList<>();
        for (int i = 0; i < 2; i++) {

            SubCategoryItem subCategoryItem = new SubCategoryItem();
            subCategoryItem.setCategoryId(String.valueOf(i));
            subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            subCategoryItem.setSubCategoryName(newRoleList[i]);
            arSubCategory.add(subCategoryItem);
        }


        dataItem.setSubCategory(arSubCategory);
        arCategory.add(dataItem);

        dataItem = new DataItem();
        dataItem.setCategoryId("2");
        dataItem.setCategoryName("Accounting");
        arSubCategory = new ArrayList<>();
        for (int j = 2; j < 7; j++) {

            SubCategoryItem subCategoryItem = new SubCategoryItem();
            subCategoryItem.setCategoryId(String.valueOf(j));
            subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            subCategoryItem.setSubCategoryName(newRoleList[j]);
            arSubCategory.add(subCategoryItem);
        }
        dataItem.setSubCategory(arSubCategory);
        arCategory.add(dataItem);

        dataItem = new DataItem();
        dataItem.setCategoryId("3");
        dataItem.setCategoryName("Sales & Marketing");
        arSubCategory = new ArrayList<>();
        for (int k = 7; k < 12; k++) {

            SubCategoryItem subCategoryItem = new SubCategoryItem();
            subCategoryItem.setCategoryId(String.valueOf(k));
            subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            subCategoryItem.setSubCategoryName(newRoleList[k]);
            arSubCategory.add(subCategoryItem);
        }

        dataItem.setSubCategory(arSubCategory);
        arCategory.add(dataItem);


        dataItem = new DataItem();
        dataItem.setCategoryId("4");
        dataItem.setCategoryName("Customer Care");
        arSubCategory = new ArrayList<>();
        for (int k = 12; k < 18; k++) {

            SubCategoryItem subCategoryItem = new SubCategoryItem();
            subCategoryItem.setCategoryId(String.valueOf(k));
            subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            subCategoryItem.setSubCategoryName(newRoleList[k]);
            arSubCategory.add(subCategoryItem);
        }

        dataItem.setSubCategory(arSubCategory);
        arCategory.add(dataItem);


        dataItem = new DataItem();
        dataItem.setCategoryId("5");
        dataItem.setCategoryName("Operations");
        arSubCategory = new ArrayList<>();
        for (int k = 18; k < 26; k++) {

            SubCategoryItem subCategoryItem = new SubCategoryItem();
            subCategoryItem.setCategoryId(String.valueOf(k));
            subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            subCategoryItem.setSubCategoryName(newRoleList[k]);
            arSubCategory.add(subCategoryItem);
        }

        dataItem.setSubCategory(arSubCategory);
        arCategory.add(dataItem);


        dataItem = new DataItem();
        dataItem.setCategoryId("6");
        dataItem.setCategoryName("Procument");
        arSubCategory = new ArrayList<>();
        for (int k = 26; k < 32; k++) {

            SubCategoryItem subCategoryItem = new SubCategoryItem();
            subCategoryItem.setCategoryId(String.valueOf(k));
            subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            subCategoryItem.setSubCategoryName(newRoleList[k]);
            arSubCategory.add(subCategoryItem);
        }

        dataItem.setSubCategory(arSubCategory);
        arCategory.add(dataItem);

        for (DataItem data : arCategory) {
//                        Log.i("Item id",item.id);
            ArrayList<HashMap<String, String>> childArrayList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> mapParent = new HashMap<String, String>();

            mapParent.put(ConstantManager.Parameter.CATEGORY_ID, data.getCategoryId());
            mapParent.put(ConstantManager.Parameter.CATEGORY_NAME, data.getCategoryName());

            int countIsChecked = 0;
            for (SubCategoryItem subCategoryItem : data.getSubCategory()) {

                HashMap<String, String> mapChild = new HashMap<String, String>();
                mapChild.put(ConstantManager.Parameter.SUB_ID, subCategoryItem.getSubId());
                mapChild.put(ConstantManager.Parameter.SUB_CATEGORY_NAME, subCategoryItem.getSubCategoryName());
                mapChild.put(ConstantManager.Parameter.CATEGORY_ID, subCategoryItem.getCategoryId());
                mapChild.put(ConstantManager.Parameter.IS_CHECKED, subCategoryItem.getIsChecked());

                if (subCategoryItem.getIsChecked().equalsIgnoreCase(ConstantManager.CHECK_BOX_CHECKED_TRUE)) {

                    countIsChecked++;
                }
                childArrayList.add(mapChild);
            }

            if (countIsChecked == data.getSubCategory().size()) {

                data.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_TRUE);
            } else {
                data.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            }

            mapParent.put(ConstantManager.Parameter.IS_CHECKED, data.getIsChecked());
            childItems.add(childArrayList);
            parentItems.add(mapParent);

        }

        ConstantManager.parentItems = parentItems;
        ConstantManager.childItems = childItems;

        myCategoriesExpandableListAdapter = new MyCategoriesExpandableListAdapter(this, parentItems, childItems, false);
        lvCategory.setAdapter(myCategoriesExpandableListAdapter);
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
