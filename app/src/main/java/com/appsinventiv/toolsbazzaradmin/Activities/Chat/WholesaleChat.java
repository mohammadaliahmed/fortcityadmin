package com.appsinventiv.toolsbazzaradmin.Activities.Chat;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.appsinventiv.toolsbazzaradmin.Adapters.ChatAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.ChatModel;
import com.appsinventiv.toolsbazzaradmin.Models.Customer;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.KeyboardUtils;
import com.appsinventiv.toolsbazzaradmin.Utils.NotificationAsync;
import com.appsinventiv.toolsbazzaradmin.Utils.NotificationObserver;
import com.appsinventiv.toolsbazzaradmin.Utils.SharedPrefs;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WholesaleChat extends AppCompatActivity implements NotificationObserver {

    DatabaseReference mDatabase;
    EditText message;
    ImageView send;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ChatAdapter adapter;
    ArrayList<ChatModel> chatModelArrayList = new ArrayList<>();
    int soundId;
    SoundPool sp;
    String userFcmKey;
    String username;
    private Customer customer;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_chat);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }

        Intent i = getIntent();
        username = i.getStringExtra("username");
        this.setTitle(username);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        send = findViewById(R.id.send);
        message = findViewById(R.id.message);
        sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        soundId = sp.load(WholesaleChat.this, R.raw.tick_sound, 1);
        KeyboardUtils.addKeyboardToggleListener(this, new KeyboardUtils.SoftKeyboardToggleListener()
        {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible)
            {
                recyclerView.getLayoutManager().scrollToPosition(adapter.getItemCount() - 1);

                recyclerView.scrollToPosition(chatModelArrayList.size() - 1);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserDetails();
        getMessagesFromServer();
        sendMessageToServer();
        readAllMessages();
    }

    private void getUserDetails() {
        mDatabase.child("Customers").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    customer = dataSnapshot.getValue(Customer.class);
                    if (customer != null) {
                        userFcmKey = customer.getFcmKey();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void readAllMessages() {
        mDatabase.child("Chats/WholesaleChats").child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ChatModel chatModel = snapshot.getValue(ChatModel.class);
                        if (chatModel != null) {
                            if (!chatModel.getUsername().equals(SharedPrefs.getUsername())) {
                                mDatabase.child("Chats/WholesaleChats").child(username).child(chatModel.getId()).child("status").setValue("read");
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getMessagesFromServer() {
        recyclerView = findViewById(R.id.chats);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setReverseLayout(true);

        adapter = new ChatAdapter(WholesaleChat.this, chatModelArrayList);
        layoutManager.setStackFromEnd(true);

        recyclerView.setAdapter(adapter);

        mDatabase.child("Chats/WholesaleChats").child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    chatModelArrayList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ChatModel model = snapshot.getValue(ChatModel.class);
                        if (model != null) {
                            chatModelArrayList.add(model);

                        }
                    }
                    recyclerView.scrollToPosition(chatModelArrayList.size() - 1);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void sendMessageToServer() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (message.getText().length() == 0) {
                    message.setError("Cant send empty message");
                } else {
                    final String msg = message.getText().toString();
                    message.setText(null);
                    final String key = mDatabase.push().getKey();
                    mDatabase.child("Chats/WholesaleChats").child(username).child(key)
                            .setValue(new ChatModel(key, msg, "admin" //TODO
                                    , System.currentTimeMillis(), "sending", username,
                                    customer.getName(),"",
                                    SharedPrefs.getEmployee().getName()
                            )).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {


                            sp.play(soundId, 1, 1, 0, 0, 1);
                            adapter.notifyDataSetChanged();
                            recyclerView.scrollToPosition(chatModelArrayList.size() - 1);

                            mDatabase.child("Chats/WholesaleChats").child(username).child(key).child("status").setValue("sent");


                            NotificationAsync notificationAsync = new NotificationAsync(WholesaleChat.this);
                            String NotificationTitle = "New message from " + SharedPrefs.getUsername();
                            String NotificationMessage = "Message: " + msg;
                            notificationAsync.execute("ali", userFcmKey, NotificationTitle, NotificationMessage, "WholesaleChat", key);

                        }
                    });
                }
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {

            Intent i = new Intent(WholesaleChat.this, Chats.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onSuccess(String chatId) {
        mDatabase.child("Chats/WholesaleChats").child(username).child(chatId).child("status").setValue("delivered");
    }

    @Override
    public void onFailure() {

    }

    @Override
    public void onBackPressed() {

        finish();
    }
}
