package com.elisoft.kache_conductor;


import android.Manifest;
import android.app.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.elisoft.kache_conductor.chat.Chat;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;


public class LLamar_usuario extends AppCompatActivity {

    Button bt_llegare_en_5;
    Button bt_estoy_en_camino;
    Button bt_direccion_incorrecta;
    Button bt_llegue;
    Button bt_vocina;
    Button bt_llamada_usuario;
    Button bt_mensaje_usuario;
    Button bt_whatsapp_usuario;


    ImageView im_perfil_usuario;
    TextView tv_nombre;
Suceso suceso;

    LinearLayout pb_cargando;
    LinearLayout.LayoutParams cero = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
    LinearLayout.LayoutParams wrap_content  = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

Context context;



    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llamar_usuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=getApplicationContext();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        verificar_todos_los_permisos();

        final SharedPreferences preferencias = getSharedPreferences("ultimo_pedido_conductor", Context.MODE_PRIVATE);
       /*
        Intent servicio_contacto = new Intent(LLamar_usuario.this, Servicio_guardar_contacto.class);
        servicio_contacto.setAction(Constants.ACTION_RUN_ISERVICE);
        servicio_contacto.putExtra("nombre",preferencias .getString("nombre_usuario", ""));
        servicio_contacto.putExtra("telefono",preferencias .getString("celular", ""));
        startService(servicio_contacto);
        */
        // get prompts.xml view



        bt_llegare_en_5= (Button)findViewById(R.id.bt_llegare_en_5);
        bt_estoy_en_camino= (Button)findViewById(R.id.bt_estoy_en_camino);
        bt_direccion_incorrecta= (Button)findViewById(R.id.bt_direccion_incorrecta);
        bt_llegue= (Button)findViewById(R.id.bt_llegue);
        bt_vocina= (Button)findViewById(R.id.bt_vocina);
        bt_llamada_usuario= (Button)findViewById(R.id.bt_llamada_usuario);
        bt_mensaje_usuario= (Button)findViewById(R.id.bt_mensaje_usuario);
        im_perfil_usuario=(ImageView)findViewById(R.id.im_perfil_usuario);
        bt_whatsapp_usuario= (Button)findViewById(R.id.bt_whatsapp_usuario);

        pb_cargando=(LinearLayout) findViewById(R.id.pb_cargando);
        pb_cargando.setLayoutParams(cero);

        final ImageView im_perfil_usuario=(ImageView)findViewById(R.id.im_perfil_usuario);
        final TextView tv_nombre=(TextView)findViewById(R.id.tv_nombre);
        tv_nombre.setText(preferencias.getString("nombre_usuario", ""));

        getImage(preferencias.getString("id_usuario",""));



