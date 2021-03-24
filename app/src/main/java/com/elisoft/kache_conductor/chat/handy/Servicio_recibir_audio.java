package com.elisoft.kache_conductor.chat.handy;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.elisoft.valle_grande_conductor.R;
import com.elisoft.kache_conductor.SqLite.AdminSQLiteOpenHelper;
import com.elisoft.kache_conductor.Suceso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import okio.ByteString;


/**
 * Created by ELIO on 12/01/2018.
 */

public class Servicio_recibir_audio extends IntentService {
   String  url="";
   int id_chat=0;
   String  canal;
    private MediaPlayer mPlayer;
    static List<byte[]> sLista = new ArrayList<>();

    Suceso suceso;


    public Servicio_recibir_audio() {
        super("Servicio_recibir_audio");
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

                Bundle bundle=intent.getExtras();
                id_chat=bundle.getInt("id_chat");
                canal=bundle.getString("canal");
                url=bundle.getString("url");
                handleActionRun();

        }
    }

    /**
     * Maneja la acción de ejecución del servicio
     */
    private void handleActionRun() {
        try {


            servicio_recibir_audio(
                    String.valueOf(id_chat),
                    canal,
                    url
            );

            // Quitar de primer plano
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void servicio_recibir_audio(String id_chat,String canal,String v_url) {

        try {
            JSONObject jsonParam= new JSONObject();
            jsonParam.put("canal", canal);
            jsonParam.put("id_chat", id_chat);


            RequestQueue queue = Volley.newRequestQueue(this);

            JsonObjectRequest myRequest= new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jsonParam,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject respuestaJSON) {

                            try {
                                suceso=new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));

                                if (suceso.getSuceso().equals("1")) {
                                    //limpiar audio
                                    sLista.clear();
                                    JSONArray usu=respuestaJSON.getJSONArray("audio");
                                    for (int i=0;i<usu.length();i++) {
                                        String audio = usu.getJSONObject(i).getString("audio");

                                        //convertimos el audio en BYTES
                                        ByteString d = ByteString.decodeHex(audio);
                                        byte[] bytes1 = d.toByteArray();

                                        sLista.add(bytes1);
                                    }
                                    //escuchar audio
                                    escuchar();
                                } else  {
                                    //mensaje_error("Vuelva a intentarlo de nuevo.");
                                }



                            } catch (JSONException e) {
                                e.printStackTrace();
                                //  mensaje_error("Falla en tu conexión a Internet.");
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    // mensaje_error("Falla en tu conexión a Internet.");
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
        }

    }



    public void escuchar()
    {
        //escuchar audio
        playReceivedFile();
    }

    private void playReceivedFile() {
        File f = buildAudioFileFromReceivedBytes();

        playAudio(f);
    }


    @NonNull
    private File buildAudioFileFromReceivedBytes() {
        File f = new File(getCacheDir().getAbsolutePath() + "/recibido_"+id_chat+".3gp");
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        try {
            out = (new FileOutputStream(f));
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            for (byte[] b : sLista) {
                out.write(b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }


    private void playAudio(File f) {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(this, Uri.parse(f.getPath()));
            mPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("servicio audio", "onClosing: dudation in millis: " + mPlayer.getDuration());

        mPlayer.start();
        actualizar_audio(String.valueOf(id_chat),"1");
    }

    public void actualizar_audio(
            String id,
            String visto
    ) {
        try {
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, getString(R.string.nombre_sql), null, Integer.parseInt(getString(R.string.version_sql)));
            SQLiteDatabase bd = admin.getWritableDatabase();
            ContentValues registro = new ContentValues();
            registro.put("visto", visto);
            bd.update("audio",  registro,"id="+id,null);
            bd.close();
        } catch (Exception e) {
            Log.w("update Audio", e.toString());
        }
    }




}

