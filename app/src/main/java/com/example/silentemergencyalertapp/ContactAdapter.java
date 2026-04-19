package com.example.silentemergencyalertapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.silentemergencyalertapp.ContactModel;
import com.example.silentemergencyalertapp.DBHelper;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    Context context;
    ArrayList<ContactModel> list;
    DBHelper db;

    public ContactAdapter(Context context, ArrayList<ContactModel> list, DBHelper db) {
        this.context = context;
        this.list = list;
        this.db = db;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvPhone;
        Button btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ContactModel model = list.get(position);

        holder.tvName.setText(model.name);
        holder.tvPhone.setText(model.phone);

        holder.btnDelete.setOnClickListener(v -> {
            db.deleteContact(model.id);
            list.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}