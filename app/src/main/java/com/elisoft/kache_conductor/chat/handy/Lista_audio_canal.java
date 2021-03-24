package com.elisoft.kache_conductor.chat.handy;


import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.elisoft.valle_grande_conductor.R;
import com.elisoft.kache_conductor.Suceso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Lista_audio_canal extends AppCompatActivity {

    private AudioArrayAdapter audioArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private FloatingActionButton buttonSend;

    ImageView im_perfil;
    TextView tv_nombre;
    private boolean side =true;
    SharedPreferences perfil;
    String id_usuario;

    Suceso suceso;

    public static final String mBroadcastStringAction = "com.truiton.broadcast.string";
    private IntentFilter mIntentFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_audio_canal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonSend = (FloatingActionButton) findViewById(R.id.send);

        listView = (ListView) findViewById(R.id.msgview);

        audioArrayAdapter = new AudioArrayAdapter(getApplicationContext(), R.layout.right);
        listView.setAdapter(audioArrayAdapter);

        chatText = (EditText) findViewById(R.id.msg);
        im_perfil = (ImageView) findViewById(R.id.im_perfil);
        tv_nombre = (TextView) findViewById(R.id.tv_nombre);


        audioArrayAdapter = new AudioArrayAdapter(getApplicationContext(), R.layout.right);
        listView.setAdapter(audioArrayAdapter);



        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(audioArrayAdapter);

        //to scroll the list view to bottom on data change
        audioArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(audioArrayAdapter.getCount() - 1);

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CAudio audio=audioArrayAdapter.getItem(i);
                Intent intent = new Intent(getApplicationContext(), Servicio_recibir_audio.class);
                intent.putExtra("id_chat",audio.id_chat);
                intent.putExtra("canal","TAXI VALLE");
                intent.putExtra("url",getString(R.string.servidor) + "frmChat.php?opcion=get_audio_canal_por_id_chat");
                startService(intent);


            }
        });



        //Intent filter para recibir datos del servicio.
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(mBroadcastStringAction);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }





    public static String hora() {
        String DATE_FORMAT_NOW = "HH:mm:ss";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }

    @Override
    protected void onStart() {
        servicio_ultimo_audio("TAXI VALLE",
                getString(R.string.servidor) + "frmChat.php?opcion=get_ultimo_max_audio_canal");
        super.onStart();
    }

    private void servicio_ultimo_audio(String canal  , String v_url) {

        try {
            JSONObject jsonParam= new JSONObject();
            jsonParam.put("canal", canal);


            RequestQueue queue = Volley.newRequestQueue(this);

            JsonObjectRequest myRequest= new JsonObjectRequest(
                    Request.Method.POST,
                    v_url,
                    jsonParam,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject respuestaJSON) {

                            try {
                                suceso=new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));

                                if (suceso.getSuceso().equals("1")) {
                                    //limpiar audio

                                    audioArrayAdapter = new AudioArrayAdapter(getApplicationContext(), R.layout.right);

                                    JSONArray usu=respuestaJSON.getJSONArray("canal");
                                    for (int i=0;i<usu.length();i++) {

                                        int id = Integer.parseInt(usu.getJSONObject(i).getString("id"));
                                        int id_cond = Integer.parseInt(usu.getJSONObject(i).getString("id_conductor"));
                                        int id_usu = Integer.parseInt(usu.getJSONObject(i).getString("id_usuario"));
                                        int id_adm = Integer.parseInt(usu.getJSONObject(i).getString("id_administrador"));
                                        String titulo = usu.getJSONObject(i).getString("titulo");
                                        String mensaje =usu.getJSONObject(i).getString("mensaje");
                                        String fecha = usu.getJSONObject(i).getString("fecha");
                                        String hora = usu.getJSONObject(i).getString("hora");
                                        int estado = Integer.parseInt(usu.getJSONObject(i).getString("estado"));
                                        String yo =usu.getJSONObject(i).getString("yo");
                                        String canal =usu.getJSONObject(i).getString("canal");
                                        String tipo =usu.getJSONObject(i).getString("tipo");


                                        audioArrayAdapter.add(new CAudio(id,false, mensaje,
                                                titulo,
                                                fecha,
                                                hora,
                                                estado,
                                                id_usu,
                                                id_cond,
                                                Integer.parseInt(yo),
                                                tipo,canal,id_adm));


                                    }

                                    listView.setAdapter(audioArrayAdapter);
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






}
