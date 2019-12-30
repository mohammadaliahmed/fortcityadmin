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

import com.appsinventiv.toolsbazzaradmin.Activities.CategoryPackage.AddSubCategories;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.Constants;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by AliAh on 27/11/2018.
 */

public class AddSubSubAttributeAdapter extends RecyclerView.Adapter<AddSubSubAttributeAdapter.ViewHolder> {
    Context context;
    ArrayList<String> list;

    AddSubSubAttributeAdapterCallbacks callbacks;

    public AddSubSubAttributeAdapter(Context context, ArrayList<String> list,AddSubSubAttributeAdapterCallbacks callbacks) {
        this.context = context;
        this.list = list;
        this.callbacks=callbacks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sub_sub_attribute_list_item, parent, false);
        AddSubSubAttributeAdapter.ViewHolder viewHolder = new AddSubSubAttributeAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String title = list.get(position);
        holder.title.setText(title);
        if (Constants.SKU_ATT.toLowerCase().contains("sku")) {
            Glide.with(context).load(R.drawable.ic_sku).into(holder.icon);

        } else {
            Glide.with(context).load(R.drawable.ic_att).into(holder.icon);

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               callbacks.onItemClick(title);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
        }
    }
    public interface AddSubSubAttributeAdapterCallbacks{
        public void onItemClick(String title);
    }



}
