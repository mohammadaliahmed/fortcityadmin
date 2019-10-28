package com.appsinventiv.toolsbazzaradmin.Activities.Employees;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.MainPage.MainActivity;
import com.appsinventiv.toolsbazzaradmin.Models.Employee;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.appsinventiv.toolsbazzaradmin.Utils.SharedPrefs;
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
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewEmployee extends AppCompatActivity {
    String username;
    DatabaseReference mDatabase;
    TextView name, role, employeeRole, phone, email, user_name, password;
    Button update, assignRole;
    String number;
    String roleId;
    ScrollView mScrollView;
    LinearLayout mStitchingWorksListView;
    EditText salary;
    Spinner spinner;


    boolean oldValue;
    String[] rolesList = new String[]{"Select Role", "Admin", "Sales & Marketing",
            "Accountant/Cashier", "Purchasing Officer", "Operation Executive", "Delivery Boy", "Family"};
    public static String[] newRoleList = new String[]{"CEO", "IT Department", "Accounts Manager", "Accounting Team Leader",
            "Accounting Supervisor", "Senior Accountant", "Accountant", "Sales & Marketing Manager", "Sales & Marketing Leader"
            , "Sales & Marketing Supervisor", "Senior Sales & Marketing Executive", "Sales Executive", "Customer Care Manager",
            "Customer Care Leader", "Customer Care Supervisor", "Senior Customer Care Executive", "Customer Care Executive",
            "Call Center Executive", "Operations Manager", "Operations Leader", "Operations Supervisor", "Senior Operations Executive",
            "Operations Executive III", "Operations Executive II", "Operations Executive I", "Delivery Boy", "Procurement Manager",
            "Procurement Leader", "Procurement Supervisor", "Senior Procurement Executive",
            "Procurement Executive", "Purchasing Officer"};
    CircleImageView userImage;
    TextView textName, textRole;

    Switch approveSwitch;


    private Button btn;
    private ExpandableListView lvCategory;

    private ArrayList<DataItem> arCategory;
    private ArrayList<SubCategoryItem> arSubCategory;
    private ArrayList<ArrayList<SubCategoryItem>> arSubCategoryFinal;

    private ArrayList<HashMap<String, String>> parentItems;
    private ArrayList<ArrayList<HashMap<String, String>>> childItems;
    private MyCategoriesExpandableListAdapter myCategoriesExpandableListAdapter;
    private Employee employee;

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
//        this.setTitle(username);

        mStitchingWorksListView = findViewById(R.id.mStitchingWorksListView);
        mScrollView = findViewById(R.id.mScrollView);
        name = findViewById(R.id.name);


        employeeRole = findViewById(R.id.employeeRole);
        spinner = findViewById(R.id.spinner);
        phone = findViewById(R.id.phone);
        salary = findViewById(R.id.salary);
        email = findViewById(R.id.email);
        update = findViewById(R.id.update);
        assignRole = findViewById(R.id.assignRole);
        user_name = findViewById(R.id.username);
        password = findViewById(R.id.password);
        userImage = findViewById(R.id.userImage);
        textRole = findViewById(R.id.textRole);
        textName = findViewById(R.id.textName);
        approveSwitch = findViewById(R.id.approveSwitch);


        approveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isPressed()) {
                    shwoAlert(b);
                }
            }
        });


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Admin").child("Employees").child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    employee = dataSnapshot.getValue(Employee.class);
                    if (employee != null) {
                        ViewEmployee.this.setTitle(employee.getName());

                        if(SharedPrefs.getEmployee().getUsername().equalsIgnoreCase(employee.getUsername())){
                            SharedPrefs.setEmployee(employee);
                        }
                        name.setText(employee.getName());
                        phone.setText(employee.getPhone());
                        email.setText(employee.getEmail());
                        number = employee.getPhone();
                        user_name.setText(employee.getUsername());
                        password.setText(employee.getPassword());
                        textName.setText(employee.getName());
                        salary.setText("" + employee.getSalary());

                        setupReferences();


                        if (employee.isApproved()) {
                            approveSwitch.setChecked(true);
                            oldValue = true;
                        }

                        if (employee.getPicUrl() != null) {
                            try {
                                Glide.with(ViewEmployee.this).load(employee.getPicUrl()).into(userImage);

                            } catch (Exception e) {

                            }
                        }
                        if (employee.getRole() == null) {
                            employeeRole.setText("No role assigned");
                            textRole.setText("No role assigned");
                        } else {
                            employeeRole.setText(employee.getRole());
                            textRole.setText(employee.getRole());

                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                long sala = 0;
//                String sal = salary.getText().toString();
//                if (sal.equalsIgnoreCase("")) {
//                    sala = Long.parseLong(sal);
//                }

                mDatabase.child("Admin").child("Employees").child(username).child("role").setValue(roleId).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Roles updated");
                        Employee employee = SharedPrefs.getEmployee();
                        employee.setRole(roleId);
                        SharedPrefs.setEmployee(employee);
                    }
                });
            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmployeRolesToDB();
