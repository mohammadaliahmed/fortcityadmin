package com.appsinventiv.toolsbazzaradmin.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Models.SalaryModel;
import com.appsinventiv.toolsbazzaradmin.R;

import java.util.ArrayList;

/**
 * Created by AliAh on 29/10/2018.
 */

public class SalariesReportAdapter extends RecyclerView.Adapter<SalariesReportAdapter.ViewHolder> {
    Context context;
    ArrayList<SalaryModel> itemList;

    public SalariesReportAdapter(Context context, ArrayList<SalaryModel> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.salaries_report_item_layout, parent, false);
        SalariesReportAdapter.ViewHolder viewHolder = new SalariesReportAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SalaryModel model = itemList.get(position);
        holder.name.setText(model.getUserId());
        holder.salary.setText("Rs " + model.getBasicSalary());
        holder.overTime.setText("Rs " + model.getOverTime());
        holder.etf.setText("Rs " + model.getETFandEPF());
        holder.bonus.setText("Rs " + model.getBonus());
        holder.deduction.setText("Rs " + model.getDeduction());
        holder.reason.setText("" + model.getReason());
        holder.total.setText("Rs " + model.getTotal());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, role, salary, overTime, bonus, etf, deduction, reason, total;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            role = itemView.findViewById(R.id.role);
            salary = itemView.findViewById(R.id.salary);
            overTime = itemView.findViewById(R.id.overTime);
            etf = itemView.findViewById(R.id.etf);
            bonus = itemView.findViewById(R.id.bonus);
            deduction = itemView.findViewById(R.id.deduction);
            reason = itemView.findViewById(R.id.reason);
            total = itemView.findViewById(R.id.total);
        }
    }
}
