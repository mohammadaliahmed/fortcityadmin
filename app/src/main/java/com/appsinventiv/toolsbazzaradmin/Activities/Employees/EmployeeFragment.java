package com.appsinventiv.toolsbazzaradmin.Activities.Employees;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.appsinventiv.toolsbazzaradmin.Adapters.EmployeesListAdapter;
import com.appsinventiv.toolsbazzaradmin.Adapters.OrdersAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.Employee;
import com.appsinventiv.toolsbazzaradmin.Models.OrderModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.appsinventiv.toolsbazzaradmin.Utils.SwipeControllerActions;
import com.appsinventiv.toolsbazzaradmin.Utils.SwipeToDeleteCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class EmployeeFragment extends Fragment {

    String type;
    DatabaseReference mDatabase;
    ArrayList<Employee> employeesList = new ArrayList<>();
    RecyclerView recyclerView;
    EmployeesListAdapter adapter;
    private SwipeToDeleteCallback swipeController;
    private Context context;

    public EmployeeFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public EmployeeFragment(String type) {
        this.type = type;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for context fragment
        View rootView = inflater.inflate(R.layout.fragment_employees, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new EmployeesListAdapter(context, employeesList, new EmployeesListAdapter.EmployeeCallbacks() {
            @Override
            public void onChangeStatus(Employee model, boolean abc) {
                mDatabase.child("Admin").child("Employees").child(model.getUsername()).child("active").setValue(abc).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Employee status changed");
                        adapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        CommonUtils.showToast(e.getMessage());
                    }
                });
            }

            @Override
            public void onApprove(Employee model, boolean value) {
                showApproveAlert(model, value);
            }

            @Override
            public void onBlock(Employee model, boolean value) {
                showBlockAlert(model, value);
            }
        });
        recyclerView.setAdapter(adapter);
        swipeController = new SwipeToDeleteCallback(new SwipeControllerActions() {
            @Override
            public void onRightClicked(final int position) {

                deleteEmployee(employeesList.get(position));

            }
        });
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
        getVendorsFromDb();

        return rootView;

    }

    private void showBlockAlert(final Employee model, final boolean value) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Alert");
        builder.setMessage(value ? "Block employee? " : "Unblock employee");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabase.child("Admin").child("Employees").child(model.getUsername()).child("blocked").setValue(value).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast(value ? "Employee blocked" : "Employee Unblocked");
                        adapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        CommonUtils.showToast(e.getMessage());
                    }
                });

            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showApproveAlert(final Employee model, final boolean value) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Alert");
        builder.setMessage(value ? "Approve employee?" : "un approve employee?");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabase.child("Admin").child("Employees").child(model.getUsername()).child("approved").setValue(value).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast(value ? "Approved" : "Unapproved");
                    }
                });

            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();


    }

    private void deleteEmployee(final Employee employee) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Delete employee?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        mDatabase.child("Admin").child("Employees").child(employee.getUsername()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                CommonUtils.showToast("Employee deleted");
                                adapter.notifyDataSetChanged();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                CommonUtils.showToast(e.getMessage());
                            }
                        });
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void getVendorsFromDb() {
        employeesList.clear();
        mDatabase.child("Admin").child("Employees").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    employeesList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Employee model = snapshot.getValue(Employee.class);
                        if (model != null) {
                            if (type.equalsIgnoreCase("All")) {
                                employeesList.add(model);
                            } else if (model.getRole().equalsIgnoreCase(type)) {
                                employeesList.add(model);
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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }


}
