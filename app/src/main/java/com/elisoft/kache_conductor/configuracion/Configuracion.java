package com.elisoft.kache_conductor.configuracion;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.elisoft.valle_grande_conductor.R;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class Configuracion extends AppCompatActivity implements  View.OnClickListener{

    Spinner sp_hora_inicial;
    Spinner sp_hora_final;

    Button bt_guardar;


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bt_guardar=(Button)findViewById(R.id.bt_guardar);
        sp_hora_inicial=(Spinner)findViewById(R.id.sp_hora_inicial);
        sp_hora_final=(Spinner)findViewById(R.id.sp_hora_final);

        bt_guardar.setOnClickListener(this);

        obtener_configuracion();


    }
    public void obtener_configuracion() {
        SharedPreferences sconfiguracion = getSharedPreferences(getString(R.string.share_configuracion), Context.MODE_PRIVATE);
        int hora_inicio = 6;
        int hora_fin = 7;

        hora_inicio = sconfiguracion.getInt(getString(R.string.hora_inicio), 6);
        hora_fin = sconfiguracion.getInt(getString(R.string.hora_fin), 18);

        sp_hora_inicial.setSelection(hora_inicio-1);
        sp_hora_final.setSelection(hora_fin-1);

    }

    public void guardar_configuracion()
    {
        SharedPreferences sconfiguracion = getSharedPreferences(getString(R.string.share_configuracion), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sconfiguracion.edit();
        editor.putInt(getString(R.string.hora_inicio),Integer.parseInt(sp_hora_inicial.getSelectedItem().toString().trim()));
        editor.putInt(getString(R.string.hora_fin),Integer.parseInt(sp_hora_final.getSelectedItem().toString().trim()));

        editor.commit();

        mensaje_error_final("Configuración guardada correctamente.");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_guardar:
                guardar_configuracion();
                break;
        }

    }

    public void mensaje_error_final(String mensaje)
    {try {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(mensaje);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                finish();
            }
        });
        builder.create();
        builder.show();
    }catch (Exception e)
    {
        Log.e("mensaje error final",e.toString());
    }
    }
}
