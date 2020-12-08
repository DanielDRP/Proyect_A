package com.example.proyect_a;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
Esta Activity permitirá la actualización, borrado y visualizado de una nota guardada con anterioridad.
 */
public class UpdateNote extends AppCompatActivity {
    TextView tituloUpdateEditText;
    private EditText lugarUpdateEditText, coordenadasUpdateEditText, textoUpdateEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);
        tituloUpdateEditText = findViewById(R.id.tituloUpdateTextView);
        lugarUpdateEditText = findViewById(R.id.lugarUpdateEditText);
        coordenadasUpdateEditText = findViewById(R.id.coordenadasUpdateEditText);
        textoUpdateEditText = findViewById(R.id.textoUpdateEditText);

        //Recibe el titulo o el lugar y muestra los datos en cada caso
        Intent intent = getIntent();
        String titulo = intent.getStringExtra("titulo");
        String lugar = intent.getStringExtra("lugar");

        if (titulo != null) {
            mostrarNota(titulo);

        }
        if (lugar != null) {
            mostrarNotaMarcador(lugar);
        }

    }

    //Muestra la nota seleccionada
    public void mostrarNota(String titulo) {
        //Traemos de la base de datos la nota seleccionada
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "notas", null, 1);
        SQLiteDatabase dataBase = admin.getWritableDatabase();
        String[] camposSelect = new String[]{"titulo", "latitud", "longitud", "lugar", "texto"};
        String[] whereArgs = new String[]{titulo};
        Cursor cursor = dataBase.query("notas", camposSelect, "titulo =?", whereArgs, null, null, null);

        //En caso de que no se haya traido ningun titulo (nueva nota)

        if (titulo != null) {
            if (cursor.moveToFirst()) {
                Double lat = cursor.getDouble(1);
                Double lon = cursor.getDouble(2);
                if (lat == null && lon == null) {
                    coordenadasUpdateEditText.setText("");
                } else {
                    coordenadasUpdateEditText.setText(lat + "," + lon);
                }
                //La cargamos para visualizar
                tituloUpdateEditText.setText(cursor.getString(0));
                textoUpdateEditText.setText(cursor.getString(4));
                lugarUpdateEditText.setText(cursor.getString(3));
            }
        } else {
            Toast.makeText(this, "Error al mostrar la nota", Toast.LENGTH_SHORT);
        }

    }

    //Muestra la nota seleccionada
    public void mostrarNotaMarcador(String lugar) {
        //Traemos de la base de datos la nota seleccionada
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "notas", null, 1);
        SQLiteDatabase dataBase = admin.getWritableDatabase();
        String[] camposSelect = new String[]{"titulo", "latitud", "longitud", "lugar", "texto"};
        String[] whereArgs = new String[]{lugar};
        Cursor cursor = dataBase.query("notas", camposSelect, "lugar =?", whereArgs, null, null, null);

        //En caso de que no se haya traido ningun titulo (nueva nota)

        if (lugar != null) {
            if (cursor.moveToFirst()) {

                Double lat = cursor.getDouble(1);
                Double lon = cursor.getDouble(2);

                if (lat == null && lon == null) {

                    coordenadasUpdateEditText.setText("");

                } else {

                    coordenadasUpdateEditText.setText(lat + "," + lon);

                }
                //La cargamos para visualizar
                tituloUpdateEditText.setText(cursor.getString(0));
                textoUpdateEditText.setText(cursor.getString(4));
                lugarUpdateEditText.setText(cursor.getString(3));
            }
        } else {

            Toast.makeText(this, "Error al mostrar la nota", Toast.LENGTH_SHORT);

        }

    }

    //Persiste la nota en la base de datos
    public void updateNote(View view) {
        //Obtenemos los datos
        Pattern pattern = Pattern.compile("^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$");
        String titulo = tituloUpdateEditText.getText().toString();
        String texto = textoUpdateEditText.getText().toString();
        String lugar = lugarUpdateEditText.getText().toString();
        String coord = coordenadasUpdateEditText.getText().toString();
        Double latitud = null;
        Double longitud = null;

        //Comprobamos el campo cordenadas
        Matcher matcher = pattern.matcher(coord);

        //Comprueba que las coordenadas no estén vacias
        if (!coord.isEmpty()) {

            //Comprueba que las coordenadas sean correctas
            if (matcher.matches()) {

                String[] coordenadas = coord.split(",");
                latitud = Double.parseDouble(coordenadas[0]);
                longitud = Double.parseDouble(coordenadas[1]);

            }

        }
        //Comprueba que el titulo no esté vacio
        if (!titulo.isEmpty()) {

            Nota nota = new Nota(titulo, texto, latitud, longitud, lugar);
            if (updateNoteDatabase(nota) != -1) {

                Toast.makeText(this, "Guardado correctamente", Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(this, "Este titulo ya existe", Toast.LENGTH_SHORT).show();

            }

        } else {

            Toast.makeText(this, "Titule su nota", Toast.LENGTH_SHORT).show();

        }
    }

    //Realiza la actualización de la nota en la base de datos
    public long updateNoteDatabase(Nota nota) {

        //Obtiene la base de datos
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "notas", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();

        //Crea el contentValues
        ContentValues cv = new ContentValues();
        cv.put("titulo", nota.getTitulo());
        cv.put("texto", nota.getTexto());
        cv.put("latitud", nota.getLatitud());
        cv.put("longitud", nota.getLongitud());
        cv.put("lugar", nota.getLugar());
        String[] buscar = new String[]{nota.getTitulo()};

        //Realiza la actualización de la nota
        return baseDeDatos.update("notas", cv, "titulo =?", buscar);
    }

    //Comprueba los datos para borrar la nota
    public void deleteNote(View view) {
        String titulo = tituloUpdateEditText.getText().toString();
        //Devuelve mensajes en caso de borrado correcto o no
        if (deleteNoteDatabase(titulo) != -1) {

            Toast.makeText(this, "Nota Eliminada", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        } else {

            Toast.makeText(this, "Error al eliminar la nota", Toast.LENGTH_SHORT).show();

        }

    }

    //Realiza el borrado en la base de datos
    public int deleteNoteDatabase(String titulo) {

        //Llama a la base de datos y ejecuta la query
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "notas", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        Cursor fila = db.rawQuery("select * from notas where titulo = '" + titulo + "'", null);
        String[] argumentos = {titulo};

        //borra y devuelve el resultado
        if (fila.moveToFirst()) {

            return db.delete("notas", "titulo = ?", argumentos);

        } else {
            return -1;
        }


    }
}