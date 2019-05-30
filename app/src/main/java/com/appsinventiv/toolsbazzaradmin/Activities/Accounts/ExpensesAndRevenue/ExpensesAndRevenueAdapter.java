package com.appsinventiv.toolsbazzaradmin.Activities.Accounts.ExpensesAndRevenue;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Adapters.YearViewPOAdapter;
import com.appsinventiv.toolsbazzaradmin.Models.Temporarymodel;
import com.appsinventiv.toolsbazzaradmin.R;

import java.util.ArrayList;

/**
 * Created by AliAh on 03/11/2018.
 */

public class ExpensesAndRevenueAdapter extends RecyclerView.Adapter<ExpensesAndRevenueAdapter.ViewHolder> {
    Context context;
    ArrayList<ExpensesModel> itemList;
    ChangeLayout changeLayout;
    String type;

    public ExpensesAndRevenueAdapter(Context context, ArrayList<ExpensesModel> itemList, String type, ChangeLayout changeLayout) {
        this.context = context;
        this.itemList = itemList;
        this.changeLayout = changeLayout;
        this.type = type;
    }

    @NonNull
    @Override
    public ExpensesAndRevenueAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.expenses_item_layout, parent, false);
        ExpensesAndRevenueAdapter.ViewHolder viewHolder = new ExpensesAndRevenueAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExpensesAndRevenueAdapter.ViewHolder holder, final int position) {
        final ExpensesModel model = itemList.get(position);
        holder.rightText.setText(model.getRightText());
        holder.leftText.setText(model.getLeftText());
        holder.which.setText(model.getWhich().equals("")?model.getKey():model.getWhich()+"/"+model.getKey());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLayout.onClick(model, position, "month", model.getKey());
//
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView which, leftText, rightText;

        public ViewHolder(View itemView) {
            super(itemView);
            which = itemView.findViewById(R.id.which);
            leftText = itemView.findViewById(R.id.leftText);
            rightText = itemView.findViewById(R.id.rightText);
        }
    }

    public interface ChangeLayout {
        public void onClick(ExpensesModel model, int position, String type, String key);
    }
}
