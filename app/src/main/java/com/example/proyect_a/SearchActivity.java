package com.example.proyect_a;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    List<ListElement> elements;
    EditText buscarEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        buscarEditText = findViewById(R.id.buscarEditText);
        init();
    }

    public void init(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"notas",null,1);
        SQLiteDatabase db = admin.getWritableDatabase();
        String[] campos = new String[]{"titulo","lugar"};
        Cursor cursor = db.query("notas", campos, null, null, null, null, null);
        elements = new ArrayList<>();
        while (cursor.moveToNext()) {
            elements.add(new ListElement(cursor.getString(0), cursor.getString(1)));
        }
        ListAdapter listAdapter = new ListAdapter(elements, this, new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ListElement item) {
                moveToNote(item);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.lista);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }

    public void moveToNote(ListElement item){
        Intent intent = new Intent(this,UpdateNote.class);
        intent.putExtra("titulo",item.getTitulo());
        startActivity(intent);
    }

    public void refresh(View view){
        init();
    }

    public void searchNote(View view){
        String buscar = buscarEditText.getText().toString();

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"notas",null,1);
        SQLiteDatabase db = admin.getWritableDatabase();

        String[] campos = new String[]{"titulo","lugar"};
        String[] busqueda = new String[]{buscar};

        Cursor cursor = db.query("notas", campos, "titulo =?", busqueda, null, null, null);

        elements = new ArrayList<>();
        while (cursor.moveToNext()) {
            elements.add(new ListElement(cursor.getString(0), cursor.getString(1)));
        }
        ListAdapter listAdapter = new ListAdapter(elements, this, new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ListElement item) {
                moveToNote(item);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.lista);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }

}