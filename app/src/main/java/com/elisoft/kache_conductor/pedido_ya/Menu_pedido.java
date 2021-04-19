package com.elisoft.kache_conductor.pedido_ya;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.elisoft.kache_conductor.chat.handy.Menu_Canal;
import com.elisoft.kache_conductor.chat.handy.Servicio_enviar_audio;
import com.elisoft.kache_conductor.detalle_pedido_carrito.Detalle_pedido_delivery;
import com.elisoft.kache_conductor.notificaciones.SharedPrefManager;
import com.elisoft.kache_conductor.solicitudes.Solicitudes;
import com.elisoft.kache_conductor.Cancelar_pedido_taxi;
import com.elisoft.kache_conductor.Constants;
import com.elisoft.kache_conductor.LLamar_usuario;
import com.elisoft.kache_conductor.MapAnimator;
import com.elisoft.kache_conductor.Menu_taxi;
import com.elisoft.kache_conductor.SqLite.AdminSQLiteOpenHelper;
import com.elisoft.kache_conductor.Suceso;
import com.elisoft.kache_conductor.animacion.Inicio_GradientBackgroundExampleActivity;
import com.elisoft.kache_conductor.chat.Chat;
import com.elisoft.kache_conductor.servicio.Servicio_cargar_punto_google;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.elisoft.kache_conductor.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.multidex.MultiDex;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import okio.ByteString;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class Menu_pedido extends AppCompatActivity
        implements
        OnMapReadyCallback,View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener {
    //solo para la publicidad

    private  String AD_UNIT_ID ; //My code
//fin publicidad

    private static final String LOG_TAG = "AudioRecordTest";
    public static final int RC_RECORD_AUDIO = 1000;
    public static String sRecordedFileName;
    private MediaRecorder mRecorder;
  /*
   audio
     */

    //variables de solicitudes
    private ProgressDialog pDialog;
    // fin de variables de solicitudes


    LocationManager manager=null;
    AlertDialog alert = null;
    AlertDialog alert2 = null;

    boolean sw_grabacion;



    private static final String LOGTAG = "android-localizacion";

    private static final int PETICION_PERMISO_LOCALIZACION = 101;
    private static final int PETICION_CONFIG_UBICACION = 201;

    private GoogleApiClient apiClient;

    private LocationRequest locRequest;



    private GoogleMap mMap;



    private TextView ubicacion,tv_nombre,tv_direccion,tv_referencia,tv_clase_vehiculo,tv_monto_carrito;
    private Button bt_finalizar,bt_cancelar,bt_comenzar_carrera,bt_nueva_carrera;

    Button bt_uno,bt_dos,bt_solicitud;

    ImageButton bt_contacto_usuario,bt_chat;
    private LinearLayout estado;



    AlertDialog.Builder dialogo1 ;
    LinearLayout lpedido,ll_carrera ,ll_perfil_usuario;
    ImageView im_perfil_usuario;

    FloatingActionButton fb_ruta;



    int sw_acercar_a_mi_ubicacion;
    double latitud,longitud;
    double altura=0;

    Switch sw_mapa;

    boolean marcado_sw=false;
    boolean pedido=false;

    Suceso suceso;

    JSONObject rutas=null;
    private List<LatLng> lista_ruta_empresa;
    private List<LatLng> lista_ruta_pasajero;


    ProgressBar pb_cargando;

//Intent servicio_cargar_punto;


    LinearLayout.LayoutParams cero = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
    LinearLayout.LayoutParams wrap_content  = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);




    int version=0;
    int cantidad_conexion=0;


    ImageView im_grabar;
    MediaPlayer mp;


    Marker m_pasajero=null;
    Marker m_empresa=null;

    FrameLayout fl_map;
    Date fecha_conexion;

    int clase_vehiculo=5;


    RequestQueue queue=null;
    RequestQueue queue_cantidad=null;


    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onStart() {
        cantidad_conexion=0;
        if(estaConectado()) {
            boolean gps=manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!gps) {
                AlertNoGps();
            }


            ///verifica si el GPS esta activo.
            actualizar();
            get_credito();

            try{
                cargar_mapa_estilo_con_horario();}
            catch (Exception e){}
        }else{
            mensaje_error_final("Tu Dispositivo no tiene Conexion a Internet.");
        }



        super.onStart();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultiDex.install(this);
        setContentView(R.layout.activity_menu_pedido);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);






        ubicacion=(TextView)findViewById(R.id.ubicacion);
        tv_nombre=(TextView)findViewById(R.id.tv_nombre);
        tv_direccion=(TextView)findViewById(R.id.tv_direccion);
        tv_referencia=(TextView)findViewById(R.id.tv_referencia);
        tv_clase_vehiculo=(TextView)findViewById(R.id.tv_clase_vehiculo);
        tv_monto_carrito=(TextView)findViewById(R.id.tv_monto_carrito);
        bt_cancelar = (Button) findViewById(R.id.bt_cancelar);
        bt_comenzar_carrera = (Button) findViewById(R.id.bt_comenzar_carrera);
        bt_nueva_carrera = (Button) findViewById(R.id.bt_nueva_carrera);
        bt_finalizar = (Button) findViewById(R.id.bt_finalizar);

        bt_uno = (Button) findViewById(R.id.bt_uno);
        bt_dos = (Button) findViewById(R.id.bt_dos);
        bt_solicitud = (Button) findViewById(R.id.bt_solicitud);

        estado=(LinearLayout) findViewById(R.id.estado);
        bt_contacto_usuario=(ImageButton) findViewById(R.id.bt_contacto_usuario);
        lpedido=(LinearLayout)findViewById(R.id.lpedido);
        ll_carrera=(LinearLayout)findViewById(R.id.ll_carrera);

        fl_map=findViewById(R.id.fl_map);


        bt_chat=(ImageButton)findViewById(R.id.bt_chat);

        ll_perfil_usuario=(LinearLayout)findViewById(R.id.ll_perfil_usuario);

        im_perfil_usuario=(ImageView)findViewById(R.id.im_perfil_usuario);

        dialogo1= new AlertDialog.Builder(this);

        fb_ruta=(FloatingActionButton)findViewById(R.id.fb_ruta);
        pb_cargando=(ProgressBar) findViewById(R.id.pb_cargando);

        sw_mapa=(Switch)findViewById(R.id.sw_mapa);

        im_grabar=(ImageView)findViewById(R.id.im_grabar);

        fb_ruta.setOnClickListener(this);

        bt_cancelar.setOnClickListener(this);
        bt_comenzar_carrera.setOnClickListener(this);
        bt_nueva_carrera.setOnClickListener(this);
        bt_finalizar.setOnClickListener(this);
        bt_contacto_usuario.setOnClickListener(this);
        sw_mapa.setOnClickListener(this);
        ll_perfil_usuario.setOnClickListener(this);

        bt_chat.setOnClickListener(this);

        //im_grabar.setOnClickListener(this);

        pb_cargando.setLayoutParams(cero);


        bt_uno.setOnClickListener(this);
        bt_dos.setOnClickListener(this);
        bt_solicitud.setOnClickListener(this);

        //       servicio_cargar_punto=new Intent(this,Servicio_cargar_punto.class);





        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        SharedPreferences usuario=getSharedPreferences("perfil_conductor",MODE_PRIVATE);
        if(usuario.getString("ci","").equals("")==true)
        {
            eliminar_datos_share();
            cerrar_sesion();
        }






        // localizacion automatica

        //Construcción cliente API Google
        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        enableLocationUpdates();
        manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );





//AUDIO
        sRecordedFileName = getCacheDir().getAbsolutePath() + "/audiorecordtest.3gp";
        im_grabar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(LOG_TAG, "onTouch: " + event.getAction());
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    preparar_mensaje_canal();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    detener_grabacion();
                }
                return true;
            }
        });
