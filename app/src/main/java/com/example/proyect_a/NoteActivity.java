package com.example.proyect_a;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NoteActivity extends AppCompatActivity {
    private EditText tituloEditText, lugarEditText, coordenadasEditText, textoEditText;

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
    public void saveNote(View view) {
        //Obtenemos los datos
        Pattern pattern = Pattern.compile("^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$");
        String titulo = tituloEditText.getText().toString();
        String texto = textoEditText.getText().toString();
        String lugar = lugarEditText.getText().toString();
        String coord = coordenadasEditText.getText().toString();
        Double latitud = null;
        Double longitud = null;

        Matcher matcher = pattern.matcher(coord);

        //Comprueba si las coordenadas están vacias
        if (!coord.isEmpty()) {
            if (matcher.matches()) {
                String[] coordenadas = coord.split(",");
                latitud = Double.parseDouble(coordenadas[0]);
                longitud = Double.parseDouble(coordenadas[1]);
            }
        }
        //Comprueba si el titulo está vacio
        if (!titulo.isEmpty()) {
            Nota nota = new Nota(titulo, texto, latitud, longitud, lugar);
            if (saveNoteDatabase(nota) != -1) {
                Toast.makeText(this, "Nota guardada", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Este titulo ya existe", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Titule su nota", Toast.LENGTH_SHORT).show();
        }
    }

    public long saveNoteDatabase(Nota nota) {
        ///Obtiene la base de datos
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "notas", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();

        //Crea el content values
        ContentValues cv = new ContentValues();
        cv.put("titulo", nota.getTitulo());
        cv.put("texto", nota.getTexto());
        cv.put("latitud", nota.getLatitud());
        cv.put("longitud", nota.getLongitud());
        cv.put("lugar", nota.getLugar());

        //Realiza la insercion y devuelve -1 en caso de error
        return baseDeDatos.insert("notas", null, cv);
    }


}