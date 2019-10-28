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

import java.util.ArrayList;

/**
 * Created by AliAh on 27/11/2018.
 */

public class AddSubSubAttributeAdapter extends RecyclerView.Adapter<AddSubSubAttributeAdapter.ViewHolder> {
    Context context;
    ArrayList<String> list;

    public AddSubSubAttributeAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
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

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }
    }

}
