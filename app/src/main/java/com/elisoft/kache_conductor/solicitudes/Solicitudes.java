package com.elisoft.kache_conductor.solicitudes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.elisoft.kache_conductor.notificaciones.SharedPrefManager;
import com.elisoft.valle_grande_conductor.R;
import com.elisoft.kache_conductor.Suceso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class Solicitudes extends AppCompatActivity {
    Suceso suceso;
    private ProgressDialog pDialog;
    ArrayList<CSolicitud> cSolicitud=new ArrayList<CSolicitud>();
    ListView lv_lista;

    RequestQueue queue=null;

    @Override
    protected void onStart() {
        super.onStart();
        servicio_volley_lista();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitudes);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lv_lista=(ListView)findViewById(R.id.lv_lista);

        lv_lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CSolicitud hi=new CSolicitud();
                hi=cSolicitud.get(i);
                abrir_solicitud(hi);
            }
        });
        setSonido(0);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        servicio_volley_lista();

    }



    public  void setSonido(int sw)
    {
        SharedPreferences sonido=getSharedPreferences(getString(R.string.sonido),MODE_PRIVATE);
        SharedPreferences.Editor editor=sonido.edit();
        editor.putInt("sonido",sw);
        editor.commit();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void abrir_solicitud(CSolicitud hi) {
        if(hi.getEstado().equals("100")){
            aceptar_solicitud(hi.getId());
        }
        /*
        Intent dialogIntent = new Intent(this, Notificacion_pedido_m.class);
        dialogIntent.putExtra("id_pedido", hi.getId());
        dialogIntent.putExtra("nombre", hi.getNombre_usuario());
        dialogIntent.putExtra("latitud", hi.getLatitud());
        dialogIntent.putExtra("longitud", hi.getLongitud());
        dialogIntent.putExtra("mensaje", "");
        dialogIntent.putExtra("clase_vehiculo", Integer.parseInt(hi.getClase_vehiculo()));
        dialogIntent.putExtra("tipo_pedido_empresa", 0);
        dialogIntent.putExtra("indicacion", hi.getDireccion());
        startActivity(dialogIntent);
        */
    }


    public  void aceptar_solicitud(String id_pedido){
        SharedPreferences perfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);

        servicio_aceptar_pedido(String.valueOf(id_pedido),perfil.getString("ci",""),perfil.getString("placa",""));
    }




    private void servicio_aceptar_pedido(String id_pedido, String ci, String placa) {


        try {

            pDialog = new ProgressDialog(Solicitudes.this);
            pDialog.setTitle(getString(R.string.app_name));
            pDialog.setMessage("Autenticando . . .");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

            String token= SharedPrefManager.getInstance(this).getDeviceToken();

            JSONObject jsonParam= new JSONObject();
            jsonParam.put("ci",ci);
            jsonParam.put("id_pedido", id_pedido);
            jsonParam.put("placa", placa);
            jsonParam.put("token", token);
            String url=getString(R.string.servidor) + "frmPedido.php?opcion=aceptar_pedido_multiples";
            if (queue == null) {
                queue = Volley.newRequestQueue(this);
                Log.e("volley","Setting a new request queue");
            }


            JsonObjectRequest myRequest= new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jsonParam,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject respuestaJSON) {



                            try {
                                pDialog.dismiss();

                                suceso= new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));

                                if (suceso.getSuceso().equals("1")) {
 /*
                                    JSONArray usu=respuestaJSON.getJSONArray("pedido");

                                    String id_pedido=usu.getJSONObject(0).getString("id");
                                    String id_usuario=usu.getJSONObject(0).getString("id_usuario");
                                    String fecha_pedido=usu.getJSONObject(0).getString("fecha_pedido");
                                    String fecha_proceso=usu.getJSONObject(0).getString("fecha_proceso");
                                    String mensaje=usu.getJSONObject(0).getString("direccion");
                                    String estado=usu.getJSONObject(0).getString("estado");
                                    String nombre_usuario=usu.getJSONObject(0).getString("nombre_usuario");
                                    String celular=usu.getJSONObject(0).getString("celular");
                                    double latitud=Double.parseDouble(usu.getJSONObject(0).getString("latitud"));
                                    double longitud=Double.parseDouble(usu.getJSONObject(0).getString("longitud"));
                                    //iniciar el servicio de pedido en curso
                                    //cargar_pedido_en_curso(id_pedido,id_usuario,fecha_pedido,fecha_proceso,mensaje,estado,latitud,longitud,nombre_usuario,celular);
//////////////////-----------------------------------------------------------------------------------

                                    Intent i=new Intent(getApplicationContext(),Menu_taxi.class);
                                    i.putExtra("id_pedido",String.valueOf(id_pedido));
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);

                                     */
                                  mensaje_error_final(suceso.getMensaje());

                                }else if(suceso.getSuceso().equals("2"))
                                {

                                    mensaje_error(suceso.getMensaje());
                                }
                                else
                                {
                                    aceptar_solicitud(id_pedido);
                                }
                            } catch (JSONException e) {
                                pDialog.dismiss();

                                e.printStackTrace();
                                aceptar_solicitud(id_pedido);
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                    aceptar_solicitud(id_pedido);
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


            myRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(myRequest);


        } catch (Exception e) {
            mensaje_error("No pudimos conectarnos al servidor.\nVuelve a intentarlo.");
        }
    }



    private void servicio_volley_lista() {

        try {
            String v_url= getString(R.string.servidor)+"frmPedido.php?opcion=lista_pedido_todo_por_ci";
            SharedPreferences prefe = getSharedPreferences("perfil_conductor", Context.MODE_PRIVATE);
            String ci=prefe.getString("ci", "");

            JSONObject jsonParam= new JSONObject();
            jsonParam.put("ci", ci);
            if (queue == null) {
                queue = Volley.newRequestQueue(this);
                Log.e("volley","Setting a new request queue");
            }


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
                                    JSONArray usu=respuestaJSON.getJSONArray("historial");
                                    cSolicitud=new ArrayList<CSolicitud>();
                                    for (int i=0;i<usu.length();i++)
                                    {
                                        String id=usu.getJSONObject(i).getString("id");
                                        String latitud=usu.getJSONObject(i).getString("latitud");
                                        String longitud=usu.getJSONObject(i).getString("longitud");
                                        String latitud_inicio=usu.getJSONObject(i).getString("latitud_inicio");
                                        String longitud_inicio=usu.getJSONObject(i).getString("longitud_inicio");
                                        String latitud_final=usu.getJSONObject(i).getString("latitud_final");
                                        String longitud_final=usu.getJSONObject(i).getString("longitud_final");
                                        String direccion=usu.getJSONObject(i).getString("direccion");
                                        String direccion_inicio=usu.getJSONObject(i).getString("direccion_inicio");
                                        String direccion_final=usu.getJSONObject(i).getString("direccion_final");
                                        String monto_total=usu.getJSONObject(i).getString("monto_total");
                                        String monto_pedido=usu.getJSONObject(i).getString("monto_pedido");
                                        String id_vehiculo=usu.getJSONObject(i).getString("id_vehiculo");
                                        String id_conductor=usu.getJSONObject(i).getString("id_conductor");
                                        String id_usuario=usu.getJSONObject(i).getString("id_usuario");
                                        String nombre_usuario=usu.getJSONObject(i).getString("nombre_usuario");
                                        String nombre_conductor=usu.getJSONObject(i).getString("nombre_conductor");
                                        String numero_movil=usu.getJSONObject(i).getString("numero_movil");
                                        String direccion_imagen_usuario=usu.getJSONObject(i).getString("direccion_imagen_usuario");
                                        String fecha_pedido=usu.getJSONObject(i).getString("fecha_pedido");
                                        String fecha_proceso=usu.getJSONObject(i).getString("fecha_proceso");
                                        String calificacion_conductor=usu.getJSONObject(i).getString("calificacion_conductor");
                                        String calificacion_vehiculo=usu.getJSONObject(i).getString("calificacion_vehiculo");
                                        String estado=usu.getJSONObject(i).getString("estado");
                                        String clase_vehiculo=usu.getJSONObject(i).getString("clase_vehiculo");
                                        String tiempo=usu.getJSONObject(i).getString("tiempo");
                                        String distancia=usu.getJSONObject(i).getString("distancia");


                                        cSolicitud.add(new CSolicitud( id,
                                                latitud,
                                                longitud,
                                                latitud_inicio,
                                                longitud_inicio,
                                                latitud_final,
                                                longitud_final,
                                                direccion,
                                                direccion_inicio,
                                                direccion_final,
                                                monto_total,
                                                id_vehiculo,
                                                id_conductor,
                                                id_usuario,
                                                nombre_usuario,
                                                nombre_conductor,
                                                numero_movil,
                                                direccion_imagen_usuario,
                                                fecha_pedido,
                                                fecha_proceso,
                                                calificacion_conductor,
                                                calificacion_vehiculo,
                                                estado,
                                                clase_vehiculo,
                                                tiempo,
                                                distancia,
                                                monto_pedido));
                                    }

                                    Items_solicitud adaptador = new Items_solicitud(Solicitudes.this,cSolicitud);
                                    lv_lista.setAdapter(adaptador);

                                } else  {
                                    cSolicitud.clear();
                                    Items_solicitud adaptador = new Items_solicitud(Solicitudes.this,cSolicitud);
                                    lv_lista.setAdapter(adaptador);
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


            myRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(myRequest);

        } catch (Exception e) {

        }


    }



    public void mensaje_error(String mensaje)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Importante");
        builder.setMessage(mensaje);
        builder.setPositiveButton("OK", null);
        builder.create();
        builder.show();
    }



    public void mensaje_error_final(String mensaje)
    {try {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
