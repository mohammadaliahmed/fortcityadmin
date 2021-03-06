package com.appsinventiv.toolsbazzaradmin.Activities.AppSettings;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.appsinventiv.toolsbazzaradmin.R;

import java.util.ArrayList;

/**
 * Created by AliAh on 27/11/2018.
 */

public class LanguageListAdapter extends RecyclerView.Adapter<LanguageListAdapter.ViewHolder> {
    Context context;
    ArrayList<LanguageModel> list;
    Listener listener;

    public LanguageListAdapter(Context context, ArrayList<LanguageModel> list) {
        this.context = context;
        this.list = list;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.choose_category_item, parent, false);
        LanguageListAdapter.ViewHolder viewHolder = new LanguageListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final LanguageModel model = list.get(position);
        holder.title.setText(model.getLanguageName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
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

    public interface Listener {
        public void countryChosen(LanguageModel model);
    }

}
