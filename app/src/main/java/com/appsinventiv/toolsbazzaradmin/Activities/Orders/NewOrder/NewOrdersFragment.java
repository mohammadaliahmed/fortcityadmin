package com.appsinventiv.toolsbazzaradmin.Activities.Orders.NewOrder;

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
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.appsinventiv.toolsbazzaradmin.Activities.Orders.Orders;
import com.appsinventiv.toolsbazzaradmin.Activities.Products.NewProductsModel;
import com.appsinventiv.toolsbazzaradmin.Adapters.OrdersAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.OrderModel;
import com.appsinventiv.toolsbazzaradmin.Models.Product;
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
import java.util.Map;


public class NewOrdersFragment extends Fragment {

    Context context;
    String orderStatus;
    NewOrdersAdapter adapter;
    ProgressBar progress;
    DatabaseReference mDatabase;
    String by;

    private ArrayList<OrderModel> orderModelArrayList = new ArrayList<>();
    private ArrayList<NewOrderModel> itemList = new ArrayList<>();
    HashMap<String, NewOrderModel> map = new HashMap<>();
    ImageView noOrder;

    public NewOrdersFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public NewOrdersFragment(String orderStatus) {
        this.orderStatus = orderStatus;

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
        noOrder = rootView.findViewById(R.id.noOrder);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NewOrdersAdapter(context, itemList);
        recyclerView.setAdapter(adapter);

        return rootView;

    }


    @Override
    public void onResume() {
        super.onResume();
        getDataFromServer();
    }

    private void getDataFromServer() {


        mDatabase.child("Orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    orderModelArrayList.clear();
                    map.clear();
                    int count = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        OrderModel order = snapshot.getValue(OrderModel.class);
                        if (order != null) {
                            if (order.getOrderStatus().equalsIgnoreCase(orderStatus)) {
                                if (order.getOrderFor() != null) {
                                    if (order.getOrderFor().equalsIgnoreCase("admin")) {

                                        if (map.get("Fort City") != null) {
                                            count = map.get("Fort City").getCount();
                                        } else {
                                            count = 0;
                                        }
                                        if (map.containsKey("Fort City")) {
                                            count = count + 1;
                                            map.put("Fort City",
                                                    new NewOrderModel("Fort City", "Fort City", orderStatus, "", count
                                                    )
                                            );
                                        } else {
                                            map.put("Fort City",
                                                    new NewOrderModel("Fort City", "Fort City", orderStatus, "", 1
                                                    )
                                            );

                                        }


                                    } else if (order.getOrderFor().equalsIgnoreCase("seller")) {
                                        if (order.getVendor() != null) {
                                            if (map.get(order.getVendor().getStoreName()) != null) {
                                                count = map.get(order.getVendor().getStoreName()).getCount();
                                            } else {
                                                count = 0;
                                            }
                                            if (map.containsKey(order.getVendor().getStoreName())) {
                                                count = count + 1;
                                                map.put(order.getVendor().getStoreName(),
                                                        new NewOrderModel(order.getVendor().getUsername(), order.getVendor().getStoreName(), orderStatus, order.getVendor().getPicUrl(), count
                                                        )
                                                );
                                            } else {
                                                map.put(order.getVendor().getStoreName(),
                                                        new NewOrderModel(order.getVendor().getUsername(), order.getVendor().getStoreName(), orderStatus, order.getVendor().getPicUrl(), 1
                                                        )
                                                );
                                            }
                                        }

                                    }

                                }

                            } else {
                                if (itemList.size() < 1) {
                                    noOrder.setVisibility(View.VISIBLE);
                                } else {
                                    noOrder.setVisibility(View.GONE);

                                }
                            }

                        }
                        if (map.size() > 0) {
                            itemList.clear();
                            for (Map.Entry<String, NewOrderModel> entry : map.entrySet()) {
                                itemList.add(entry.getValue());
                                progress.setVisibility(View.GONE);
                            }
                            if (itemList.size() < 1) {
                                noOrder.setVisibility(View.VISIBLE);
                            } else {
                                noOrder.setVisibility(View.GONE);

                            }
                            adapter.updatelist(itemList);
                            adapter.notifyDataSetChanged();

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
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }


}
