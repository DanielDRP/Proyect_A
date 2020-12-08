package com.example.proyect_a;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    //Carga la lista con todos als notas guardadas en la base de datos
    public void init() {
        //Llama a la base de datos y obtiene el cursor
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "notas", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        String[] campos = new String[]{"titulo", "lugar"};
        Cursor cursor = db.query("notas", campos, null, null, null, null, null);
        elements = new ArrayList<>();
        //Mientras haya mas notas en la base de datos se añaden a la lista
        while (cursor.moveToNext()) {
            elements.add(new ListElement(cursor.getString(0), cursor.getString(1)));
        }

        //Se crea el evento OnClick de cada elemento de la lista
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

    //Lanza la activity de nota y le pasa el titulo
    public void moveToNote(ListElement item) {
        Intent intent = new Intent(this, UpdateNote.class);
        intent.putExtra("titulo", item.getTitulo());
        startActivity(intent);
    }

    //Refresca la lista, volviendo a cargar todos los elementos
    public void refresh(View view) {
        init();
    }

    //Recibe el titulo de una lista y procede a cargar la lista con este elemento
    public void searchNote(View view) {
        String buscar = buscarEditText.getText().toString();
        //Llama a la base de datos y obtiene los datos
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "notas", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        //Se asignan los campos y el elemento de busqueda
        String[] campos = new String[]{"titulo", "lugar"};
        String[] busqueda = new String[]{buscar};
        //Se carga el cursor
        Cursor cursor = db.query("notas", campos, "titulo =?", busqueda, null, null, null);

        elements = new ArrayList<>();
        //Mientras la busqueda devuelva datos los carga en la lista
        while (cursor.moveToNext()) {
            elements.add(new ListElement(cursor.getString(0), cursor.getString(1)));
        }
        //Se asigna el onClick a cada elemento de la lista
        ListAdapter listAdapter = new ListAdapter(elements, this, new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ListElement item) {
                moveToNote(item);
            }
        });

        //Se carga la lista
        RecyclerView recyclerView = findViewById(R.id.lista);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }

}