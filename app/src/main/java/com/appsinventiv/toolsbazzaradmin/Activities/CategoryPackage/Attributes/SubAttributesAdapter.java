package com.appsinventiv.toolsbazzaradmin.Activities.CategoryPackage.Attributes;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.CategoryPackage.AddOtherMainCategories;
import com.appsinventiv.toolsbazzaradmin.Activities.CategoryPackage.ChooseMainCategory;
import com.appsinventiv.toolsbazzaradmin.Activities.CategoryPackage.MainCategoryModel;
import com.appsinventiv.toolsbazzaradmin.Activities.Products.AddProduct;
import com.appsinventiv.toolsbazzaradmin.Activities.Products.EditProduct;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Locale;

public class SubAttributesAdapter extends RecyclerView.Adapter<SubAttributesAdapter.ViewHolder> {
    Context context;
    ArrayList<SubAttributeModel> itemList;
    SubAttributesCallBacks callBacks;
    int to = 0;
    private ArrayList<SubAttributeModel> arrayList;


    public SubAttributesAdapter(Context context, ArrayList<SubAttributeModel> itemList, int to, SubAttributesCallBacks callBacks) {
        this.context = context;
        this.itemList = itemList;
        this.callBacks = callBacks;
        this.to = to;
        this.arrayList = new ArrayList<>(itemList);
    }

    public void updateList(ArrayList<SubAttributeModel> list) {
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
            for (SubAttributeModel text : arrayList) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.sub_attribute_item_layout, parent, false);
        SubAttributesAdapter.ViewHolder viewHolder = new SubAttributesAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SubAttributeModel model = itemList.get(position);
        holder.maincategory.setText(model.getMainCategory());
        holder.subtitle.setText(model.getSelection());
//        Glide.with(context).load(model.getUrl()).into(holder.icon);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddProduct.categoryList.add(model.getMainCategory());
                EditProduct.categoryList.add(model.getMainCategory());
                if (to == 1) {
                    if (model.getSelection().equalsIgnoreCase("userInput")) {
                        CommonUtils.showToast("This is user input field");
                    } else {
                        Intent i = new Intent(context, AddSubSubAttributes.class);
                        i.putExtra("subAttribute", model.getMainCategory());

                        context.startActivity(i);
                    }
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

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView maincategory, subtitle;
        ImageView icon, delete;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            subtitle = itemView.findViewById(R.id.subtitle);
            maincategory = itemView.findViewById(R.id.maincategory);
            delete = itemView.findViewById(R.id.delete);
        }
    }

    public interface SubAttributesCallBacks {
        public void deleteCategory(SubAttributeModel model);
    }
}
