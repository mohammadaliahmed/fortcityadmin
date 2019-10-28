package com.appsinventiv.toolsbazzaradmin.Activities.CategoryPackage.Attributes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.CategoryPackage.AddOtherMainCategories;
import com.appsinventiv.toolsbazzaradmin.Activities.CategoryPackage.ChooseMainCategory;
import com.appsinventiv.toolsbazzaradmin.Activities.CategoryPackage.ChooseOtherMainCategory;
import com.appsinventiv.toolsbazzaradmin.Activities.CategoryPackage.MainCategoryModel;
import com.appsinventiv.toolsbazzaradmin.Activities.Products.AddProduct;
import com.appsinventiv.toolsbazzaradmin.Activities.Products.EditProduct;
import com.appsinventiv.toolsbazzaradmin.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Locale;

public class MainAttributesAdapter extends RecyclerView.Adapter<MainAttributesAdapter.ViewHolder> {
    Context context;
    ArrayList<MainCategoryModel> itemList;
    MainAttributesCallBacks callBacks;
    int to = 0;
    private ArrayList<MainCategoryModel> arrayList;


    public MainAttributesAdapter(Context context, ArrayList<MainCategoryModel> itemList, int to, MainAttributesCallBacks callBacks) {
        this.context = context;
        this.itemList = itemList;
        this.callBacks = callBacks;
        this.to = to;
        this.arrayList = new ArrayList<>(itemList);
    }

    public void updateList(ArrayList<MainCategoryModel> list) {
        this.itemList = list;
        arrayList.clear();
        arrayList.addAll(list);
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        itemList.clear();
        if (charText.length() == 0) {
            itemList.addAll(arrayList);
        } else {
            for (MainCategoryModel text : arrayList) {
                if (text.getMainCategory().toLowerCase().contains(charText)
                        ) {
                    itemList.add(text);
                }
            }


        }
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_category_item_layout, parent, false);
        MainAttributesAdapter.ViewHolder viewHolder = new MainAttributesAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MainCategoryModel model = itemList.get(position);
        holder.maincategory.setText(model.getMainCategory());
        holder.subCategories.setText(model.getSubCategories() == null ? "" : model.getSubCategories());
        Glide.with(context).load(model.getUrl()).into(holder.icon);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddProduct.categoryList.add(model.getMainCategory());
                EditProduct.categoryList.add(model.getMainCategory());
                if (to == 1) {
//                    Intent i = new Intent(context, AddSubAttributes.class);
//                    i.putExtra("mainAttribute", model.getMainCategory());
//
//                    context.startActivity(i);
                    showWarrantyAlert(model);
//                    ((ChooseMainCategory) context).finish();
                } else if (to == 0) {
//                    Intent i = new Intent(context, ChooseOtherMainCategory.class);
//                    i.putExtra("mainCategory", model.getMainCategory());
//
//                    context.startActivity(i);
//                    ((ChooseMainCategory) context).finish();
                }
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBacks.deleteCategory(model);
            }
        });

    }

    private void showWarrantyAlert(final MainCategoryModel model) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select option");

        final ArrayList<String> ab = new ArrayList<>();
        ab.add("Single Input");
        ab.add("Multiple Input");
        ab.add("User Input");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);
        arrayAdapter.add("Single Input");
        arrayAdapter.add("Multiple Input");
        arrayAdapter.add("User Input");

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(context, AddSubAttributes.class);
                i.putExtra("mainAttribute", model.getMainCategory());
                String abc = "single";
                if (which == 0) {
                    abc = "single";
                } else if (which == 1) {
                    abc = "multiple";
                } else {
                    abc = "userInput";
                }
                i.putExtra("option", abc);
                i.putExtra("alaw", ab.get(which));

                context.startActivity(i);

            }
        });
        builderSingle.show();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView maincategory, subCategories;
        ImageView icon, delete;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            subCategories = itemView.findViewById(R.id.subCategories);
            maincategory = itemView.findViewById(R.id.maincategory);
            delete = itemView.findViewById(R.id.delete);
        }
    }

    public interface MainAttributesCallBacks {
        public void deleteCategory(MainCategoryModel model);
    }
}