//AUDIO
        setSonido(0);




        fecha_conexion = new Date();




    }




    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Porfavor presione dos veces para salir", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }


    private void actualizar() {
        SharedPreferences prefe = getSharedPreferences("perfil_conductor", Context.MODE_PRIVATE);
        try {
            int id_conductor = Integer.parseInt(prefe.getString("ci", ""));
            // Servicio hilo = new Servicio();
            //hilo.execute(getString(R.string.servidor) + "frmTaxi.php?opcion=get_pedido_estado_por_id_conductor", "1", String.valueOf(id_conductor));// parametro que recibe el doinbackground
            servicio_get_pedido_estado_por_id_conductor(String.valueOf(id_conductor));
        } catch (Exception e) {
            Log.e("actualizar",e.toString());
        }

    }

    private void servicio_get_pedido_estado_por_id_conductor(String id_conductor) {

        pb_cargando.setLayoutParams(wrap_content);
        try {
            String v_url= getString(R.string.servidor) + "frmTaxi.php?opcion=get_pedido_estado_por_id_conductor";

            JSONObject jsonParam= new JSONObject();
            jsonParam.put("ci", id_conductor);
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
                            pb_cargando.setLayoutParams(cero);

                            try {
                                suceso=new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));

                                if (suceso.getSuceso().equals("1")) {
                                    SharedPreferences perfil = getSharedPreferences("perfil_conductor", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = perfil.edit();
                                    editor.putString("estado", respuestaJSON.getString("estado"));
                                    editor.commit();

                                    //FINAL
                                    //SIN PEDIDO
                                   // vista_button(true,false,false);
                                   // iniciar_verificacion_version();
                                    finish();
                                } else if(suceso.getSuceso().equals("2"))
                                {
                                    SharedPreferences perfil = getSharedPreferences("perfil_conductor", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = perfil.edit();
                                    editor.putString("estado", respuestaJSON.getString("estado"));
                                    editor.commit();


                                    //FINAL
                                    //SIN PEDIDO
                                   // vista_button(true,false,false);
                                   // iniciar_verificacion_version();
                                    finish();
                                }else if(suceso.getSuceso().equals("3")){

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
                                    double latitud_fin=Double.parseDouble(usu.getJSONObject(0).getString("latitud_final"));
                                    double longitud_fin=Double.parseDouble(usu.getJSONObject(0).getString("longitud_final"));
                                    String direccion_fin=usu.getJSONObject(0).getString("direccion_final");

                                    int clase=Integer.parseInt(usu.getJSONObject(0).getString("clase_vehiculo"));

                                    String smonto_total=usu.getJSONObject(0).getString("monto_total");
                                    String smonto_pedido=usu.getJSONObject(0).getString("monto_pedido");

                                    String snombre_lugar = usu.getJSONObject(0).getString("nombre_lugar");
                                    String sdireccion_lugar = usu.getJSONObject(0).getString("direccion_lugar");
                                    String stelefono_lugar = usu.getJSONObject(0).getString("telefono_lugar");
                                    String swhatsapp_lugar = usu.getJSONObject(0).getString("whatsapp_lugar");
                                    String sdireccion_logo_lugar = usu.getJSONObject(0).getString("direccion_logo_lugar");
                                    String sdireccion_banner_lugar = usu.getJSONObject(0).getString("direccion_banner_lugar");
                                    double latitud_lugar = Double.parseDouble(usu.getJSONObject(0).getString("latitud_lugar"));
                                    double longitud_lugar = Double.parseDouble(usu.getJSONObject(0).getString("longitud_lugar"));


                                    cargar_pedido_en_curso(id_pedido,
                                            id_usuario,
                                            fecha_pedido,
                                            fecha_proceso,
                                            mensaje,estado,
                                            latitud,
                                            longitud,
                                            nombre_usuario,
                                            celular,
                                            clase,
                                            latitud_fin,
                                            longitud_fin,
                                            direccion_fin,
                                            smonto_pedido,
                                            smonto_total,
                                             snombre_lugar,
                                     sdireccion_lugar,
                                     stelefono_lugar,
                                     swhatsapp_lugar,
                                     sdireccion_logo_lugar,
                                     sdireccion_banner_lugar,
                                     latitud_lugar,
                                     longitud_lugar);

                                    //FINAL
                                    //PEDIDO ACEPTADO

                                        vista_button(false,true,false);


                                }else if(suceso.getSuceso().equals("4")){


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
                                    double latitud_fin=Double.parseDouble(usu.getJSONObject(0).getString("latitud_final"));
                                    double longitud_fin=Double.parseDouble(usu.getJSONObject(0).getString("longitud_final"));
                                    String direccion_fin=usu.getJSONObject(0).getString("direccion_final");

                                    int clase=Integer.parseInt(usu.getJSONObject(0).getString("clase_vehiculo"));

                                    String smonto_total=usu.getJSONObject(0).getString("monto_total");
                                    String smonto_pedido=usu.getJSONObject(0).getString("monto_pedido");


                                    String snombre_lugar = usu.getJSONObject(0).getString("nombre_lugar");
                                    String sdireccion_lugar = usu.getJSONObject(0).getString("direccion_lugar");
                                    String stelefono_lugar = usu.getJSONObject(0).getString("telefono_lugar");
                                    String swhatsapp_lugar = usu.getJSONObject(0).getString("whatsapp_lugar");
                                    String sdireccion_logo_lugar = usu.getJSONObject(0).getString("direccion_logo_lugar");
                                    String sdireccion_banner_lugar = usu.getJSONObject(0).getString("direccion_banner_lugar");
                                    double latitud_lugar = Double.parseDouble(usu.getJSONObject(0).getString("latitud_lugar"));
                                    double longitud_lugar = Double.parseDouble(usu.getJSONObject(0).getString("longitud_lugar"));

                                    cargar_pedido_en_curso(id_pedido,id_usuario,fecha_pedido,fecha_proceso,mensaje,estado,latitud,longitud,nombre_usuario,celular,clase,latitud_fin,longitud_fin,direccion_fin,smonto_pedido,
                                            smonto_total,
                                            snombre_lugar,
                                            sdireccion_lugar,
                                            stelefono_lugar,
                                            swhatsapp_lugar,
                                            sdireccion_logo_lugar,
                                            sdireccion_banner_lugar,
                                            latitud_lugar,
                                            longitud_lugar);


                                    SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
                                    SharedPreferences.Editor editor=pedido.edit();
                                    JSONArray carrera=respuestaJSON.getJSONArray("carrera");
                                    editor.putString("id_carrera",carrera.getJSONObject(0).getString("id"));
                                    editor.putString("numero",respuestaJSON.getString("numero"));
                                    editor.commit();

                                    //FINAL
                                    //EN CARRERA LA COMENZADA

                                        vista_button(false, false, true);

                                }else{
                                    //FINAL
                                    //SIN PEDIDO
                                    vista_button(true,false,false);
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
                    pb_cargando.setLayoutParams(cero);
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
            pb_cargando.setLayoutParams(cero);
        }
    }

    public  void setSonido(int sw)
    {
        SharedPreferences sonido=getSharedPreferences(getString(R.string.sonido),MODE_PRIVATE);
        SharedPreferences.Editor editor=sonido.edit();
        editor.putInt("sonido",sw);
        editor.commit();

    }

    public void cargar_mapa_estilo_con_horario(){
        SharedPreferences sconfiguracion = getSharedPreferences(getString(R.string.share_configuracion), Context.MODE_PRIVATE);
        int hora_inicio=6;
        int hora_fin=18;
        int hora=12;
        try{
            hora_inicio=sconfiguracion.getInt(getString(R.string.hora_inicio),6);
            hora_fin=sconfiguracion.getInt(getString(R.string.hora_fin),18);


            Calendar calendario = new GregorianCalendar();
            hora =calendario.get(Calendar.HOUR_OF_DAY);
            Log.w("hora:",""+hora);

        }catch (Exception e){

        }

        if(hora_inicio<=hora && hora_fin>hora){
            //en el dia
            try {
                // Customise the styling of the base map using a JSON object defined
                // in a raw resource file.
                boolean success = mMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                this, R.raw.style_mapa_dia));

                if (!success) {
                    Log.e("style", "Style parsing failed.");
                }

            } catch (Resources.NotFoundException e) {
                Log.e("style", "Can't find style. Error: ", e);
            }

        }
        else{
            //en la noche
            try {
                // Customise the styling of the base map using a JSON object defined
                // in a raw resource file.
                boolean success = mMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                this, R.raw.style_mapa_noche));

                if (!success) {
                    Log.e("style", "Style parsing failed.");
                }

            } catch (Resources.NotFoundException e) {
                Log.e("style", "Can't find style. Error: ", e);
            }
        }

    }

    public boolean esDiurno(){
        boolean sw=false;

        SharedPreferences sconfiguracion = getSharedPreferences(getString(R.string.share_configuracion), Context.MODE_PRIVATE);
        int hora_inicio=6;
        int hora_fin=18;
        int hora=12;
        try{
            hora_inicio=sconfiguracion.getInt(getString(R.string.hora_inicio),6);
            hora_fin=sconfiguracion.getInt(getString(R.string.hora_fin),18);


            Calendar calendario = new GregorianCalendar();
            hora =calendario.get(Calendar.HOUR_OF_DAY);
            Log.w("hora:",""+hora);

        }catch (Exception e){

        }

        if(hora_inicio<=hora && hora_fin>hora){
            //en el dia
            sw=true;

        }
        else{
            //en la noche
            sw=false;
        }
        return sw;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;

        cargar_mapa_estilo_con_horario();



        m_pasajero =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(0,0))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_point1))
                .anchor((float)0.5,(float)0.5)
                .flat(true)
                .rotation(0));


        m_empresa =mMap.addMarker(new MarkerOptions()
                .position(new LatLng(0,0))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_punto_fin_1))
                .anchor((float)0.5,(float)0.5)
                .flat(true)
                .rotation(0));





        cargar_datos_de_pedido_textview();
        // Controles UI
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);


            View mapView = (View) getSupportFragmentManager().findFragmentById(R.id.map).getView();
//bicacion del button de Myubicacion de el fragento..
            View btnMyLocation = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            params.setMargins(20, 0, 0, 0);
            btnMyLocation.setLayoutParams(params);
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



        SharedPreferences pe=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
        if(pe.getString("id_pedido","").equals("")==false)
        {
            pedido=true;
            marcado_sw=true;
        }
        else
        {pedido=false;
        }
        marcar_ruta_general();
    }

    public void marcar_ruta_general()
    {
        SharedPreferences ped = getSharedPreferences("ultimo_pedido_conductor", MODE_PRIVATE);
        if(ped.getString("id_carrera","").equals("1") && clase_vehiculo==5)
        {
            marcar_ruta_direccion_pasajero();
        }else
        {
            try{
                mMap.clear();
                m_pasajero=null;
                m_empresa=null;
                crear_marker();
                marcar_ruta_empresa(pedido);
            }catch (Exception e)
            {

            }
        }
    }

    @Override
    public void onClick(View v) {

        SharedPreferences spedido = getSharedPreferences("ultimo_pedido_conductor", Context.MODE_PRIVATE);
        SharedPreferences pedido2=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
        SharedPreferences perfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);

        SharedPreferences punto = getSharedPreferences("mi ubicacion_2", Context.MODE_PRIVATE);
        double latitud_fin = Double.parseDouble(punto.getString("latitud", "0"));
        double longitud_fin = Double.parseDouble(punto.getString("longitud", "0"));
        double altura_fin = Double.parseDouble(punto.getString("altura", "0"));

        switch (v.getId())
        {
            case R.id.bt_uno:
                break;
            case R.id.bt_dos:
                break;
            case R.id.bt_solicitud:
                startActivity(new Intent(this, Solicitudes.class));
                break;
            case R.id.im_grabar:
                startActivity(new Intent(this, Menu_Canal.class));
                break;
            case R.id.bt_chat:
                try {
                    SharedPreferences preferencias = getSharedPreferences("ultimo_pedido_conductor", Context.MODE_PRIVATE);
                    Intent it_chat=new Intent(getApplicationContext(),Chat.class);
                    it_chat.putExtra("id_usuario",preferencias.getString("id_usuario",""));
                    it_chat.putExtra("titulo",preferencias.getString("nombre_usuario",""));
                    startActivity(it_chat);
                }catch (Exception e)
                {}
                break;
            case  R.id.sw_mapa:

                boolean b=sw_mapa.isChecked();

                if(b==true) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }
                else
                {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
                break;
            case  R.id.bt_cancelar:

                int clase_v=1;
                SharedPreferences pe2=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
                int id_p=0 ;
                try{
                    id_p=Integer.parseInt(pe2.getString("id_pedido","0"));
                }catch (Exception e) {
                    id_p=0;
                }

                try{
                    clase_v=Integer.parseInt(pe2.getString("clase_vehiculo","5"));
                }catch (Exception e){
                    clase_v=1;
                }
                if (clase_v==5)
                {
                    Intent ed_c=new Intent(this, Detalle_pedido_delivery.class);
                    ed_c.putExtra("id_pedido",id_p);
                    startActivity(ed_c);
                }else {
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
                    dialogo1.setTitle(getString(R.string.app_name));
                    dialogo1.setMessage("¿Desea cancelar?");
                    dialogo1.setCancelable(false);
                    dialogo1.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            //cargamos los datos
                            startActivity(new Intent(getApplicationContext(), Cancelar_pedido_taxi.class));
                            SharedPreferences pe = getSharedPreferences("ultimo_pedido_conductor", MODE_PRIVATE);
                            if (pe.getString("id_pedido", "").equals("") == false) {

                                vista_button(false, true, false);
                            } else {
                                pedido = false;
                                vista_button(true, false, false);

                            }

                        }
                    });
                    dialogo1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {

                        }
                    });
                    dialogo1.show();
                }

                break;



            case R.id.fb_ruta:
                marcado_sw=false;
                marcar_ruta_general();
                break;
            case R.id.bt_finalizar:
                //cargar video
                loadRewardedVideoAd();



                try{
                    clase_vehiculo=Integer.parseInt(pedido2.getString("clase_vehiculo","5"));
                }catch (Exception e){
                    clase_vehiculo=5;
                }

                servicio_get_carrera_por_id(getString(R.string.servidor) + "frmCarrera.php?opcion=get_carrera_por_id", spedido.getString("id_pedido",""),perfil.getString("ci",""),perfil.getString("placa",""),spedido.getString("id_carrera",""));






                break;
            case R.id.bt_comenzar_carrera:



                // Servicio_taxi hilo_ = new Servicio_taxi();
                // hilo_.execute(getString(R.string.servidor) + "frmCarrera.php?opcion=comenzar_carrera", "10", spedido.getString("id_pedido", ""), String.valueOf(latitud_fin), String.valueOf(longitud_fin), String.valueOf(altura_fin), perfil.getString("ci", ""), perfil.getString("placa", ""), spedido.getString("id_usuario", ""), tv_direccion.getText().toString());
               // servicio_comenzar_carrera(spedido.getString("id_pedido", ""), String.valueOf(latitud_fin), String.valueOf(longitud_fin), String.valueOf(altura_fin), perfil.getString("ci", ""), perfil.getString("placa", ""), spedido.getString("id_usuario", ""), tv_direccion.getText().toString());


                break;
            case R.id.bt_nueva_carrera:


                int id_pedido=0 ;
                try{
                    id_pedido=Integer.parseInt(pedido2.getString("id_pedido","0"));
                }catch (Exception e) {
                    id_pedido=0;
                }

                try{
                    clase_vehiculo=Integer.parseInt(pedido2.getString("clase_vehiculo","5"));
                }catch (Exception e){
                    clase_vehiculo=5;
                }
                if (clase_vehiculo==5)
                {
                    Intent ed_c=new Intent(this,Detalle_pedido_delivery.class);
                    ed_c.putExtra("id_pedido",id_pedido);
                    startActivity(ed_c);
                }else {
                    //Servicio_taxi hilo12 = new Servicio_taxi();
                    //hilo12.execute(getString(R.string.servidor) + "frmCarrera.php?opcion=get_carrera_por_id", "14", spedido.getString("id_pedido", ""), perfil.getString("ci", ""), perfil.getString("placa", ""), spedido.getString("id_carrera", ""));
                    servicio_get_carrera_por_id_nueva(
                            getString(R.string.servidor) + "frmCarrera.php?opcion=get_carrera_por_id",
                            spedido.getString("id_pedido", ""),
                            perfil.getString("ci", ""),
                            perfil.getString("placa", ""),
                            spedido.getString("id_carrera", ""));
                }

                break;
            case R.id.bt_contacto_usuario:
                // llamar_usuario();
                break;

            case R.id.ll_perfil_usuario:
                SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
                int id_carrera=0;
                try{
                        id_carrera=Integer.parseInt(pedido.getString("id_carrera","0"));
                    }   catch (Exception ee){
                        id_carrera=0;
                    }


                if (id_carrera != 0) {
                    llamar_usuario_start();
                }


                break;
            case R.id.bt_empresa:

                try {
                    SharedPreferences pedido1=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
                    int id_pe=Integer.parseInt(pedido1.getString("id_pedido","0"));

                     Servicio_corporativo hilo_p = new  Servicio_corporativo();
                    hilo_p.execute(getString(R.string.servidor) + "frmGuia_turistica.php?opcion=get_lugar_por_id_pedido", "2", String.valueOf(id_pe));
                } catch (Exception e) {

                }
                break;

        }

    }

    private void formulario_finalizar_delivery(final double distancia, double d_altura,
                                               String tiempo,
                                               String monto_p,
                                               String sdetalle,
                                               String stotal,
                                               String notificacion_monto)
    {
        SharedPreferences punto = getSharedPreferences("mi ubicacion_2", Context.MODE_PRIVATE);
        final double latitud_fin = Double.parseDouble(punto.getString("latitud", "0"));
        final double longitud_fin = Double.parseDouble(punto.getString("longitud", "0"));



        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(Menu_pedido.this);
        View promptView = layoutInflater.inflate(R.layout.input_text, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Menu_pedido.this);
        alertDialogBuilder.setView(promptView);

        final EditText et_monto_total = (EditText) promptView.findViewById(R.id.et_monto_total);
        final TextView tv_distancia = (TextView) promptView.findViewById(R.id.tv_distancia);
        final TextView tv_detalle = (TextView) promptView.findViewById(R.id.tv_detalle);
        final TextView tv_tiempo = (TextView) promptView.findViewById(R.id.tv_tiempo);
        final TextView tv_total = (TextView) promptView.findViewById(R.id.tv_total);
        DecimalFormat precision = new DecimalFormat("0.00");

        tv_detalle.setText(sdetalle);
        tv_total.setText(stotal+" BOB");

        //tv_distancia.setText("Distancia recorrida:"+distancia+" Mtrs.");
        //tv_tiempo.setText("Tiempo recorrido:"+tiempo+" Hrs.");
        //et_monto_total.setText(monto_p);

        if(notificacion_monto.equals("0"))
        {
            et_monto_total.setVisibility(View.INVISIBLE);
            tv_total.setText("- - -");
            tv_detalle.setText("- - - ");
        }

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
                        SharedPreferences perfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);

                        SharedPreferences punto = getSharedPreferences("mi ubicacion", Context.MODE_PRIVATE);
                        double latitud_fin = Double.parseDouble(punto.getString("latitud", "0"));
                        double longitud_fin = Double.parseDouble(punto.getString("longitud", "0"));
                        double altura_fin = Double.parseDouble(punto.getString("altura", "0"));

