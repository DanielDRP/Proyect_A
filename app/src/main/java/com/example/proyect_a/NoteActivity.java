package com.example.proyect_a;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NoteActivity extends AppCompatActivity {
    private EditText tituloEditText,lugarEditText,coordenadasEditText, textoEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        tituloEditText = findViewById(R.id.tituloEditText);
        lugarEditText = findViewById(R.id.lugarEditText);
        coordenadasEditText = findViewById(R.id.coordenadasEditText);
        textoEditText = findViewById(R.id.textoEditText);




    }

    //Persiste la nota en la base de datos
    public void save(View view) {
        //Obtenemos los datos
        Pattern pattern = Pattern.compile("^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$");
        String titulo = tituloEditText.getText().toString();
        String texto = textoEditText.getText().toString();
        String lugar = lugarEditText.getText().toString();
        String coord = coordenadasEditText.getText().toString();

        Matcher matcher = pattern.matcher(coord);

        if (lugar.isEmpty()) {
            lugar = "Lugar no asignado";
        }
        if (texto.isEmpty()) {
            texto = "";
        }
        if(coord.isEmpty()){
            coord = "0,0";
        }
        if (!titulo.isEmpty()) {
            if(matcher.matches() || coord.equals("0,0")){
                Nota nota = new Nota(titulo, texto, coord, lugar);
                if (nuevaNota(nota) != -1) {
                    Toast.makeText(this, "Guardado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Este titulo ya existe", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Las coordenadas no son correctas", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Titule su nota", Toast.LENGTH_SHORT).show();
        }
    }
    public long nuevaNota(Nota nota) {

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "notas", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("titulo", nota.getTitulo());
        cv.put("texto", nota.getTexto());
        cv.put("latitud", nota.getCoordenadas().split(",")[0]);
        cv.put("longitud", nota.getCoordenadas().split(",")[1]);
        cv.put("lugar", nota.getLugar());
        return baseDeDatos.insert("notas", null, cv);
    }


}