        bt_llegare_en_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
                    SharedPreferences perfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);
                    String detalle=bt_llegare_en_5.getText().toString();

                    Servicio_notificacion hilo = new Servicio_notificacion();
                    hilo.execute(getString(R.string.servidor) + "frmPedido.php?opcion=enviar_notificacion_conductor", "1",pedido.getString("id_pedido",""),perfil.getString("ci",""),perfil.getString("placa",""),pedido.getString("id_usuario",""),detalle);// parametro que recibe el doinbackground
                }catch (Exception e)
                {
                    mensaje("No se pudo enviar la notificación.");
                }


            }
        });

        bt_llamada_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SharedPreferences preferencias = getSharedPreferences("ultimo_pedido_conductor", Context.MODE_PRIVATE);
                    Intent llamada = new Intent(Intent.ACTION_CALL);
                    llamada.setData(Uri.parse("tel:" + preferencias.getString("celular", "")));
                    if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        verificar_permiso_llamada();
                    }
                    startActivity(llamada);
                }catch (Exception e)
                {}
                finish();

            }});
        bt_whatsapp_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    Intent it_chat=new Intent(getApplicationContext(),Chat.class);
                    it_chat.putExtra("id_usuario",preferencias.getString("id_usuario",""));
                    it_chat.putExtra("titulo",preferencias.getString("nombre_usuario",""));
                    startActivity(it_chat);
                }catch (Exception e)
                {}
               finish();
            }});

        bt_mensaje_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SharedPreferences preferencias = getSharedPreferences("ultimo_pedido_conductor", Context.MODE_PRIVATE);
                    EnviarMensaje("+591"+preferencias.getString("celular", ""),"Estoy en el lugar.");
                }catch (Exception e)
                {}
                finish();
            }});


        bt_estoy_en_camino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
                    SharedPreferences perfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);
                    String detalle=bt_estoy_en_camino.getText().toString();

                    Servicio_notificacion hilo = new Servicio_notificacion();
                    hilo.execute(getString(R.string.servidor) + "frmPedido.php?opcion=enviar_notificacion_conductor", "1",pedido.getString("id_pedido",""),perfil.getString("ci",""),perfil.getString("placa",""),pedido.getString("id_usuario",""),detalle);// parametro que recibe el doinbackground
                }catch (Exception e)
                {
                    mensaje("No se pudo enviar la notificación.");
                }
                finish();
            }
        });

        bt_direccion_incorrecta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
                    SharedPreferences perfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);
                    String detalle=bt_direccion_incorrecta.getText().toString();

                    Servicio_notificacion hilo = new Servicio_notificacion();
                    hilo.execute(getString(R.string.servidor) + "frmPedido.php?opcion=enviar_notificacion_conductor", "1",pedido.getString("id_pedido",""),perfil.getString("ci",""),perfil.getString("placa",""),pedido.getString("id_usuario",""),detalle);// parametro que recibe el doinbackground
                }catch (Exception e)
                {
                    mensaje("No se pudo enviar la notificación.");
                }

            }
        });

        bt_llegue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
                    SharedPreferences perfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);
                    String detalle=bt_llegue.getText().toString();

                    Servicio_notificacion hilo = new Servicio_notificacion();
                    hilo.execute(getString(R.string.servidor) + "frmPedido.php?opcion=enviar_notificacion_conductor", "1",pedido.getString("id_pedido",""),perfil.getString("ci",""),perfil.getString("placa",""),pedido.getString("id_usuario",""),detalle);// parametro que recibe el doinbackground
                }catch (Exception e)
                {
                    mensaje("No se pudo enviar la notificación.");
                }

            }
        });
        bt_vocina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
                Servicio_taxi hil = new Servicio_taxi();
                String id_pedido = pedido.getString("id_pedido", "");
                hil.execute(getString(R.string.servidor) + "frmPedido.php?opcion=notificacion_llego_el_taxi", "8", id_pedido);// parametro que recibe

            }
        });


    }


    private void EnviarMensaje (String Numero, String Mensaje){
        try {
            Uri sms_uri = Uri.parse("smsto:+" +Numero);
            Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
            sms_intent.putExtra("sms_body", Mensaje);
            startActivity(sms_intent);
        }

        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Mensaje no enviado, datos incorrectos.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
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
                    ActivityCompat.requestPermissions((Activity) context,
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
            ActivityCompat.requestPermissions(this,
                    PERMISSIONS,
                    1);
        }
    }

    private void getImage(String id)//
    {


        String  url=  getString(R.string.servidor)+"usuario/imagen/perfil/"+id+"_perfil.png";
        Picasso.with(this).load(url).into(target);
    }

    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            Drawable dw = new BitmapDrawable(getResources(), bitmap);
            //se edita la imagen para ponerlo en circulo.

            if( bitmap==null)
            { dw = getResources().getDrawable(R.mipmap.ic_perfil);
            }

            imagen_circulo(dw,im_perfil_usuario);

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    public void imagen_circulo(Drawable id_imagen, ImageView imagen) {
        Bitmap originalBitmap = ((BitmapDrawable) id_imagen).getBitmap();
        if (originalBitmap.getWidth() > originalBitmap.getHeight()) {
            originalBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getHeight(), originalBitmap.getHeight());
        } else if (originalBitmap.getWidth() < originalBitmap.getHeight()) {
            originalBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getWidth());
        }

//creamos el drawable redondeado
        RoundedBitmapDrawable roundedDrawable =
                RoundedBitmapDrawableFactory.create(getResources(), originalBitmap);

