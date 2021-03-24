package com.elisoft.kache_conductor.detalle_pedido_carrito;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.elisoft.kache_conductor.Cancelar_pedido_taxi;
import com.elisoft.valle_grande_conductor.R;
import com.elisoft.kache_conductor.Suceso;
import com.elisoft.kache_conductor.notificaciones.SharedPrefManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Detalle_pedido_delivery extends AppCompatActivity
        implements View.OnClickListener{

    ListView lista;
    ArrayList<CCarrito> carrito;
    Suceso suceso;

    ProgressDialog pDialog;

    ImageView im_perfil;
    TextView tv_nombre,tv_nit,tv_fecha,tv_direccion,tv_monto_carrito,tv_estado,tv_que_necesitas;
    Button bt_comenzar_carrera,bt_cancelar;

    String razon_social,nit,fecha,direccion,direccion_imagen,monto_carrito="",whatsapp_lugar,mensaje_carrito="",que_necesitas="";
    int id_usuario=0;
    int id_pedido = 0,estado_pedido=0;

    ImageButton ib_whatsapp;

    LinearLayout ll_pedido;


    @Override
    protected void onRestart() {
        actualizar();
        super.onRestart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pedido_delivery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        lista = (ListView) findViewById(R.id.lista);


        im_perfil=(ImageView) findViewById(R.id.im_perfil);
        tv_nombre=(TextView) findViewById(R.id.tv_nombre);
        tv_nit=(TextView) findViewById(R.id.tv_nit); tv_que_necesitas=(TextView) findViewById(R.id.tv_que_necesitas);

        tv_fecha=(TextView) findViewById(R.id.tv_fecha);
        tv_estado=(TextView) findViewById(R.id.tv_estado);
        tv_direccion=(TextView) findViewById(R.id.tv_direccion);
        tv_monto_carrito=(TextView) findViewById(R.id.tv_monto_carrito);
        ib_whatsapp=(ImageButton)findViewById(R.id.ib_whatsapp);
        ll_pedido=findViewById(R.id.ll_pedido);

        bt_cancelar=findViewById(R.id.bt_cancelar);
        bt_comenzar_carrera=findViewById(R.id.bt_comenzar_carrera);





        try {
            Bundle bundle = getIntent().getExtras();

            id_pedido = bundle.getInt("id_pedido");

        } catch (Exception e) {
            finish();
        }

        ib_whatsapp.setOnClickListener(this);
        bt_comenzar_carrera.setOnClickListener(this);
        bt_cancelar.setOnClickListener(this);


        actualizar();
        en_carrera();


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }


    private void actualizar() {


        Servicio servicio = new Servicio();
        servicio.execute(getString(R.string.servidor) + "frmPedido.php?opcion=get_delivery_proceso_detalle_por_id", "1", String.valueOf(id_pedido));

    }




    public void mensaje_error(String mensaje) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(mensaje);
        builder.setPositiveButton("OK", null);
        builder.create();
        builder.show();
    }



    public void mensaje_error_final(String mensaje) {
        try {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.app_name));
            builder.setCancelable(false);
            builder.setMessage(mensaje);
            builder.create();
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    finish();
                }
            });
            builder.show();
        } catch (Exception e) {
            Log.e("mensaje_error", e.toString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        case R.id.ib_whatsapp:
            boolean isWhatsapp = appInstalledOrNot("com.whatsapp");
            if (isWhatsapp)
                AbrirWhatsApp();
        break;

            case  R.id.bt_cancelar:

                SharedPreferences pe2=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
                int id_p=0 ;
                try{
                    id_p=Integer.parseInt(pe2.getString("id_pedido","0"));
                }catch (Exception e) {
                    id_p=0;
                }



                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
                    dialogo1.setTitle(getString(R.string.app_name));
                    dialogo1.setMessage("¿Desea cancelar?");
                    dialogo1.setCancelable(false);
                    dialogo1.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            //cargamos los datos
                            startActivity(new Intent(getApplicationContext(), Cancelar_pedido_taxi.class));

                        }
                    });
                    dialogo1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {

                        }
                    });
                    dialogo1.show();


                break;


            case R.id.bt_comenzar_carrera:
                SharedPreferences punto = getSharedPreferences("mi ubicacion_2", Context.MODE_PRIVATE);
                SharedPreferences perfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);

                double latitud_fin = Double.parseDouble(punto.getString("latitud", "0"));
                double longitud_fin = Double.parseDouble(punto.getString("longitud", "0"));
                double altura_fin = Double.parseDouble(punto.getString("altura", "0"));

                SharedPreferences spedido = getSharedPreferences("ultimo_pedido_conductor", Context.MODE_PRIVATE);
                servicio_comenzar_carrera(spedido.getString("id_pedido", ""), String.valueOf(latitud_fin), String.valueOf(longitud_fin), String.valueOf(altura_fin), perfil.getString("ci", ""), perfil.getString("placa", ""), spedido.getString("id_usuario", ""), tv_direccion.getText().toString());
                break;

        }
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }

    void AbrirWhatsApp() {

        SharedPreferences datos_perfil = getSharedPreferences("perfil_conductor", Context.MODE_PRIVATE);
        String nombre_conductor=datos_perfil.getString("nombre","")+" "+datos_perfil.getString("paterno","")+" "+datos_perfil.getString("materno","");

        PackageManager packageManager = getPackageManager();
        Intent i = new Intent(Intent.ACTION_VIEW);
        String mensaje="Hola '"+razon_social+"' soy "+nombre_conductor+" de *Traigo* " +
                "necesito me tenga listo la siguiente Lista de Carrito:\n\n" +
                "*Tambien necesito:* \n"+
                que_necesitas+
                " \n \n"+mensaje_carrito+"-----------------\n *Total:"+monto_carrito+ "bs.*\n\n" +
                "Pasare en unos minutos a recoger.";
        try {
            String url = "https://wa.me/"+whatsapp_lugar+"/?text=" + URLEncoder.encode(mensaje, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    // comenzar el servicio los carritos ....
    public class Servicio extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url = null;  // url donde queremos obtener informacion
            String devuelve = "500";
            if (isCancelled() == false) {
                devuelve = "-1";
                if (params[1] == "1") {
                    try {
                        HttpURLConnection urlConn;

                        url = new URL(cadena);
                        urlConn = (HttpURLConnection) url.openConnection();
                        urlConn.setDoInput(true);
                        urlConn.setDoOutput(true);
                        urlConn.setUseCaches(false);
                        urlConn.setRequestProperty("Content-Type", "application/json");
                        urlConn.setRequestProperty("Accept", "application/json");
                        urlConn.connect();

                        //se crea el objeto JSON
                        JSONObject jsonParam = new JSONObject();
                        jsonParam.put("id_pedido", params[2]);

                        //Envio los prametro por metodo post
                        OutputStream os = urlConn.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                        writer.write(jsonParam.toString());
                        writer.flush();
                        writer.close();

                        int respuesta = urlConn.getResponseCode();

                        StringBuilder result = new StringBuilder();

                        if (respuesta == HttpURLConnection.HTTP_OK) {

                            String line;
                            BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                            while ((line = br.readLine()) != null) {
                                result.append(line);
                            }


                            JSONObject respuestaJSON = new JSONObject(result.toString());//Creo un JSONObject a partir del
                            suceso = new Suceso(respuestaJSON.getString("suceso"), respuestaJSON.getString("mensaje"));

                            if (suceso.getSuceso().equals("1")) {
                                carrito = new ArrayList<CCarrito>();
                                // vacia los datos que estan registrados en nuestra base de datos SQLite..

                                JSONArray jpedido = respuestaJSON.getJSONArray("pedido");
                                JSONArray jcarrito = respuestaJSON.getJSONArray("carrito");
                                for (int i = 0; i < jpedido.length(); i++) {

                                    id_usuario=Integer.parseInt(jpedido.getJSONObject(i).getString("id_usuario"));
                                    estado_pedido=Integer.parseInt(jpedido.getJSONObject(i).getString("estado_pedido"));
                                    razon_social=jpedido.getJSONObject(i).getString("razon_social");
                                    nit=jpedido.getJSONObject(i).getString("nit");
                                    fecha=jpedido.getJSONObject(i).getString("fecha_pedido");
                                    direccion=jpedido.getJSONObject(i).getString("direccion_empresa");
                                    direccion_imagen=jpedido.getJSONObject(i).getString("direccion_logo");
                                    monto_carrito= jpedido.getJSONObject(i).getString("monto_pedido");
                                    whatsapp_lugar= jpedido.getJSONObject(i).getString("whatsapp_lugar");
                                    whatsapp_lugar= jpedido.getJSONObject(i).getString("whatsapp_lugar");
                                    que_necesitas= jpedido.getJSONObject(i).getString("que_necesitas");

                                }

                                mensaje_carrito="";
                                for (int i = 0; i < jcarrito.length(); i++) {

                                    int id_producto=Integer.parseInt(jcarrito.getJSONObject(i).getString("id_producto"));
                                    int cantidad=Integer.parseInt(jcarrito.getJSONObject(i).getString("cantidad"));
                                    String nombre=jcarrito.getJSONObject(i).getString("nombre");
                                    String descripcion=jcarrito.getJSONObject(i).getString("descripcion");
                                    String imagen1=jcarrito.getJSONObject(i).getString("imagen1");
                                    double monto_unidad=Double.parseDouble(jcarrito.getJSONObject(i).getString("monto_unidad"));
                                    double monto_total=Double.parseDouble(jcarrito.getJSONObject(i).getString("monto_total"));

                                    mensaje_carrito+= cantidad+" "+nombre+" x"+monto_unidad+" = "+monto_total+" bs \n";
                                    CCarrito hi =new CCarrito(id_producto,
                                            id_pedido,
                                            cantidad,
                                            nombre,
                                            descripcion,
                                            imagen1,
                                            monto_unidad,
                                            monto_total
                                    );
                                    carrito.add(hi);
                                }


                                devuelve = "1";
                            } else {
                                devuelve = "2";
                            }

                        }

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            return devuelve;
        }


        @Override
        protected void onPreExecute() {
            //para el progres Dialog

            try {
                pDialog = new ProgressDialog(Detalle_pedido_delivery.this);
                pDialog.setMessage("Autenticando. . .");
                pDialog.setIndeterminate(true);
                pDialog.setCancelable(true);
                pDialog.show();
            } catch (Exception e) {
                mensaje_error("Por favor actualice la aplicación.");
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                pDialog.cancel();
            }catch (Exception e)
            {
                Log.e("onPostExcute=", "" + s);
            }

            if (s.equals("1")) {
                Item_carrito adaptador = new Item_carrito(Detalle_pedido_delivery.this,Detalle_pedido_delivery.this,carrito);
                lista.setAdapter(adaptador);
                tv_nombre.setText(razon_social);
                tv_nit.setText("Nit:"+nit);
                tv_fecha.setText(fecha);
                tv_que_necesitas.setText(que_necesitas);
                tv_direccion.setText("Dirección:"+direccion);
                tv_monto_carrito.setText("Total de carrito:"+monto_carrito);


                String url = getString(R.string.servidor_web)+"storage/" + direccion_imagen;
                Picasso.with(Detalle_pedido_delivery.this).load(url).placeholder(R.mipmap.ic_empresa).into(im_perfil);


                if (estado_pedido==0) {
                    tv_estado.setText("Falta completar el carrito");
                } else if (estado_pedido == 1) {
                    tv_estado.setText("Aceptado");
                } else if (estado_pedido == 10) {
                    tv_estado.setText("Enviado a la tienda");
                } else if (estado_pedido == 11) {
                    tv_estado.setText("Aceptado por la Empresa");
                }else if (estado_pedido == 12) {
                    tv_estado.setText("El pedido se esta preparando");
                } else if (estado_pedido == 13) {
                    tv_estado.setText("Pedido en camino al domicilio");
                } else if (estado_pedido == 14) {
                    tv_estado.setText("Cancelado");
                }  else if (estado_pedido == 15) {
                    tv_estado.setText("Entregado correctamente");
                } else {
                    tv_estado.setText("Cancelado");
                }

            } else if (s.equals("2")) {

            } else {
                mensaje_error_final("Error Al conectar con el servidor.");
            }


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

    }



    private void servicio_comenzar_carrera(String id_pedido, String latitud, String longitud, String altura, String ci, String placa, String id_usuario, String direccion) {
        try {

            try {
                //para el progres Dialog
                pDialog = new ProgressDialog(Detalle_pedido_delivery.this);
                pDialog.setTitle(getString(R.string.app_name));
                pDialog.setMessage("Recogiendo el pedido");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }
            catch (Exception e)
            {

            }

            String token= SharedPrefManager.getInstance(this).getDeviceToken();

            JSONObject jsonParam= new JSONObject();
            jsonParam.put("id_pedido", id_pedido);
            jsonParam.put("latitud", latitud);
            jsonParam.put("longitud", longitud);
            jsonParam.put("altura", altura);
            jsonParam.put("ci", ci);
            jsonParam.put("placa", placa);
            jsonParam.put("id_usuario", id_usuario);
            jsonParam.put("direccion", direccion);
            jsonParam.put("token", token);
            String url=getString(R.string.servidor) + "frmCarrera.php?opcion=comenzar_carrera";
            RequestQueue queue = Volley.newRequestQueue(this);


            JsonObjectRequest myRequest= new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jsonParam,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject respuestaJSON) {

                            try{
                                pDialog.cancel();
                            }catch (Exception e)
                            {}

                            try {


                                suceso= new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));

                                if (suceso.getSuceso().equals("1")) {


                                    SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
                                    SharedPreferences.Editor editor=pedido.edit();
                                    editor.putString("id_carrera","1");
                                    editor.putString("estado","1");
                                    editor.putString("numero","1");
                                    editor.commit();
//////////////////-----------------------------------------------------------------------------------
                                    finish();

                                }else if(suceso.getSuceso().equals("2"))
                                {

                                    mensaje_error(suceso.getMensaje());
                                }
                                else
                                {

                                }
                            } catch (JSONException e) {
                                try{
                                    pDialog.cancel();
                                }catch (Exception ee)
                                {}

                                e.printStackTrace();

                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try{
                        pDialog.cancel();
                    }catch (Exception e)
                    {}
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

    public void en_carrera(){
        SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
        int id_pedido=0,id_carrera=0;
        try{
            id_pedido=Integer.parseInt(pedido.getString("id_pedido","0"));
            try{
                id_carrera=Integer.parseInt(pedido.getString("id_carrera","0"));
            }   catch (Exception ee){
                id_carrera=0;
                id_pedido=0;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if(id_pedido!=0)
        {
           if (id_carrera != 0) {
               LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
               ll_pedido.setLayoutParams(parms);
            }
        }
    }

}

