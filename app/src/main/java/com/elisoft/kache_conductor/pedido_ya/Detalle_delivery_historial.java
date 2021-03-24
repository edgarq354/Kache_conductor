package com.elisoft.kache_conductor.pedido_ya;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.elisoft.kache_conductor.detalle_pedido_carrito.CCarrito;
import com.elisoft.kache_conductor.detalle_pedido_carrito.Item_carrito;
import com.elisoft.valle_grande_conductor.R;
import com.elisoft.kache_conductor.Suceso;
import com.elisoft.kache_conductor.carreras.Carreras;
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
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class Detalle_delivery_historial extends AppCompatActivity  implements View.OnClickListener{

    ListView lista;
    ArrayList<CCarrito> carrito;
    Suceso suceso;

    ProgressDialog pDialog;

    ImageView im_perfil;
    TextView tv_nombre,tv_nit,tv_fecha,tv_direccion,tv_monto_carrito,tv_estado,tv_monto_envio,tv_pasajero;

    String razon_social,nit,fecha,direccion,direccion_imagen,monto_carrito;
    int id_usuario=0;
    int id_pedido = 0,estado_pedido=0;
    String id_vehiculo="",id_conductor="",monto_envio="";

    ImageButton ib_recorrido;


    @Override
    protected void onRestart() {
        actualizar();
        super.onRestart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_delivery_historial);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        lista = (ListView) findViewById(R.id.lista);


        im_perfil=(ImageView) findViewById(R.id.im_perfil);
        tv_nombre=(TextView) findViewById(R.id.tv_nombre);
        tv_pasajero=(TextView) findViewById(R.id.tv_pasajero);
        tv_nit=(TextView) findViewById(R.id.tv_nit);
        tv_fecha=(TextView) findViewById(R.id.tv_fecha);
        tv_estado=(TextView) findViewById(R.id.tv_estado);
        tv_direccion=(TextView) findViewById(R.id.tv_direccion);
        tv_monto_carrito=(TextView) findViewById(R.id.tv_monto_carrito);
        tv_monto_envio=(TextView) findViewById(R.id.tv_monto_envio);
        ib_recorrido=(ImageButton)findViewById(R.id.ib_recorrido);






        try {
            Bundle bundle = getIntent().getExtras();

            id_pedido = bundle.getInt("id_pedido");

        } catch (Exception e) {
            finish();
        }
        actualizar();

        ib_recorrido.setOnClickListener(this);


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }


    private void actualizar() {

        Servicio servicio = new  Servicio();
        servicio.execute(getString(R.string.servidor) + "frmPedido.php?opcion=get_delivery_proceso_detalle_por_id", "1", String.valueOf(id_pedido));// parametro que recibe el doinbackground

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


        switch (v.getId())
        {
            case R.id.ib_recorrido:
                Intent i = new Intent(this, Carreras.class);
                i.putExtra("id_pedido", String.valueOf(id_pedido));
                i.putExtra("id_vehiculo", String.valueOf(id_vehiculo));
                startActivity(i);
                break;



        }
    }




    // comenzar el servicio los carritos ....
    public class Servicio extends AsyncTask<String, Integer, String> {

String pasajero="";
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
                                    pasajero=jpedido.getJSONObject(i).getString("nombre")+" "+jpedido.getJSONObject(i).getString("apellido");
                                    nit=jpedido.getJSONObject(i).getString("nit");
                                    fecha=jpedido.getJSONObject(i).getString("fecha_pedido");
                                    direccion=jpedido.getJSONObject(i).getString("direccion_empresa");
                                    direccion_imagen=jpedido.getJSONObject(i).getString("direccion_logo");
                                    monto_carrito= jpedido.getJSONObject(i).getString("monto_pedido");
                                    monto_envio= jpedido.getJSONObject(i).getString("monto_total");
                                    id_vehiculo= jpedido.getJSONObject(i).getString("id_vehiculo");
                                    id_conductor= jpedido.getJSONObject(i).getString("id_conductor");



                                }

                                for (int i = 0; i < jcarrito.length(); i++) {

                                    int id_producto=Integer.parseInt(jcarrito.getJSONObject(i).getString("id_producto"));
                                    int cantidad=Integer.parseInt(jcarrito.getJSONObject(i).getString("cantidad"));
                                    String nombre=jcarrito.getJSONObject(i).getString("nombre");
                                    String descripcion=jcarrito.getJSONObject(i).getString("descripcion");
                                    String imagen1=jcarrito.getJSONObject(i).getString("imagen1");
                                    double monto_unidad=Double.parseDouble(jcarrito.getJSONObject(i).getString("monto_unidad"));
                                    double monto_total=Double.parseDouble(jcarrito.getJSONObject(i).getString("monto_total"));


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
                pDialog = new ProgressDialog(Detalle_delivery_historial.this);
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
                Item_carrito adaptador = new Item_carrito(Detalle_delivery_historial.this,Detalle_delivery_historial.this,carrito);
                lista.setAdapter(adaptador);
                tv_nombre.setText(razon_social);
                tv_nit.setText("Nit:"+nit);
                tv_fecha.setText(fecha);
                tv_direccion.setText("Dirección:"+direccion);
                tv_monto_carrito.setText("Total de carrito: "+monto_carrito+" Bs");
                tv_monto_carrito.setText("Monto por envio: "+monto_envio+" Bs");
                tv_pasajero.setText(pasajero);


                String url = getString(R.string.servidor_web)+"storage/" + direccion_imagen;
                Picasso.with(Detalle_delivery_historial.this).load(url).placeholder(R.mipmap.ic_empresa).into(im_perfil);


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


}