/*
                        Servicio_taxi hilo_ = new Servicio_taxi();
                        hilo_.execute(getString(R.string.servidor) + "frmCarrera.php?opcion=finalizar_pedido", "9",
                                pedido.getString("id_pedido",""),
                                String.valueOf(latitud_fin),
                                String.valueOf(longitud_fin),
                                String.valueOf(altura_fin),
                                perfil.getString("ci",""),
                                perfil.getString("placa",""),
                                pedido.getString("id_usuario",""),
                                pedido.getString("id_carrera",""),
                                et_monto_total.getText().toString().trim(),
                                String.valueOf(distancia),
                                tv_direccion.getText().toString(),
                                "comentario");*/
                        servicio_volley_finalizar_pedido(
                                pedido.getString("id_pedido",""),
                                String.valueOf(latitud_fin),
                                String.valueOf(longitud_fin),
                                String.valueOf(altura_fin),
                                perfil.getString("ci",""),
                                perfil.getString("placa",""),
                                pedido.getString("id_usuario",""),
                                pedido.getString("id_carrera",""),
                                et_monto_total.getText().toString().trim(),
                                String.valueOf(distancia),
                                tv_direccion.getText().toString(),
                                "comentario");


                    }
                })
                .setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();

    }


    private void servicio_get_carrera_por_id_nueva(
            String v_url,
            String id_pedido,
            String ci,
            String placa,
            String id_carrera) {

        final double[] latitud_inicio = {0};
        final double[] longitud_inicio = { 0 };
        final double[] altura_inicio = { 0 };
        final double[] distancia_i = {0};

        final String[] tiempo = {""};
        final String[] monto = { "" };

        try{
            pb_cargando.setLayoutParams(wrap_content);}
        catch (Exception e)
        { }

        try {



// set_ubicacion_punto  ----- cargar punto de ubicacion....

            String token= SharedPrefManager.getInstance(this).getDeviceToken();

            JSONObject jsonParam= new JSONObject();
            jsonParam.put("id_pedido", id_pedido);
            jsonParam.put("ci", ci);
            jsonParam.put("placa", placa);
            jsonParam.put("id_carrera", id_carrera);

            jsonParam.put("token", token);
            String url=v_url;
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

                            SharedPreferences punto = getSharedPreferences("mi ubicacion_2", Context.MODE_PRIVATE);
                            double latitud_fin = Double.parseDouble(punto.getString("latitud", "0"));
                            double longitud_fin = Double.parseDouble(punto.getString("longitud", "0"));
                            double altura_fin = Double.parseDouble(punto.getString("altura", "0"));

                            try{
                                pb_cargando.setLayoutParams(cero);
                            }catch (Exception e)
                            {}

                            try {

                                suceso= new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));

                                if (suceso.getSuceso().equals("1")) {
                                    try{
                                        JSONArray usu=respuestaJSON.getJSONArray("carrera");

                                        latitud_inicio[0] =Double.parseDouble(usu.getJSONObject(0).getString("latitud_inicio"));
                                        longitud_inicio[0] =Double.parseDouble(usu.getJSONObject(0).getString("longitud_inicio"));
                                        altura_inicio[0] =Double.parseDouble(usu.getJSONObject(0).getString("altura_inicio"));
                                        tiempo[0] =usu.getJSONObject(0).getString("tiempo");
                                        monto[0] =respuestaJSON.getString("monto");

                                        distancia_i[0] =Double.parseDouble(respuestaJSON.getString("distancia"));


                                        //obtuvo latitud longitud y altura de la carrera que va a finalizar.
                                        double d_altura = altura_inicio[0] - altura_fin;
                                        mostrar_datos_finalizar_carrera(distancia_i[0], d_altura, tiempo[0], monto[0]);
                                    }catch (Exception e)
                                    {
                                        mensaje_error("Vuelva a intentarlo de nuevo.");
                                    }

                                    //final

                                }
                                else
                                {
                                    mensaje_error("Vuelva a intentarlo de nuevo.");
                                }

                                //----------- final


                                vaciar_toda_la_base_de_datos_pedido_notificacion();
                                SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
                                int id_pedido=0,id_carrera=0;
                                try{
                                    id_pedido=Integer.parseInt(pedido.getString("id_pedido","0"));
                                }catch (Exception e){
                                    try{
                                        id_carrera=Integer.parseInt(pedido.getString("id_carrera","0"));
                                    }   catch (Exception ee){
                                        id_carrera=0;
                                        id_pedido=0;
                                    }

                                    if(id_pedido==0)
                                    {
                                        vista_button(true,false,false);
                                    }
                                    else if(id_carrera==0){
                                        vista_button(false,false,true);
                                    }else{
                                        vista_button(false,true,false);
                                    }
                                }

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


            myRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(myRequest);


        } catch (Exception e) {

        }

    }



    private void servicio_get_carrera_por_id(String v_url,String v_id_pedido,String v_ci,String v_placa,String v_id_carrera) {


        try {
            JSONObject jsonParam= new JSONObject();
            jsonParam.put("id_pedido", v_id_pedido);
            jsonParam.put("ci", v_ci);
            jsonParam.put("placa", v_placa);
            jsonParam.put("id_carrera", v_id_carrera);


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

                            pb_cargando.setLayoutParams(cero);
                            double latitud_inicio=0,longitud_inicio=0,altura_inicio=0;
                            double distancia_i=0;
                            String tiempo="",monto="",sdetalle="",stotal="0.0 BOB";

                            try {
                                suceso=new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));

                                if (suceso.getSuceso().equals("1")) {
                                    try{
                                        JSONArray usu=respuestaJSON.getJSONArray("carrera");

                                        String latitud_i=usu.getJSONObject(0).getString("latitud_inicio");
                                        String longitud_i=usu.getJSONObject(0).getString("longitud_inicio");
                                        String altura=usu.getJSONObject(0).getString("altura_inicio");
                                        tiempo=usu.getJSONObject(0).getString("tiempo");

                                        monto=respuestaJSON.getString("monto");
                                        String notificacion_monto=respuestaJSON.getString("notificacion_pedido_finalizado");

                                        distancia_i=Double.parseDouble(respuestaJSON.getString("distancia"));

                                        latitud_inicio=Double.parseDouble(latitud_i);
                                        longitud_inicio=Double.parseDouble(longitud_i);
                                        altura_inicio=Double.parseDouble(altura);

                                        try{
                                            sdetalle=respuestaJSON.getString("detalle");
                                            stotal=respuestaJSON.getString("total");
                                        }
                                        catch (Exception e)
                                        {}


                                        //final
                                        SharedPreferences punto = getSharedPreferences("mi ubicacion", Context.MODE_PRIVATE);
                                        double altura_fin = Double.parseDouble(punto.getString("altura", "0"));

                                        double d_altura = altura_inicio - altura_fin;
                                        if(clase_vehiculo==5)
                                        {
                                            //DELIVERY
                                            SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
                                            String smonto_total=pedido.getString("monto_total","0");
                                            String smonto_pedido=pedido.getString("monto_pedido","0");

                                            double total=Double.parseDouble(smonto_total)+Double.parseDouble(smonto_pedido);
                                            formulario_finalizar_delivery(distancia_i,
                                                    d_altura, tiempo,
                                                    "",
                                                    "Pedido:"+smonto_pedido+" Bs. \nEnvio:"+smonto_total+" Bs",
                                                    ""+total,notificacion_monto);
                                        }else{
                                            formulario_mostrar_datos_finalizar_pedido(distancia_i, d_altura, tiempo,monto,sdetalle,stotal,notificacion_monto);
                                        }


                                    }catch (Exception e)
                                    {Log.e("Error",e.toString());
                                        mensaje_error("Vuelva a intentarlo de nuevo.");
                                    }





                                } else  {
                                    mensaje_error("Vuelva a intentarlo de nuevo.");
                                }


                                ///botones visuales
                                vaciar_toda_la_base_de_datos_pedido_notificacion();
                                SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
                                int id_pedido=0,id_carrera=0;
                                try{
                                    id_pedido=Integer.parseInt(pedido.getString("id_pedido","0"));
                                }catch (Exception e){
                                    try{
                                        id_carrera=Integer.parseInt(pedido.getString("id_carrera","0"));
                                    }   catch (Exception ee){
                                        id_carrera=0;
                                        id_pedido=0;
                                    }

                                    if(id_pedido==0)
                                    {
                                        vista_button(true,false,false);
                                    }
                                    else if(id_carrera==0){
                                        vista_button(false,false,true);
                                    }else{
                                        vista_button(false,true,false);
                                    }
                                }




                            } catch (JSONException e) {
                                e.printStackTrace();
                                mensaje_error("Falla en tu conexión a Internet.");
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pb_cargando.setLayoutParams(cero);
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

            pb_cargando.setLayoutParams(wrap_content);

            myRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(myRequest);
        } catch (Exception e) {
        }

    }






    private void marcar_ruta_primera_carrera() {
        SharedPreferences punto=getSharedPreferences("ultimo_pedido_conductor",Context.MODE_PRIVATE);
        double latitud_fin = Double.parseDouble(punto.getString("latitud_final", "0"));
        double longitud_fin = Double.parseDouble(punto.getString("longitud_final", "0"));
        String direccion_fi= obtener_direccion_string(latitud_fin,longitud_fin);
        try{
            mMap.clear();
            crear_marker();



        }catch (Exception e)
        {

        }
    }





    private void crear_marker() {

        SharedPreferences prefe=getSharedPreferences("ultimo_pedido_conductor", Context.MODE_PRIVATE);
        String id_pedido= prefe.getString("id_pedido","");

        if(id_pedido.equals("")==false)
        {
            double lat=Double.parseDouble(prefe.getString("latitud","0"));
            double lon=Double.parseDouble(prefe.getString("longitud","0"));

            double lat_empresa=Double.parseDouble(prefe.getString("latitud_lugar","0"));
            double lon_empresa=Double.parseDouble(prefe.getString("longitud_lugar","0"));
            String direccion_fi= obtener_direccion_string(lat_empresa,lon_empresa);


            try{

                    m_pasajero = this.mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_point1))
                            .anchor((float)0.5,(float)0.8)
                            .flat(true)
                            .position(new LatLng(lat, lon)));

                    m_empresa = this.mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_punto_fin_1))
                            .anchor((float)0.5,(float)0.8)
                            .flat(true)
                            .title(direccion_fi)
                            .snippet(direccion_fi)
                            .position(new LatLng(lat_empresa, lon_empresa)));

            }catch (Exception e)
            {
            }





        }
        else
        {

        }
    }



    private void cargar_datos_de_pedido_textview() {

        SharedPreferences prefe=getSharedPreferences("ultimo_pedido_conductor", Context.MODE_PRIVATE);
        String id_pedido= prefe.getString("id_pedido","");

        tv_monto_carrito.setText(prefe.getString("monto_pedido","0") + " Bs");

        if(id_pedido.equals("")==false)
        {
            pedido=true;

            double lat=Double.parseDouble(prefe.getString("latitud","0"));
            double lon=Double.parseDouble(prefe.getString("longitud","0"));

            double lat_empresa=Double.parseDouble(prefe.getString("latitud_lugar","0"));
            double lon_empresa=Double.parseDouble(prefe.getString("longitud_lugar","0"));


            try{
                if(m_pasajero==null)
                {
                    m_pasajero = this.mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_point1))
                            .anchor((float)0.5,(float)0.8)
                            .flat(true)
                            .position(new LatLng(lat, lon)));
                }else{
                    m_pasajero.setPosition(new LatLng(lat, lon));
                }

                if(m_empresa==null)
                {
                    m_empresa = this.mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_punto_fin_1))
                            .anchor((float)0.5,(float)0.8)
                            .flat(true)
                            .position(new LatLng(lat_empresa, lon_empresa)));
                }else{
                    m_empresa.setPosition(new LatLng(lat_empresa, lon_empresa));
                }



