package com.example.proyect_a;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateNote extends AppCompatActivity {
    private EditText lugarUpdateEditText,coordenadasUpdateEditText, textoUpdateEditText;
    TextView tituloUpdateEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);
        tituloUpdateEditText = findViewById(R.id.tituloUpdateTextView);
        lugarUpdateEditText = findViewById(R.id.lugarUpdateEditText);
        coordenadasUpdateEditText = findViewById(R.id.coordenadasUpdateEditText);
        textoUpdateEditText = findViewById(R.id.textoUpdateEditText);

        Intent intent = getIntent();
        String titulo = intent.getStringExtra("titulo");
        if(titulo != null){
            mostrarNota(titulo);
        }
    }
    //Muestra la nota seleccionada
    public void mostrarNota(String titulo){
        //Traemos de la base de datos la nota seleccionada
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "notas", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        String[] campos = new String[]{"titulo", "latitud", "longitud", "lugar","texto"};
        String[] buscar = new String[]{titulo};
        Cursor cursor = db.query("notas", campos, "titulo =?", buscar, null, null, null);
        //En caso de que no se haya traido ningun titulo (nueva nota)


        if(titulo != null){
            //Si exite la nota
            if (cursor.moveToFirst()) {
                Double lat = cursor.getDouble(1);
                Double lon = cursor.getDouble(2);
                if(lat == 0.0 && lon == 0.0){
                    coordenadasUpdateEditText.setText("");
                }else{
                    coordenadasUpdateEditText.setText(lat + "," + lon);
                }
                //La cargamos para visualizar
                tituloUpdateEditText.setText(cursor.getString(0));
                textoUpdateEditText.setText(cursor.getString(4));
                lugarUpdateEditText.setText(cursor.getString(3));
            }else{
                Toast.makeText(this,"Error al mostrar la nota", Toast.LENGTH_SHORT);
            }
        }

    }
    //Persiste la nota en la base de datos
    public void save(View view) {
        //Obtenemos los datos
        Pattern pattern = Pattern.compile("^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$");
        String titulo = tituloUpdateEditText.getText().toString();
        String texto = textoUpdateEditText.getText().toString();
        String lugar = lugarUpdateEditText.getText().toString();
        String coord = coordenadasUpdateEditText.getText().toString();

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
                if (updateNote(nota) != -1) {
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
    public long updateNote(Nota nota) {

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "notas", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("titulo", nota.getTitulo());
        cv.put("texto", nota.getTexto());
        cv.put("latitud", nota.getCoordenadas().split(",")[0]);
        cv.put("longitud", nota.getCoordenadas().split(",")[1]);
        cv.put("lugar", nota.getLugar());
        String[] buscar = new String[]{nota.getTitulo()};
        return baseDeDatos.update("notas", cv, "titulo =?",buscar);
    }
    public void delete(View view) {
        String titulo = tituloUpdateEditText.getText().toString();
        if (eliminarNota(titulo) != -1) {
            Toast.makeText(this, "Nota Eliminada", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Error al eliminar la nota", Toast.LENGTH_SHORT).show();
        }

    }
    public int eliminarNota(String titulo) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "notas", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        Cursor fila = db.rawQuery("select * from notas where titulo = '" + titulo + "'", null);
        String[] argumentos = {titulo};
        if(fila.moveToFirst()){
            return db.delete("notas", "titulo = ?", argumentos);
        }else{
            return -1;
        }


    }
}