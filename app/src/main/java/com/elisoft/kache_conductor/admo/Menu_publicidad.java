package com.elisoft.kache_conductor.admo;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.elisoft.valle_grande_conductor.R;

public class Menu_publicidad extends AppCompatActivity  implements View.OnClickListener {
Button bt_generar_uno;
Button bt_generar_dos;
Button bt_generar_tres;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_publicidad);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bt_generar_uno=(Button)findViewById(R.id.bt_generar_uno);
        bt_generar_dos=(Button)findViewById(R.id.bt_generar_dos);
        bt_generar_tres=(Button)findViewById(R.id.bt_generar_tres);

        bt_generar_uno.setOnClickListener(this);
        bt_generar_dos.setOnClickListener(this);
        bt_generar_tres.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_generar_uno:
                startActivity(new Intent(this, Anuncio_bonificado.class));
                break;
            case R.id.bt_generar_dos:
                startActivity(new Intent(this, Anuncio_bonificado.class));
                break;
            case R.id.bt_generar_tres:
                startActivity(new Intent(this, Anuncio_bonificado.class));
                break;
        }

    }
}
