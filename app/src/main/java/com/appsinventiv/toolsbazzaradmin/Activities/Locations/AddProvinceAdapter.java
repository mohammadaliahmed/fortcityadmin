package com.appsinventiv.toolsbazzaradmin.Activities.Locations;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by AliAh on 27/11/2018.
 */

public class AddProvinceAdapter extends RecyclerView.Adapter<AddProvinceAdapter.ViewHolder> {
    Context context;
    ArrayList<String> list;
    private boolean yesOrNo;

    public AddProvinceAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    public void canGoNext(boolean yesOrNo) {
        this.yesOrNo = yesOrNo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_list_item, parent, false);
        AddProvinceAdapter.ViewHolder viewHolder = new AddProvinceAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String title = list.get(position);
        holder.title.setText(title);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!yesOrNo) {
                    Intent i = new Intent(context, AddDistricts.class);
                    i.putExtra("province", title);
                    context.startActivity(i);
                }else{
                    CommonUtils.showToast("No need to add cities ");
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, countryCode, currency;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            currency = itemView.findViewById(R.id.currency);
            countryCode = itemView.findViewById(R.id.countryCode);
            image = itemView.findViewById(R.id.image);
        }
    }

}