//asignamos el CornerRadius
        roundedDrawable.setCornerRadius(originalBitmap.getWidth());

        imagen.setImageDrawable(roundedDrawable);
    }
    public void verificar_todos_los_permisos()
    {
        final String[] SMS_PERMISSIONS1 = {
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.CALL_PHONE};


        ActivityCompat.requestPermissions( this,
                SMS_PERMISSIONS1,
                1000);


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
          if(requestCode == 1000)
        {
            int per=0;
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 ) {
                for (int i=0;i<grantResults.length;i++){
                    if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                        per++;
                    }
                }

                // permission was granted, yay! Do the
                // contacts-related task you need to do.

            } else {

                finish();
            }

            if(per<grantResults.length-1){
                finish();
            }else{
                //tiene todos los permisos...

            }
            return;

        }
    }


    // comenzar el servicio con el taxista....
    public class Servicio_notificacion extends AsyncTask<String,Integer,String> {


        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url = null;  // url donde queremos obtener informacion
            String devuelve = "";

            //ENVIAR NOTIFICACION AL USUARIO
            if (params[1] == "1") { //mandar JSON metodo post ENVIAR LA CONFIRMACION DEL LLEGADO DEL MOTISTA.,,,
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
                    jsonParam.put("ci", params[3]);
                    jsonParam.put("placa", params[4]);
                    jsonParam.put("id_usuario", params[5]);
                    jsonParam.put("detalle", params[6]);


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

                        SystemClock.sleep(950);
                        JSONObject respuestaJSON = new JSONObject(result.toString());//Creo un JSONObject a partir del
                        suceso=new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));

                        if (suceso.getSuceso().equals("1")) {
                            devuelve="1";
                        } else  {
                            devuelve = "2";
                        }

                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                    devuelve="500";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



            return devuelve;
        }


        @Override
        protected void onPreExecute() {
            //para el progres Dialog
            pb_cargando.setLayoutParams(wrap_content);
            /*
            pDialog = new ProgressDialog(Menu_taxi.this);
            pDialog.setTitle(getString(R.string.app_name));
            pDialog.setMessage("Autenticando ....");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
            */
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pb_cargando.setLayoutParams(cero);
            // pDialog.cancel();//ocultamos proggress dialog
            // Log.e("respuesta del servidor=", "" + s);
            if(s.equals("1"))
            {
                //mensaje(suceso.getMensaje());
                finish();
            }else
            if(s.equals("500"))
            {
                mensaje_error("No pudimos conectarnos al servidor.\nVuelve a intentarlo.");
            }
            else if(s.equals("2"))
            {

                mensaje(suceso.getMensaje());
            }
            else
            {
                mensaje_error("No pudimos conectarnos al servidor.\nVuelve a intentarlo.");

            }
            SharedPreferences prefe=getSharedPreferences("perfil_conductor",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=prefe.edit();

            editor.commit();



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




    // comenzar el servicio con el taxista....
    public class Servicio_taxi extends AsyncTask<String,Integer,String> {
        double latitud_inicio=0,longitud_inicio=0,altura_inicio=0;
        double distancia_i=0;

        String tiempo="",monto="";

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url = null;  // url donde queremos obtener informacion
            String devuelve = "";

            if (params[1] == "8") {
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

                        //SystemClock.sleep(950);

                        JSONObject respuestaJSON = new JSONObject(result.toString());//Creo un JSONObject a partir del

                        suceso =new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));

                        if (suceso.getSuceso().equals("1")) {
                            devuelve="17";

                        }
                        else
                        {
                            devuelve="18";
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
            try{
                pb_cargando.setLayoutParams(wrap_content);}
            catch (Exception e)
            {

            }
            /*
            pDialog = new ProgressDialog(Menu_taxi.this);
            pDialog.setTitle(getString(R.string.app_name));
            pDialog.setMessage("Autenticando ....");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
            */
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            SharedPreferences punto = getSharedPreferences("mi ubicacion_2", Context.MODE_PRIVATE);
            double latitud_fin = Double.parseDouble(punto.getString("latitud", "0"));
            double longitud_fin = Double.parseDouble(punto.getString("longitud", "0"));
            double altura_fin = Double.parseDouble(punto.getString("altura", "0"));

            try{
                pb_cargando.setLayoutParams(cero);
            }catch (Exception e)
            {}
            //pDialog.cancel(); ocultamos proggress dialog
            // Log.e("respuesta del servidor=", "" + s);
            if (s.equals("17") || s.equals("18")) {
                finish();
            }   else {
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



    public void mensaje(String mensaje)
    {
        Toast toast =Toast.makeText(this,mensaje,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
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
}
