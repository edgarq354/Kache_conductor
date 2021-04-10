package com.elisoft.kache_conductor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;


import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;


import androidx.appcompat.app.AppCompatActivity;


public class Animacion extends AppCompatActivity {
    ProgressBar cargando;
    Handler handle=new Handler();


    boolean sw=true;

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        sw=false;
    }


    @Override
    protected void onStop() {
        sw=false;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        sw=false;
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        sw=false;
        super.onPause();
    }

    int i,cant_i;


    MediaPlayer mp;

    @Override
    protected void onStart() {
        sw=true;
        if (verificar_login_taxi()) {
            startActivity(new Intent(this, Menu_taxi.class));
            finish();
        }else{

            progress_en_proceso();}
        super.onStart();
    }

    @Override
    public void onBackPressed() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_animacion);
        cargando=(ProgressBar)findViewById(R.id.cargando);

         getSupportActionBar().hide();


        try{
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }catch (Exception e)

       {}


        sw=true;

}


    public  void progress_en_proceso()
    {
        mp= MediaPlayer.create(this, R.raw.inicio_zello);
       // mp.start();

        i=0;
        cant_i=0;
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (i<51)
                {
                    i=i+1;

                    handle.post(new Runnable() {
                        @Override
                        public void run() {
                            cargando.setProgress(i);

                            if(i<11)
                            {
                                try{
                                    cant_i+=9;


                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }else if(i<30){
                                try{
                                    cant_i+=3;

                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }else{
                                try{
                                    cant_i+=10;

                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }

                            if(i>=50)
                            {
                                cargando.setVisibility(View.INVISIBLE);
                            }

                        }
                    });
                    try{
                        Thread.sleep(100);
                    }catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
                if(sw==true){
                    startActivity(new Intent(getApplicationContext(), Iniciar_sesion.class));
                }
            }
        }).start();



    }



    public boolean verificar_login_taxi()
    {
        SharedPreferences perfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);
        return (perfil.getString("login_taxi","").equals("1"));

    }


}
