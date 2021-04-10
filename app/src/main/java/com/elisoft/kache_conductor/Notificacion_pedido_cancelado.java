package com.elisoft.kache_conductor;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class Notificacion_pedido_cancelado extends AppCompatActivity implements  View.OnClickListener{
  TextView tv_mensaje;
    Button bt_aceptar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacion_pedido_cancelado);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        tv_mensaje=(TextView)findViewById(R.id.tv_mensaje);
        bt_aceptar=(Button)findViewById(R.id.bt_aceptar);

        try{
            Bundle bundle=getIntent().getExtras();
            String mensaje="";
            mensaje=bundle.getString("mensaje","");
            if(mensaje.trim().equals("")==true)
            {
                tv_mensaje.setText("El Pasajero cancelo el pedido.");
            }else
            {
                tv_mensaje.setText(mensaje);
            }

        }catch (Exception e)
        {
            finish();
        }

        bt_aceptar.setOnClickListener(this);
        getSupportActionBar().hide();

    }
    @Override
    public void onClick(View view) {
        if(view.getId()== R.id.bt_aceptar)
        {
         finish();
        }
    }

}
