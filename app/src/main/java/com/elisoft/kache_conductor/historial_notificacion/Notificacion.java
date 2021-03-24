package com.elisoft.kache_conductor.historial_notificacion;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.elisoft.kache_conductor.Notificacion_pedido_m;
import com.elisoft.valle_grande_conductor.R;
import com.elisoft.kache_conductor.SqLite.AdminSQLiteOpenHelper;

import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by ELIO on 15/11/2016.
 */


public class Notificacion extends AppCompatActivity {
    ListView lista_carrera;
    ArrayList<CNotificacion> carrera;
    private ProgressDialog pDialog;
    int id_pedido;
    Bundle save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.save=savedInstanceState;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setContentView(R.layout.activity_notificacion);
        lista_carrera = (ListView) findViewById(R.id.lista_notificacion);

        lista_carrera.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(carrera.get(i).getTipo().equals("1"))
                {

                }else if(carrera.get(i).getTipo().equals("2"))
                {
                    notificacion_pedido_taxi(carrera.get(i).getId_pedido(),carrera.get(i).getNombre(),carrera.get(i).getLatitud(),carrera.get(i).getLongitud());
                }
             modificacion(String.valueOf(carrera.get(i).getId()));

            }
        });



        cargar_carrera_en_la_lista();

    }

    private void notificacion_pedido_taxi(String id_pedido, String nombre, String latitud, String longitud) {

        Intent moto = new Intent(getApplicationContext(),Notificacion_pedido_m.class);
        moto.putExtra("id_pedido",id_pedido);
        moto.putExtra("nombre",nombre);
        moto.putExtra("latitud",latitud);
        moto.putExtra("longitud",longitud);
        startActivity(moto);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                cargar_carrera_en_la_lista( );
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_usuario, menu);
        return true;
    }


    public void modificacion(String id) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, getString(R.string.nombre_sql), null, Integer.parseInt(getString(R.string.version_sql)));

        SQLiteDatabase bd = admin.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("leido", "1");
        int cant = bd.update("notificacion", registro, "id=" +id, null);
        bd.close();

    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public void actualizar_lista() {

        Items_notificacion adaptador = new Items_notificacion(Notificacion.this,save,this, carrera);
        lista_carrera.setAdapter(adaptador);

    }



    public void cargar_carrera_en_la_lista( ) {
        carrera = new ArrayList<CNotificacion>();
try {
    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, getString(R.string.nombre_sql), null, Integer.parseInt(getString(R.string.version_sql)));

    SQLiteDatabase bd = admin.getWritableDatabase();
    Cursor fila = bd.rawQuery("select * from notificacion  ORDER BY id DESC limit 30 ", null);

    if (fila.moveToFirst()) {  //si ha devuelto 1 fila, vamos al primero (que es el unico)
        Log.e("id", fila.getString(0) + " leido=" + fila.getString(11));
        int id = Integer.parseInt(fila.getString(0));
        int leido = Integer.parseInt(fila.getString(11));
        do {
            CNotificacion hi = new CNotificacion(id, fila.getString(1), fila.getString(2), fila.getString(3), fila.getString(4), fila.getString(5), fila.getString(6), fila.getString(7), fila.getString(8), fila.getString(9), fila.getString(10), leido);
            carrera.add(hi);
        } while (fila.moveToNext());

    } else
        Toast.makeText(this, "No hay registrados",
                Toast.LENGTH_SHORT).show();

    bd.close();
    actualizar_lista();
}catch (Exception e)
{
Log.e("notificacion",""+e);
}
    }







}

