package com.elisoft.kache_conductor.servicio;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.elisoft.kache_conductor.Menu_taxi;
import com.elisoft.kache_conductor.R;
import com.elisoft.kache_conductor.Suceso;
import com.elisoft.kache_conductor.notificaciones.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

public class Servicio_cargar_punto extends Service {

    // public static MediaPlayer mp;
    Suceso suceso;
    double latitud_a;
    double longitud_a;
    int numero = 0;
    int rotacion = 0;
    double altura=0;

    int id_star = 0;
    boolean sw_subiendo ;


    private LocationManager locationManager;
    boolean sw_estado_servicio=true;
    NotificationManager mNotificationManager;


    public Servicio_cargar_punto() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sw_estado_servicio=true;
        //    mp.start();
        super.onStart(intent, startId);
        id_star=startId;
        Log.e("Google", "Service Started cargarpunto.. " + startId);

        locationManager = (LocationManager) getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);
        try {

            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, listener);
            }

            sw_subiendo=false;

        } catch (Exception e) {
            e.printStackTrace();
        }

        show_estoy_libre();
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub

        //   mp = MediaPlayer.create(this, R.raw.audio);// raw/s.mp3
        super.onCreate();


        // Toast.makeText(getApplicationContext(), "Service Created",
        // Toast.LENGTH_SHORT).show();

        Log.e("Google", "Service Created");


    }




    private LocationListener listener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub
            if(sw_estado_servicio==true) {

                Log.e("Google", "Location Changed cargando......"+id_star);

                if (location == null)
                    return;

                if (isConnectingToInternet(getApplicationContext())) {
                    if (sw_subiendo == false) {

                        try {
                            float precision = location.getAccuracy();
                            float bearing = location.getBearing();


// si esta en camino hace un pedido.. el id_carrera se lo va a colocar con un -1
             /*       SharedPreferences prefe=getSharedPreferences("ultimo_pedido",Context.MODE_PRIVATE);
                        int estado=Integer.parseInt(prefe.getString("estado","0"));
                        String id_pedido=prefe.getString("id_pedido","");
                        */
                            // Bucle de simulación de pedido cuando tiene estado del pedido=0  o sino con un estado=1 cuando tiene carreras...

                            SharedPreferences prefe = getSharedPreferences("perfil_conductor", Context.MODE_PRIVATE);
                            String id = prefe.getString("ci", "");
                            String placa = prefe.getString("placa", "");

                            int estado_perfil = 0;
                            try {
                                estado_perfil = Integer.parseInt(prefe.getString("estado", "0"));
                            } catch (Exception e) {
                                estado_perfil = 0;
                            }


                            //verificamos el estado del motista estaactivo o inactivo.
                            //verificamos el estado del motista estaactivo o inactivo.
                            if (estado_perfil == 0) {
                                locationManager.removeUpdates(listener);
                                Log.e("servicio google", "eliminado.." + id_star);
                            }


                            double latitud = location.getLatitude();
                            double longitud = location.getLongitude();
                            latitud_a = latitud;
                            longitud_a = longitud;
                            altura=location.getAltitude();
                            if (location.hasBearing()) {
                                rotacion = Math.round(bearing);
                            }
                            //servicio para cargar puntos..




                            if (id.equals("") == false && latitud != 0 && longitud != 0  ) {

                                try {
                                    SharedPreferences punto_2 = getSharedPreferences("mi ubicacion_2", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = punto_2.edit();
                                    editor.putString("latitud", String.valueOf(latitud_a));
                                    editor.putString("longitud", String.valueOf(longitud_a));
                                    editor.putString("altura", String.valueOf(altura));
                                    editor.putString("rotacion", String.valueOf(rotacion));
                                    editor.commit();


                                    SharedPreferences punto = getSharedPreferences("mi ubicacion", Context.MODE_PRIVATE);
                                    double latitud_fin = Double.parseDouble(punto.getString("latitud", "0"));
                                    double longitud_fin = Double.parseDouble(punto.getString("longitud", "0"));
                                    int rotacion_fin = Integer.parseInt(punto.getString("rotacion","0"));
                                    double altura_fin = Double.parseDouble(punto.getString("altura","0"));
                                    double distancia = getDistancia(latitud, longitud, latitud_fin, longitud_fin);

                                    Log.d("latitude", "(" + location.getLatitude() + "," + location.getLongitude() + "),Precision:" + precision + ". Rotacion:" + rotacion + ". Distancia:" + distancia + ". start:" + id_star);

                                    SharedPreferences ped_2 = getSharedPreferences("ultimo_pedido_conductor", MODE_PRIVATE);
                                    int id_pedido = 0;
                                    try {
                                        id_pedido = Integer.parseInt(ped_2.getString("id_pedido", ""));
                                    } catch (Exception e) {
                                        id_pedido = 0;
                                    }
                                    if (id_pedido == 0) {
                                        precision = 0;
                                    }



                                    try {
                                        if (distancia >= 3 && precision < 30) {

                                            int id_carrera = Integer.parseInt(ped_2.getString("id_carrera", ""));
                                            numero = Integer.parseInt(ped_2.getString("numero", "1"));

                                            if (ped_2.getString("id_pedido", "").equals("") == false && ped_2.getString("id_pedido", "0").equals("0") == false && ped_2.getString("estado", "").equals("1") == true) {
                                                //hilo_traking.execute(getString(R.string.servidor) + "frmTaxi.php?opcion=set_ubicacion_punto_carrera", "2", id, String.valueOf(latitud), String.valueOf(longitud), placa, String.valueOf(id_carrera), String.valueOf(numero), String.valueOf(id_pedido), String.valueOf(distancia));

                                                servicio_ubicacion_punto_carrera(id,
                                                        String.valueOf(latitud),
                                                        String.valueOf(longitud),
                                                        placa,
                                                        String.valueOf(id_carrera),
                                                        String.valueOf(numero),
                                                        String.valueOf(id_pedido),
                                                        String.valueOf(distancia));
                                            } else {
                                                //hilo_traking.execute(getString(R.string.servidor) + "frmTaxi.php?opcion=set_ubicacion_punto", "1", id, String.valueOf(latitud), String.valueOf(longitud), placa);// parametro que recibe el doinbackground
                                                servicio_ubicacion_punto(
                                                        id,
                                                        String.valueOf(latitud),
                                                        String.valueOf(longitud),
                                                        placa);
                                            }
                                        } else   {
                                            if (ped_2.getString("id_pedido", "").equals("") == false && ped_2.getString("id_pedido", "0").equals("0") == false && ped_2.getString("estado", "").equals("1") == true) {
                                            } else {
                                                //hilo_traking.execute(getString(R.string.servidor) + "frmTaxi.php?opcion=set_ubicacion_punto", "1", id, String.valueOf(latitud), String.valueOf(longitud), placa);// parametro que recibe el doinbackground
                                                servicio_ubicacion_punto(
                                                        id,
                                                        String.valueOf(latitud),
                                                        String.valueOf(longitud),
                                                        placa);
                                            }
                                        }
                                    } catch (Exception e) {
                                        try {


                                          //  hilo_traking.execute(getString(R.string.servidor) + "frmTaxi.php?opcion=set_ubicacion_punto", "1", id, String.valueOf(latitud), String.valueOf(longitud), placa);// parametro que recibe el doinbackground
                                            servicio_ubicacion_punto(
                                                    id,
                                                    String.valueOf(latitud),
                                                    String.valueOf(longitud),
                                                    placa);

                                        } catch (Exception a) {
                                            a.printStackTrace();
                                            sw_subiendo = false;
                                            reconectando("←←← ▼ →→→");
                                        }
                                    }

                                    //envia una notificacion al pasajero cuando esta a 50 metros de deistancia de donde pidio el Taxi

                                    SharedPreferences ped = getSharedPreferences("ultimo_pedido_conductor", MODE_PRIVATE);
                                    if (ped.getString("id_pedido", "").equals("") == false) {
                                        SharedPreferences punto_taxi = getSharedPreferences("mi ubicacion", MODE_PRIVATE);
                                        double lat_taxi = Double.parseDouble(punto_taxi.getString("latitud", "0"));
                                        double lon_taxi = Double.parseDouble(punto_taxi.getString("longitud", "0"));
                                        double lat_pedido = Double.parseDouble(ped.getString("latitud", "0"));
                                        double lon_pedido = Double.parseDouble(ped.getString("longitud", "0"));
                                        double distancia_cerca = getDistancia(lat_taxi, lon_taxi, lat_pedido, lon_pedido);
                                        int notificacion_cerca = ped.getInt("notificacion_cerca", 0);
                                        int notificacion_llego = ped.getInt("notificacion_llego", 0);

                                        if (distancia_cerca <= 500 && notificacion_cerca == 0) {
                                            SharedPreferences pedido = getSharedPreferences("ultimo_pedido_conductor", MODE_PRIVATE);
                                            SharedPreferences.Editor editor2 = pedido.edit();
                                            editor2.putInt("notificacion_cerca", 1);
                                            editor2.commit();

                                            String id_pedido_1 = ped.getString("id_pedido", "");
                                            servicio_estoy_cerca(id_pedido_1);
                                        } else if (distancia_cerca <= 50 && notificacion_llego == 0) {
                                            SharedPreferences pedido = getSharedPreferences("ultimo_pedido_conductor", MODE_PRIVATE);
                                            SharedPreferences.Editor editor3 = pedido.edit();
                                            editor3.putInt("notificacion_llego", 1);
                                            editor3.commit();

                                            String id_pedido_1 = ped.getString("id_pedido", "");
                                            servicio_notificacion_llego(id_pedido_1);
                                        }

                                    }

                                } catch (Exception e) {
                                    SharedPreferences punto = getSharedPreferences("mi ubicacion", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = punto.edit();
                                    editor.putString("latitud", "0");
                                    editor.putString("longitud", "0");
                                    editor.putString("altura", "0");
                                    editor.putString("rotacion", "0");
                                    editor.commit();

                                    reconectando("←←← ▲ →→→");
                                    sw_subiendo = false;
                                }


                            }

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            sw_subiendo = false;
                            e.printStackTrace();
                            reconectando("Localizando - ►►►");
                        }
                    }

                }else{
                    sw_subiendo=false;
                    reconectando("Sin acceso a internet");
                }
            }else{
                //  locationManager.removeUpdates(listener);
                Log.e("servicio google", "eliminado.." + id_star);
                reconectando("☼☼("+id_star+")☼☼");
            }

        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
            Log.e("location","Provider Disabled");
            reconectando("GPS desactivado");

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
            Log.e("location","Provider Enabled");
            reconectando("GPS activo");


        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
            Log.e("location","Status Changed");


        }
    };

    private void servicio_notificacion_llego(String id_pedido_1)  {

            try {


                String token= SharedPrefManager.getInstance(this).getDeviceToken();

                JSONObject jsonParam= new JSONObject();
                jsonParam.put("id_pedido",id_pedido_1);
                jsonParam.put("rotacion",String.valueOf(rotacion));

                jsonParam.put("token", token);
                String url=getString(R.string.servidor) + "frmPedido.php?opcion=notificacion_llego_el_taxi";
                RequestQueue queue = Volley.newRequestQueue(this);


                JsonObjectRequest myRequest= new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        jsonParam,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject respuestaJSON) {



                                try {

                                    suceso= new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));

                                    if (suceso.getSuceso().equals("1")) {
                                        //final
                                        //se envio la notificacion... al pasajero
                                        SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
                                        SharedPreferences.Editor editor=pedido.edit();
                                        editor.putInt("notificacion_llego",1);
                                        editor.commit();
                                    }
                                    else
                                    {
                                        //final
                                        SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
                                        SharedPreferences.Editor editor=pedido.edit();
                                        editor.putInt("notificacion_llego",0);
                                        editor.commit();
                                    }
                                    //final

                                } catch (JSONException e) {


                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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

    private void servicio_estoy_cerca(String id_pedido_1)  {
            try {

                final String latitud_h;
                final String longitud_h;
                final String rotacion_h;


// set_ubicacion_punto  ----- cargar punto de ubicacion....

                String token= SharedPrefManager.getInstance(this).getDeviceToken();

                JSONObject jsonParam= new JSONObject();
                jsonParam.put("id_pedido",id_pedido_1);

                jsonParam.put("token", token);
                String url=getString(R.string.servidor) + "frmPedido.php?opcion=estoy_cerca";
                RequestQueue queue = Volley.newRequestQueue(this);


                JsonObjectRequest myRequest= new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        jsonParam,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject respuestaJSON) {



                                try {

                                    suceso= new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));

                                    if (suceso.getSuceso().equals("1")) {
                                        //final
                                        //se envio la notificacion... al pasajero
                                        SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
                                        SharedPreferences.Editor editor=pedido.edit();
                                        editor.putInt("notificacion_cerca",1);
                                        editor.commit();
                                    }
                                    else
                                    {
                                        //final
                                        //se envio la notificacion... al pasajero
                                        SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
                                        SharedPreferences.Editor editor=pedido.edit();
                                        editor.putInt("notificacion_cerca",0);
                                        editor.commit();
                                    }
                                    //final

                                } catch (JSONException e) {


                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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

    private void servicio_ubicacion_punto(
            String id_conductor,
            String latitud,
            String longitud,
            String placa) {
        try {

            final String latitud_h;
            final String longitud_h;
            final String rotacion_h;

            sw_subiendo = true;
// set_ubicacion_punto  ----- cargar punto de ubicacion....

            String token= SharedPrefManager.getInstance(this).getDeviceToken();

            JSONObject jsonParam= new JSONObject();
            jsonParam.put("ci", id_conductor);
            jsonParam.put("latitud", latitud);
            jsonParam.put("longitud", longitud);
            jsonParam.put("placa", placa);
            jsonParam.put("rotacion",String.valueOf(rotacion));
            latitud_h=latitud;
            longitud_h=longitud;
            rotacion_h=String.valueOf(rotacion);

            jsonParam.put("token", token);
            String url=getString(R.string.servidor) + "frmTaxi.php?opcion=set_ubicacion_punto";
            RequestQueue queue = Volley.newRequestQueue(this);


            JsonObjectRequest myRequest= new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jsonParam,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject respuestaJSON) {



                            try {

                                suceso= new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));

                                if (suceso.getSuceso().equals("1")) {
                                    //final
                                    SharedPreferences punto=getSharedPreferences("mi ubicacion",Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor=punto.edit();
                                    editor.putString("latitud",latitud_h);
                                    editor.putString("longitud",longitud_h);
                                    editor.putString("rotacion",rotacion_h);
                                    editor.commit();

                                    Log.w("Servicio cargar punto","Se cargo la ubicacion al servidor ("+latitud_a+","+longitud_a+")");
                                    reconectando("ESTOY LIBRE ☺");

                                }
                                else
                                {
                                    //final
                                    reconectando("ESTOY LIBRE ☻");
                                }
                                //final
                                sw_subiendo=false;
                            } catch (JSONException e) {


                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
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

    private void servicio_ubicacion_punto_carrera(
            String id_conductor,
            String latitud,
            String longitud,
            String placa,
            String id_carrera,
            String numero,
            String id_pedido,
            String distancia) {

        try {

            final String latitud_h;
            final String longitud_h;
            final String rotacion_h;

            sw_subiendo = true;
// set_ubicacion_punto  ----- cargar punto de ubicacion....

            String token= SharedPrefManager.getInstance(this).getDeviceToken();

            JSONObject jsonParam= new JSONObject();
            jsonParam.put("ci", id_conductor);
            jsonParam.put("latitud", latitud);
            jsonParam.put("longitud", longitud);
            jsonParam.put("placa", placa);
            jsonParam.put("id_carrera", id_carrera);
            jsonParam.put("numero", numero);
            jsonParam.put("id_pedido", id_pedido);
            jsonParam.put("distancia", distancia);
            jsonParam.put("rotacion",String.valueOf(rotacion));
            latitud_h=latitud;
            longitud_h=longitud;
            rotacion_h=String.valueOf(rotacion);

            jsonParam.put("token", token);
            String url=getString(R.string.servidor) + "frmTaxi.php?opcion=set_ubicacion_punto_carrera";
            RequestQueue queue = Volley.newRequestQueue(this);


            JsonObjectRequest myRequest= new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jsonParam,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject respuestaJSON) {



                            try {

                                suceso= new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));

                                if (suceso.getSuceso().equals("1")) {

                                    try {
                                        SharedPreferences ped = getSharedPreferences("ultimo_pedido_conductor", MODE_PRIVATE);
                                        int numero_carrera = Integer.parseInt(ped.getString("numero", "1"));
                                        numero_carrera++;
                                        SharedPreferences.Editor editar = ped.edit();
                                        editar.putString("numero", String.valueOf(numero_carrera));
                                        editar.commit();
                                    }catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }

                                    //final
                                    SharedPreferences punto=getSharedPreferences("mi ubicacion",Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor=punto.edit();
                                    editor.putString("latitud",latitud_h);
                                    editor.putString("longitud",longitud_h);
                                    editor.putString("rotacion",rotacion_h);
                                    editor.commit();

                                    Log.w("Servicio cargar punto","Se cargo la ubicacion al servidor ("+latitud_a+","+longitud_a+")");

                                    reconectando("EN SERVICIO - ►");

                                }
                                else
                                {
                                    try {
                                        SharedPreferences ped = getSharedPreferences("ultimo_pedido_conductor", MODE_PRIVATE);
                                        int numero_carrera = Integer.parseInt(ped.getString("numero", "1"));
                                        numero_carrera++;
                                        SharedPreferences.Editor editar = ped.edit();
                                        editar.putString("numero", String.valueOf(numero_carrera));
                                        editar.commit();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    reconectando("EN SERVICIO ☻");
                                }
                                //final
                                sw_subiendo=false;
                            } catch (JSONException e) {


                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
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

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            //   mp.stop();
            show_estoy_ocupado();
            sw_estado_servicio=false;
        }catch (Exception e)
        {
            int id_stard=id_star;
            Log.e("Wake Lock  error","release"+id_stard);
        }
    }

    public static boolean isConnectingToInternet(Context _context) {
        ConnectivityManager connectivity = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

     public  double getDistancia(double lat_a,double lon_a, double lat_b, double lon_b){
        long  Radius = 6371000;
        double dLat = Math.toRadians(lat_b-lat_a);
        double dLon = Math.toRadians(lon_b-lon_a);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) * Math.sin(dLon /2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double distancia=(Radius * c);
        return  distancia;
    }

    private void reconectando(String mensaje){
        SharedPreferences perfil = getSharedPreferences("perfil_conductor", MODE_PRIVATE);
        if(perfil.getString("estado","0").equals("0")==true){
            show_estoy_ocupado();
        }else{
            Intent intent=new Intent(this, Menu_taxi.class);
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            this.getApplicationContext(),
                            1234,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );


            NotificationCompat.Builder mBuilder =
                    (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_logo_conductor)
                            .setContentTitle(getString(R.string.app_name))
                            .setContentIntent(resultPendingIntent)
                            .setContentText(mensaje)
                            .setPriority(2)
                            .setOnlyAlertOnce(false);
            mBuilder.setOngoing(true);
            mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1234, mBuilder.build());
        }


    }
    private void show_estoy_libre() {

        Intent intent=new Intent(this, Menu_taxi.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this.getApplicationContext(),
                        1234,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        Uri sonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_logo_app)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentIntent(resultPendingIntent)
                        .setContentText("ESTOY LIBRE ("+id_star+")")
                        .setPriority(2)
                        .setOnlyAlertOnce(false);
        mBuilder.setOngoing(true);
        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1234, mBuilder.build());
    }

    private void show_estoy_ocupado() {
        MediaPlayer mp;
        mNotificationManager.cancel(1234);
    }
}