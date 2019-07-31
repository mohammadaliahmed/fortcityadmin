package com.appsinventiv.toolsbazzaradmin.Activities.Customers;

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

import com.appsinventiv.toolsbazzaradmin.Adapters.OrdersAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.Customer;
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
import java.util.HashMap;


public class CustomerFragment extends Fragment {

    Context context;
    ArrayList<Customer> arrayList = new ArrayList<>();
    CustomersListAdapter adapter;
    ProgressBar progress;
    DatabaseReference mDatabase;
    String type;
    private SwipeToDeleteCallback swipeController;
    HashMap<String, Boolean> map = new HashMap<>();


    public CustomerFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ValidFragment")
    public CustomerFragment(String type) {
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_orders, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_orders);
        progress = rootView.findViewById(R.id.progress);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CustomersListAdapter(context, arrayList, new CustomersListAdapter.CustomerListCallback() {
            @Override
            public void onStatusChanged(Customer customers, boolean status) {
                changeCustomerStatus(customers, status);
            }
        });
        recyclerView.setAdapter(adapter);
        swipeController = new SwipeToDeleteCallback(new SwipeControllerActions() {
            @Override
            public void onRightClicked(final int position) {

                deleteUser(arrayList.get(position).getUsername());

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


        return rootView;

    }

    private void deleteUser(String username) {
        mDatabase.child("Customers").child(username).child("isDeleted").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("User deleted");
            }
        });
    }

    private void changeCustomerStatus(Customer customers, final boolean status) {
        mDatabase.child("Customers").child(customers.getUsername()).child("status").setValue(status).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast(status ? "User is enabled" : "User is disabled");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        getDataFromServer();
    }

    private void getDataFromServer() {

        mDatabase.child("Customers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    arrayList.clear();
                    map.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Customer customer = snapshot.getValue(Customer.class);
                        if (customer != null) {
                            if (customer.getName() != null) {
                                if (customer.getCustomerType().equalsIgnoreCase(type)) {
                                    if (!customer.isDeleted()) {
                                        arrayList.add(customer);
                                        map.put(customer.getUsername(), customer.isOnline());
                                        adapter.setUserStatus(map);
//                                    getUserStatus(customer.getUsername());
                                    }
                                }

                            }
                        }
                    }
                    Collections.sort(arrayList, new Comparator<Customer>() {
                        @Override
                        public int compare(Customer listData, Customer t1) {
                            String ob1 = listData.getName();
                            String ob2 = t1.getName();

                            return ob1.compareTo(ob2);

                        }
                    });
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getUserStatus(String userId) {

        mDatabase.child("Customers").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Customer customer = dataSnapshot.getValue(Customer.class);
                if (customer != null) {

                    map.put(customer.getUsername(), customer.isOnline());
                    adapter.setUserStatus(map);
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
