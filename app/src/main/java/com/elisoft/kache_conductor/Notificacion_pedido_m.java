package com.elisoft.kache_conductor;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.elisoft.kache_conductor.notificaciones.SharedPrefManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Notificacion_pedido_m extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    private GoogleMap mMap;
    int id_pedido=0;
    Button bt_aceptar,bt_cancelar;
    TextView tv_clase_vehiculo;
    LinearLayout ll_clase_vehiculo;
    double latitud,longitud;
    String nombre;
    Suceso suceso;
    ProgressDialog pDialog;
    TextView tv_referencia,tv_direccion,tv_tiempo_llegada,tv_distancia_llegada,tv_contador_tiempo,tv_nombre;
    JSONObject rutas=null;
    ProgressBar pb_tiempo;

    Handler handle = new Handler();
    Thread pro_bar;
    TextView tv_titulo;

    int i,tipo_pedido_empresa=0,clase_vehiculo=0;

    LocationManager manager=null;
    AlertDialog alert = null;

    public static MediaPlayer mp;









    @Override
    protected void onDestroy() {
        super.onDestroy();
        setSonido(0);
        try {
            mp.stop();
        }catch (Exception e){

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        setSonido(0);
        try {
            mp.stop();
        }catch (Exception e){

        }
    }

    @Override
    public void onBackPressed() {
        try {
            mp.stop();
        }catch (Exception e){

        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacion_pedido_m);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);



        bt_aceptar=(Button)findViewById(R.id.bt_aceptar);
        bt_cancelar=(Button)findViewById(R.id.bt_cancelar);
        tv_referencia=(TextView)findViewById(R.id.tv_referencia);
        tv_nombre=(TextView)findViewById(R.id.tv_nombre);
        tv_direccion=(TextView)findViewById(R.id.tv_direccion);
        pb_tiempo=(ProgressBar)findViewById(R.id.pb_tiempo);
        tv_tiempo_llegada=(TextView)findViewById(R.id.tv_tiempo_llegada);
        tv_distancia_llegada=(TextView)findViewById(R.id.tv_distancia_llegada);
        tv_contador_tiempo=(TextView)findViewById(R.id.tv_contador_tiempo);
        tv_clase_vehiculo=(TextView)findViewById(R.id.tv_clase_vehiculo);
        ll_clase_vehiculo=(LinearLayout)findViewById(R.id.ll_clase_vehiculo);
        tv_titulo=(TextView)findViewById(R.id.tv_titulo);


        bt_aceptar.setOnClickListener(this);
        bt_cancelar.setOnClickListener(this);

        try{
            Bundle bundle=getIntent().getExtras();
            id_pedido=Integer.parseInt(bundle.getString("id_pedido"));
            latitud=Double.parseDouble (bundle.getString("latitud"));
            longitud=Double.parseDouble (bundle.getString("longitud"));
            tv_referencia.setText(bundle.getString("indicacion"));
            tv_nombre.setText(bundle.getString("nombre"));
            clase_vehiculo=bundle.getInt("clase_vehiculo");
            tipo_pedido_empresa=bundle.getInt("tipo_pedido_empresa");
            marcar_ruta(true);
            obtener_direccion(latitud,longitud);


            mp = MediaPlayer.create(this, R.raw.pedir_taxi);
            if(clase_vehiculo==2){
                tv_titulo.setText(getString(R.string.taxi_lujo));
                toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorLujo));
            }else if (clase_vehiculo==3){
                tv_titulo.setText(getString(R.string.taxi_con_aire));
                toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAire));
            }else if(clase_vehiculo==4)
            {
                tv_titulo.setText(getString(R.string.taxi_con_maletero));
                toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorMaletero));
            }else if(clase_vehiculo==5){
                mp = MediaPlayer.create(this, R.raw.hay_un_pedido);
                tv_titulo.setText("Un Movil para Delivery");
                toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPedido));
            }else if(clase_vehiculo==7){
                tv_titulo.setText("Una Moto");
                toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPedido));
            }else if(clase_vehiculo==11)
            {
                tv_titulo.setText(getString(R.string.taxi_con_parrilla));
                toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorMaletero));
            } else if(clase_vehiculo==15)
            {
                tv_titulo.setText(getString(R.string.taxi_camioneta));
                toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPedido));
            }


            mp.start();
            setSonido(1);
            i = 60;
            pro_bar=new Thread(new Runnable() {
                @Override
                public void run() {
//1800 es 3 minutos.
                    while (i>0) {
                        i--;

                        handle.post(new Runnable() {
                            @Override
                            public void run() {
                                pb_tiempo.setProgress(i);
                                tv_contador_tiempo.setText(String.valueOf(i));
                                if(i%10==0&& getSonido()==1){
                                    mp.start();
                                }
                            }
                        });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    finish();
                }
            });
            pro_bar.start();
        }catch (Exception e)
        {
            finish();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ///verifica si el GPS esta activo.
        manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            AlertNoGps();
        }
    }


    private void AlertNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("El sistema GPS esta desactivado, ¿Desea activarlo?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        alert = builder.create();
        alert.show();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        LatLng sydney = new LatLng(latitud,longitud);
        Marker marker =mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title(nombre)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_point1))
                .anchor((float)0.5,(float)0.5)
                .flat(true)
                .rotation(0));
        marker.showInfoWindow();
        //agregaranimacion al mover la camara...
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitud,longitud))      // Sets the center of the map to Mountain View
                .zoom(15)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Mostrar diálogo explicativo
            } else {
                // Solicitar permiso
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }

        }
    }

    @Override
    public void onClick(View v) {

        if(v.getId()== R.id.bt_aceptar)
        {
            setSonido(0);
            aceptar_solicitud();
        }
        else if(v.getId()== R.id.bt_cancelar)
        {
            setSonido(0);
            i=-1;
            finish();
        }
    }

    public  void aceptar_solicitud(){
        SharedPreferences perfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);

        servicio_aceptar_pedido(String.valueOf(id_pedido),perfil.getString("ci",""),perfil.getString("placa",""));
    }

    private void servicio_aceptar_pedido(String id_pedido, String ci, String placa) {


        try {

            pDialog = new ProgressDialog(Notificacion_pedido_m.this);
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
            String url=getString(R.string.servidor) + "frmPedido.php?opcion=aceptar_pedido";
            RequestQueue queue = Volley.newRequestQueue(this);


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
                                    cargar_pedido_en_curso(id_pedido,id_usuario,fecha_pedido,fecha_proceso,mensaje,estado,latitud,longitud,nombre_usuario,celular);
//////////////////-----------------------------------------------------------------------------------
                                    i=-1;
                                    Intent i=new Intent(getApplicationContext(),Menu_taxi.class);
                                    i.putExtra("id_pedido",String.valueOf(id_pedido));
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    finish();

                                }else if(suceso.getSuceso().equals("2"))
                                {

                                    mensaje_error(suceso.getMensaje());
                                }
                                else
                                {
                                    aceptar_solicitud();
                                }
                            } catch (JSONException e) {
                                pDialog.dismiss();

                                e.printStackTrace();
                               aceptar_solicitud();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                  aceptar_solicitud();
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
            mensaje_error("No pudimos conectarnos al servidor.\nVuelve a intentarlo.");
        }
    }



    public void mensaje(String mensaje)
    {
        Toast toast =Toast.makeText(this,mensaje,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    private void cargar_pedido_en_curso(String id_pedido, String id_usuario, String fecha_pedido, String fecha_proceso, String mensaje, String estado, double latitud, double longitud, String nombre_usuario,String celuular) {

        SharedPreferences preferencias=getSharedPreferences("ultimo_pedido_conductor", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putString("id_pedido",id_pedido);
        editor.putString("id_usuario",id_usuario);
        editor.putString("fecha_pedido",fecha_pedido);
        editor.putString("fecha_proceso",fecha_proceso);
        editor.putString("indicacion",mensaje);
        editor.putString("latitud",String.valueOf(latitud));
        editor.putString("longitud",String.valueOf(longitud));
        editor.putString("nombre_usuario",nombre_usuario);
        editor.putString("celular",celuular);
        editor.putString("estado",estado);
        editor.commit();

    }

    public void mensaje_error(String mensaje)
    {




        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage(mensaje);
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                finish();
            }
        });

        dialogo1.show();




    }

    public void marcar_ruta(boolean sw)
    {
        try{


            //buscamos una ruta para el motista     SOLO CO ACCESO A INTERNET

            SharedPreferences punto=getSharedPreferences("mi ubicacion",Context.MODE_PRIVATE);
            double latitud_fin=Double.parseDouble(punto.getString("latitud","0"));
            double longitud_fin=Double.parseDouble(punto.getString("longitud","0"));

           Servicio_taxi_ruta hilo = new Servicio_taxi_ruta();
            hilo.execute("https://maps.googleapis.com/maps/api/directions/json?origin=" + latitud + "," + longitud + "&destination=" + latitud_fin + "," + longitud_fin + "&mode=driving&key="+getString(R.string.google_key_api), "4");// parametro que recibe el doinbackground

        }catch (Exception e)
        {

        }
    }

    // comenzar el servicio con el motista....
    public class Servicio_taxi_ruta extends AsyncTask<String,Integer,String> {


        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url = null;  // url donde queremos obtener informacion
            String devuelve = "";

            //Obtener el camino mas corto. para llegar mas rapido ..
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



                        rutas= new JSONObject(result.toString());//Creo un JSONObject a partir del

                        devuelve="7";
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    devuelve="500";
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
            // Log.e("respuesta del servidor=", "" + s);
            if(s.equals("7"))
            {
                dibujar_ruta(rutas);
            }else if(s.equals("500"))
            {
                dibujar_ruta(rutas);
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

    public void dibujar_ruta(JSONObject jObject){

        String tiempo="",distancia="";
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;
        boolean sw_punto=false;
        LatLng punto=new LatLng(0,0);
        PolylineOptions polylineOptions = new PolylineOptions();

        try {

            jRoutes = jObject.getJSONArray("routes");

            /** Traversing all routes */
            for(int i=0;i<jRoutes.length();i++){
                jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                /** Traversing all legs */
                for(int j=0;j<jLegs.length();j++){
                    jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");
                    /** Traversing all steps */
                    for(int k=0;k<jSteps.length();k++){
                        String polyline = "";
                        polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);

                        /** Traversing all points */
                        for(int l=0;l<list.size();l++){
                            double lat=((LatLng)list.get(l)).latitude;
                            double lon=((LatLng)list.get(l)).longitude;
                            punto = new LatLng(lat, lon);
                            polylineOptions.add(punto);
                            sw_punto=true;

                        }
                    }

                    tiempo=(String)((JSONObject)((JSONObject)jLegs.get(j)).get("duration")).get("text");
                    distancia=(String)((JSONObject)((JSONObject)jLegs.get(j)).get("distance")).get("text");



                }
            }



            //dibujar las lineas

            if(sw_punto==true) {


                punto=new LatLng(latitud,longitud);
                mMap.addPolyline(polylineOptions.width(8).color(Color.BLACK));


                //agregaranimacion al mover la camara...
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(punto)      // Sets the center of the map to Mountain View
                        .zoom(17)                   // Sets the zoom
                        .bearing(0)                // Sets the orientation of the camera to east
                        .tilt(10)                   // Sets the tilt of the camera to 30 degrees
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                // marcado_sw=false;
            }


            try {



                tv_tiempo_llegada.setText(tiempo);
                tv_distancia_llegada.setText(distancia);

            } catch (Exception e) {
                tv_tiempo_llegada.setText("");
                tv_distancia_llegada.setText("");
            }

            try {
                SharedPreferences punto_2=getSharedPreferences("mi ubicacion",Context.MODE_PRIVATE);
                double latitud_fin=Double.parseDouble(punto_2.getString("latitud","0"));
                double longitud_fin=Double.parseDouble(punto_2.getString("longitud","0"));

                if(clase_vehiculo==7)
                {
                    Marker marker = this.mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_moto))
                            .anchor((float) 0.5, (float) 0.8)
                            .flat(true)
                            .position(new LatLng(latitud_fin, longitud_fin)));
                }else {
                    Marker marker = this.mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_movil))
                            .anchor((float) 0.5, (float) 0.8)
                            .flat(true)
                            .position(new LatLng(latitud_fin, longitud_fin)));
                }
            } catch (Exception e) {

            }






















        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
        }

    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    public void obtener_direccion(double lat, double lon) {
        Geocoder geocoder = new Geocoder(getBaseContext());
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.

        } catch (IllegalArgumentException illegalArgumentException) {

        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size() == 0) {
            //error. o no tiene datos recolectados...
        } else {
            String direcciones="";
            Address address = addresses.get(0);
            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                direcciones+=","+address.getAddressLine(i);
            }

        /*    // Creamos el string a partir del elemento direccion
            String direccionText = String.format("%s, %s, %s",
                    address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                    address.getLocality(),
                    address.getCountryName());
                    */
            tv_direccion.setText(direcciones);

            //  et_direccion.setText(address.getFeatureName()+" | "+address.getSubAdminArea ()+" | "+address.getSubLocality ()+" | "+address.getLocality ()+" | "+address.getSubLocality ()+" | "+address.getPremises ()+" | "+addresses.get(0).getThoroughfare()+" | "+address.getAddressLine(0));
        }
    }


    public  int getSonido()
    {
        SharedPreferences perfil=getSharedPreferences(getString(R.string.sonido),MODE_PRIVATE);
        return perfil.getInt("sonido",0);
    }
    public  void setSonido(int sw)
    {
        SharedPreferences sonido=getSharedPreferences(getString(R.string.sonido),MODE_PRIVATE);
        SharedPreferences.Editor editor=sonido.edit();
        editor.putInt("sonido",sw);
        editor.commit();

    }

}
