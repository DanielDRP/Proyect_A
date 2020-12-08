package com.example.proyect_a;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<ListElement> elements;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    public void refresh(View view){
        init();
    }

    public void moveToNote(ListElement item){
        Intent intent = new Intent(this,UpdateNote.class);
        intent.putExtra("titulo",item.getTitulo());
        startActivity(intent);
    }
    public void newNote(View view){
        Intent intent = new Intent(this,NoteActivity.class);
        startActivity(intent);
    }






}