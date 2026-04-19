package com.example.silentemergencyalertapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DBHelper db;
    ArrayList<HistoryModel> historyList;
    HistoryAdapter adapter;

    Button btnbk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.recyclerHistory);
        db = new DBHelper(this);
        historyList = new ArrayList<>();

        loadHistory();

        adapter = new HistoryAdapter(this, historyList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnbk = findViewById(R.id.btnbk);

        btnbk.setOnClickListener(v -> {
            finish();
        });

    }

    private void loadHistory() {
        historyList.clear();

        Cursor cursor = db.getAllHistory();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String contact = cursor.getString(1);
                String date = cursor.getString(2);
                String time = cursor.getString(3);

                historyList.add(new HistoryModel(contact, date, time));

            } while (cursor.moveToNext());

            cursor.close();
        }
    }
}