//mover la camara del mapa

                //agregaranimacion al mover la camara...
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(lat_empresa, lon_empresa))      // Sets the center of the map to Mountain View
                        .zoom(15)                   // Sets the zoom
                        .bearing(0)                // Sets the orientation of the camera to east
                        .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }catch (Exception e)
            {
                e.printStackTrace();
            }






                int id_carrera=0;
                try{
                    id_carrera=Integer.parseInt(prefe.getString("id_carrera","0"));
                }   catch (Exception ee){
                    id_carrera=0;
                }
                if (id_carrera == 0) {
                    //muestra datos de empresa
                   // String direcciones= obtener_direccion(lat_empresa,lon_empresa);
                   // tv_direccion.setText(direcciones);
                    tv_direccion.setText(prefe.getString("direccion_lugar",""));
                    tv_referencia.setText(prefe.getString("indicacion",""));
                    tv_nombre.setText(prefe.getString("nombre_lugar",""));
                    String url = getString(R.string.servidor_web)+"storage/"+prefe.getString("direccion_logo_lugar","");
                    Picasso.with(this).load(url).placeholder(R.drawable.ic_perfil_negro).into(im_perfil_usuario);

                }else
                {
                    //muestra datos de pasajero
                    String direcciones= obtener_direccion(lat,lon);
                    tv_direccion.setText(direcciones);
                    tv_referencia.setText(prefe.getString("indicacion",""));
                    tv_nombre.setText(prefe.getString("nombre_usuario",""));
                    String  url=  getString(R.string.servidor)+"usuario/imagen/perfil/"+prefe.getString("id_usuario","")+"_perfil.png";
                    Picasso.with(this).load(url).placeholder(R.drawable.ic_perfil_negro).into(im_perfil_usuario);
                }
        }
        else
        {
            //poner en 0 altura de Linear layout LPEDIDO de pedidos...
            pedido=false;
        }
    }



    public void marcar_ruta_empresa(boolean sw)
    {
        try{
            if(sw==true)
            {

                //buscamos una ruta para el motista     SOLO CO ACCESO A INTERNET

                SharedPreferences prefe1=getSharedPreferences("ultimo_pedido_conductor", MODE_PRIVATE);
                double lat=Double.parseDouble(prefe1.getString("latitud","0"));
                double lon=Double.parseDouble(prefe1.getString("longitud","0"));
                double lat_empresa=Double.parseDouble(prefe1.getString("latitud_lugar","0"));
                double lon_empresa=Double.parseDouble(prefe1.getString("longitud_lugar","0"));
                if(prefe1.getString("id_pedido","").equals("")==false) {
                     servicio_taxi_ruta("https://maps.googleapis.com/maps/api/directions/json?origin=" + latitud + "," + longitud + "&destination=" + lat_empresa + "," + lon_empresa + "&mode=driving&key="+getString(R.string.google_key_api));

                    try {
                        m_pasajero.setPosition(new LatLng(lat, lon));
                        m_pasajero.showInfoWindow();

                        m_empresa.setPosition(new LatLng(lat_empresa, lon_empresa));
                        m_empresa.showInfoWindow();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    public void marcar_ruta_direccion_pasajero()
    {
        try{


            //buscamos una ruta para el motista     SOLO CO ACCESO A INTERNET

            SharedPreferences prefe1=getSharedPreferences("ultimo_pedido_conductor", MODE_PRIVATE);
            double lat=Double.parseDouble(prefe1.getString("latitud",""));
            double lon=Double.parseDouble(prefe1.getString("longitud",""));

            String nombre_pasajero=prefe1.getString("nombre_usuario","");
            String direccion_fin=prefe1.getString("indicacion","");

            if(prefe1.getString("id_pedido","").equals("")==false) {
                servicio_taxi_ruta_pasajero("https://maps.googleapis.com/maps/api/directions/json?origin=" + latitud + "," + longitud + "&destination=" + lat + "," + lon + "&mode=driving&key="+getString(R.string.google_key_api));



                try {

                    m_pasajero.setTitle(nombre_pasajero);
                    m_pasajero.setSnippet(direccion_fin);
                    m_pasajero.setPosition(new LatLng(lat, lon));
                } catch (Exception e) {

                }
            }


        }catch (Exception e)
        {

        }
    }

    public void dibujar_ruta_empresa(JSONObject jObject){


        if (lista_ruta_empresa== null) {
            lista_ruta_empresa = new ArrayList<>();
        } else {
            lista_ruta_empresa.clear();
        }



        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;
        boolean sw_punto=false;
        LatLng punto=new LatLng(0,0);
        PolylineOptions polylineOptions = new PolylineOptions();

        String tiempo="";
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

                            //PUNTO AGREGADO
                            lista_ruta_empresa.add(punto);

                        }
                    }
                    tiempo=(String)((JSONObject)((JSONObject)jLegs.get(j)).get("duration")).get("text");
                }
            }

            try
            {
                mMap.clear();
                m_pasajero=null;
                m_empresa=null;
                crear_marker();
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            //dibujar las lineas

            if(sw_punto==true) {


                punto=new LatLng(latitud,longitud);
                start_nimacion_empresa();
                /*
                if(esDiurno()){
                    mMap.addPolyline(polylineOptions.width(15).color(Color.BLACK));
                }else{
                    mMap.addPolyline(polylineOptions.width(15).color(getResources().getColor(R.color.colorPrimary_light)));
                }
                */


                //agregaranimacion al mover la camara...
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(punto)      // Sets the center of the map to Mountain View
                        .zoom(17)                   // Sets the zoom
                        .bearing(0)                // Sets the orientation of the camera to east
                        .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                marcado_sw=false;
            }
            try {
                SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
                double latitud_pedido=Double.parseDouble(pedido.getString("latitud","0"));
                double longitud_pedido=Double.parseDouble(pedido.getString("longitud","0"));
                m_pasajero.setPosition(new LatLng(latitud_pedido, longitud_pedido));
                m_pasajero.setTitle(""+tiempo);
                m_pasajero.showInfoWindow();

                double latitud_empresa=Double.parseDouble(pedido.getString("latitud_lugar","0"));
                double longitud_empresa=Double.parseDouble(pedido.getString("longitud_lugar","0"));
                String nombre_lugar=pedido.getString("nombre_lugar","");
                m_empresa.setPosition(new LatLng(latitud_empresa, longitud_empresa));
                m_empresa.setTitle(nombre_lugar);
                m_empresa.showInfoWindow();

            } catch (Exception e) {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
        }

    }

    private void start_nimacion_pasajero() {
        if (mMap != null && lista_ruta_pasajero.size()>1) {
            MapAnimator.getInstance().animateRoute(mMap, lista_ruta_pasajero);
        } else {
            Toast.makeText(getApplicationContext(), "No hay ruta", Toast.LENGTH_LONG).show();
        }
    }



    public void dibujar_ruta_pasajero(JSONObject jObject){

        if (lista_ruta_pasajero == null) {
            lista_ruta_pasajero = new ArrayList<>();
        } else {
            lista_ruta_pasajero.clear();
        }

        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;

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

                            lista_ruta_pasajero.add(punto);

                        }
                    }

                }
            }

            try
            {
                mMap.clear();
                m_pasajero=null;
                m_empresa=null;
                crear_marker();
            }catch (Exception e)
            {

            }

            //dibujar las lineas



            punto=new LatLng(latitud,longitud);

            start_nimacion_pasajero();


            //agregaranimacion al mover la camara...
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(punto)      // Sets the center of the map to Mountain View
                    .zoom(17)                   // Sets the zoom
                    .bearing(0)                // Sets the orientation of the camera to east
                    .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            marcado_sw=false;

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
        }

    }


    private void start_nimacion_empresa() {
        if (mMap != null && lista_ruta_empresa.size()>1) {
            MapAnimator.getInstance().animateRoute(mMap, lista_ruta_empresa);
        } else {
            Toast.makeText(getApplicationContext(), "No hay ruta", Toast.LENGTH_LONG).show();
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
    private void cargar_pedido_en_curso(String id_pedido,
                                        String id_usuario,
                                        String fecha_pedido,
                                        String fecha_proceso,
                                        String mensaje,
                                        String estado,
                                        double latitud,
                                        double longitud,
                                        String nombre_usuario,
                                        String celuular,int clase,
                                        double latitud_fin,
                                        double longitud_fin,
                                        String direccion_fin,
                                        String monto_pedido,
                                        String monto_total,
                                        String snombre_lugar,
                                        String sdireccion_lugar,
                                        String stelefono_lugar,
                                        String swhatsapp_lugar,
                                        String sdireccion_logo_lugar,
                                        String sdireccion_banner_lugar,
                                        double latitud_lugar,
                                        double longitud_lugar) {
        SharedPreferences preferencias=getSharedPreferences("ultimo_pedido_conductor", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putString("id_pedido",id_pedido);
        editor.putString("id_usuario",id_usuario);
        editor.putString("fecha_pedido",fecha_pedido);
        editor.putString("fecha_proceso",fecha_proceso);
        editor.putString("indicacion",mensaje);
        editor.putString("latitud",String.valueOf(latitud));
        editor.putString("longitud",String.valueOf(longitud));
        editor.putString("latitud_final",String.valueOf(latitud_fin));
        editor.putString("longitud_final",String.valueOf(longitud_fin));
        editor.putString("direccion_final",direccion_fin);
        editor.putString("nombre_usuario",nombre_usuario);
        editor.putString("celular",celuular);
        editor.putString("estado",estado);
        editor.putString("clase_vehiculo",String.valueOf(clase));
        editor.putString("monto_pedido",String.valueOf(monto_pedido));
        editor.putString("monto_total",String.valueOf(monto_total));

        editor.putString("nombre_lugar",snombre_lugar);
        editor.putString("direccion_lugar",sdireccion_lugar);
        editor.putString("telefono_lugar",stelefono_lugar);
        editor.putString("whatsapp_lugar",swhatsapp_lugar);
        editor.putString("direccion_logo_lugar",sdireccion_logo_lugar);
        editor.putString("direccion_banner_lugar",sdireccion_banner_lugar);
        editor.putString("latitud_lugar",String.valueOf(latitud_lugar));
        editor.putString("longitud_lugar",String.valueOf(longitud_lugar));

        editor.commit();

/*
        Intent servicio_contacto = new Intent(Menu_pedido.this, Servicio_guardar_contacto.class);
        servicio_contacto.setAction(Constants.ACTION_RUN_ISERVICE);
        servicio_contacto.putExtra("nombre",preferencias .getString("nombre_usuario", ""));
        servicio_contacto.putExtra("telefono",preferencias .getString("celular", ""));
        startService(servicio_contacto);
        */
    }
    private void cargar_pedido_en_curso(String id_pedido, String id_usuario, String fecha_pedido, String fecha_proceso, String mensaje, String estado, double latitud, double longitud, String nombre_usuario,String celuular,String id_carrera, int clase,double latitud_fin,double longitud_fin,String direccion_fin) {

        SharedPreferences preferencias=getSharedPreferences("ultimo_pedido_conductor",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferencias.edit();
        editor.putString("id_pedido",id_pedido);
        editor.putString("id_usuario",id_usuario);
        editor.putString("fecha_pedido",fecha_pedido);
        editor.putString("fecha_proceso",fecha_proceso);
        editor.putString("indicacion",mensaje);
        editor.putString("latitud",String.valueOf(latitud));
        editor.putString("longitud",String.valueOf(longitud));
        editor.putString("latitud_final",String.valueOf(latitud_fin));
        editor.putString("longitud_final",String.valueOf(longitud_fin));
        editor.putString("direccion_final",direccion_fin);
        editor.putString("nombre_usuario",nombre_usuario);
        editor.putString("celular",celuular);
        editor.putString("estado",estado);
        editor.putString("id_carrera",id_carrera);
        editor.putString("clase_vehiculo",String.valueOf(clase));

        editor.commit();

        /*
        Intent servicio_contacto = new Intent(Menu_pedido.this, Servicio_guardar_contacto.class);
        servicio_contacto.setAction(Constants.ACTION_RUN_ISERVICE);
        servicio_contacto.putExtra("nombre",preferencias .getString("nombre_usuario", ""));
        servicio_contacto.putExtra("telefono",preferencias .getString("celular", ""));
        startService(servicio_contacto);
*/
        pedido=true;

    }
    private void cerrar_sesion() {
        SharedPreferences datos_perfil = getSharedPreferences("perfil_conductor", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = datos_perfil.edit();
        editor.putString("nombre","");
        editor.putString("paterno","");
        editor.putString("materno","");
        editor.putString("ci","");
        editor.putString("celular","");
        editor.putString("email","");
        editor.putString("marca","");
        editor.putString("modelo","");
        editor.putString("placa","");
        editor.putString("direccion","");
        editor.putString("telefono","");
        editor.putString("referencia","");
        editor.putString("codigo","");
        editor.putString("credito","");
        editor.putString("estado","");
        editor.putString("login","");
        editor.putString("ci","");
        editor.putString("id_perfil","");
        editor.putString("login_usuario", "0");
        editor.putString("login_taxi", "0");
        editor.putString("id_empresa", "0");
        editor.commit();

        vaciar_toda_la_base_de_datos();

        // stopService(new Intent(getApplicationContext(), Servicio_cargar_punto.class));

        estoy_ocupado();

        startActivity(new Intent(this, Inicio_GradientBackgroundExampleActivity.class));

        finish();
    }

    private void eliminar_datos_share() {
        SharedPreferences datos_perfil = getSharedPreferences("perfil_conductor", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = datos_perfil.edit();
        editor.putString("nombre","");
        editor.putString("paterno","");
        editor.putString("materno","");
        editor.putString("ci","");
        editor.putString("celular","");
        editor.putString("email","");
        editor.putString("marca","");
        editor.putString("modelo","");
        editor.putString("placa","");
        editor.putString("direccion","");
        editor.putString("telefono","");
        editor.putString("referencia","");
        editor.putString("codigo","");
        editor.putString("credito","");
        editor.putString("estado","");
        editor.putString("login","");
        editor.putString("ci","");
        editor.putString("id_perfil","");
        editor.putString("login_usuario", "0");
        editor.putString("login_taxi", "0");
        editor.putString("id_empresa", "0");
        editor.commit();

        vaciar_toda_la_base_de_datos();
    }
    public void vaciar_toda_la_base_de_datos() {
        try {
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, getString(R.string.nombre_sql), null, Integer.parseInt(getString(R.string.version_sql)));

            SQLiteDatabase db = admin.getWritableDatabase();
            db.delete( "pedido",null,null);
            db.delete( "direccion",null,null);
            db.delete( "notificacion",null,null);
            db.delete( "chat",null,null);
            db.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        // Log.e("sqlite ", "vaciar todo");
    }

    public void vaciar_toda_la_base_de_datos_pedido_notificacion() {
        try {
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, getString(R.string.nombre_sql), null, Integer.parseInt(getString(R.string.version_sql)));

            SQLiteDatabase db = admin.getWritableDatabase();
            db.delete( "pedido",null,null);
            db.delete( "notificacion",null,null);
            db.close();
        }catch (Exception e)
        {

        }
        // Log.e("sqlite ", "vaciar todo");
    }

    public void imagen_en_vista(ImageView imagen)
    { Drawable dw;
        SharedPreferences perfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);
        String mPath = Environment.getExternalStorageDirectory() + File.separator + getString(R.string.nombre_carpeta)+"/Imagen"
                + File.separator + perfil.getString("ci","")+"_perfil.jpg";


        File newFile = new File(mPath);
        Bitmap bitmap = BitmapFactory.decodeFile(mPath);
        //Convertir Bitmap a Drawable.
        dw = new BitmapDrawable(getResources(), bitmap);
        //se edita la imagen para ponerlo en circulo.

        if( bitmap==null)
        { dw = getResources().getDrawable(R.drawable.ic_perfil_blanco);}

        imagen_circulo(dw,imagen);
    }

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

        try {
            imagen.setImageDrawable(roundedDrawable);
        }catch (Exception e)
        {

        }
    }

    public void mensaje(String mensaje)
    {
        Toast toast =Toast.makeText(this,mensaje,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }





    public void vista_button(boolean estado,boolean lpedido,boolean ll_carrera)
    { LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        LinearLayout.LayoutParams mat = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        this.ll_perfil_usuario.setLayoutParams(parms);

        this.lpedido.setLayoutParams(parms);
        this.ll_carrera.setLayoutParams(parms);

        this.estado.setVisibility(View.INVISIBLE);
        this.fl_map.setVisibility(View.VISIBLE);

        this.bt_contacto_usuario.setVisibility(View.INVISIBLE);
        this.bt_chat.setVisibility(View.INVISIBLE);

        bt_nueva_carrera.setText("NUEVA CARRERA");
        bt_cancelar.setText("CANCELAR");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        //Codigo del menu desplegable ...




        if(estado==true)
        {
            this.estado.setVisibility(View.VISIBLE);
            this.fl_map.setVisibility(View.INVISIBLE);



            SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
            int id_pedido=0;
            try{
                id_pedido=Integer.parseInt(pedido.getString("id_pedido","0"));
            }catch (Exception e){
                id_pedido=0;
            }
            if(id_pedido==0)
            {
                //    stopService(new Intent(getApplicationContext(), Servicio_cargar_punto.class));
            }

            SharedPreferences perfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);
            if(perfil.getString("estado","").equals("1"))
            {

                try{
                    //startService(servicio_cargar_punto);
                }catch (Exception e)
                {}

                mMap.clear();
                crear_marker();

            }
            else
            {



                vaciar_toda_la_base_de_datos_pedido_notificacion();

                SharedPreferences pedido2=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
                try{
                    id_pedido=Integer.parseInt(pedido2.getString("id_pedido","0"));
                }catch (Exception e){
                    id_pedido=0;
                }
                if(id_pedido==0)
                {
                    //    stopService(servicio_cargar_punto);
                }
            }

            tv_clase_vehiculo.setText("");
            toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color_TRANSPARENTE));






        }else if(lpedido)
        {
            ll_perfil_usuario.setLayoutParams(mat);
            bt_chat.setVisibility(View.VISIBLE);
            //CLASE DE VEHICULO
            int clase_vehiculo=5;
            SharedPreferences pedido2=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
            try{
                clase_vehiculo=Integer.parseInt(pedido2.getString("clase_vehiculo","1"));
            }catch (Exception e){
                clase_vehiculo=5;
            }

                tv_clase_vehiculo.setText(getString(R.string.taxi_delivery));
                toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPedido));
                bt_cancelar.setText("VER CARRITO");





            cargar_datos_de_pedido_textview();
            this.lpedido.setLayoutParams(mat);
            pedido=true;
        }
        else if(ll_carrera) {
            //CLASE DE VEHICULO
            ll_perfil_usuario.setLayoutParams(mat);
            bt_chat.setVisibility(View.VISIBLE);

            int clase_vehiculo=5;
            SharedPreferences pedido2=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
            try{
                clase_vehiculo=Integer.parseInt(pedido2.getString("clase_vehiculo","1"));
            }catch (Exception e){
                clase_vehiculo=5;
            }


                tv_clase_vehiculo.setText(getString(R.string.taxi_delivery));
                toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPedido));
                bt_nueva_carrera.setText("VER CARRITO");





            try{
                mMap.clear();
                crear_marker();
            }catch (Exception e)
            {

            }
            cargar_datos_de_pedido_textview();
            this.ll_carrera.setLayoutParams(mat);
            pedido=true;

            marcar_ruta_general();

        }
        //poner en 0 altura de Linear layout LPEDIDO de pedidos...

    }


    private void eliminar_pedido() {

        SharedPreferences pedido = getSharedPreferences("ultimo_pedido_conductor", MODE_PRIVATE);
        SharedPreferences.Editor editar=pedido.edit();
        editar.putString("nombre_taxi", "");
        editar.putString("celular", "");
        editar.putString("marca", "");
        editar.putString("placa", "");
        editar.putString("color", "");
        editar.putString("id_pedido", "");
        editar.putString("id_carrera", "");
        editar.putString("numero", "");
        editar.putString("estado", "");
        editar.putInt("notificacion_cerca", 0);
        editar.putInt("notificacion_llego", 0);

        editar.commit();
        vista_button(true,false,false);


    }






    //INICIO DE SERVICIO DE COORDENADAS
    private void enableLocationUpdates() {

        locRequest = new LocationRequest();
        locRequest.setInterval(1000);
        locRequest.setFastestInterval(100);
        locRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest locSettingsRequest =
                new LocationSettingsRequest.Builder()
                        .addLocationRequest(locRequest)
                        .build();

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        apiClient, locSettingsRequest);

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:

                        Log.i(LOGTAG, "Configuración correcta");
                        startLocationUpdates();

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            Log.i(LOGTAG, "Se requiere actuación del usuario");
                            status.startResolutionForResult(Menu_pedido.this, PETICION_CONFIG_UBICACION);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(LOGTAG, "Error al intentar solucionar configuración de ubicación");
                        }

                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(LOGTAG, "No se puede cumplir la configuración de ubicación necesaria");
                        break;
                }
            }
        });
    }

    private void disableLocationUpdates() {

        LocationServices.FusedLocationApi.removeLocationUpdates(
                apiClient, this);

    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(Menu_pedido.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            //Ojo: estamos suponiendo que ya tenemos concedido el permiso.
            //Sería recomendable implementar la posible petición en caso de no tenerlo.

            Log.i(LOGTAG, "Inicio de recepción de ubicaciones");
            try {
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        apiClient, locRequest, Menu_pedido.this);
            }catch (Exception e){
                finish();
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        //Se ha producido un error que no se puede resolver automáticamente
        //y la conexión con los Google Play Services no se ha establecido.

        Log.e(LOGTAG, "Error grave al conectar con Google Play Services");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Conectado correctamente a Google Play Services

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
        } else {

            Location lastLocation =
                    LocationServices.FusedLocationApi.getLastLocation(apiClient);

            updateUI(lastLocation);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Se ha interrumpido la conexión con Google Play Services

        Log.e(LOGTAG, "Se ha interrumpido la conexión con Google Play Services");
    }

    private void updateUI(Location loc) {
        if (loc != null) {




            latitud=loc.getLatitude();
            longitud=loc.getLongitude();
            altura=loc.getAltitude();

            SharedPreferences prefe = getSharedPreferences("perfil_conductor", Context.MODE_PRIVATE);
            String id=prefe.getString("ci", "");
            String lat= String.valueOf(loc.getLatitude());
            String lon=String.valueOf(loc.getLongitude());
            ubicacion.setText("p(" + lat + "," + lon + ")");



            if(sw_acercar_a_mi_ubicacion==0) {
                //mover la camara del mapa a mi ubicacion.,,
                try {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon))));
                    //agregaranimacion al mover la camara...
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(loc.getLatitude(), loc.getLongitude()))      // Sets the center of the map to Mountain View
                            .zoom(15)                   // Sets the zoom
                            .bearing(0)                // Sets the orientation of the camera to east
                            .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                            .build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                } catch (Exception e) {

                }
                sw_acercar_a_mi_ubicacion=1;
            }

            if(marcado_sw==true)
            {  mMap.clear();
                m_pasajero=null;
                m_empresa=null;
                crear_marker();

                marcar_ruta_general();
            }


            SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);

            if( m_pasajero!=null){
                servicio_ubicacion_pasajero_volley(getString(R.string.servidor) + "frmPedido.php?opcion=get_ubicacion_pedido",pedido.getString("id_pedido","0"));
            }





        } else {
            // lblLatitud.setText("Latitud: (desconocida)");

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PETICION_PERMISO_LOCALIZACION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Permiso concedido

                @SuppressWarnings("MissingPermission")
                Location lastLocation =
                        LocationServices.FusedLocationApi.getLastLocation(apiClient);

                updateUI(lastLocation);

            } else {
                finish();

                Log.e(LOGTAG, "Permiso denegado");
            }
        }else if(requestCode == 1000)
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

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PETICION_CONFIG_UBICACION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(LOGTAG, "El usuario no ha realizado los cambios de configuración necesarios");
                        break;
                }
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.i(LOGTAG, "Recibida nueva ubicación!");

        //Mostramos la nueva ubicación recibida
        updateUI(location);
    }
    //FIN DE SERVICIO DE COORDENADAS
    public void mensaje_error(String mensaje)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Importante");
        builder.setMessage(mensaje);
        builder.setPositiveButton("OK", null);
        builder.create();
        builder.show();
    }

    public  int getDistancia(double lat_a,double lon_a, double lat_b, double lon_b){
        long  Radius = 6371000;
        double dLat = Math.toRadians(lat_b-lat_a);
        double dLon = Math.toRadians(lon_b-lon_a);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) * Math.sin(dLon /2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return (int) (Radius * c);
    }

    public String obtener_direccion(double lat, double lon) {
        String direccion="";
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

            Address address = addresses.get(0);
            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                direccion+=","+address.getAddressLine(i);
            }

        /*    // Creamos el string a partir del elemento direccion
            String direccionText = String.format("%s, %s, %s",
                    address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                    address.getLocality(),
                    address.getCountryName());
                    */


            //  et_direccion.setText(address.getFeatureName()+" | "+address.getSubAdminArea ()+" | "+address.getSubLocality ()+" | "+address.getLocality ()+" | "+address.getSubLocality ()+" | "+address.getPremises ()+" | "+addresses.get(0).getThoroughfare()+" | "+address.getAddressLine(0));
        }
        return direccion;
    }


    public String obtener_direccion_string(double lat, double lon) {
        String res="";
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

            res=direcciones;

            //  et_direccion.setText(address.getFeatureName()+" | "+address.getSubAdminArea ()+" | "+address.getSubLocality ()+" | "+address.getLocality ()+" | "+address.getSubLocality ()+" | "+address.getPremises ()+" | "+addresses.get(0).getThoroughfare()+" | "+address.getAddressLine(0));
        }
        return res;
    }

    public class Servicio_corporativo extends AsyncTask<String,Integer,String> {
        String id_empresa,nit,razon_social,telefono,celular,direccion,direccion_logo;

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url = null;  // url donde queremos obtener informacion
            String devuelve = "";
            //enviar pedir taxi..


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

                        SystemClock.sleep(950);
                        JSONObject respuestaJSON = new JSONObject(result.toString());//Creo un JSONObject a partir del
                        suceso = new Suceso(respuestaJSON.getString("suceso"), respuestaJSON.getString("mensaje"));
                        if (suceso.getSuceso().equals("1")) {
                            id_empresa = respuestaJSON.getString("id");
                            nit = respuestaJSON.getString("nit");
                            razon_social = respuestaJSON.getString("razon_social");
                            direccion_logo = respuestaJSON.getString("direccion_logo");
                            direccion = respuestaJSON.getString("direccion");
                            telefono = respuestaJSON.getString("telefono");
                            celular = respuestaJSON.getString("celular");

                            devuelve = "3";
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

            pDialog = new ProgressDialog(Menu_pedido.this);
            pDialog.setTitle(getString(R.string.app_name));
            pDialog.setMessage("Ver Empresa. . .");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //ocultamos proggress dialog
            // Log.e("onPostExcute=", "" + s);
            pDialog.cancel();//ocultamos proggress dialog
            if (s.equals("3")) {
                //EMPRESA CON DELIVERY
                Intent menu_corporativo=new Intent(getApplicationContext(), Empresa_delivery.class);
                menu_corporativo.putExtra("id_empresa",id_empresa);
                menu_corporativo.putExtra("razon_social",razon_social);
                menu_corporativo.putExtra("direccion",direccion);
                menu_corporativo.putExtra("direccion_logo",direccion_logo);
                menu_corporativo.putExtra("nit",nit);
                menu_corporativo.putExtra("telefono",telefono);
                menu_corporativo.putExtra("celular",celular);
                startActivity(menu_corporativo);
            }
            else  if(s.equals("2"))
            {
                mensaje_error(suceso.getMensaje());
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

    public class Servicio_taxi_cerrar_sesion extends AsyncTask<String,Integer,String> {


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
                    jsonParam.put("ci", params[2]);
                    jsonParam.put("placa", params[3]);

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
                            devuelve="1";
                        } else  {
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
            pb_cargando.setLayoutParams(wrap_content);
            /*
            pDialog = new ProgressDialog(Menu_pedido.this);
            pDialog.setTitle(getString(R.string.app_name));
            pDialog.setMessage("Cerrando sesión");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            */
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pb_cargando.setLayoutParams(cero);
            //   pDialog.cancel();//ocultamos proggress dialog
            //  Log.e("onPostExcute=", "" + s);

            if (s.equals("1")) {
                eliminar_datos_share();
                cerrar_sesion();
            } else if(s.equals("2")) {
                mensaje(suceso.getMensaje());
            }
            else
            {
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



    private void AlertNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("El sistema GPS esta desactivado, ¿Desea activarlo?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
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



    public void mostrar_datos_finalizar_carrera(final double distancia, double d_altura, String tiempo, String monto_p)
    {
        SharedPreferences punto = getSharedPreferences("mi ubicacion_2", Context.MODE_PRIVATE);
        final double latitud_fin = Double.parseDouble(punto.getString("latitud", "0"));
        final double longitud_fin = Double.parseDouble(punto.getString("longitud", "0"));
        final double altura_fin = Double.parseDouble(punto.getString("altura", "0"));


        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(Menu_pedido.this);
        View promptView = layoutInflater.inflate(R.layout.input_text, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Menu_pedido.this);
        alertDialogBuilder.setView(promptView);

        final TextView et_monto_total = (TextView) promptView.findViewById(R.id.et_monto_total);
        final TextView tv_distancia = (TextView) promptView.findViewById(R.id.tv_distancia);
        final TextView tv_tiempo = (TextView) promptView.findViewById(R.id.tv_tiempo);
        final TextView tv_total = (TextView) promptView.findViewById(R.id.tv_total);


        DecimalFormat precision = new DecimalFormat("0.00");
        String s_distancia=precision.format(distancia);
        tv_distancia.setText("Distancia recorrida:"+s_distancia+" Mtrs.");
        tv_tiempo.setText("Tiempo recorrido:"+tiempo+" Hrs.");
        et_monto_total.setText(monto_p);
        tv_total.setText(monto_p);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
                        SharedPreferences perfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);

                       /* Servicio_taxi carrera = new Servicio_taxi();
                        carrera.execute(getString(R.string.servidor) + "frmCarrera" +
                                ".php?opcion=nueva_carrera", "11",
                                pedido.getString("id_pedido",""),
                                String.valueOf(latitud_fin),
                                String.valueOf(longitud_fin),
                                String.valueOf(altura_fin),
                                perfil.getString("ci",""),
                                perfil.getString("placa",""),
                                pedido.getString("id_usuario",""),
                                et_monto_total.getText().toString().trim(),
                                String.valueOf(distancia),
                                tv_direccion.getText().toString());
                        */

                        servicio_nueva_carrera(
                                getString(R.string.servidor) + "frmCarrera" +
                                        ".php?opcion=nueva_carrera",
                                pedido.getString("id_pedido",""),
                                String.valueOf(latitud_fin),
                                String.valueOf(longitud_fin),
                                String.valueOf(altura_fin),
                                perfil.getString("ci",""),
                                perfil.getString("placa",""),
                                pedido.getString("id_usuario",""),
                                et_monto_total.getText().toString().trim(),
                                String.valueOf(distancia),
                                tv_direccion.getText().toString()
                        );


                    }
                })
                .setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();


    }

    private void servicio_nueva_carrera(
            String v_url,
            String id_pedido,
            String latitud_fin,
            String longitud_fin,
            String altura_fin,
            String ci,
            String placa,
            String id_usuario,
            String monto_total,
            String distancia,
            String direccion) {

        try{
            pb_cargando.setLayoutParams(wrap_content);}
        catch (Exception e)
        {}

        try {


// set_ubicacion_punto  ----- cargar punto de ubicacion....

            String token= SharedPrefManager.getInstance(this).getDeviceToken();

            JSONObject jsonParam= new JSONObject();
            jsonParam.put("id_pedido", id_pedido);
            jsonParam.put("latitud", latitud_fin);
            jsonParam.put("longitud", longitud_fin);
            jsonParam.put("altura", altura_fin);
            jsonParam.put("ci", ci);
            jsonParam.put("placa", placa);
            jsonParam.put("id_usuario", id_usuario);
            jsonParam.put("monto", monto_total);
            jsonParam.put("distancia", distancia);
            jsonParam.put("direccion", direccion);


            jsonParam.put("token", token);
            String url=v_url;
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
                            try{
                                pb_cargando.setLayoutParams(cero);
                            }catch (Exception e)
                            {}

                            SharedPreferences punto = getSharedPreferences("mi ubicacion_2", Context.MODE_PRIVATE);
                            double latitud_fin = Double.parseDouble(punto.getString("latitud", "0"));
                            double longitud_fin = Double.parseDouble(punto.getString("longitud", "0"));
                            double altura_fin = Double.parseDouble(punto.getString("altura", "0"));



                            try {

                                suceso= new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));

                                if (suceso.getSuceso().equals("1")) {

                                    SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
                                    SharedPreferences.Editor editor=pedido.edit();
                                    editor.putString("id_carrera",respuestaJSON.getString("id_carrera"));
                                    editor.putString("numero","1");
                                    editor.commit();

                                    //final
                                    mensaje_error(suceso.getMensaje());

                                }
                                else
                                {
                                    mensaje(suceso.getMensaje());
                                }
                                //final


                                vaciar_toda_la_base_de_datos_pedido_notificacion();
                                SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
                                int id_pedido=0,id_carrera=0;
                                try{
                                    id_pedido=Integer.parseInt(pedido.getString("id_pedido","0"));
                                }catch (Exception e){
                                    try{
                                        id_carrera=Integer.parseInt(pedido.getString("id_carrera","0"));
                                    }   catch (Exception ee){
                                        id_carrera=0;
                                        id_pedido=0;
                                    }

                                    if(id_pedido==0)
                                    {
                                        vista_button(true,false,false);
                                    }
                                    else if(id_carrera==0){
                                        vista_button(false,false,true);
                                    }else{
                                        vista_button(false,true,false);
                                    }
                                }

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


            myRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(myRequest);


        } catch (Exception e) {

        }

    }

    private void servicio_ubicacion_pasajero_volley(
            String v_url,
            String id_pedido) {


        try {


// set_ubicacion_punto  ----- cargar punto de ubicacion....


            JSONObject jsonParam= new JSONObject();
            jsonParam.put("id_pedido", id_pedido);
            String url=v_url;
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

                                suceso= new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));

                                if (suceso.getSuceso().equals("1")) {
                                    SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
                                    SharedPreferences.Editor editar=pedido.edit();
                                    editar.putString("latitud",respuestaJSON.getString("latitud"));
                                    editar.putString("longitud",respuestaJSON.getString("longitud"));
                                    editar.commit();

                                    if(m_pasajero!=null){
                                        try {
                                            m_pasajero.setPosition(new LatLng(Double.parseDouble(respuestaJSON.getString("latitud")), Double.parseDouble(respuestaJSON.getString("longitud"))));
                                        }catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }
                                    //final

                                }
                                else
                                {
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


            myRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(myRequest);


        } catch (Exception e) {

        }

    }

    public void formulario_mostrar_datos_finalizar_pedido(final double distancia, double d_altura,
                                                          String tiempo,
                                                          String monto_p,String sdetalle,String stotal,
                                                          String notificacion_monto)
    {
        SharedPreferences punto = getSharedPreferences("mi ubicacion_2", Context.MODE_PRIVATE);
        final double latitud_fin = Double.parseDouble(punto.getString("latitud", "0"));
        final double longitud_fin = Double.parseDouble(punto.getString("longitud", "0"));
        final double altura_fin = Double.parseDouble(punto.getString("altura", "0"));


        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(Menu_pedido.this);
        View promptView = layoutInflater.inflate(R.layout.input_text, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Menu_pedido.this);
        alertDialogBuilder.setView(promptView);

        final EditText et_monto_total = (EditText) promptView.findViewById(R.id.et_monto_total);
        final TextView tv_distancia = (TextView) promptView.findViewById(R.id.tv_distancia);
        final TextView tv_detalle = (TextView) promptView.findViewById(R.id.tv_detalle);
        final TextView tv_tiempo = (TextView) promptView.findViewById(R.id.tv_tiempo);
        final TextView tv_tota = (TextView) promptView.findViewById(R.id.tv_total);
        DecimalFormat precision = new DecimalFormat("0.00");

        tv_detalle.setText(sdetalle);
        tv_tota.setText(stotal+" BOB");

        tv_distancia.setText("Distancia recorrida:"+distancia+" Mtrs.");
        tv_tiempo.setText("Tiempo recorrido:"+tiempo+" Hrs.");
        et_monto_total.setText(monto_p);

        if(notificacion_monto.equals("0"))
        {
            et_monto_total.setVisibility(View.INVISIBLE);
            tv_tota.setText("- - -");
            tv_detalle.setText("- - - ");
        }

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
                        SharedPreferences perfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);

                        SharedPreferences punto = getSharedPreferences("mi ubicacion", Context.MODE_PRIVATE);
                        double latitud_fin = Double.parseDouble(punto.getString("latitud", "0"));
                        double longitud_fin = Double.parseDouble(punto.getString("longitud", "0"));
                        double altura_fin = Double.parseDouble(punto.getString("altura", "0"));

                        servicio_volley_finalizar_pedido(
                                pedido.getString("id_pedido",""),
                                String.valueOf(latitud_fin),
                                String.valueOf(longitud_fin),
                                String.valueOf(altura_fin),
                                perfil.getString("ci",""),
                                perfil.getString("placa",""),
                                pedido.getString("id_usuario",""),
                                pedido.getString("id_carrera",""),
                                et_monto_total.getText().toString().trim(),
                                String.valueOf(distancia),
                                tv_direccion.getText().toString(),
                                "comentario");


                    }
                })
                .setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();

    }



    public void llamar_usuario(View v)
    {
        llamar_usuario_start();
    }
    public void llamar_usuario_start()
    {
        startActivity(new Intent(this, LLamar_usuario.class));
    }




    public int get_estado()
    {
        SharedPreferences  prefe = getSharedPreferences("perfil_conductor", Context.MODE_PRIVATE);
        int estado_perfil=0;
        try{
            estado_perfil=Integer.parseInt(prefe.getString("estado","0"));
        }catch (Exception e)
        {
            estado_perfil=0;
        }
        return estado_perfil;
    }
    public void set_estado(int estado)
    {
        SharedPreferences  prefe = getSharedPreferences("perfil_conductor", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=prefe.edit();
        editor.putString("estado",String.valueOf(estado));
        editor.commit();
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


    private void enviar(String[] to, String[] cc,
                        String asunto, String mensaje) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        emailIntent.putExtra(Intent.EXTRA_CC, cc);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, asunto);
        emailIntent.putExtra(Intent.EXTRA_TEXT, mensaje);
        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent, "Email "));
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


    //VERIFICAR SI ESTA CON CONEXION WIFI
    protected Boolean conectadoWifi(){
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }
    //VERIFICAR SI ESTA CON CONEXION DE DATOS
    protected Boolean conectadoRedMovil(){
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected Boolean estaConectado(){
        boolean sw=false;

        boolean connected = false;
        ConnectivityManager connec = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Recupera todas las redes (tanto móviles como wifi)
        NetworkInfo[] redes = connec.getAllNetworkInfo();

        for (int i = 0; i < redes.length; i++) {
            // Si alguna red tiene conexión, se devuelve true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                connected = true;
            }
        }
        sw=connected;

        if(sw==true)
        {
            return true;
        }else{
            mensaje_error_final("Tu Dispositivo no tiene Conexion a Internet."+2282);
            return false;
        }
       /*
        if(conectadoWifi()==true){
            return true;
        }else{
            if(conectadoRedMovil()==true){
                return true;
            }else{

                return false;
            }
        }
        */
    }


    public void mensaje_error_final(String mensaje)
    {try {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
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
    public void cuenta_iniciar_en_otro_celular(String mensaje)
    {eliminar_datos_share();
        try {
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle(getString(R.string.app_name));
            dialogo1.setMessage(mensaje);
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    cerrar_sesion();
                }
            });
            dialogo1.show();
        }catch (Exception e){
        }

    }

    private void turnGPSOn(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(!provider.contains("gps")){ //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
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
                    ActivityCompat.requestPermissions(Menu_pedido.this,
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
            ActivityCompat.requestPermissions(Menu_pedido.this,
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
                    ActivityCompat.requestPermissions(Menu_pedido.this,
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
            ActivityCompat.requestPermissions(Menu_pedido.this,
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
                    ActivityCompat.requestPermissions(Menu_pedido.this,
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
            ActivityCompat.requestPermissions(Menu_pedido.this,
                    PERMISSIONS,
                    1);
        }
    }




    //INICIO DE VERIFICACION DE VERSION DE DATOS
    public void iniciar_verificacion_version(){
        SharedPreferences perfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);

        try {
            String devuelve="";

            String token= SharedPrefManager.getInstance(this).getDeviceToken();

            JSONObject jsonParam= new JSONObject();
            jsonParam.put("id_conductor",perfil.getString("ci","") );
            jsonParam.put("id_vehiculo",perfil.getString("placa",""));
            jsonParam.put("token", token);
            String url=getString(R.string.servidor) + "frm_version.php?opcion=kache_conductor";
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
                                version= Integer.valueOf(respuestaJSON.getString("version"));
                                suceso= new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));

                                if (suceso.getSuceso().equals("1")) {
                                    verificar_version();
                                }else if(suceso.getSuceso().equals("2"))
                                {
                                    cuenta_iniciar_en_otro_celular(suceso.getMensaje());

                                }
                                else
                                {
                                    mensaje_error_final("Error: Al conectar con el Servidor.\nVerifique su acceso a Internet.");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                mensaje_error_final("Falla en tu conexión a Internet.");
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mensaje_error_final("Falla en tu conexión a Internet.");
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



    public boolean verificar_version()
    {boolean sw=false;
        // notificacion para verificar la actualizacion nueva
        int actual=getVersionCode(this);
        if(version>actual)
        {
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Actualización");
            dialogo1.setMessage("Hay una nueva version.Por favor actualice la aplicación desde Play Store.");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("ACTULIZAR", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.elisoft.kache_conductor&hl=es");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });

            dialogo1.show();
            sw=true;
        }
        return sw;
    }


    public static int getVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException ex) {}
        return 0;
    }

    public class Servicio_version extends AsyncTask<String,Integer,String> {


        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url = null;  // url donde queremos obtener informacion
            String devuelve = "";
//Registrar usuario.
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
                    jsonParam.put("id_conductor", params[2]);
                    jsonParam.put("id_vehiculo", params[3]);
                    jsonParam.put("token", params[4]);

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

                        version= Integer.valueOf(respuestaJSON.getString("version"));
                        devuelve="1";

                        suceso= new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));
                        if(suceso.getSuceso().equals("2")){
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
            //para el progres Dialog
            try {
                /*
                pDialog = new ProgressDialog(Menu_pedido.this);
                pDialog.setTitle(getString(R.string.app_name));
                pDialog.setMessage("Verificando la versión.");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
                */
            }catch (Exception e)
            {}
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if (s.equals("1")) {
                verificar_version();
            }else if(s.equals("2"))
            {
                cuenta_iniciar_en_otro_celular(suceso.getMensaje());

            }
            else
            {
                mensaje_error_final("Error: Al conectar con el Servidor.\nVerifique su acceso a Internet.");
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
    //FIN DE VERIFICACION DE DATOS.....






    public void verificar_todos_los_permisos()
    {
        final String[] SMS_PERMISSIONS1 = {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.RECORD_AUDIO

        };

        ActivityCompat.requestPermissions( this,
                SMS_PERMISSIONS1,
                1000);


    }



    //AUDIO

    @AfterPermissionGranted(RC_RECORD_AUDIO)
    private void startRecording() {
        String[] perms = {Manifest.permission.RECORD_AUDIO};
        if (EasyPermissions.hasPermissions(this, perms)) {

            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setOutputFile(sRecordedFileName);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            try {
                mRecorder.prepare();
            } catch (IOException e) {
                Log.e(LOG_TAG, "prepare() failed");
            }

            mRecorder.start();
        } else {
            EasyPermissions.requestPermissions(this, "Hi", RC_RECORD_AUDIO, perms);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
    }
    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    private void setRecordIcon(boolean record) {
        if (record) {
            im_grabar.setImageDrawable(
                    VectorDrawableCompat.create(getResources(), R.drawable.ic_enviando_audio_conexion, getTheme()));
        } else {
            im_grabar.setImageDrawable(
                    VectorDrawableCompat.create(getResources(), R.drawable.ic_error_conexion, getTheme()));
        }
    }

    public void send() {
        sendAudio();
    }

    public void sendAudio() {
        FileChannel in = null;

        try {
            File f = new File(sRecordedFileName);
            in = new FileInputStream(f).getChannel();

            //mSocket.send(START);

            sendAudioBytes(in);

            //mSocket.send(END);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendAudioBytes(FileChannel in) throws IOException {
        ByteBuffer buff = ByteBuffer.allocateDirect(32);

        String audio="";
        while (in.read(buff) > 0) {

            buff.flip();
            String bytes = ByteString.of(buff).toString();
            //mSocket.send(bytes);

            try {
                String hexValue = bytes.substring(bytes.indexOf("hex=") + 4, bytes.length() - 1);

                audio+=hexValue;

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }

            buff.clear();
        }


        SharedPreferences prefe = getSharedPreferences("perfil_conductor", MODE_PRIVATE);

        Intent intent = new Intent(Menu_pedido.this, Servicio_enviar_audio.class);
        intent.putExtra("id_conductor",prefe.getString("ci","0"));
        intent.putExtra("id_usuario",0);
        intent.putExtra("id_administrador",0);
        intent.putExtra("titulo",prefe.getString("nombre", "") + " " + prefe.getString("paterno", ""));
        intent.putExtra("mensaje","envio de audio canal");
        intent.putExtra("audio",audio);
        intent.putExtra("tipo","AUDIO");
        intent.putExtra("canal","TAXI VALLE");
        intent.putExtra("url",getString(R.string.servidor) + "frmChat.php?opcion=enviar_canal_conductor");
        startService(intent);


    }

    private void servicio_taxi_ruta(String v_url) {
        try {




            JSONObject jsonParam= new JSONObject();

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
                                rutas= new JSONObject(respuestaJSON.toString());//Creo un JSONObject a partir del
                                // -----   final  ----------------
                                dibujar_ruta_empresa(rutas);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                //  mensaje_error("Falla en tu conexión a Internet.");
                                dibujar_ruta_empresa(rutas);
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    mensaje_error("No pudimos conectarnos al servidor.\nVuelve a intentarlo.");

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

    private void servicio_taxi_ruta_pasajero(String v_url) {
        try {

            String token= SharedPrefManager.getInstance(this).getDeviceToken();


            JSONObject jsonParam= new JSONObject();
            jsonParam.put("token", token);

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
                                rutas= new JSONObject(respuestaJSON.toString());//Creo un JSONObject a partir del
                                // -----   final  ----------------
                                dibujar_ruta_pasajero(rutas);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                //  mensaje_error("Falla en tu conexión a Internet.");
                                dibujar_ruta_pasajero(rutas);
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    mensaje_error("No pudimos conectarnos al servidor.\nVuelve a intentarlo.");

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

    private void servicio_preparar_mensaje_canal(String v_canal,
                                                 String v_id_conductor,
                                                 String v_titulo,
                                                 String v_url) {

        try {
            JSONObject jsonParam= new JSONObject();
            jsonParam.put("canal", v_canal);
            jsonParam.put("id_conductor", v_id_conductor);
            jsonParam.put("titulo", v_titulo);


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
                                    inicia_grabacion();

                                } else  {
                                    preparar_mensaje_canal();
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

            myRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(myRequest);
        } catch (Exception e) {
        }

    }

    private void preparar_mensaje_canal()
    {
        im_grabar.setImageDrawable(
                VectorDrawableCompat.create(getResources(), R.drawable.ic_preparando_conexion, getTheme()));
        SharedPreferences prefe = getSharedPreferences("perfil_conductor", MODE_PRIVATE);
        servicio_preparar_mensaje_canal("TAXI VALLE",
                prefe.getString("ci","0"),
                prefe.getString("nombre", "") + " " + prefe.getString("paterno", ""),
                getString(R.string.servidor) + "frmChat.php?opcion=preparar_mensaje_canal");
    }
    private void inicia_grabacion()
    {
        mp= MediaPlayer.create(this, R.raw.sonido_conexion);
        mp.start();
        sw_grabacion=true;
        setRecordIcon(true);
        startRecording();
    }

    private void detener_grabacion()
    {
        if(sw_grabacion==true) {
            sw_grabacion=false;
            setRecordIcon(false);
            stopRecording();
            send();

        }
    }

    //AUDIO


    public void get_credito(){
        SharedPreferences prefe = getSharedPreferences("perfil_conductor", Context.MODE_PRIVATE);
        String id_conductor=prefe.getString("ci", "");

        Servicio_credito servicio = new Servicio_credito();
        servicio.execute(getString(R.string.servidor) + "frmTaxi.php?opcion=get_credito", "1", id_conductor);
    }


    //Obtener el credito del conductor
    public class Servicio_credito extends AsyncTask<String, Integer, String> {
        String credito="0 de Credito";

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

                        //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                        JSONObject respuestaJSON = new JSONObject(result.toString());//Creo un JSONObject a partir del
                        // StringBuilder pasando a cadena.                    }

                        SystemClock.sleep(950);

                        //Accedemos a vector de resultados.
                        String error = respuestaJSON.getString("suceso");// suceso es el campo en el Json
                        String mensaje = respuestaJSON.getString("mensaje");
                        suceso = new Suceso(error, mensaje);

                        if (error.equals("1")) {
                            credito=respuestaJSON.getString("credito");
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

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //  Log.e("onPostExcute=", "" + s);
        }
    }

     private void loadRewardedVideoAd() {

    }

    private void showRewardedVideo() {


    }

    private void servicio_volley_set_credito(String ci, String credito) {
        pDialog = new ProgressDialog(Menu_pedido.this);
        pDialog.setTitle(getString(R.string.app_name));
        pDialog.setMessage("Autenticando ....");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        try {
            String v_url= getString(R.string.servidor) + "frmTaxi.php?opcion=set_credito";

            JSONObject jsonParam= new JSONObject();
            jsonParam.put("ci", ci);
            jsonParam.put("credito", credito);

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
                            pDialog.cancel();

                            try {
                                String error = respuestaJSON.getString("suceso");// suceso es el campo en el Json
                                String mensaje = respuestaJSON.getString("mensaje");
                                suceso = new Suceso(error, mensaje);

                                if (suceso.getSuceso().equals("1")) {


                                } else  {
                                    mensaje_error(suceso.getMensaje());
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
            pDialog.cancel();
        }


    }







    private void servicio_volley_finalizar_pedido(String id_pedido,
                                                  String latitud,
                                                  String longitud,
                                                  String altura,
                                                  String ci,
                                                  String placa,
                                                  String id_usuario,
                                                  String id_carrera,
                                                  String monto_total,
                                                  String distancia,
                                                  String direccion,
                                                  String comentario) {
        String v_url=getString(R.string.servidor) + "frmCarrera.php?opcion=finalizar_pedido";

        try{
            pb_cargando.setLayoutParams(wrap_content);}
        catch (Exception e)
        { }

        try {




            JSONObject jsonParam= new JSONObject();
            jsonParam.put("id_pedido", id_pedido);
            jsonParam.put("latitud", latitud);
            jsonParam.put("longitud", longitud);
            jsonParam.put("altura", altura);
            jsonParam.put("ci", ci);
            jsonParam.put("placa", placa);
            jsonParam.put("id_usuario", id_usuario);
            jsonParam.put("id_carrera", id_carrera);
            jsonParam.put("monto_total", monto_total);
            jsonParam.put("distancia", distancia);
            jsonParam.put("direccion", direccion);
            jsonParam.put("comentario", comentario);
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
                                pb_cargando.setLayoutParams(cero);

                                suceso= new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));

                                if (suceso.getSuceso().equals("1")) {
                                    try {
                                        mMap.clear();
                                        crear_marker();
                                    } catch (Exception e) {

                                    }
                                    SharedPreferences perfil = getSharedPreferences("perfil_conductor", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = perfil.edit();
                                    editor.putString("estado", "1");
                                    editor.commit();

                                    eliminar_pedido();

                                    //mostrar el video de publicidad
                                    showRewardedVideo();


                                }
                                else
                                {
                                    vista_button(true,false,false);
                                }



                                vaciar_toda_la_base_de_datos_pedido_notificacion();
                                SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
                                int id_pedido=0,id_carrera=0;
                                try{
                                    id_pedido=Integer.parseInt(pedido.getString("id_pedido","0"));
                                }catch (Exception e){
                                    try{
                                        id_carrera=Integer.parseInt(pedido.getString("id_carrera","0"));
                                    }   catch (Exception ee){
                                        id_carrera=0;
                                        id_pedido=0;
                                    }

                                    if(id_pedido==0)
                                    {
                                        vista_button(true,false,false);
                                    }
                                    else if(id_carrera==0){
                                        vista_button(false,false,true);
                                    }else{
                                        vista_button(false,true,false);
                                    }
                                }

                                startActivity(new Intent(Menu_pedido.this,Menu_taxi.class));

                            } catch (JSONException e) {

                                try{
                                    pb_cargando.setLayoutParams(cero);
                                }catch (Exception ee)
                                {}
                                e.printStackTrace();

                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try{
                        pb_cargando.setLayoutParams(cero);
                    }catch (Exception ee)
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


    public void estoy_libre(){
        Intent startIntent = new Intent(Menu_pedido.this, Servicio_cargar_punto_google.class);
        startIntent.setAction(Constants.ACTION.START_ACTION);
        startService(startIntent);
    }

    public void estoy_ocupado(){
        Intent stopIntent = new Intent(Menu_pedido.this, Servicio_cargar_punto_google.class);
        stopIntent.setAction(Constants.ACTION.STOP_ACTION);
        startService(stopIntent);
    }





}
