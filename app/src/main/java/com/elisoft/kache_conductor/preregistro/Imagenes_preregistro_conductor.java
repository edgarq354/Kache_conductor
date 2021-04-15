package com.elisoft.kache_conductor.preregistro;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.elisoft.kache_conductor.notificaciones.SharedPrefManager;
import com.elisoft.kache_conductor.Menu_taxi;
import com.elisoft.kache_conductor.R;
import com.elisoft.kache_conductor.Suceso;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
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

import static com.elisoft.kache_conductor.perfil.Perfil_Conductor.ReducirImagen_b;

public class Imagenes_preregistro_conductor extends AppCompatActivity implements  View.OnClickListener {

    ImageView im_perfil_conductor;
    ImageView im_imagen_carnet_1;
    ImageView im_imagen_carnet_2;
    ImageView im_imagen_licencia_1;
    ImageView im_imagen_licencia_2;


    String direccion_imagen="";
    String direccion_imagen_carnet_1="";
    String direccion_imagen_carnet_2="";
    String direccion_imagen_licencia_1="";
    String direccion_imagen_licencia_2="";



    Suceso suceso;
    ProgressDialog pDialog;

    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;
    String ci="";
    String mPath="";
    private Uri path;
    String opcion_subir_imagen="";


    Bitmap bitmap_aux=null;

    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagenes_preregistro_conductor);
        im_perfil_conductor=(ImageView)findViewById(R.id.im_perfil_conductor);

        im_imagen_carnet_1=(ImageView)findViewById(R.id.im_imagen_carnet_1);
        im_imagen_carnet_2=(ImageView)findViewById(R.id.im_imagen_carnet_2);
        im_imagen_licencia_1=(ImageView)findViewById(R.id.im_imagen_licencia_1);
        im_imagen_licencia_2=(ImageView)findViewById(R.id.im_imagen_licencia_2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        im_perfil_conductor.setOnClickListener(this);


        im_imagen_carnet_1.setOnClickListener(this);
        im_imagen_carnet_2.setOnClickListener(this);
        im_imagen_licencia_1.setOnClickListener(this);
        im_imagen_licencia_2.setOnClickListener(this);

        try{
            Bundle bundle=getIntent().getExtras();
            ci=bundle.getString("ci");
            direccion_imagen=bundle.getString("direccion_imagen");

            direccion_imagen_carnet_1=bundle.getString("direccion_imagen_carnet_1");
            direccion_imagen_carnet_2=bundle.getString("direccion_imagen_carnet_2");
            direccion_imagen_licencia_1=bundle.getString("direccion_imagen_licencia_1");
            direccion_imagen_licencia_2=bundle.getString("direccion_imagen_licencia_2");


            getImage(getString(R.string.servidor_web)+"public/"+direccion_imagen,"5");
            getImage(getString(R.string.servidor_web)+"storage/"+direccion_imagen_carnet_1,"6");
            getImage(getString(R.string.servidor_web)+"storage/"+direccion_imagen_carnet_2,"7");
            getImage(getString(R.string.servidor_web)+"storage/"+direccion_imagen_licencia_1,"8");
            getImage(getString(R.string.servidor_web)+"storage/"+direccion_imagen_licencia_2,"9");



            verificar_todas_las_fotos();

        }catch (Exception e)
        {
            finish();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View v) {
         opcion_subir_imagen="";
        switch (v.getId()){
            case R.id.im_perfil_conductor:
                opcion_subir_imagen="perfil";
                break;
            case R.id.im_imagen_carnet_1:
                opcion_subir_imagen="imagen_carnet_1";
                break;
            case R.id.im_imagen_carnet_2:
                opcion_subir_imagen="imagen_carnet_2";
                break;
            case R.id.im_imagen_licencia_1:
                opcion_subir_imagen="imagen_licencia_1";
                break;
            case R.id.im_imagen_licencia_2:
                opcion_subir_imagen="imagen_licencia_2";
                break;

        }



        if(opcion_subir_imagen!="")
        {
            Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent.createChooser(intent,"Seleccione app de imagen"),SELECT_PICTURE);

        }

    }


    private void getImage(String url, final String opcion)//
    {
        class GetImage extends AsyncTask<String,Void,Bitmap> {



            public GetImage() {

            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);

                if( bitmap!=null)
                {

                    Drawable dd = new BitmapDrawable(getResources(), bitmap);

                    if(opcion.equals("5"))
                    {
                        im_perfil_conductor.setBackground(dd);
                    }else if(opcion.equals("6"))
                    {
                        im_imagen_carnet_1.setBackground(dd);
                    }else if(opcion.equals("7"))
                    {
                        im_imagen_carnet_2.setBackground(dd);
                    }else if(opcion.equals("8"))
                    {
                        im_imagen_licencia_1.setBackground(dd);
                    }else if(opcion.equals("9"))
                    {
                        im_imagen_licencia_2.setBackground(dd);
                    }

                }


            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Bitmap doInBackground(String... strings) {
                String url =strings[0];//hace consulta ala Bd para recurar la imagen

                Bitmap mIcon =null;
                try {
                    InputStream in = new URL(url).openStream();
                    mIcon = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                }
                return mIcon;
            }
        }

        GetImage gi = new GetImage();
        gi.execute(url);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            //ubicacion de la imagen

            Bitmap img_cargar;

            String uploadImage="";

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

                    //uploadImage = getStringImage(img_cargar);
                    subir_imagen_servidor(opcion_subir_imagen,uploadImage,bitmap);

                    break;
                case SELECT_PICTURE:
                    path = data.getData();
                    try {//convertir Uri a BitMap
                        Bitmap tempBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(path));

                        guardar_imagen_temp(tempBitmap);
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
            }
        }else{
            showExplanation();
        }
    }


    private void guardar_imagen_temp(Bitmap bitmapImage)
    {
        FileOutputStream fos = null;
        try {
            String MEDIA_DIRECTORY = getString(R.string.app_name)+"/Pre registro/";//nombre de directorio
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

    private void subir_imagen_servidor(String opcion_subir_imagen, String uploadImage, Bitmap bitmap) {


        //Convertir Bitmap a Drawable.
        Drawable dw = new BitmapDrawable(getResources(), bitmap);
        bitmap_aux=bitmap;

        switch (opcion_subir_imagen)
        {
            case "perfil":
                serverUpdate(getString(R.string.servidor) + "frmTaxi.php?opcion=insertar_imagen_perfil_conductor",opcion_subir_imagen,ci);

                //  hilo_imagen.execute(getString(R.string.servidor) + "frmTaxi.php?opcion=insertar_imagen_rua", "1",sperfil.getString("placa",""),uploadImage);// parametro que recibe el doinbackground
                break;
             case "imagen_carnet_1":
                serverUpdate(getString(R.string.servidor) + "frmTaxi.php?opcion=insertar_imagen_carnet_1",opcion_subir_imagen,ci);
                break;
            case "imagen_carnet_2":
                serverUpdate(getString(R.string.servidor) + "frmTaxi.php?opcion=insertar_imagen_carnet_2",opcion_subir_imagen,ci);
                break;
            case "imagen_licencia_1":
                serverUpdate(getString(R.string.servidor) + "frmTaxi.php?opcion=insertar_imagen_licencia_1",opcion_subir_imagen,ci);
                break;
            case "imagen_licencia_2":
                serverUpdate(getString(R.string.servidor) + "frmTaxi.php?opcion=insertar_imagen_licencia_2",opcion_subir_imagen,ci);
                break;

        }
    }



    class ServerUpdate extends AsyncTask<String,String,String> {

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
                        Toast.makeText(Imagenes_preregistro_conductor.this, suceso.getMensaje(),Toast.LENGTH_LONG).show();
                    }
                });
            else
                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        AlertDialog.Builder builder = new AlertDialog.Builder(Imagenes_preregistro_conductor.this);
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
            pDialog = new ProgressDialog(Imagenes_preregistro_conductor.this);
            pDialog.setMessage("Subiendo la imagen, espere..." );
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();

            if(resultado.equals("1"))
            {//CEDULA

                agregar_imagen(bitmap_aux, im_perfil_conductor);
                direccion_imagen="direccion de imagen agregada recientemente";
                verificar_todas_las_fotos();
            }else if(resultado.equals("6"))
            {//CARNET 1

                agregar_imagen(bitmap_aux,im_imagen_carnet_1);
                direccion_imagen_carnet_1="direccion de imagen agregada recientemente";
                verificar_todas_las_fotos();
            } else if(resultado.equals("7"))
            {//CARNET 2

                agregar_imagen(bitmap_aux,im_imagen_carnet_2);
                direccion_imagen_carnet_2="direccion de imagen agregada recientemente";
                verificar_todas_las_fotos();
            } else if(resultado.equals("8"))
            {//LICENCIA 1

                agregar_imagen(bitmap_aux,im_imagen_licencia_1);
                direccion_imagen_licencia_1="direccion de imagen agregada recientemente";
                verificar_todas_las_fotos();
            } else if(resultado.equals("9"))
            {//LICENCIA 2

                agregar_imagen(bitmap_aux,im_imagen_licencia_2);
                direccion_imagen_licencia_2="direccion de imagen agregada recientemente";
                verificar_todas_las_fotos();
            }else if(resultado.equals("500"))
            {
                mensaje(suceso.getMensaje());
            }else
              {
                mensaje("Falla en tu conexion a internet.");
            }
        }

    }

    private void serverUpdate(String url,String tipo,String placa){
        if (file.exists()){
            new ServerUpdate().execute(url,tipo,placa);
        }
        else
        {
            Log.e("Imagen","No se pudo localizar la imagen");
        }
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
            mpEntity.addPart("placa", new StringBody(placa));
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

                if(tipo=="perfil"){

                    devuelve="1";
                }else if(tipo=="imagen_carnet_1"){
                    devuelve="6";
                }else if(tipo=="imagen_carnet_2"){
                    devuelve="7";
                }else if(tipo=="imagen_licencia_1"){
                    devuelve="8";
                }else if(tipo=="imagen_licencia_2"){
                    devuelve="9";
                }else{
                devuelve = "500";
            }
            }else
            {
                devuelve = "500";
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

    public void mensaje(String mensaje)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Importante");
        builder.setMessage(mensaje);
        builder.setPositiveButton("OK", null);
        builder.create();
        builder.show();
    }
    public void mensaje_final(String mensaje)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Importante");
        builder.setMessage(mensaje);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                iniciar_sesion();
            }
        });
        builder.create();
        builder.setCancelable(false);
        builder.show();
    }

    public void mensaje_error(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Importante");
        builder.setMessage(mensaje);
        builder.setPositiveButton("OK", null);
        builder.create();
        builder.show();
    }

    public void iniciar_sesion()
    {
        String token = SharedPrefManager.getInstance(this).getDeviceToken();
        if (token != null || token == "") {
             Servicio_taxi servicio = new  Servicio_taxi();
            servicio.execute(getString(R.string.servidor) + "frmTaxi.php?opcion=iniciar_sesion", "1", ci,ci, token, "");// parametro que recibe el doinbackground
        } else {
            mensaje_error("No se a podido generar el Token. porfavor active sus datos de Red e instale Google Pay Service");
        }
    }

    public void verificar_todas_las_fotos()
    {
        if(direccion_imagen.length()<5)
        {
            mensaje("Agregue su foto de la parte delantera de su carnet");
        }else if(direccion_imagen_carnet_1.length()<5)
        {
            mensaje("Agregue su foto de la parte delantera de su carnet");
        }else if(direccion_imagen_carnet_2.length()<5)
        {
            mensaje("Agregue su foto de la parte detras de su carnet");
        }else if(direccion_imagen_licencia_1.length()<5)
        {
            mensaje("Agregue su foto de la parte delantera de su licencia de conducir");
        }else if(direccion_imagen_licencia_2.length()<5)
        {
            mensaje("Agregue su foto de la parte detras de su licencia de conducir");
        }else{
            mensaje_final("Gracias por completar tu registro");
        }




    }


    public void agregar_imagen(Bitmap bitmap,ImageView im_imagen ) {
        bitmap=ReducirImagen_b(bitmap,150,130);
        Drawable d = new BitmapDrawable(getResources(), bitmap);

            im_imagen.setBackground(d);
    }






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
                            String id_empresa = dato.getJSONObject(0).getString("id_empresa");

                            cargar_datos_m(snombre, spaterno, smaterno, semail, scelular, sid, marca, modelo, placa, color, estado, credito, login, tipo, ci, scodigo, id_empresa);

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
            try {
                pDialog = new ProgressDialog(Imagenes_preregistro_conductor.this);
                pDialog.setTitle(getString(R.string.app_name));
                pDialog.setMessage("Autenticando ....");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }catch (Exception e){

            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                pDialog.cancel();//ocultamos proggress dialog
            }catch (Exception e)
            {

            }
            //  Log.e("onPostExcute=", "" + s);

            if (s.equals("1")) {
                iniciar_sesion_taxi();
            } else if (s.equals("2")) {
                mensaje_error(suceso.getMensaje());

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


    public void cargar_datos_m(String nombre, String paterno, String materno, String email, String celular, String id, String marca, String modelo, String placa, String color, String estado, String credito, String login, String tipo_taxi, String ci, String codigo, String id_empresa) {
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
        editar.putString("imei", "");
        editar.commit();

    }


}