//                Intent intent = new Intent(ViewEmployee.this, CheckedActivity.class);
//                startActivity(intent);
            }
        });


//        setupReferences();
        setUpSpinner();

    }


    private void updateEmployeRolesToDB() {
        int count = 0;
        List<String> roles = new ArrayList<>();
        for (int i = 0; i < MyCategoriesExpandableListAdapter.parentItems.size(); i++) {

            String isChecked = MyCategoriesExpandableListAdapter.parentItems.get(i).get(ConstantManager.Parameter.IS_CHECKED);

            if (isChecked.equalsIgnoreCase(ConstantManager.CHECK_BOX_CHECKED_TRUE)) {
//                tvParent.setText(tvParent.getText() + MyCategoriesExpandableListAdapter.parentItems.get(i).get(ConstantManager.Parameter.CATEGORY_NAME));
            }

            for (int j = 0; j < MyCategoriesExpandableListAdapter.childItems.get(i).size(); j++) {
                count++;
                String isChildChecked = MyCategoriesExpandableListAdapter.childItems.get(i).get(j).get(ConstantManager.Parameter.IS_CHECKED);

                if (isChildChecked.equalsIgnoreCase(ConstantManager.CHECK_BOX_CHECKED_TRUE)) {
                    roles.add(ViewEmployee.newRoleList[count]);
//                    tvChild.setText(tvChild.getText() + " , " + MyCategoriesExpandableListAdapter.parentItems.get(i).get(ConstantManager.Parameter.CATEGORY_NAME) + " " + (j + 1));
                }

            }

        }
        if (roles.size() > 0) {
            mDatabase.child("Admin").child("Employees").child(username).child("roles").setValue(roles).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    CommonUtils.showToast("Roles updated");
                }
            });
        }
    }

    private void shwoAlert(final boolean b) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewEmployee.this);
        builder.setTitle("Alert");
        builder.setMessage(b ? "Approve employee" : "Un Approve employee?");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabase.child("Admin").child("Employees").child(username).child("approved").setValue(b).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast(b ? "Approved" : "Un Approved");
                    }
                });

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                approveSwitch.setChecked(oldValue);

            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
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

            if (employee.getRoles() != null && employee.getRoles().contains(newRoleList[i])) {
                subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_TRUE);
            } else {
                subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            }
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
//            subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            if (employee.getRoles() != null && employee.getRoles().contains(newRoleList[j])) {
                subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_TRUE);
            } else {
                subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            }
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
//            subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            if (employee.getRoles() != null && employee.getRoles().contains(newRoleList[k])) {
                subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_TRUE);
            } else {
                subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            }
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
//            subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            if (employee.getRoles() != null && employee.getRoles().contains(newRoleList[k])) {
                subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_TRUE);
            } else {
                subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            }
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
//            subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            if (employee.getRoles() != null && employee.getRoles().contains(newRoleList[k])) {
                subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_TRUE);
            } else {
                subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            }
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
//            subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            if (employee.getRoles() != null && employee.getRoles().contains(newRoleList[k])) {
                subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_TRUE);
            } else {
                subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            }
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
                roleId = items[position];
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
