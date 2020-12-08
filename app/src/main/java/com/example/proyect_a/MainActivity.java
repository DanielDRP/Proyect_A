package com.example.proyect_a;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    //Carga la lista con todos als notas guardadas en la base de datos
    public void init() {
        //Se obtiene la base de datos
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "notas", null, 1);
        SQLiteDatabase dataBase = admin.getWritableDatabase();

        //Obtiene las notas de la base de datos
        String[] campos = new String[]{"titulo", "lugar"};
        Cursor cursor = dataBase.query("notas", campos, null, null, null, null, null);

        elements = new ArrayList<>();

        //Siempre que hayan más notas en la base de datos se cargaran en la lista
        while (cursor.moveToNext()) {
            elements.add(new ListElement(cursor.getString(0), cursor.getString(1)));
        }

        //Asigna un metodo onClick a cada elemento de la lista
        ListAdapter listAdapter = new ListAdapter(elements, this, new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ListElement item) {
                moveToNote(item);
            }
        });

        //Carga la lista
        RecyclerView recyclerView = findViewById(R.id.lista);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }

    //Refresca la lista volviendo a cargar los elementos
    public void refresh(View view) {
        init();
    }

    //Lanza la activity UpdateNote y le envia el titulo de el elemento
    public void moveToNote(ListElement item) {
        Intent intent = new Intent(this, UpdateNote.class);
        intent.putExtra("titulo", item.getTitulo());
        startActivity(intent);
    }

    //Lanza la Note activity
    public void newNote(View view) {
        Intent intent = new Intent(this, NoteActivity.class);
        startActivity(intent);
    }

    //Lanza la MapsActivity
    public void moveToMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    //Lanza la SearchActivity
    public void search(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }


}