package com.example.silentemergencyalertapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DBHelper db;
    ArrayList<ContactModel> contactList;
    ContactAdapter adapter;

    Button btnAddContact;

    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        recyclerView = findViewById(R.id.recyclerContacts);
        db = new DBHelper(this);
        contactList = new ArrayList<>();

        loadContacts();

        adapter = new ContactAdapter(this, contactList, db);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnAddContact = findViewById(R.id.btnAddContact);

        btnAddContact.setOnClickListener(v -> {
            startActivity(new Intent(ContactsActivity.this, AddContactActivity.class));
        });

        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void loadContacts() {
        Cursor cursor = db.getAllContacts();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String phone = cursor.getString(2);

            contactList.add(new ContactModel(id, name, phone));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadContacts();
        adapter.notifyDataSetChanged();
    }
}