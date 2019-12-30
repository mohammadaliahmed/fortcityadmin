package com.appsinventiv.toolsbazzaradmin.Activities.CategoryPackage;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.CategoryPackage.Attributes.AddMainAttributes;
import com.appsinventiv.toolsbazzaradmin.Activities.CategoryPackage.Attributes.MainAttributesAdapter;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.appsinventiv.toolsbazzaradmin.Utils.Constants;
import com.appsinventiv.toolsbazzaradmin.Utils.SwipeControllerActions;
import com.appsinventiv.toolsbazzaradmin.Utils.SwipeToDeleteCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AttributesFragment extends Fragment {
    DatabaseReference mDatabase;
    Context context;

    RecyclerView recyclerview;
    ArrayList<MainCategoryModel> itemList = new ArrayList<>();
    MainAttributesAdapter adapter;
    FloatingActionButton addd;
    public static Activity activity;
    private SwipeToDeleteCallback swipeController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_atributes, container, false);
        addd = rootView.findViewById(R.id.addd);
        recyclerview = rootView.findViewById(R.id.recyclerview);
        addd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.EDITING_ATTRIBUTES=true;
                Intent i = new Intent(context, AddMainAttributes.class);
                startActivity(i);
            }
        });


        recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        adapter = new MainAttributesAdapter(context, itemList, 1, new MainAttributesAdapter.MainAttributesCallBacks() {
            @Override
            public void deleteCategory(MainCategoryModel model) {
                showAlert(model);
            }
        });


//        swipeController = new SwipeToDeleteCallback(new SwipeControllerActions() {
//            @Override
//            public void onRightClicked(final int position) {
//                showAlert(itemList.get(position));
//
//            }
//        });
//        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
//        itemTouchhelper.attachToRecyclerView(recyclerview);
//
//        recyclerview.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//                swipeController.onDraw(c);
//            }
//        });


        recyclerview.setAdapter(adapter);
        getMainAttrbutesFromDB();

        return rootView;
    }

    private void showAlert(final MainCategoryModel model) {


        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = layoutInflater.inflate(R.layout.alert_dialog_curved, null);

        dialog.setContentView(layout);

        TextView message = layout.findViewById(R.id.message);
        TextView no = layout.findViewById(R.id.no);
        TextView yes = layout.findViewById(R.id.yes);

        message.setText("Do you want to delete this category?");

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                deleteCategory(model);
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

    private void deleteCategory(MainCategoryModel model) {
        mDatabase.child("Settings/Attributes/MainAttributes").child(model.getMainCategory()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Category Deleted");
            }
        });
    }

    private void getMainAttrbutesFromDB() {

        mDatabase.child("Settings/Attributes/MainAttributes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    itemList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        MainCategoryModel model = snapshot.getValue(MainCategoryModel.class);
                        if (model != null) {
                            itemList.add(model);
                        }
                    }
                    adapter.updateList(itemList);
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
