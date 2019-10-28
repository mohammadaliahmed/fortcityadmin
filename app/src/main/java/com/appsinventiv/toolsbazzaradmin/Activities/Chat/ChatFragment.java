package com.appsinventiv.toolsbazzaradmin.Activities.Chat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.Customers.SellerModel;
import com.appsinventiv.toolsbazzaradmin.Adapters.ChatAdapter;
import com.appsinventiv.toolsbazzaradmin.Adapters.ChatListAdapter;
import com.appsinventiv.toolsbazzaradmin.Adapters.OrdersAdapter;
import com.appsinventiv.toolsbazzaradmin.Interfaces.TabCountCallbacks;
import com.appsinventiv.toolsbazzaradmin.Models.ChatModel;
import com.appsinventiv.toolsbazzaradmin.Models.Customer;
import com.appsinventiv.toolsbazzaradmin.Models.OrderModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.appsinventiv.toolsbazzaradmin.Utils.SharedPrefs;
import com.appsinventiv.toolsbazzaradmin.Utils.SwipeControllerActions;
import com.appsinventiv.toolsbazzaradmin.Utils.SwipeToDeleteCallback;
import com.bumptech.glide.Glide;
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

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatFragment extends Fragment {

    Context context;
    RecyclerView recyclerview;
    ArrayList<ChatModel> itemList = new ArrayList<>();
    ChatListAdapter adapter;
    DatabaseReference mDatabase;
    String chatWith;

    TabCountCallbacks callbacks;
    int chatCount = 0;
    private SwipeToDeleteCallback swipeController;
    HashMap<String, Boolean> map = new HashMap<>();
    HashMap<String, Integer> unreadCount = new HashMap<>();

    public ChatFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public ChatFragment(String chatWith, TabCountCallbacks callbacks) {
        // Required empty public constructor
        this.chatWith = chatWith;
        this.callbacks = callbacks;


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
        adapter = new ChatListAdapter(context, itemList, chatWith);


        recyclerView.setAdapter(adapter);
        swipeController = new SwipeToDeleteCallback(new SwipeControllerActions() {
            @Override
            public void onRightClicked(final int position) {

                deleteChat(itemList.get(position).getInitiator(), itemList.get(position).getNameToShow());

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

    private void deleteChat(final String idd, String nameToShow) {

        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = layoutInflater.inflate(R.layout.alert_dialog_curved, null);

        dialog.setContentView(layout);

        TextView message = layout.findViewById(R.id.message);
        TextView no = layout.findViewById(R.id.no);
        TextView yes = layout.findViewById(R.id.yes);

        message.setText("Delete chat with: " + nameToShow);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                final String chatName = chatWith + "Chats";

                mDatabase.child("Chats").child(chatName).child(idd).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                CommonUtils.showToast("Chat deleted");
                            }
                        });
            }
        });


        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });



        dialog.show();

    }


    @Override
    public void onResume() {
        super.onResume();
        getDataFromServer();
    }

    private void getDataFromServer() {
        final String chatName = chatWith + "Chats";
        final String userType = chatWith;
        mDatabase.child("Chats").child(chatName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemList.clear();
                chatCount = 0;
                if (dataSnapshot.getValue() != null) {
                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        getUnreadCount("Chats/" + chatName + "/" + snapshot.getKey());
                        getUserStatus(snapshot.getKey(), userType);
                        mDatabase.child("Chats").child(chatName).child(snapshot.getKey()).limitToLast(1).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    for (DataSnapshot abc : dataSnapshot.getChildren()) {
                                        ChatModel model = abc.getValue(ChatModel.class);
                                        if (model != null) {

                                            itemList.add(model);


                                            Collections.sort(itemList, new Comparator<ChatModel>() {
                                                @Override
                                                public int compare(ChatModel listData, ChatModel t1) {
                                                    Long ob1 = listData.getTime();
                                                    Long ob2 = t1.getTime();

                                                    return ob2.compareTo(ob1);

                                                }
                                            });
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

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void getUserStatus(String userId, String key) {
        if (key.equalsIgnoreCase("Seller")) {
            mDatabase.child("Sellers").child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    SellerModel seller = dataSnapshot.getValue(SellerModel.class);
                    if (seller != null) {

                        map.put(seller.getUsername(), seller.isOnline());
                        adapter.setUserStatus(map);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
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

    }

    private void getUnreadCount(String s) {
        mDatabase.child(s).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    int count = 0;
                    for (DataSnapshot abc : dataSnapshot.getChildren()) {
                        ChatModel model = abc.getValue(ChatModel.class);
                        if (model != null) {
                            if (!model.getUsername().equalsIgnoreCase(SharedPrefs.getUsername())) {
                                if (!model.getStatus().equalsIgnoreCase("read")) {
                                    count = count + 1;
                                    unreadCount.put(model.getInitiator(), count);
                                    adapter.setUnreadCount(unreadCount);
                                    chatCount = chatCount + 1;

                                } else {
                                    unreadCount.put(model.getInitiator(), 0);
                                    adapter.setUnreadCount(unreadCount);
                                }
                            }

//                            itemList.add(model);

                        }
                    }
                    callbacks.newCount(chatCount, 0);
                    setChatCount();


                } else {
                    unreadCount.clear();
                    adapter.setUnreadCount(unreadCount);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setChatCount() {
        SharedPrefs.setChatCount("" + chatCount);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }


}
