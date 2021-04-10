package com.elisoft.kache_conductor;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class Notificacion_mensaje extends AppCompatActivity implements  View.OnClickListener{
    TextView tv_mensaje;
    Button bt_aceptar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacion_mensaje);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        tv_mensaje=(TextView)findViewById(R.id.tv_mensaje);
        bt_aceptar=(Button)findViewById(R.id.bt_aceptar);
        String mensaje="";
        String titulo="";
        try{
            Bundle bundle=getIntent().getExtras();
            mensaje=bundle.getString("mensaje","");
            titulo=bundle.getString("titulo","");
            tv_mensaje.setText(mensaje);
        }catch (Exception e)
        {
            finish();
        }
        try {
            getSupportActionBar().setTitle("Notificación-" + titulo);
        }catch (Exception e)
        {
        }
            bt_aceptar.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        if(view.getId()== R.id.bt_aceptar)
        {
            finish();
        }
    }

}
