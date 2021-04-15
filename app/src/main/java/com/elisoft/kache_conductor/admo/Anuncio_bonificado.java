package com.elisoft.kache_conductor.admo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.elisoft.kache_conductor.Suceso;
import com.elisoft.kache_conductor.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;




public class Anuncio_bonificado extends AppCompatActivity {

    ProgressDialog pDialog;
    Suceso suceso;
    private RewardedVideoAd mRewardedVideoAd;
    private  String AD_UNIT_ID ; //My code

    Button bt_show,bt_cargar;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Get the view from singleitemview.xml
        setContentView(R.layout.activity_anuncio_bonificado);

        MobileAds.initialize(this, getString(R.string.id_AdMob_aplicacion));
        AD_UNIT_ID=getString(R.string.bonificado_ad_unit_id);

        bt_show = (Button) findViewById(R.id.bt_show);
        bt_cargar = (Button) findViewById(R.id.bt_cargar);

        //Anuncio probar video
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener()


        {

            @Override
            public void onRewardedVideoAdLoaded()
            {
                Toast.makeText(Anuncio_bonificado.this, "Video Cargado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdOpened()
            {
                Toast.makeText(Anuncio_bonificado.this, "Abriendo Video", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoStarted()
            {
                Toast.makeText(Anuncio_bonificado.this, "Reproduciendo...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdClosed()
            {
                Toast.makeText(Anuncio_bonificado.this, "Cerrando...", Toast.LENGTH_SHORT).show();
                // Preload the next video ad.
                loadRewardedVideoAd();
            }

            @Override
            public void onRewarded(RewardItem rewardItem)
            {
                Toast.makeText(Anuncio_bonificado.this,String.format(Locale.getDefault(),"Mira el video completo!", rewardItem.getAmount(), rewardItem.getType()),Toast.LENGTH_SHORT).show();
                int credito = 0;
                try{
                    credito=Integer.parseInt(String.format(Locale.getDefault(),"%d", rewardItem.getAmount(), rewardItem.getType()));
                }catch (Exception e){
                    credito=0;
                }

                SharedPreferences prefe = getSharedPreferences("perfil_conductor", Context.MODE_PRIVATE);
                String id_conductor=prefe.getString("ci", "");

                servicio_volley_set_credito( id_conductor, String.valueOf(credito));

            }

            @Override
            public void onRewardedVideoAdLeftApplication()
            {
                Toast.makeText(Anuncio_bonificado.this, "onRewardedVideoAdLeftApplication", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i)
            {
                Toast.makeText(Anuncio_bonificado.this, "Regresa mas tarde...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoCompleted() {

            }
        });

        bt_show.setOnClickListener(new View.OnClickListener()
    {
        public void onClick(View arg0)
        {
            Log.i("probar","video");
            showRewardedVideo();
                /*System.out.println("click boton probar video");
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                }*/
        }
    });

        bt_cargar.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                loadRewardedVideoAd();
                Toast.makeText(Anuncio_bonificado.this, "Video Cargado", Toast.LENGTH_SHORT).show();
            }
        });

        loadRewardedVideoAd();
    }

    private void loadRewardedVideoAd() {
        if (!mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.loadAd(AD_UNIT_ID, new AdRequest.Builder().build());
        }
    }

    private void showRewardedVideo() {

        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }



    private void servicio_volley_set_credito(String ci, String credito) {
        pDialog = new ProgressDialog(Anuncio_bonificado.this);
        pDialog.setTitle(getString(R.string.app_name));
        pDialog.setMessage("Autenticando ....");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        try {
            String v_url= getString(R.string.servidor) + "frmTaxi.php?opcion=set_credito";

            JSONObject jsonParam= new JSONObject();
            jsonParam.put("ci", ci);
            jsonParam.put("credito", credito);

            RequestQueue queue = Volley.newRequestQueue(this);



            JsonObjectRequest myRequest= new JsonObjectRequest(
                    Request.Method.POST,
                    v_url,
                    jsonParam,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject respuestaJSON) {
                            pDialog.cancel();

                            try {
                                String error = respuestaJSON.getString("suceso");// suceso es el campo en el Json
                                String mensaje = respuestaJSON.getString("mensaje");
                                suceso = new Suceso(error, mensaje);

                                if (suceso.getSuceso().equals("1")) {


                                } else  {
                                    mensaje_error(suceso.getMensaje());
                                }

                                //...final de final....................





                            } catch (JSONException e) {
                                e.printStackTrace();
                                mensaje_error("Falla en tu conexión a Internet.");
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    mensaje_error("Falla en tu conexión a Internet.");
                }
            }
            ){
                public Map<String,String> getHeaders() throws AuthFailureError {
                    Map<String,String> parametros= new HashMap<>();
                    parametros.put("content-type","application/json; charset=utf-8");
                    parametros.put("Authorization","apikey 849442df8f0536d66de700a73ebca-us17");
                    parametros.put("Accept", "application/json");

                    return  parametros;
                }
            };


            queue.add(myRequest);
        } catch (Exception e) {
            pDialog.cancel();
        }


    }




    public void mensaje(String mensaje) {
        Toast toast = Toast.makeText(this, mensaje, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }


    public void mensaje_error(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Importante");
        builder.setMessage(mensaje);
        builder.setPositiveButton("OK", null);
        builder.create();
        builder.show();
    }


}