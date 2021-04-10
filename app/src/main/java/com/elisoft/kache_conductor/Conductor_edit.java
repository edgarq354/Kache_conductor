package com.elisoft.kache_conductor;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.elisoft.kache_conductor.SqLite.AdminSQLiteOpenHelper;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Conductor_edit extends AppCompatActivity implements View.OnClickListener {

    EditText et_email, et_imei, et_nro_ham, et_nro_transito, et_nro_factura_luz, et_nro_antecedente_transito, et_nro_antecedente_felcc;
    TextView tv_placa, tv_cedula;
    FloatingActionButton editar_email;
    ImageView perfil;
    TextView credito, tv_cal_conductor, tv_cal_vehiculo;
    ImageButton ib_guardar;
    LinearLayout ll_placa, ll_cedula;
    Suceso suceso;
    ProgressDialog pDialog;

    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;
    private String mPath;
    private Uri path;


    TableRow tr_ci;
    TableRow tr_licencia, tr_ham, tr_identificacion_transito, tr_factura_luz, tr_antecedente_transito, tr_antecedente_felcc;
    FloatingActionButton im_foto_ci, im_foto_licencia, im_foto_ham, im_foto_identificacion_transito, im_foto_factura_luz, im_foto_antecedente_transito, im_foto_antecedente_felcc;
    String opcion_subir_imagen = "ninguno";

    Bitmap bitmap_aux = null;

    private File file;
    String id_foto = "", id_fotocopia = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conductor_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        et_email = (EditText) findViewById(R.id.et_email);
        et_imei = (EditText) findViewById(R.id.et_imei);
        et_nro_ham = (EditText) findViewById(R.id.et_nro_ham);
        et_nro_transito = (EditText) findViewById(R.id.et_nro_transito);
        et_nro_factura_luz = (EditText) findViewById(R.id.et_nro_factura_luz);
        et_nro_antecedente_transito = (EditText) findViewById(R.id.et_nro_antecedente_transito);
        et_nro_antecedente_felcc = (EditText) findViewById(R.id.et_nro_antecedente_felcc);

        ib_guardar = (ImageButton) findViewById(R.id.ib_guardar);

        tr_ci = (TableRow) findViewById(R.id.tr_ci);
        tr_licencia = (TableRow) findViewById(R.id.tr_licencia);
        tr_ham = (TableRow) findViewById(R.id.tr_ham);
        tr_identificacion_transito = (TableRow) findViewById(R.id.tr_identificacion_transito);
        tr_factura_luz = (TableRow) findViewById(R.id.tr_factura_luz);
        tr_antecedente_transito = (TableRow) findViewById(R.id.tr_antecedente_transito);
        tr_antecedente_felcc = (TableRow) findViewById(R.id.tr_antecedente_felcc);

        im_foto_ci = (FloatingActionButton) findViewById(R.id.im_foto_ci);
        im_foto_licencia = (FloatingActionButton) findViewById(R.id.im_foto_licencia);
        im_foto_ham = (FloatingActionButton) findViewById(R.id.im_foto_ham);
        im_foto_identificacion_transito = (FloatingActionButton) findViewById(R.id.im_foto_identificacion_transito);
        im_foto_factura_luz = (FloatingActionButton) findViewById(R.id.im_foto_factura_luz);
        im_foto_antecedente_transito = (FloatingActionButton) findViewById(R.id.im_foto_antecedente_transito);
        im_foto_antecedente_felcc = (FloatingActionButton) findViewById(R.id.im_foto_antecedente_felcc);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        im_foto_ci.setOnClickListener(this);
        im_foto_licencia.setOnClickListener(this);
        im_foto_ham.setOnClickListener(this);
        im_foto_identificacion_transito.setOnClickListener(this);
        im_foto_factura_luz.setOnClickListener(this);
        im_foto_antecedente_transito.setOnClickListener(this);
        im_foto_antecedente_felcc.setOnClickListener(this);
        ib_guardar.setOnClickListener(this);

        mostrar_foto();
        mostrar_conductor();


        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        et_imei.setText(telephonyManager.getDeviceId());

    }


    public void mostrar_conductor()
    {
        SharedPreferences sperfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);
        SharedPreferences conductor=getSharedPreferences("conductor",MODE_PRIVATE);
        et_email.setText(conductor.getString("email",""));
        et_nro_ham.setText(conductor.getString("nro_ham",""));
        et_nro_transito.setText(conductor.getString("nro_transito",""));
        et_nro_factura_luz.setText(conductor.getString("nro_factura_luz",""));
        et_nro_antecedente_transito.setText(conductor.getString("nro_antecedente_transito",""));
        et_nro_antecedente_felcc.setText(conductor.getString("nro_antecedente_felcc",""));

        Servicio_actualizar hilo = new Servicio_actualizar();

        hilo.execute(getString(R.string.servidor) + "frmTaxi.php?opcion=get_conductor_por_ci", "2",sperfil.getString("ci",""));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View v) {
        opcion_subir_imagen="";
        if(v.getId()== R.id.im_foto_ci){
            opcion_subir_imagen="CI";
        } else if(v.getId()== R.id.im_foto_licencia){
            opcion_subir_imagen="LICENCIA";
        }else if(v.getId()== R.id.im_foto_ham){
            opcion_subir_imagen="HAM";
        }else if(v.getId()== R.id.im_foto_identificacion_transito){
            opcion_subir_imagen="IDENTIFICACION-TRANSITO";
        }else if(v.getId()== R.id.im_foto_factura_luz){
            opcion_subir_imagen="FACTURA-LUZ";
        }else if(v.getId()== R.id.im_foto_antecedente_transito){
            opcion_subir_imagen="ANTECEDENTE-TRANSITO";
        }else if(v.getId()== R.id.im_foto_antecedente_felcc){
            opcion_subir_imagen="ANTECEDENTE-FELCC";
        }else if(v.getId()== R.id.ib_guardar)
        {
            SharedPreferences sperfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);
            Servicio_actualizar hilo = new Servicio_actualizar();
            hilo.execute(getString(R.string.servidor) + "frmTaxi.php?opcion=actualizar_datos_conductor", "1",sperfil.getString("ci",""),et_email.getText().toString(),et_imei.getText().toString(),et_nro_ham.getText().toString(),et_nro_transito.getText().toString(),et_nro_factura_luz.getText().toString(),et_nro_antecedente_transito.getText().toString(),et_nro_antecedente_felcc.getText().toString());// parametro que recibe el doinbackground

         }


        if(opcion_subir_imagen!="")
      {
          final CharSequence[] options={"Tomar foto","Elegir de galeria","Cancelar"};
          final AlertDialog.Builder builder= new AlertDialog.Builder(this);
          builder.setTitle("Elige una opcion");
          builder.setItems(options, new DialogInterface.OnClickListener(){

              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                  if(options[i]=="Tomar foto")
                  {
                      openCamara();
                  }else if(options[i]=="Elegir de galeria")
                  {
                      Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                      intent.setType("image/*");
                      startActivityForResult(intent.createChooser(intent,"Seleccione app de imagen"),SELECT_PICTURE);

                  }else if (options[i]=="Cancelar")
                  {
                      dialogInterface.dismiss();
                  }
              }
          });
          builder.show();
      }
    }


    // comenzar el servicio con el motista....
    public class Servicio_actualizar extends AsyncTask<String,Integer,String> {
        String devuelve = "";

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url = null;  // url donde queremos obtener informacion



//Insertar nueva imagen de perfil en servico por parte del taxi..

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
                    jsonParam.put("ci", params[2]);
                    jsonParam.put("email", params[3]);
                    jsonParam.put("imei", params[4]);
                    jsonParam.put("nro_ham", params[5]);
                    jsonParam.put("nro_transito", params[6]);
                    jsonParam.put("nro_factura_luz", params[7]);
                    jsonParam.put("nro_antecedente_transito", params[8]);
                    jsonParam.put("nro_antecedente_felcc", params[9]);
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
                        suceso=new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));
                        if (suceso.getSuceso().equals("1")) {
                            devuelve="1";
                        } else  {
                            devuelve = "500";
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

            if (params[1] == "2") {
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
                    jsonParam.put("ci", params[2]);
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
                        suceso=new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));
                        if (suceso.getSuceso().equals("1")) {
                            SharedPreferences vehiculo=getSharedPreferences("conductor",MODE_PRIVATE);
                            SharedPreferences.Editor editor=vehiculo.edit();
                            editor.putString("ci",respuestaJSON.getString("ci"));
                            editor.putString("email",respuestaJSON.getString("email"));
                            editor.putString("nro_ham",respuestaJSON.getString("nro_ham"));
                            editor.putString("nro_transito",respuestaJSON.getString("nro_transito"));
                            editor.putString("nro_factura_luz",respuestaJSON.getString("nro_factura_luz"));
                            editor.putString("nro_antecedente_transito",respuestaJSON.getString("nro_antecedente_transito"));
                            editor.putString("nro_antecedente_felcc",respuestaJSON.getString("nro_antecedente_felcc"));
                            editor.commit();

                            devuelve="2";
                        } else  {
                            devuelve = "500";
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
            pDialog = new ProgressDialog(Conductor_edit.this);
            pDialog.setTitle(getString(R.string.app_name));
            pDialog.setMessage("Sincronizando ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();//ocultamos proggress dialog
            // Log.e("onPostExcute=", "" + s);
            if(s.equals("1"))
            {//CEDULA
                mensaje(suceso.getMensaje());
            }else if(s.equals("2")){
                SharedPreferences conductor=getSharedPreferences("conductor",MODE_PRIVATE);
                et_email.setText(conductor.getString("email",""));
                et_nro_ham.setText(conductor.getString("nro_ham",""));
                et_nro_transito.setText(conductor.getString("nro_transito",""));
                et_nro_factura_luz.setText(conductor.getString("nro_factura_luz",""));
                et_nro_antecedente_transito.setText(conductor.getString("nro_antecedente_transito",""));
                et_nro_antecedente_felcc.setText(conductor.getString("nro_antecedente_felcc",""));

            }else if(s.equals("500"))
            {
                mensaje(suceso.getMensaje());
            }
            else
            {

                mensaje_error("No pudimos conectarnos al servidor.\nVuelve a intentarlo.");
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


    // comenzar el servicio con el motista....
    public class Servicio extends AsyncTask<String,Integer,String> {
        String devuelve = "",id_foto="",id_fotocopia="";

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url = null;  // url donde queremos obtener informacion



//Insertar nueva imagen de perfil en servico por parte del taxi..

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
                    jsonParam.put("ci", params[2]);
                    jsonParam.put("imagen", params[3]);
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
                        suceso=new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));
                        if (suceso.getSuceso().equals("1")) {
                            id_foto=respuestaJSON.getString("id_foto");
                            id_fotocopia=respuestaJSON.getString("id_fotocopia");
                            devuelve="1";
                        } else  {
                            devuelve = "500";
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

            if (params[1] == "2") {
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
                    jsonParam.put("ci", params[2]);
                    jsonParam.put("imagen", params[3]);
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
                        suceso=new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));
                        if (suceso.getSuceso().equals("1")) {
                            id_foto=respuestaJSON.getString("id_foto");
                            id_fotocopia=respuestaJSON.getString("id_fotocopia");
                            devuelve="2";
                        } else  {
                            devuelve = "500";
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

            if (params[1] == "3") {
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
                    jsonParam.put("ci", params[2]);
                    jsonParam.put("imagen", params[3]);
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
                        suceso=new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));
                        if (suceso.getSuceso().equals("1")) {
                            id_foto=respuestaJSON.getString("id_foto");
                            id_fotocopia=respuestaJSON.getString("id_fotocopia");
                            devuelve="3";
                        } else  {
                            devuelve = "500";
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

            if (params[1] == "4") {
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
                    jsonParam.put("ci", params[2]);
                    jsonParam.put("imagen", params[3]);
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
                        suceso=new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));
                        if (suceso.getSuceso().equals("1")) {
                            id_foto=respuestaJSON.getString("id_foto");
                            id_fotocopia=respuestaJSON.getString("id_fotocopia");
                            devuelve="4";
                        } else  {
                            devuelve = "500";
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

            if (params[1] == "5") {
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
                    jsonParam.put("ci", params[2]);
                    jsonParam.put("imagen", params[3]);
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
                        suceso=new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));
                        if (suceso.getSuceso().equals("1")) {
                            id_foto=respuestaJSON.getString("id_foto");
                            id_fotocopia=respuestaJSON.getString("id_fotocopia");
                            devuelve="5";
                        } else  {
                            devuelve = "500";
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

            if (params[1] == "6") {
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
                    jsonParam.put("ci", params[2]);
                    jsonParam.put("imagen", params[3]);
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
                        suceso=new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));
                        if (suceso.getSuceso().equals("1")) {
                            id_foto=respuestaJSON.getString("id_foto");
                            id_fotocopia=respuestaJSON.getString("id_fotocopia");
                            devuelve="6";
                        } else  {
                            devuelve = "500";
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
            if (params[1] == "7") {
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
                    jsonParam.put("ci", params[2]);
                    jsonParam.put("imagen", params[3]);
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
                        suceso=new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));
                        if (suceso.getSuceso().equals("1")) {
                            id_foto=respuestaJSON.getString("id_foto");
                            id_fotocopia=respuestaJSON.getString("id_fotocopia");
                            devuelve="7";
                        } else  {
                            devuelve = "500";
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
            pDialog = new ProgressDialog(Conductor_edit.this);
            pDialog.setTitle(getString(R.string.app_name));
            pDialog.setMessage("Insertando la Imagen ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();//ocultamos proggress dialog
            // Log.e("onPostExcute=", "" + s);
            if(s.equals("1"))
            {//CEDULA
                mensaje(suceso.getMensaje());
                agregar_imagen(bitmap_aux,tr_ci,id_foto,"CI",id_fotocopia);
            }else if(s.equals("2"))
            {//LICENCIA
                mensaje(suceso.getMensaje());
                agregar_imagen(bitmap_aux,tr_licencia,id_foto,"LICENCIA",id_fotocopia);
            } else if(s.equals("3"))
            {//IDENTIFICACION HAMM
                mensaje(suceso.getMensaje());
                agregar_imagen(bitmap_aux,tr_ham,id_foto,"HAM",id_fotocopia);
            } else if(s.equals("4"))
            {//IDENTIFICACION HAMM
                mensaje(suceso.getMensaje());
                agregar_imagen(bitmap_aux,tr_identificacion_transito,id_foto,"IDENTIFICACION-TRANSITO",id_fotocopia);
            } else if(s.equals("5"))
            {//IDENTIFICACION HAMM
                mensaje(suceso.getMensaje());
                agregar_imagen(bitmap_aux,tr_factura_luz,id_foto,"FACTURA-LUZ",id_fotocopia);
            } else if(s.equals("6"))
            {//IDENTIFICACION HAMM
                mensaje(suceso.getMensaje());
                agregar_imagen(bitmap_aux,tr_antecedente_transito,id_foto,"ANTECEDENTE-TRANSITO",id_fotocopia);
            } else if(s.equals("7"))
            {//IDENTIFICACION HAMM
                mensaje(suceso.getMensaje());
                agregar_imagen(bitmap_aux,tr_antecedente_felcc,id_foto,"ANTECEDENTE-FELCC",id_fotocopia);
            }
            else if(s.equals("500"))
            {
                mensaje(suceso.getMensaje());
            }
            else
            {

                mensaje_error("No pudimos conectarnos al servidor.\nVuelve a intentarlo.");
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



    private void guardar_en_memoria(Bitmap bitmapImage,String nombre,String tipo,int id_foto,int id_fotocopia)
    {
        File file=null;
        FileOutputStream fos = null;
        try {
            SharedPreferences perfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);
            String APP_DIRECTORY = "Taxi La Carroza Conductor/";//nombre de directorio
            String MEDIA_DIRECTORY = APP_DIRECTORY + "Imagen/";//nombre de la carpeta
            MEDIA_DIRECTORY = MEDIA_DIRECTORY + tipo;//nombre de la carpeta
            file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
            File mypath=new File(file,perfil.getString("ci","")+nombre+".jpg");//nombre del archivo imagen

            //guardar la direccion de la imagen en memoria
            cargar_foto(id_foto,id_fotocopia,tipo,MEDIA_DIRECTORY+"/"+perfil.getString("ci","")+nombre+".jpg");

            boolean isDirectoryCreated = file.exists();//pregunto si esxiste el directorio creado
            if(!isDirectoryCreated)
                isDirectoryCreated = file.mkdirs();

            if(isDirectoryCreated) {
                fos = new FileOutputStream(mypath);
                // Use the compress method on the BitMap object to write image to the OutputStream
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            //ubicacion de la imagen
            SharedPreferences sperfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);
            Bitmap img_cargar;
             Servicio hilo ;
            String uploadImage = "";

            switch (requestCode){
                case PHOTO_CODE:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    //Convertir MPath a Bitmap
                    Bitmap bitmap = BitmapFactory.decodeFile(mPath);

                    file = new File(mPath);

                    img_cargar=bitmap;
                    img_cargar=ReducirImagen_b(bitmap,1000,1000);
                    //uploadImage = getStringImage(img_cargar);
                    subir_imagen_servidor(opcion_subir_imagen,uploadImage,bitmap);

                    break;
                case SELECT_PICTURE:
                    path = data.getData();
                    try {//convertir Uri a BitMap
                        Bitmap tempBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(path));

                        guardar_imagen_temp(tempBitmap);
                        img_cargar=tempBitmap;
                        img_cargar=ReducirImagen_b(tempBitmap,1000,1000);
                        //uploadImage = getStringImage(img_cargar);
                        subir_imagen_servidor(opcion_subir_imagen,uploadImage,tempBitmap);

                    }
                    catch (Exception e)
                    {

                    }
                    break;

            }
        }

    }

    private void subir_imagen_servidor(String opcion_subir_imagen, String uploadImage, Bitmap bitmap) {

        SharedPreferences sperfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);
       // Servicio hilo_imagen = new Servicio();
        //Convertir Bitmap a Drawable.
        Drawable dw = new BitmapDrawable(getResources(), bitmap);
        bitmap_aux=bitmap;

        switch (opcion_subir_imagen)
        {
           case "CI":
               serverUpdate(getString(R.string.servidor) + "frmTaxi.php?opcion=insertar_imagen_cedula",opcion_subir_imagen,sperfil.getString("ci",""));
               //hilo_imagen.execute(getString(R.string.servidor) + "frmUsuario.php?opcion=insertar_imagen_cedula", "1",sperfil.getString("ci",""),uploadImage);// parametro que recibe el doinbackground
               break;
           case "LICENCIA":
               serverUpdate(getString(R.string.servidor) + "frmTaxi.php?opcion=insertar_imagen_licencia",opcion_subir_imagen,sperfil.getString("ci",""));
              // hilo_imagen.execute(getString(R.string.servidor) + "frmUsuario.php?opcion=insertar_imagen_licencia", "2",sperfil.getString("ci",""),uploadImage);// parametro que recibe el doinbackground
               break;
           case "HAM":
               serverUpdate(getString(R.string.servidor) + "frmTaxi.php?opcion=insertar_imagen_identificacion_ham",opcion_subir_imagen,sperfil.getString("ci",""));
               //hilo_imagen.execute(getString(R.string.servidor) + "frmUsuario.php?opcion=insertar_imagen_identificacion_ham", "3",sperfil.getString("ci",""),uploadImage);// parametro que recibe el doinbackground
               break;
           case "IDENTIFICACION-TRANSITO":
               serverUpdate(getString(R.string.servidor) + "frmTaxi.php?opcion=insertar_imagen_identificacion_transito",opcion_subir_imagen,sperfil.getString("ci",""));
              // hilo_imagen.execute(getString(R.string.servidor) + "frmUsuario.php?opcion=insertar_imagen_identificacion_transito", "4",sperfil.getString("ci",""),uploadImage);// parametro que recibe el doinbackground
                break;
           case "FACTURA-LUZ":
               serverUpdate(getString(R.string.servidor) + "frmTaxi.php?opcion=insertar_imagen_factura_luz",opcion_subir_imagen,sperfil.getString("ci",""));
              // hilo_imagen.execute(getString(R.string.servidor) + "frmUsuario.php?opcion=insertar_imagen_factura_luz", "5",sperfil.getString("ci",""),uploadImage);// parametro que recibe el doinbackground
               break;
          case "ANTECEDENTE-TRANSITO":
              serverUpdate(getString(R.string.servidor) + "frmTaxi.php?opcion=insertar_imagen_antecedente_transito",opcion_subir_imagen,sperfil.getString("ci",""));
              //hilo_imagen.execute(getString(R.string.servidor) + "frmUsuario.php?opcion=insertar_imagen_antecedente_transito", "6",sperfil.getString("ci",""),uploadImage);// parametro que recibe el doinbackground
              break;
          case "ANTECEDENTE-FELCC":
              serverUpdate(getString(R.string.servidor) + "frmTaxi.php?opcion=insertar_imagen_antecedente_felcc",opcion_subir_imagen,sperfil.getString("ci",""));
             // hilo_imagen.execute(getString(R.string.servidor) + "frmUsuario.php?opcion=insertar_imagen_antecedente_felcc", "7",sperfil.getString("ci",""),uploadImage);// parametro que recibe el doinbackground
              break;

        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("file_path", mPath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mPath = savedInstanceState.getString("file_path");
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100){
            if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permisos aceptados", Toast.LENGTH_SHORT).show();
                perfil.setEnabled(true);
            }
        }else{
            showExplanation();
        }
    }

    public void agregar_imagen(Bitmap id_imagen,TableRow tr_fila,String id_foto,String tipo,String id_fotocopia) {

        guardar_en_memoria(id_imagen,id_foto,tipo,Integer.parseInt(id_foto),Integer.parseInt(id_fotocopia));

        ImageView imagen=new ImageView(Conductor_edit.this);

        tr_fila.addView(imagen);

        ImageView imageView  = new ImageView(this);

        id_imagen=ReducirImagen_b(id_imagen,150,130);
        imageView.setImageBitmap(id_imagen);
        imageView.setPadding(5, 5, 5, 15);
        tr_fila.addView(imageView);
    }

    private void guardar_imagen_temp(Bitmap bitmapImage)
    {
        FileOutputStream fos = null;
        try {
            String MEDIA_DIRECTORY = "Taxi La Carroza Conductor/";//nombre de directorio
            file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
            File mypath=new File(file,"temp.jpg");//nombre del archivo imagen


            boolean isDirectoryCreated = file.exists();//pregunto si esxiste el directorio creado
            if(!isDirectoryCreated)
                isDirectoryCreated = file.mkdirs();

            if(isDirectoryCreated) {
                fos = new FileOutputStream(mypath);
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
            }
            file=mypath;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void openCamara() {
        File file = new File(Environment.getExternalStorageDirectory(), "Taxi La Carroza Conductor/Imagen");
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if(isDirectoryCreated){
            Long timestamp = System.currentTimeMillis() / 1000;
            String imageName = timestamp.toString() + ".jpg";

            SharedPreferences perfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);

            mPath = Environment.getExternalStorageDirectory() + File.separator + "Taxi La Carroza Conductor/Imagen"
                    + File.separator + imageName;

            File newFile = new File(mPath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
            startActivityForResult(intent, PHOTO_CODE);
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Permisos denegados");
        builder.setMessage("Para usar las funciones de la app necesitas aceptar los permisos");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.show();
    }

    public Bitmap imagen_cuadrado(Bitmap originalBitmap)
    {
        if (originalBitmap.getWidth() > originalBitmap.getHeight()){
            originalBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getHeight(), originalBitmap.getHeight());
        }else if (originalBitmap.getWidth() < originalBitmap.getHeight()) {
            originalBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getWidth());
        }
        return originalBitmap;
    }


    public static Bitmap ReducirImagen_b( Bitmap BitmapOrg, int w, int h) {

        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;
        // calculamos el escalado de la imagen destino
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // para poder manipular la imagen
        // debemos crear una matriz
        Matrix matrix = new Matrix();
        // Cambiar el tamaño del mapa de bits
        matrix.postScale(scaleWidth, scaleHeight);
        // volvemos a crear la imagen con los nuevos valores
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width, height, matrix, true);

        return resizedBitmap;
    }

    /*Ahora que se tiene la imagen cargado en mapa de bits.
Vamos a convertir este mapa de bits a cadena de base64
este método es para convertir este mapa de bits a la cadena de base64*/
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void mensaje(String mensaje)
    {
        Toast toast =Toast.makeText(this,mensaje,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    private void cargar_foto( int id,int id_fotocopia, String tipo,String direccion)
    {
        try {
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, getString(R.string.nombre_sql), null, Integer.parseInt(getString(R.string.version_sql)));

            SQLiteDatabase bd = admin.getWritableDatabase();
            ContentValues registro = new ContentValues();
            registro.put("id", String.valueOf(id));
            registro.put("id_fotocopia", String.valueOf(id_fotocopia));
            registro.put("direccion", direccion);
            registro.put("tipo", String.valueOf(tipo));
            bd.insert("foto", null, registro);
            bd.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void mostrar_foto()
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, getString(R.string.nombre_sql), null, Integer.parseInt(getString(R.string.version_sql)));

        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor fila = bd.rawQuery("select id,id_fotocopia,direccion,tipo from foto ", null);

        if (fila.moveToFirst()) {  //si ha devuelto 1 fila, vamos al primero (que es el unico)

            do {

                String id_foto=fila.getString(0);
                String id_fotocopia=fila.getString(1);
                String direccion=fila.getString(2);
                String tipo=fila.getString(3);

                Bitmap bm_imagen=get_imagen_bitmap(direccion);
                switch (tipo)
                {
                    case "CI":
                        agregar_imagen_en_table_row(bm_imagen,tr_ci,id_foto,tipo,id_fotocopia);
                        break;
                    case "LICENCIA":
                        agregar_imagen_en_table_row(bm_imagen,tr_licencia,id_foto,tipo,id_fotocopia);
                        break;
                    case "HAM":
                        agregar_imagen_en_table_row(bm_imagen,tr_ham,id_foto,tipo,id_fotocopia);
                        break;
                    case "IDENTIFICACION-TRANSITO":
                        agregar_imagen_en_table_row(bm_imagen,tr_identificacion_transito,id_foto,tipo,id_fotocopia);
                        break;
                    case "FACTURA-LUZ":
                        agregar_imagen_en_table_row(bm_imagen,tr_factura_luz,id_foto,tipo,id_fotocopia);
                        break;
                    case "ANTECEDENTE-TRANSITO":
                        agregar_imagen_en_table_row(bm_imagen,tr_antecedente_transito,id_foto,tipo,id_fotocopia);
                        break;
                    case "ANTECEDENTE-FELCC":
                        agregar_imagen_en_table_row(bm_imagen,tr_antecedente_felcc,id_foto,tipo,id_fotocopia);
                        break;

                }

            } while(fila.moveToNext());

        } else
        {
            //NO HAY REGISTRO.
        }

        bd.close();
    }

    public void agregar_imagen_en_table_row(Bitmap id_imagen,TableRow tr_fila,String id_foto,String tipo,String id_fotocopia) {


        ImageView imageView  = new ImageView(this);

        id_imagen=ReducirImagen_b(id_imagen,150,130);
        imageView.setImageBitmap(id_imagen);
        imageView.setPadding(5, 5, 5, 15);
        tr_fila.addView(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Conductor_edit.this);
                    dialogo1.setTitle("Editar Imagen");
                    dialogo1.setMessage("Eliminar imagen?");
                    dialogo1.setCancelable(true);
                    dialogo1.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {

                        }
                    });
                    dialogo1.show();

            }
        });
    }

    public Bitmap get_imagen_bitmap(String direccion){
        Bitmap bitmap=null;

        String mPath = Environment.getExternalStorageDirectory() + File.separator + direccion;

        File fichero = new File(mPath);

        if (fichero.exists()) {
            try{
                bitmap = BitmapFactory.decodeFile(mPath);
            }catch (Exception e)
            {
                e.printStackTrace();
                bitmap=null;
            }
        }else {
            bitmap = null;
        }
        return bitmap;
    }






    ///PRUEBA DE INSERTAR IMAGEN

    private String uploadFoto(String url,String tipo,String placa){
        String devuelve="";

        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        HttpPost httppost = new HttpPost(url);

        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        ContentBody foto = new FileBody(file);
        mpEntity.addPart("imagen", foto);


        try {
            mpEntity.addPart("ci", new StringBody(placa));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httppost.setEntity(mpEntity);


        String resultado;
        HttpResponse response;
        try {
            response=httpclient.execute(httppost);
            HttpEntity entity =response.getEntity();

            InputStream inputStream= entity.getContent();
            resultado=convertStreamToString(inputStream);

            JSONObject respuestaJSON = new JSONObject(resultado.toString());//Creo un JSONObject a partir del
            suceso=new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));

            if (suceso.getSuceso().equals("1")) {
                switch (tipo) {
                    case "CI":
                        id_foto = respuestaJSON.getString("id_foto");
                        id_fotocopia = respuestaJSON.getString("id_fotocopia");
                        devuelve = "1";
                        break;
                    case "LICENCIA":
                        id_foto = respuestaJSON.getString("id_foto");
                        id_fotocopia = respuestaJSON.getString("id_fotocopia");
                        devuelve = "2";
                        break;
                    case "HAM":
                        id_foto = respuestaJSON.getString("id_foto");
                        id_fotocopia = respuestaJSON.getString("id_fotocopia");
                        devuelve = "3";
                        break;
                    case "IDENTIFICACION-TRANSITO":
                        id_foto = respuestaJSON.getString("id_foto");
                        id_fotocopia = respuestaJSON.getString("id_fotocopia");
                        devuelve = "4";
                        break;
                    case "FACTURA-LUZ":
                        id_foto = respuestaJSON.getString("id_foto");
                        id_fotocopia = respuestaJSON.getString("id_fotocopia");
                        devuelve = "5";
                        break;
                    case "ANTECEDENTE-TRANSITO":
                        id_foto = respuestaJSON.getString("id_foto");
                        id_fotocopia = respuestaJSON.getString("id_fotocopia");
                        devuelve = "6";
                        break;
                    case "ANTECEDENTE-FELCC":
                        id_foto = respuestaJSON.getString("id_foto");
                        id_fotocopia = respuestaJSON.getString("id_fotocopia");
                        devuelve = "7";
                        break;
                    default:
                        devuelve = "500";
                        break;
                }

            }else{

            }

            httpclient.getConnectionManager().shutdown();


            return devuelve;
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return devuelve;
    }

    class ServerUpdate extends AsyncTask<String,String,String>{

        ProgressDialog pDialog;
        String resultado="",tipo="";
        @Override
        protected String doInBackground(String... arg0) {
            resultado=uploadFoto(arg0[0],arg0[1],arg0[2]);
            tipo=arg0[1];


            if(suceso.getSuceso().equals("1"))
                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {

                        // TODO Auto-generated method stub
                        Toast.makeText(Conductor_edit.this, suceso.getMensaje(),Toast.LENGTH_LONG).show();
                    }
                });
            else
                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        AlertDialog.Builder builder = new AlertDialog.Builder(Conductor_edit.this);
                        builder.setTitle("Importante");
                        builder.setMessage(suceso.getMensaje());
                        builder.setPositiveButton("OK", null);
                        builder.create();
                        builder.show();
                    }
                });
            return null;
        }
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Conductor_edit.this);
            pDialog.setMessage("Actualizando Servidor, espere..." );
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();

            if(resultado.equals("1"))
            {//CEDULA
                mensaje(suceso.getMensaje());
                agregar_imagen(bitmap_aux,tr_ci,id_foto,"CI",id_fotocopia);
            }else if(resultado.equals("2"))
            {//LICENCIA
                mensaje(suceso.getMensaje());
                agregar_imagen(bitmap_aux,tr_licencia,id_foto,"LICENCIA",id_fotocopia);
            } else if(resultado.equals("3"))
            {//IDENTIFICACION HAMM
                mensaje(suceso.getMensaje());
                agregar_imagen(bitmap_aux,tr_ham,id_foto,"HAM",id_fotocopia);
            } else if(resultado.equals("4"))
            {//IDENTIFICACION HAMM
                mensaje(suceso.getMensaje());
                agregar_imagen(bitmap_aux,tr_identificacion_transito,id_foto,"IDENTIFICACION-TRANSITO",id_fotocopia);
            } else if(resultado.equals("5"))
            {//IDENTIFICACION HAMM
                mensaje(suceso.getMensaje());
                agregar_imagen(bitmap_aux,tr_factura_luz,id_foto,"FACTURA-LUZ",id_fotocopia);
            } else if(resultado.equals("6"))
            {//IDENTIFICACION HAMM
                mensaje(suceso.getMensaje());
                agregar_imagen(bitmap_aux,tr_antecedente_transito,id_foto,"ANTECEDENTE-TRANSITO",id_fotocopia);
            } else if(resultado.equals("7"))
            {//IDENTIFICACION HAMM
                mensaje(suceso.getMensaje());
                agregar_imagen(bitmap_aux,tr_antecedente_felcc,id_foto,"ANTECEDENTE-FELCC",id_fotocopia);
            }
            else if(resultado.equals("500"))
            {
                mensaje(suceso.getMensaje());
            }
            else
            {

                mensaje_error("No pudimos conectarnos al servidor.\nVuelve a intentarlo.");
            }


        }

    }

    private void serverUpdate(String url,String tipo,String placa){
        if (file.exists()){
            new Conductor_edit.ServerUpdate().execute(url,tipo,placa);
        }
        else
        {
            Log.e("Imagen","No se pudo localizar la imagen");
        }
    }

    public String convertStreamToString(InputStream is) throws IOException{
        if(is!=null)
        {
            StringBuilder sb= new StringBuilder();
            String line;
            try{
                BufferedReader reader=new BufferedReader(new InputStreamReader(is,"UTF-8"));
                while ((line=reader.readLine())!=null){
                    sb.append(line).append("\n");
                }

            }finally {
                is.close();
            }
            return  sb.toString();
        }else {
            return "";
        }
    }




    public void verificar_permiso_camara()
    {
        final String[] CAMERA_PERMISSIONS = { Manifest.permission.INTERNET,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_NETWORK_STATE };

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            //YA LO CANCELE Y VOUELVO A PERDIR EL PERMISO.

            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Atención!");
            dialogo1.setMessage("Debes otorgar permisos de acceso a CAMARA.");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Solicitar permiso", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    dialogo1.cancel();
                    ActivityCompat.requestPermissions(Conductor_edit.this,
                            CAMERA_PERMISSIONS,
                            1);

                }
            });
            dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    dialogo1.cancel();

                }
            });
            dialogo1.show();
        } else {
            ActivityCompat.requestPermissions(Conductor_edit.this,
                    CAMERA_PERMISSIONS,
                    1);
        }
    }

    public void verificar_permiso_almacenamiento()
    {
        final String[] PERMISSIONS = { Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_NETWORK_STATE };

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //YA LO CANCELE Y VOUELVO A PERDIR EL PERMISO.

            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Atención!");
            dialogo1.setMessage("Debes otorgar permisos de acceso a ALMACENAMIENTO.");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Solicitar permiso", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    dialogo1.cancel();
                    ActivityCompat.requestPermissions(Conductor_edit.this,
                            PERMISSIONS,
                            1);

                }
            });
            dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    dialogo1.cancel();

                }
            });
            dialogo1.show();
        } else {
            ActivityCompat.requestPermissions(Conductor_edit.this,
                    PERMISSIONS,
                    1);
        }
    }


    public void verificar_permiso_llamada()
    {
        final String[] PERMISSIONS = { Manifest.permission.INTERNET,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.ACCESS_NETWORK_STATE };

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
            //YA LO CANCELE Y VOUELVO A PERDIR EL PERMISO.

            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Atención!");
            dialogo1.setMessage("Debes otorgar permisos de acceso a LLAMADA.");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Solicitar permiso", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    dialogo1.cancel();
                    ActivityCompat.requestPermissions(Conductor_edit.this,
                            PERMISSIONS,
                            1);

                }
            });
            dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    dialogo1.cancel();

                }
            });
            dialogo1.show();
        } else {
            ActivityCompat.requestPermissions(Conductor_edit.this,
                    PERMISSIONS,
                    1);
        }
    }

    public void verificar_permiso_imei()
    {
        final String[] PERMISSIONS = { Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE };

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
            //YA LO CANCELE Y VOUELVO A PERDIR EL PERMISO.

            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Atención!");
            dialogo1.setMessage("Debes otorgar permisos de acceso al ID del Telefono por tema de Seguridad.");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Solicitar permiso", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    dialogo1.cancel();
                    ActivityCompat.requestPermissions(Conductor_edit.this,
                            PERMISSIONS,
                            1);

                }
            });
            dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    dialogo1.cancel();

                }
            });
            dialogo1.show();
        } else {
            ActivityCompat.requestPermissions(Conductor_edit.this,
                    PERMISSIONS,
                    1);
        }
    }


}
