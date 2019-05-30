package com.appsinventiv.toolsbazzaradmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.appsinventiv.toolsbazzaradmin.Activities.Accounts.ExpensesAndRevenue.ViewSalary;
import com.appsinventiv.toolsbazzaradmin.Models.Employee;
import com.appsinventiv.toolsbazzaradmin.Models.SalaryModel;
import com.appsinventiv.toolsbazzaradmin.R;
import com.appsinventiv.toolsbazzaradmin.Utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by AliAh on 26/10/2018.
 */

public class SalariesAdapter extends RecyclerView.Adapter<SalariesAdapter.ViewHolder> {
    Context context;
    ArrayList<Employee> itemList;
    //    ViewHolder holder;
//    int postition;
    ArrayList<SalaryModel> salaryList = new ArrayList<>();
    SalaryInterface salaryInterface;
    ArrayList<SalaryModel> salaryAlreadyList = new ArrayList<>();
    String path;


    public SalariesAdapter(Context context, ArrayList<Employee> itemList, ArrayList<SalaryModel> salaryAlreadyList,String path, SalaryInterface salaryInterface) {
        this.context = context;
        this.itemList = itemList;
        this.salaryInterface = salaryInterface;
        this.salaryAlreadyList = salaryAlreadyList;
        this.path=path;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.salaries_item_layout, parent, false);
        SalariesAdapter.ViewHolder viewHolder = new SalariesAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Employee employee = itemList.get(position);
        holder.name.setText((position + 1) + ". " + employee.getName());
        holder.role.setText("(Role: " + CommonUtils.rolesList[employee.getRole()] + ")");

        if (salaryAlreadyList.size() > 0) {
            for (int i = 0; i < salaryAlreadyList.size(); i++) {
                if (employee.getUsername().equals(salaryAlreadyList.get(i).getUserId())) {
                    holder.salary.setText("" + salaryAlreadyList.get(i).getBasicSalary());
                    holder.overTime.setText("" + salaryAlreadyList.get(i).getOverTime());
                    holder.etf.setText("" + salaryAlreadyList.get(i).getETFandEPF());
                    holder.bonus.setText("" + salaryAlreadyList.get(i).getBonus());
                    holder.deduction.setText("" + salaryAlreadyList.get(i).getDeduction());
                    holder.reason.setText("" + salaryAlreadyList.get(i).getReason());

                    salaryList.get(i).setBasicSalary(Float.parseFloat(holder.salary.getText().toString()));
                    salaryList.get(i).setOverTime(Float.parseFloat(holder.overTime.getText().toString()));
                    salaryList.get(i).setBonus(Float.parseFloat(holder.bonus.getText().toString()));
                    salaryList.get(i).setDeduction(Float.parseFloat(holder.deduction.getText().toString()));
                    salaryList.get(i).setReason(holder.reason.getText().toString());
                    salaryList.get(i).setETFandEPF(Float.parseFloat(holder.etf.getText().toString()));

                }
            }
        }


        salaryList.add(new SalaryModel());
        holder.printSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,ViewSalary.class);
                i.putExtra("salaryId",""+position);
                i.putExtra("path",path);
                context.startActivity(i);

            }
        });

        holder.salary.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    salaryList.get(position).setBasicSalary(Float.parseFloat(holder.salary.getText().toString()));
                }
            }
        });
        holder.overTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {

                    salaryList.get(position).setOverTime(Float.parseFloat(holder.overTime.getText().toString()));
                }
            }
        });
        holder.bonus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {

                    salaryList.get(position).setBonus(Float.parseFloat(holder.bonus.getText().toString()));
                }
            }
        });
        holder.deduction.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {

                    salaryList.get(position).setDeduction(Float.parseFloat(holder.deduction.getText().toString()));
                }
            }
        });
        holder.reason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {

                    salaryList.get(position).setReason(holder.reason.getText().toString());
                }
            }
        });
        holder.etf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {

                    salaryList.get(position).setETFandEPF(Float.parseFloat(holder.etf.getText().toString()));
                }
            }
        });


    }

    public void setValues() {
        salaryInterface.values(salaryList, itemList);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, role;
        Button printSalary;
        EditText salary, overTime, bonus, deduction, reason,etf;

        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            role = itemView.findViewById(R.id.role);
            printSalary = itemView.findViewById(R.id.printSalary);
            salary = itemView.findViewById(R.id.salary);
            overTime = itemView.findViewById(R.id.overTime);
            etf = itemView.findViewById(R.id.etf);
            bonus = itemView.findViewById(R.id.bonus);
            deduction = itemView.findViewById(R.id.deduction);
            reason = itemView.findViewById(R.id.reason);
        }
    }

    public interface SalaryInterface {
        public void values(ArrayList<SalaryModel> salaryModelList, ArrayList<Employee> employeeList);
    }

}
