package com.appsinventiv.toolsbazzaradmin.Activities.Customers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appsinventiv.toolsbazzaradmin.Adapters.ChatListAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.ChatModel;
import com.appsinventiv.toolsbazzaradmin.Models.Customer;
import com.appsinventiv.toolsbazzaradmin.Models.VendorModel;
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
import java.util.HashMap;


public class SellerListFragment extends Fragment {

    Context context;
    RecyclerView recyclerview;
    ArrayList<SellerModel> itemList = new ArrayList<>();
    SellerListAdapter adapter;
    DatabaseReference mDatabase;
    String type;
    private SwipeToDeleteCallback swipeController;
    HashMap<String, Boolean> map = new HashMap<>();


    public SellerListFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public SellerListFragment(String type) {
        // Required empty public constructor
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


        View rootView = inflater.inflate(R.layout.chat_fragment, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_orders);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SellerListAdapter(context, itemList, new SellerListAdapter.SellerListCallbacks() {
            @Override
            public void onStatusChanged(SellerModel sellerModel, boolean status) {
                changeSellerStatus(sellerModel, status);
            }
        });

        recyclerView.setAdapter(adapter);
        swipeController = new SwipeToDeleteCallback(new SwipeControllerActions() {
            @Override
            public void onRightClicked(final int position) {

                deleteUser(itemList.get(position).getUsername());

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
        mDatabase.child("Sellers").child(username).child("isDeleted").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("User deleted");
            }
        });
    }

    private void changeSellerStatus(SellerModel sellerModel, final boolean status) {
        mDatabase.child("Sellers").child(sellerModel.getUsername()).child("status").setValue(status).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast(status ? "Seller is enabled" : "Seller is disabled");
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
//        if(chatWith.equalsIgnoreCase("wholesale")){
//        }
        getDataFromServer();
    }

    private void getDataFromServer() {
        mDatabase.child("Sellers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    itemList.clear();
                    map.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        SellerModel model = snapshot.getValue(SellerModel.class);
                        if (model != null) {
                            if (!model.isDeleted()) {
                                itemList.add(model);
                                map.put(model.getUsername(), model.isOnline());
                                adapter.setUserStatus(map);
//                                getUserStatus(model.getUsername());

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
    private void getUserStatus(String userId) {

        mDatabase.child("Sellers").child(userId).addValueEventListener(new ValueEventListener() {
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
