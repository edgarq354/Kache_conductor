package com.elisoft.kache_conductor.preregistro;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;

import com.elisoft.kache_conductor.notificaciones.SharedPrefManager;
import com.elisoft.kache_conductor.Menu_taxi;
import com.elisoft.valle_grande_conductor.R;
import com.elisoft.kache_conductor.Suceso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class Menu_fotos_preregistro extends AppCompatActivity implements View.OnClickListener{

    Button bt_conductor,bt_vehiculo,bt_finalizar;


    String v_direccion_imagen_1="";

    String direccion_imagen_ruat="";
    String direccion_imagen_soat="";
    String v_direccion_imagen_inspeccion_tecnica="";
    String ci="",placa="";


    String direccion_imagen="";
    String direccion_imagen_carnet_1="";
    String direccion_imagen_carnet_2="";
    String direccion_imagen_licencia_1="";
    String direccion_imagen_licencia_2="";

    int cant=0;
    int total=7;

    ProgressDialog pDialog;
    Suceso suceso;
    String imei = "";
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_fotos_preregistro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bt_conductor=(Button)findViewById(R.id.bt_conductor);
        bt_vehiculo=(Button)findViewById(R.id.bt_vehiculo);
        bt_finalizar=(Button)findViewById(R.id.bt_finalizar);

        bt_conductor.setOnClickListener(this);
        bt_vehiculo.setOnClickListener(this);
        bt_finalizar.setOnClickListener(this);

        try{
            Bundle bundle=getIntent().getExtras();
            ci=bundle.getString("ci");
            placa=bundle.getString("placa");
            v_direccion_imagen_1=bundle.getString("direccion_imagen_1");
            direccion_imagen_ruat=bundle.getString("direccion_imagen_ruat");
            direccion_imagen_soat=bundle.getString("direccion_imagen_soat");
            v_direccion_imagen_inspeccion_tecnica=bundle.getString("direccion_imagen_inspeccion_tecnica");



            direccion_imagen=bundle.getString("direccion_imagen");
            direccion_imagen_carnet_1=bundle.getString("direccion_imagen_carnet_1");
            direccion_imagen_licencia_1=bundle.getString("direccion_imagen_licencia_1");

        }catch (Exception e)
        {
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_conductor:
                Intent icon=new Intent(this,Menu_datos_conductor.class);
                icon.putExtra("ci", ci);
                icon.putExtra("direccion_imagen", direccion_imagen);
                icon.putExtra("direccion_imagen_carnet_1", direccion_imagen_carnet_1);
                icon.putExtra("direccion_imagen_licencia_1", direccion_imagen_licencia_1);
                startActivity(icon);
                break;
            case R.id.bt_vehiculo:
                Intent ive=new Intent(this,Menu_datos_vehiculo.class);
                ive.putExtra("placa", placa);
                ive.putExtra("direccion_imagen_1", v_direccion_imagen_1);
                ive.putExtra("direccion_imagen_soat", direccion_imagen_soat);
                ive.putExtra("direccion_imagen_ruat", direccion_imagen_ruat);
                ive.putExtra("direccion_imagen_inspeccion_tecnica", v_direccion_imagen_inspeccion_tecnica);
                startActivity(ive);
                break;
            case R.id.bt_finalizar:
                verificar_todas_las_fotos();
                if(cant==total)
                {
                    mensaje_completado("Gracias por Completar tu registro. Ahora puedes utlizar nuestra aplicacion del conductor y ingresar.");
                }else{
                    mensaje("Aun te falta completar las imagenes.");
                }
                break;
        }
    }

    @Override
    protected void onRestart() {
        verificar_datos();
        super.onRestart();
    }

    public  void verificar_datos()
    {
        Servicio_conductor servicio = new Servicio_conductor();
        servicio.execute(getString(R.string.servidor) + "frmTaxi.php?opcion=get_direccion_conductor", "1");

        Servicio servicio2 = new Servicio();
        servicio2.execute(getString(R.string.servidor) + "frmTaxi.php?opcion=get_direccion_vehiculo", "1");// parametro que recibe el doinbackground


    }

    public class Servicio extends AsyncTask<String,Integer,String> {


        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url = null;  // url donde queremos obtener informacion
            String devuelve = "";
//guardar datos del conductor
            if (params[1] == "1") {
                try {
                    HttpURLConnection urlConn;

                    DataOutputStream printout;
                    DataOutputStream input;

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
                    jsonParam.put("placa", placa);

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

                        //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                        JSONObject respuestaJSON = new JSONObject(result.toString());//Creo un JSONObject a partir del
                        // StringBuilder pasando a cadena.                    }

                        SystemClock.sleep(950);

                        //Accedemos a vector de resultados.
                        String error = respuestaJSON.getString("suceso");// suceso es el campo en el Json


                        if (error.equals("1")) {
                            JSONArray dato=respuestaJSON.getJSONArray("perfil_vehiculo");
                            v_direccion_imagen_1= dato.getJSONObject(0).getString("direccion_imagen_adelante") ;

                            devuelve="1";

                            direccion_imagen_ruat=respuestaJSON.getString("direccion_imagen_ruat");
                            direccion_imagen_soat=respuestaJSON.getString("direccion_imagen_soat");
                            v_direccion_imagen_inspeccion_tecnica=respuestaJSON.getString("direccion_imagen_inspeccion_tecnica");


                        }else
                        {
                            devuelve="2";
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


            return devuelve;
        }


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);



            if (s.equals("1")) {
                verificar_todas_las_fotos();
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


    public class Servicio_conductor extends AsyncTask<String,Integer,String> {


        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url = null;  // url donde queremos obtener informacion
            String devuelve = "";
//verificar si los datos en la base de datos con su cedula de identidad.
            if (params[1] == "1") {
                try {
                    HttpURLConnection urlConn;

                    DataOutputStream printout;
                    DataOutputStream input;

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
                    jsonParam.put("ci",ci);

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

                        //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                        JSONObject respuestaJSON = new JSONObject(result.toString());//Creo un JSONObject a partir del
                        // StringBuilder pasando a cadena.                    }

                        SystemClock.sleep(950);

                        //Accedemos a vector de resultados.
                        String error = respuestaJSON.getString("suceso");// suceso es el campo en el Json


                        if (error.equals("1")) {
                            JSONArray dato=respuestaJSON.getJSONArray("perfil_conductor");

                            direccion_imagen=dato.getJSONObject(0).getString("direccion_imagen");
                            direccion_imagen_carnet_1=respuestaJSON.getString("direccion_imagen_carnet_1");
                            direccion_imagen_licencia_1=respuestaJSON.getString("direccion_imagen_licencia_1");


                            devuelve="1";
                        }  else
                        {
                            devuelve="2";
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


            return devuelve;
        }


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("1")) {
                verificar_todas_las_fotos();
            }
            else
            {

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


    public void verificar_todas_las_fotos()
    {
        cant=0;
        if(direccion_imagen.length()>5)
        {
            cant++;
        }

       if(direccion_imagen_carnet_1.length()>5)
       {
           cant++;
        }

    if(direccion_imagen_licencia_1.length()>5)
    {
        cant++;
    }

        if(v_direccion_imagen_1.length()>5) {
            cant++;
        }
        if(direccion_imagen_soat.length()>5)
        {
            cant++;
        }
        if(direccion_imagen_ruat.length()>5)
        {
            cant++;
        }
        if(v_direccion_imagen_inspeccion_tecnica.length()>5) {
            cant++;
        }

    }

    public void mensaje(String mensaje)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Importante");
        builder.setMessage(mensaje);
        builder.setPositiveButton("OK", null);
        builder.create();
        builder.show();
    }

    public void mensaje_completado(String mensaje)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Importante");
        builder.setMessage(mensaje);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String token = SharedPrefManager.getInstance(getApplicationContext()).getDeviceToken();

                if (token != null || token == "") {
                     Servicio_taxi servicio = new Servicio_taxi();
                    servicio.execute(getString(R.string.servidor) + "frmTaxi.php?opcion=iniciar_sesion", "1", ci,ci, token, imei);// parametro que recibe el doinbackground
                } else {
                    mensaje("No se a podido generar el Token. porfavor active sus datos de Red e instale Google Pay Service");
                }
            }
        });
        builder.create();
        builder.show();
    }






    //inicio de sesion
    public class Servicio_taxi extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url = null;  // url donde queremos obtener informacion
            String devuelve = "";
//Iniciar sesion
            if (params[1] == "1") {
                try {
                    HttpURLConnection urlConn;

                    DataOutputStream printout;
                    DataOutputStream input;

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
                    jsonParam.put("usuario", params[2]);
                    jsonParam.put("contrasenia", params[3]);
                    jsonParam.put("token", params[4]);
                    jsonParam.put("imei", params[5]);

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

                        //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                        JSONObject respuestaJSON = new JSONObject(result.toString());//Creo un JSONObject a partir del
                        // StringBuilder pasando a cadena.                    }

                        SystemClock.sleep(950);

                        //Accedemos a vector de resultados.
                        String error = respuestaJSON.getString("suceso");// suceso es el campo en el Json
                        String mensaje = respuestaJSON.getString("mensaje");
                        suceso = new Suceso(error, mensaje);

                        if (error.equals("1")) {
                            JSONArray dato = respuestaJSON.getJSONArray("perfil");
                            String snombre = dato.getJSONObject(0).getString("nombre");
                            String spaterno = dato.getJSONObject(0).getString("paterno");
                            String smaterno = dato.getJSONObject(0).getString("materno");
                            String semail = dato.getJSONObject(0).getString("correo");
                            String scodigo = dato.getJSONObject(0).getString("usuario");
                            String scelular = dato.getJSONObject(0).getString("celular");
                            String sid = dato.getJSONObject(0).getString("ci");
                            String marca = dato.getJSONObject(0).getString("marca");
                            String modelo = dato.getJSONObject(0).getString("modelo");
                            String placa = dato.getJSONObject(0).getString("placa");
                            String color = dato.getJSONObject(0).getString("color");
                            String estado = dato.getJSONObject(0).getString("estado");
                            String credito = dato.getJSONObject(0).getString("credito");
                            String login = dato.getJSONObject(0).getString("login");
                            String tipo = dato.getJSONObject(0).getString("tipo");
                            String ci = dato.getJSONObject(0).getString("ci");
                            String latitud = dato.getJSONObject(0).getString("latitud");
                            String longitud = dato.getJSONObject(0).getString("longitud");
                            String id_empresa = dato.getJSONObject(0).getString("id_empresa");

                            cargar_datos_m(snombre, spaterno, smaterno, semail, scelular, sid, marca, modelo, placa, color, estado, credito, login, tipo, ci, scodigo, id_empresa,latitud,longitud);

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
            return devuelve;
        }


        @Override
        protected void onPreExecute() {
            //para el progres Dialog
            pDialog = new ProgressDialog(Menu_fotos_preregistro.this);
            pDialog.setTitle(getString(R.string.app_name));
            pDialog.setMessage("Autenticando ....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.cancel();//ocultamos proggress dialog
            //  Log.e("onPostExcute=", "" + s);

            if (s.equals("1")) {
                iniciar_sesion_taxi();
            } else if (s.equals("2")) {
                mensaje(suceso.getMensaje());

            } else {
                mensaje("Error: Al conectar con el Servidor.");
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

    private void iniciar_sesion_taxi() {
        startActivity(new Intent(getApplicationContext(), Menu_taxi.class));
        finish();
    }


    public boolean verificar_login_taxi() {
        SharedPreferences perfil = getSharedPreferences("perfil_conductor", MODE_PRIVATE);
        return (perfil.getString("login_taxi", "").equals("1"));

    }


    public void cargar_datos_m(String nombre, String paterno, String materno, String email, String celular, String id, String marca, String modelo, String placa, String color, String estado, String credito, String login, String tipo_taxi, String ci, String codigo, String id_empresa,String latitud, String longitud) {
        SharedPreferences usuario = getSharedPreferences("perfil_conductor", MODE_PRIVATE);
        SharedPreferences.Editor editar = usuario.edit();
        editar.putString("nombre", nombre);
        editar.putString("paterno", paterno);
        editar.putString("materno", materno);
        editar.putString("email", email);
        editar.putString("celular", celular);
        editar.putString("ci", id);
        editar.putString("marca", marca);
        editar.putString("modelo", modelo);
        editar.putString("placa", placa);
        editar.putString("color", color);
        editar.putString("estado", estado);
        editar.putString("credito", credito);
        editar.putString("login", login);
        editar.putString("tipo_taxi", tipo_taxi);
        editar.putString("ci", ci);
        editar.putString("codigo", codigo);
        editar.putString("login_taxi", "1");
        editar.putString("id_empresa", id_empresa);
        editar.putString("imei", imei);
        editar.putString("latitud_domicilio", latitud);
        editar.putString("longitud_domicilio", longitud);
        editar.commit();

    }



}
