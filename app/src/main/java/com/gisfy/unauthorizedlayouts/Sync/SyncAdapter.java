package com.gisfy.unauthorizedlayouts.Sync;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.gisfy.unauthorizedlayouts.R;

import java.util.ArrayList;

class SyncAdapter extends  RecyclerView.Adapter<SyncAdapter.ViewHolder>  {
    private Context context;
    private ArrayList<SyncModel> employees;

    public SyncAdapter(Context context, ArrayList<SyncModel> employees) {
        this.context = context;
        this.employees = employees;
    }

    public void setEmployees(ArrayList<SyncModel> employees) {
        this.employees = new ArrayList<>();
        this.employees = employees;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SyncAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.sync_record_list_items, viewGroup, false);
        return new SyncAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SyncAdapter.ViewHolder multiViewHolder, int position) {
        multiViewHolder.bind(employees.get(position));
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView, textDoor, txtPhone;;
        private ImageView imageView;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txtName);
            textDoor = itemView.findViewById(R.id.fathernameadapter);
            txtPhone = itemView.findViewById(R.id.txtPhone);
            imageView = itemView.findViewById(R.id.tick);
        }

        void bind(final SyncModel employee) {
            imageView.setVisibility(employee.isChecked() ? View.VISIBLE : View.GONE);
            textView.setText(employee.getOwnername());
            textDoor.setText(employee.getFathername());
            txtPhone.setText(employee.getPhoneno());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    employee.setChecked(!employee.isChecked());
                    imageView.setVisibility(employee.isChecked() ? View.VISIBLE : View.GONE);
                }
            });
        }
    }

    public ArrayList<SyncModel> getAll() {
        return employees;
    }

    public ArrayList<SyncModel> getSelected() {
        ArrayList<SyncModel> selected = new ArrayList<>();
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).isChecked()) {
                selected.add(employees.get(i));
            }
        }
        return selected;
    }
}
