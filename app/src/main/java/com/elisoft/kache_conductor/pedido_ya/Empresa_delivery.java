package com.elisoft.kache_conductor.pedido_ya;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.elisoft.kache_conductor.R;
import com.squareup.picasso.Picasso;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;


public class Empresa_delivery extends AppCompatActivity implements View.OnClickListener{
    String id_empresa,nit,razon_social,direccion,direccion_logo,celular,telefono;
    ImageView im_logo;
    EditText et_razon_social,et_nit,et_direccion;

    Button bt_telefono,bt_celular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_delivery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        et_razon_social=(EditText)findViewById(R.id.et_razon_social);
        et_direccion=(EditText)findViewById(R.id.et_direccion);
        im_logo=(ImageView)findViewById(R.id.im_logo);
        et_nit=(EditText)findViewById(R.id.et_nit);
        bt_telefono=(Button)findViewById(R.id.bt_telefono);
        bt_celular=(Button)findViewById(R.id.bt_celular);

        bt_celular.setOnClickListener(this);
        bt_telefono.setOnClickListener(this);


        try{
            Bundle bundle=getIntent().getExtras();
            id_empresa=bundle.getString("id_empresa");
            nit=bundle.getString("nit");
            razon_social=bundle.getString("razon_social");
            direccion=bundle.getString("direccion");
            direccion_logo=bundle.getString("direccion_logo");
            celular=bundle.getString("celular");
            telefono=bundle.getString("telefono");

            et_razon_social.setText(razon_social);
            et_direccion.setText(direccion);
            et_nit.setText(nit);
            bt_celular.setText(celular);
            bt_telefono.setText(telefono);

            if(direccion_logo.length()>4) {

                String url = getString(R.string.servidor_web)+"storage/" + direccion_logo;
                Picasso.with(this).load(url).placeholder(R.mipmap.ic_empresa).into(im_logo);

            }else{

                im_logo.setImageBitmap(null);
            }



        }catch (Exception e){
            finish();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.bt_telefono:
                Intent llamada = new Intent(Intent.ACTION_CALL);
                llamada.setData(Uri.parse("tel:" + telefono));
                if (ActivityCompat.checkSelfPermission(Empresa_delivery.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                }
                startActivity(llamada);
                break;
            case R.id.bt_celular:
                Intent celular = new Intent(Intent.ACTION_CALL);
                celular.setData(Uri.parse("tel:" + celular));
                if (ActivityCompat.checkSelfPermission(Empresa_delivery.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                }
                startActivity(celular);
                break;
        }

    }
}
