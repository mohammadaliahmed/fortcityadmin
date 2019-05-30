package com.appsinventiv.toolsbazzaradmin.Activities.AppSettings;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.Products.MainCategoryModel;
import com.appsinventiv.toolsbazzaradmin.R;

import java.util.ArrayList;

public class CommissionsAdapter extends RecyclerView.Adapter<CommissionsAdapter.ViewHolder> {
    Context context;
    ArrayList<CommissionModel> itemList;
    CommissionsCallback callback;

    public CommissionsAdapter(Context context, ArrayList<CommissionModel> itemList, CommissionsCallback callback) {
        this.context = context;
        this.itemList = itemList;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.commission_item_layout, parent, false);
        CommissionsAdapter.ViewHolder viewHolder = new CommissionsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final CommissionModel model = itemList.get(position);
        holder.mainCategory.setText(model.getCategoryName());
        holder.percent.setText("" + model.getCommission());
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.percent.getText().length() == 0) {
                    holder.percent.setError("Enter value");
                } else {
                    callback.value(model, Integer.parseInt(holder.percent.getText().toString()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button update;
        EditText percent;
        TextView mainCategory;

        public ViewHolder(View itemView) {
            super(itemView);

            mainCategory = itemView.findViewById(R.id.mainCategory);
            percent = itemView.findViewById(R.id.percent);
            update = itemView.findViewById(R.id.update);
        }
    }

    public interface CommissionsCallback {
        public void value(CommissionModel model, int value);
    }
}
