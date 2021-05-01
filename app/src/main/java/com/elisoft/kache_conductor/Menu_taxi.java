package com.elisoft.kache_conductor;


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
import android.graphics.Canvas;
import android.graphics.Color;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.elisoft.kache_conductor.SqLite.AdminSQLiteOpenHelper;
import com.elisoft.kache_conductor.admo.Menu_publicidad;
import com.elisoft.kache_conductor.animacion.Inicio_GradientBackgroundExampleActivity;
import com.elisoft.kache_conductor.chat.Chat;
import com.elisoft.kache_conductor.chat.Servicio_chat;
import com.elisoft.kache_conductor.chat.handy.Menu_Canal;
import com.elisoft.kache_conductor.chat.handy.Servicio_enviar_audio;
import com.elisoft.kache_conductor.configuracion.Configuracion;
import com.elisoft.kache_conductor.detalle_pedido_carrito.Detalle_pedido_delivery;
import com.elisoft.kache_conductor.historial.Historial;
import com.elisoft.kache_conductor.historial_notificacion.Notificacion;
import com.elisoft.kache_conductor.notificaciones.SharedPrefManager;
import com.elisoft.kache_conductor.pedido_ya.Empresa_delivery;
import com.elisoft.kache_conductor.pedido_ya.Menu_multipedido;
import com.elisoft.kache_conductor.perfil.Perfil_Conductor;
import com.elisoft.kache_conductor.servicio.Servicio_cargar_punto_google;
import com.elisoft.kache_conductor.servicio.Servicio_descargar_imagen_perfil;
import com.elisoft.kache_conductor.solicitudes.CSolicitud;
import com.elisoft.kache_conductor.solicitudes.Items_solicitud;
import com.elisoft.kache_conductor.video_tutorial.Menu_video;
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
import com.google.android.material.navigation.NavigationView;
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
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.multidex.MultiDex;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import okio.ByteString;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class Menu_taxi extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback ,View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener   {
//solo para la publicidad
    private  String AD_UNIT_ID ; //My code
//fin publicidad
    /*
   audio
     */

    private static final String LOG_TAG = "AudioRecordTest";
    public static final int RC_RECORD_AUDIO = 1000;
    public static String sRecordedFileName;
    private MediaRecorder mRecorder;
  /*
   audio
     */

    //variables de solicitudes
    private ProgressDialog pDialog;
    ArrayList<CSolicitud> cSolicitud=new ArrayList<CSolicitud>();
    ListView lv_lista;
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



    private TextView ubicacion,tv_nombre,tv_direccion,tv_referencia,tv_clase_vehiculo;
    private Button bt_finalizar,bt_cancelar,bt_comenzar_carrera,bt_nueva_carrera;
    ImageButton bt_contacto_usuario,bt_chat;
    private LinearLayout estado;
    TextView tv_estado,tv_credito;
    ImageView im_estado;
    Switch sw_estado;

    ImageButton bt_empresa;


    AlertDialog.Builder dialogo1 ;
    LinearLayout lpedido,ll_carrera ,ll_perfil_usuario,ll_pedido;
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

    FrameLayout fl_map;
    Date fecha_conexion;

    int clase_vehiculo=1;
    RequestQueue queue=null;



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


        servicio_volley_lista();

        super.onStart();
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
                                    vista_button(true,false,false);
                                    iniciar_verificacion_version();
                                } else if(suceso.getSuceso().equals("2"))
                                {
                                    SharedPreferences perfil = getSharedPreferences("perfil_conductor", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = perfil.edit();
                                    editor.putString("estado", respuestaJSON.getString("estado"));
                                    editor.commit();


                                  //FINAL
                                    //SIN PEDIDO
                                    vista_button(true,false,false);
                                    iniciar_verificacion_version();
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
                                    clase_vehiculo=clase;

                                        SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
                                        SharedPreferences.Editor editor=pedido.edit();

                                        editor.putString("id_carrera","");
                                        editor.putString("numero","");
                                        editor.commit();

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


                                    SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
                                    SharedPreferences.Editor editor=pedido.edit();
                                    JSONArray carrera=respuestaJSON.getJSONArray("carrera");
                                    editor.putString("id_carrera",carrera.getJSONObject(0).getString("id"));
                                    editor.putString("numero",respuestaJSON.getString("numero"));
                                    editor.commit();

                                    //FINAL
                                    //EN CARRERA LA COMENZADA
                                    clase_vehiculo=clase;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultiDex.install(this);
        setContentView(R.layout.activity_menu_movil);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);






        ubicacion=(TextView)findViewById(R.id.ubicacion);
        tv_credito=(TextView)findViewById(R.id.tv_credito);
        tv_nombre=(TextView)findViewById(R.id.tv_nombre);
        tv_direccion=(TextView)findViewById(R.id.tv_direccion);
        tv_referencia=(TextView)findViewById(R.id.tv_referencia);
        tv_clase_vehiculo=(TextView)findViewById(R.id.tv_clase_vehiculo);
        bt_cancelar = (Button) findViewById(R.id.bt_cancelar);
        bt_comenzar_carrera = (Button) findViewById(R.id.bt_comenzar_carrera);
        bt_nueva_carrera = (Button) findViewById(R.id.bt_nueva_carrera);
        bt_finalizar = (Button) findViewById(R.id.bt_finalizar);
        im_estado=(ImageView) findViewById(R.id.im_estado);
        sw_estado=(Switch)findViewById(R.id.sw_estado);

        estado=(LinearLayout) findViewById(R.id.estado);
        tv_estado=(TextView) findViewById(R.id.tv_estado);
        bt_contacto_usuario=(ImageButton) findViewById(R.id.bt_contacto_usuario);
        lpedido=(LinearLayout)findViewById(R.id.lpedido);
        ll_carrera=(LinearLayout)findViewById(R.id.ll_carrera);
        ll_pedido=(LinearLayout)findViewById(R.id.ll_pedido);
        bt_empresa=(ImageButton)findViewById(R.id.bt_empresa);
        lv_lista=(ListView)findViewById(R.id.lv_lista);

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
        im_estado.setOnClickListener(this);
        sw_estado.setOnClickListener(this);

        bt_cancelar.setOnClickListener(this);
        bt_comenzar_carrera.setOnClickListener(this);
        bt_nueva_carrera.setOnClickListener(this);
        bt_finalizar.setOnClickListener(this);
        bt_contacto_usuario.setOnClickListener(this);
        sw_mapa.setOnClickListener(this);
        ll_perfil_usuario.setOnClickListener(this);
        bt_empresa.setOnClickListener(this);

        bt_chat.setOnClickListener(this);

        //im_grabar.setOnClickListener(this);

        pb_cargando.setLayoutParams(cero);

 //       servicio_cargar_punto=new Intent(this,Servicio_cargar_punto.class);



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
        //verificamos si tiene un pedido. en curso....
        try{
            Bundle bundle=getIntent().getExtras();
            int id_pedido=Integer.parseInt(bundle.getString("id_pedido"));

            pedido=true;
            marcado_sw=true;

        }catch (Exception e)
        {
            //vista_button(true,false,false);
        }
        SharedPreferences pe=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
        if(pe.getString("id_pedido","").equals("")==false)
        {
        }
        else
        {pedido=false;
        }
        marcar_ruta(pedido);




        // localizacion automatica

        //Construcción cliente API Google
        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        enableLocationUpdates();
        manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
/*
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
               // Log.e("gps","StatusChanged");

            }

            @Override
            public void onProviderEnabled(String s) {
              //  Log.e("gps","Provider enable");

            }

            @Override
            public void onProviderDisabled(String s) {

                dialogo1.setTitle("Importante");
                dialogo1.setMessage("Es necesario activar su GPS.");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Activar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        Toast.makeText(getApplicationContext(),"Provider disable",Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });

                dialogo1.show();

            }

        };

        //necesario para la iteraccion  necesaria para obtener la ubicacion,...
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.INTERNET}, 10);
            return;
        } else {
            locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
        }OOO

        */
// fin de la obtenecion de ubicacion....


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


        lv_lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CSolicitud hi=new CSolicitud();
                hi=cSolicitud.get(i);

                abrir_solicitud(hi);

            }
        });



        servicio_volley_lista();
        fecha_conexion = new Date();




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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }





    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.perfil) {


            Intent perfil = new Intent(this, Perfil_Conductor.class);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                verificar_permiso_llamada();
            }
            else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                verificar_permiso_camara();
            }
            else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                verificar_permiso_almacenamiento();
            } else {  startActivity(perfil);   }

        } else if (id == R.id.notificacion) {
            startActivity(new Intent(this, Notificacion.class));
        } else if (id == R.id.historial) {
            startActivity(new Intent(this, Historial.class));
        } else if (id == R.id.it_menu_publicidad) {
            startActivity(new Intent(this, Menu_publicidad.class));
        }else if(id== R.id.it_chat)
        {
            Intent it_chat=new Intent(this, Chat.class);
            it_chat.putExtra("id_usuario","12");
            startActivity(it_chat);
        }
        else if (id == R.id.it_empresa) {
            Intent empresa=new Intent(this,Empresa_radio_taxi.class);
            SharedPreferences s_empresa=getSharedPreferences("perfil_conductor",MODE_PRIVATE);
            empresa.putExtra("id_empresa",s_empresa.getString("id_empresa",""));
            startActivity(empresa);
        }
        else if (id == R.id.it_configuracion) {
            Intent empresa=new Intent(this, Configuracion.class);
            startActivity(empresa);
        }
        else if (id == R.id.it_zello) {
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.loudtalks");
            boolean isZello = appInstalledOrNot("com.loudtalks");
            if (isZello){
                startActivity(launchIntent);
            }else {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.loudtalks"));
                startActivity(i);
            }
        }else if (id == R.id.it_canal) {
            startActivity(new Intent(this, Menu_Canal.class));
        }else if (id == R.id.id_videos_tutoriales) {
            startActivity(new Intent(this, Menu_video.class));
        }else if(id== R.id.cerrar_sesion)
        {




            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle(getString(R.string.app_name));
            dialogo1.setMessage("¿Cerrar Sesion?");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    //cargamos los datos
                    SharedPreferences prefe = getSharedPreferences("perfil_conductor", Context.MODE_PRIVATE);
                    String ci=prefe.getString("ci", "");
                    String placa=prefe.getString("placa", "");
                    Servicio_taxi_cerrar_sesion servicio_taxi_cerrar_sesion=new Servicio_taxi_cerrar_sesion();
                    servicio_taxi_cerrar_sesion.execute(getString(R.string.servidor) + "frmTaxi.php?opcion=cerrar_sesion", "1",ci,placa);// parametro que recibe

                }
            });
            dialogo1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {

                }
            });
            dialogo1.show();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
             //   tv_credito.setTextColor(Color.WHITE);
            } catch (Resources.NotFoundException e) {
                Log.e("style", "Can't find style. Error: ", e);
            }
            tv_credito.setTextColor(Color.BLACK);

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
                //tv_credito.setTextColor(Color.WHITE);
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

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ActivityCompat.requestPermissions(
                            this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                            1);
                }
                else{
                    ActivityCompat.requestPermissions(
                            this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                            1);
                }

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
            case R.id.im_grabar:
                startActivity(new Intent(this,Menu_Canal.class));
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
                    clase_v=Integer.parseInt(pe2.getString("clase_vehiculo","1"));
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

            case R.id.sw_estado:
                if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                    AlertNoGps();
                }
                //Servicio_taxi hilo = new Servicio_taxi();
                SharedPreferences pref = getSharedPreferences("perfil_conductor", Context.MODE_PRIVATE);
                String id_m=pref.getString("ci", "");
                if(tv_estado.getText().toString().equals("ESTOY LIBRE")==true) {
                   // hilo.execute(getString(R.string.servidor) + "frmTaxi.php?opcion=set_estado", "1",id_m,"0");// parametro que recibe
                    servicio_set_estado(getString(R.string.servidor) + "frmTaxi.php?opcion=set_estado",
                            id_m, "0");
                }
                else
                {
                    //hilo.execute(getString(R.string.servidor) + "frmTaxi.php?opcion=set_estado", "1",id_m,"1");// parametro que recibe
                    servicio_set_estado(getString(R.string.servidor) + "frmTaxi.php?opcion=set_estado",
                            id_m,"1");
                }
                break;

            case R.id.fb_ruta:
                marcado_sw=false;
                marcar_ruta(true);
                break;
            case R.id.bt_finalizar:
                //cargar video
                loadRewardedVideoAd();



                try{
                    clase_vehiculo=Integer.parseInt(pedido2.getString("clase_vehiculo","1"));
                }catch (Exception e){
                    clase_vehiculo=1;
                }

                servicio_get_carrera_por_id(getString(R.string.servidor) + "frmCarrera.php?opcion=get_carrera_por_id",
                        spedido.getString("id_pedido",""),
                        perfil.getString("ci",""),
                        perfil.getString("placa",""),
                        spedido.getString("id_carrera",""));






                break;
            case R.id.bt_comenzar_carrera:


                    obtener_direccion(latitud_fin, longitud_fin);
                   // Servicio_taxi hilo_ = new Servicio_taxi();
                   // hilo_.execute(getString(R.string.servidor) + "frmCarrera.php?opcion=comenzar_carrera", "10", spedido.getString("id_pedido", ""), String.valueOf(latitud_fin), String.valueOf(longitud_fin), String.valueOf(altura_fin), perfil.getString("ci", ""), perfil.getString("placa", ""), spedido.getString("id_usuario", ""), tv_direccion.getText().toString());
                    servicio_comenzar_carrera(spedido.getString("id_pedido", ""), String.valueOf(latitud_fin), String.valueOf(longitud_fin), String.valueOf(altura_fin), perfil.getString("ci", ""), perfil.getString("placa", ""), spedido.getString("id_usuario", ""), tv_direccion.getText().toString());


                break;
            case R.id.bt_nueva_carrera:


                int id_pedido=0 ;
                try{
                    id_pedido=Integer.parseInt(pedido2.getString("id_pedido","0"));
                }catch (Exception e) {
                    id_pedido=0;
                }

                try{
                    clase_vehiculo=Integer.parseInt(pedido2.getString("clase_vehiculo","1"));
                }catch (Exception e){
                    clase_vehiculo=1;
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
                llamar_usuario_start();
                break;
            case R.id.bt_empresa:

                try {
                    SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
                    int id_pe=Integer.parseInt(pedido.getString("id_pedido","0"));

                    Servicio_corporativo  hilo_p = new Servicio_corporativo();
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


        obtener_direccion(latitud_fin,longitud_fin);
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(Menu_taxi.this);
        View promptView = layoutInflater.inflate(R.layout.input_text, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Menu_taxi.this);
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


    private void servicio_set_estado(String v_url, String id_conductor, String estado) {

        try{
            pb_cargando.setLayoutParams(wrap_content);}
        catch (Exception e)
        {

        }


        try {
            String token= SharedPrefManager.getInstance(this).getDeviceToken();

            JSONObject jsonParam= new JSONObject();
            jsonParam.put("ci", id_conductor);
            jsonParam.put("estado", estado);
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

                            try{
                                pb_cargando.setLayoutParams(cero);
                            }catch (Exception e)
                            {}

                            try {
                                suceso=new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));

                                if (suceso.getSuceso().equals("1")) {
                                    int estado=Integer.parseInt(respuestaJSON.getString("estado"));
                                    set_estado(estado);

                                    int sw_estado=get_estado();
                                    if (sw_estado == 1)
                                    {
                                       estoy_libre();
                                    }else
                                    {
                                        estoy_ocupado();
                                    }


                                    //final
                                    try {
                                        mMap.clear();

                                    } catch (Exception e) {

                                    }
                                    vista_button(true,false,false);



                                } else  {
                                    vista_button(true,false,false);
                                }

                                //...final de final....................

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

            myRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(myRequest);

        } catch (Exception e) {
        }


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

    private void servicio_comenzar_carrera(String id_pedido, String latitud, String longitud, String altura, String ci, String placa, String id_usuario, String direccion) {
        try {

            try{
                pb_cargando.setLayoutParams(wrap_content);}
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
                                    vista_button(false, false, true);
                                    SharedPreferences pedido1 = getSharedPreferences("ultimo_pedido_conductor", MODE_PRIVATE);
                                    int clase_vehiculo=1;
                                    try{
                                        clase_vehiculo=Integer.parseInt(pedido1.getString("clase_vehiculo","1"));
                                        if (clase_vehiculo==5)
                                        {
                                            marcar_ruta_primera_carrera();
                                        }

                                    }catch (Exception e)
                                    {

                                    }

                                }else if(suceso.getSuceso().equals("2"))
                                {

                                    mensaje(suceso.getMensaje());
                                }
                                else
                                {

                                }
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

            Marker marker = this.mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_punto_fin_1))
                    .anchor((float)0.5,(float)0.8)
                    .flat(true)
                    .title(direccion_fi)
                    .snippet(direccion_fi)
                    .position(new LatLng(latitud_fin, longitud_fin)));


        }catch (Exception e)
        {

        }
    }



    // comenzar el servicio con el taxista....
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
                    e.printStackTrace();
                    devuelve="500";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //Obtener el camino mas corto. para llegar mas rapido ..
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

                        devuelve="8";
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                    devuelve="501";
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
            }else  if(s.equals("8"))
            {
                dibujar_ruta_carrera_primera(rutas);
            }else if(s.equals("501"))
            {
                dibujar_ruta_carrera_primera(rutas);

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
            if(tv_estado.getText().toString().equals("ESTOY LIBRE")==true)
            {
                editor.putString("estado","1");
            }
            else
            {
                editor.putString("estado","3");
            }
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

    public class Servicio_estado extends AsyncTask<String,Integer,String> {


        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url = null;  // url donde queremos obtener informacion
            String devuelve = "";

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

                        SystemClock.sleep(950);
                        JSONObject respuestaJSON = new JSONObject(result.toString());//Creo un JSONObject a partir del
                        suceso=new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));
                        if (suceso.getSuceso().equals("1")) {

                            SharedPreferences perfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);
                            SharedPreferences.Editor editor=perfil.edit();
                            editor.putString("credito",respuestaJSON.getString("credito"));
                            editor.putString("cal_conductor",respuestaJSON.getString("cal_conductor"));
                            editor.putString("cal_vehiculo",respuestaJSON.getString("cal_vehiculo"));
                            editor.putString("estado",respuestaJSON.getString("estado"));
                            editor.commit();

                            devuelve="3";
                        } else {
                            devuelve = "5";
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

            int sw_estado=get_estado();
            if (sw_estado == 1)
            {
               vista_button(true,false,false);
            }else
            {

                vaciar_toda_la_base_de_datos_pedido_notificacion();
                SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);

                if(pedido.getString("id_pedido","").equals("")==true || pedido.getString("id_pedido","0").equals("0")==true)
                {
                //    stopService(new Intent(getApplicationContext(), Servicio_cargar_punto.class));
                }
                else{
                    vista_button(true,false,false);
                }
            }



            iniciar_verificacion_version();
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

    private void cargar_datos_de_pedido_textview() {

        SharedPreferences prefe=getSharedPreferences("ultimo_pedido_conductor", Context.MODE_PRIVATE);
        String id_pedido= prefe.getString("id_pedido","");

        if(id_pedido.equals("")==false)
        {
            /*
            Intent servicio_contacto = new Intent(Menu_taxi.this, Servicio_guardar_contacto.class);
            servicio_contacto.setAction(Constants.ACTION_RUN_ISERVICE);
            servicio_contacto.putExtra("nombre",prefe .getString("nombre_usuario", ""));
            servicio_contacto.putExtra("telefono",prefe .getString("celular", ""));
            startService(servicio_contacto);
            */

            pedido=true;

            double lat=0;
            double lon=0;
            try{
                lat=Double.parseDouble(prefe.getString("latitud","0"));
                lon=Double.parseDouble(prefe.getString("longitud","0"));
            }catch (Exception e){
                lat=0;
                lon=0;
            }



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


                obtener_direccion(lat,lon);
                tv_referencia.setText(prefe.getString("indicacion",""));
                tv_nombre.setText(prefe.getString("nombre_usuario",""));
                String url = getString(R.string.servidor)+"frmTaxi.php?opcion=get_imagen_usuario&id_usuario="+prefe.getString("id_usuario","");//hace consulta ala Bd para recurar la imagen
                Picasso.with(this).load(url).placeholder(R.drawable.ic_perfil_negro).into(im_perfil_usuario);
//mover la camara del mapa

                //agregaranimacion al mover la camara...
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(lat, lon))      // Sets the center of the map to Mountain View
                        .zoom(15)                   // Sets the zoom
                        .bearing(0)                // Sets the orientation of the camera to east
                        .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }catch (Exception e)
            {

            }





        }
        else
        {
            //poner en 0 altura de Linear layout LPEDIDO de pedidos...
            pedido=false;
        }
    }



    public void marcar_ruta(boolean sw)
    {
        try{
            if(sw==true)
            {

                //buscamos una ruta para el motista     SOLO CO ACCESO A INTERNET

                SharedPreferences prefe1=getSharedPreferences("ultimo_pedido_conductor", MODE_PRIVATE);
                double lat=Double.parseDouble(prefe1.getString("latitud",""));
                double lon=Double.parseDouble(prefe1.getString("longitud",""));
                if(prefe1.getString("id_pedido","").equals("")==false) {
                   // Servicio_taxi_ruta hilo = new Servicio_taxi_ruta();
                   // hilo.execute("https://maps.googleapis.com/maps/api/directions/json?origin=" + latitud + "," + longitud + "&destination=" + lat + "," + lon + "&mode=driving&key="+getString(R.string.google_key_api), "4");
                    servicio_taxi_ruta("https://maps.googleapis.com/maps/api/directions/json?origin=" + latitud + "," + longitud + "&destination=" + lat + "," + lon + "&mode=driving&key="+getString(R.string.google_key_api));

                    try {

                       m_pasajero= this.mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_point1))
                                .anchor((float) 0.5, (float) 0.8)
                                .flat(true)
                                .position(new LatLng(lat, lon)));
                        m_pasajero.showInfoWindow();
                    } catch (Exception e) {

                    }
                }
            }

        }catch (Exception e)
        {

        }
    }



    public void marcar_ruta_carrera_primera()
    {
        try{


            //buscamos una ruta para el motista     SOLO CO ACCESO A INTERNET

            SharedPreferences prefe1=getSharedPreferences("ultimo_pedido_conductor", MODE_PRIVATE);
            double lat=Double.parseDouble(prefe1.getString("latitud_final",""));
            double lon=Double.parseDouble(prefe1.getString("longitud_final",""));
            String direccion_fi= obtener_direccion_string(lat,lon);
            if(prefe1.getString("id_pedido","").equals("")==false) {
               // Servicio_taxi_ruta hilo = new Servicio_taxi_ruta();
               // hilo.execute("https://maps.googleapis.com/maps/api/directions/json?origin=" + latitud + "," + longitud + "&destination=" + lat + "," + lon + "&mode=driving&key=AIzaSyA-WZRJsd-KQx_q-oGUePzekvHeaBE-0hI", "5");// parametro que recibe el doinbackground

                servicio_taxi_ruta_primera("https://maps.googleapis.com/maps/api/directions/json?origin=" + latitud + "," + longitud + "&destination=" + lat + "," + lon + "&mode=driving&key="+getString(R.string.google_key_api));



                try {

                    Marker marker= this.mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource( R.mipmap.ic_punto_fin_1))
                            .anchor((float) 0.5, (float) 0.8)
                            .flat(true)
                            .title(direccion_fi)
                            .snippet(direccion_fi)
                            .position(new LatLng(lat, lon)));

                } catch (Exception e) {

                }
            }


        }catch (Exception e)
        {

        }
    }

    public void dibujar_ruta(JSONObject jObject){

        if (lista_ruta_pasajero == null) {
            lista_ruta_pasajero = new ArrayList<>();
        } else {
            lista_ruta_pasajero.clear();
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
                            lista_ruta_pasajero.add(punto);

                        }
                    }
                    tiempo=(String)((JSONObject)((JSONObject)jLegs.get(j)).get("duration")).get("text");
                }
            }

            try
            {
                mMap.clear();
            }catch (Exception e)
            {

            }

            //dibujar las lineas

            if(sw_punto==true) {


                punto=new LatLng(latitud,longitud);
               // start_nimacion_pasajero();

                if(esDiurno()){
                    mMap.addPolyline(polylineOptions.width(15).color(Color.BLACK));
                }else{
                    mMap.addPolyline(polylineOptions.width(15).color(getResources().getColor(R.color.colorPrimary_light)));
                }



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
                m_pasajero= this.mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_point1))
                        .anchor((float) 0.5, (float) 0.8)
                        .flat(true)
                        .position(new LatLng(latitud_pedido, longitud_pedido))
                        .title(""+tiempo));
                m_pasajero.showInfoWindow();

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


    public void dibujar_ruta_carrera_primera(JSONObject jObject){

        if (lista_ruta_empresa == null) {
            lista_ruta_empresa = new ArrayList<>();
        } else {
            lista_ruta_empresa.clear();
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

                            lista_ruta_empresa.add(punto);

                        }
                    }

                }
            }

            try
            {
                mMap.clear();
            }catch (Exception e)
            {

            }

            //dibujar las lineas



            punto=new LatLng(latitud,longitud);
            start_nimacion_empresa();



            //agregaranimacion al mover la camara...
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(punto)      // Sets the center of the map to Mountain View
                    .zoom(17)                   // Sets the zoom
                    .bearing(0)                // Sets the orientation of the camera to east
                    .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            marcado_sw=false;

            try {
                SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
                double latitud_pedido=Double.parseDouble(pedido.getString("latitud_final","0"));
                double longitud_pedido=Double.parseDouble(pedido.getString("longitud_final","0"));
                String direccion_pedido= pedido.getString("direccion_final","0");

                if (direccion_pedido.length()<8)
                {
                    direccion_pedido=obtener_direccion_string(latitud_pedido,longitud_pedido);
                }


                Marker marker1= this.mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_punto_fin_1))
                        .anchor((float) 0.5, (float) 0.8)
                        .flat(true)
                        .position(new LatLng(latitud_pedido, longitud_pedido))
                        .title(direccion_pedido));
                marker1.showInfoWindow();

            } catch (Exception e) {

            }
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
            db.delete( "pedido_taxi",null,null);
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
            db.delete( "pedido_taxi",null,null);
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
    {
        if((lpedido==true||ll_carrera==true)){
            try{
            Intent intent1 = new Intent(Menu_taxi.this, Servicio_chat.class);
            intent1.setAction(Constants.ACTION_RUN_ISERVICE);
            startService(intent1);
            }catch (Exception ee)
            {
                ee.printStackTrace();
            }
        }



        if((lpedido==true||ll_carrera==true)&& clase_vehiculo==5){
            startActivity(new Intent(this, Menu_multipedido.class));
        }

        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        LinearLayout.LayoutParams mat = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        FrameLayout.LayoutParams completo = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        FrameLayout.LayoutParams mapa_cero = new FrameLayout.LayoutParams(0,0);

        this.ll_perfil_usuario.setLayoutParams(parms);

        this.lpedido.setLayoutParams(parms);
        this.ll_carrera.setLayoutParams(parms);

        this.estado.setVisibility(View.INVISIBLE);
        this.fl_map.setLayoutParams(mapa_cero);
        this.lv_lista.setVisibility(View.INVISIBLE);


        this.ll_pedido.setVisibility(View.INVISIBLE);
        this.bt_contacto_usuario.setVisibility(View.INVISIBLE);
        this.bt_chat.setVisibility(View.INVISIBLE);

        bt_nueva_carrera.setText("NUEVA CARRERA");
        bt_cancelar.setText("CANCELAR");
        bt_empresa.setVisibility(View.INVISIBLE);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        //Codigo del menu desplegable ...
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                // carga los datos de su perfil. al momento de deslizar el menu. de perfil....
                TextView nombre, celular;
                ImageView perfil;
                nombre = (TextView) drawerView.findViewById(R.id.nombre_completo);
                celular = (TextView) drawerView.findViewById(R.id.celular);
                perfil = (ImageView) drawerView.findViewById(R.id.perfil);
                try {
                    SharedPreferences prefe = getSharedPreferences("perfil_conductor", MODE_PRIVATE);
                    nombre.setText(prefe.getString("nombre", "") + " " + prefe.getString("paterno", ""));
                    celular.setText(prefe.getString("celular", ""));

                    imagen_en_vista(perfil);

                    Intent intent = new Intent(Menu_taxi.this, Servicio_descargar_imagen_perfil.class);
                    intent.putExtra("id_conductor",prefe.getString("ci","0"));
                    startService(intent);

                } catch (Exception e)
                {}
                try {

                    perfil.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Menu_taxi.this, Perfil_Conductor.class));
                        }
                    });
                }catch (Exception e)
                {
                    Log.e("Error perfil:",e.toString());
                }
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        if(estado==true)
        {
            this.estado.setVisibility(View.VISIBLE);
            this.lv_lista.setVisibility(View.VISIBLE);
            this.fl_map.setLayoutParams(mapa_cero);

            //lista de solicitudes
            servicio_volley_lista();
            //lista de solicitudes

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
                this.tv_estado.setText("ESTOY LIBRE");
                this.tv_estado.setBackgroundResource(R.color.colorLibre);
                this.im_estado.setBackgroundResource(R.drawable.libre);


                this.sw_estado.setText("Libre");
                this.sw_estado.setTextColor( getResources().getColor(R.color.colorLibre));
                this.sw_estado.setChecked(true);
                this.sw_estado.setBackgroundResource(R.drawable.bk_estado_conductor);


                try{
                //startService(servicio_cargar_punto);
                }catch (Exception e)
                {}

                mMap.clear();

            }
            else
            {
                this.tv_estado.setText("ESTOY EN DESCANSO");
                this.tv_estado.setBackgroundResource(R.color.colorOcupado);
                this.im_estado.setBackgroundResource(R.drawable.ocupado);


                this.sw_estado.setText("Ocup.");
                this.sw_estado.setTextColor(getResources().getColor(R.color.colorOcupado));
                this.sw_estado.setChecked(false);
                this.sw_estado.setBackgroundResource(R.drawable.bk_estado_conductor_desconectado);

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

            toggle.setDrawerIndicatorEnabled(true);





        }else if(lpedido)
        {
            this.fl_map.setLayoutParams(completo);
            ll_perfil_usuario.setLayoutParams(mat);
            bt_chat.setVisibility(View.VISIBLE);
            //CLASE DE VEHICULO
            int clase_vehiculo=1;
            SharedPreferences pedido2=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
            try{
                clase_vehiculo=Integer.parseInt(pedido2.getString("clase_vehiculo","1"));
            }catch (Exception e){
                clase_vehiculo=1;
            }
            if(clase_vehiculo==2){
                tv_clase_vehiculo.setText(getString(R.string.taxi_lujo));
                toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorLujo));
            }else if (clase_vehiculo==3){
                tv_clase_vehiculo.setText(getString(R.string.taxi_con_aire));
                toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAire));
            }else if(clase_vehiculo==4)
            {
                tv_clase_vehiculo.setText(getString(R.string.taxi_con_maletero));
                toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorMaletero));
            }else if(clase_vehiculo==5){
                tv_clase_vehiculo.setText(getString(R.string.taxi_delivery));
                toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPedido));
                bt_cancelar.setText("VER CARRITO");
                bt_empresa.setVisibility(View.VISIBLE);
            }else if(clase_vehiculo==7){
                tv_clase_vehiculo.setText("Una Moto");
                toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPedido));
            }




            cargar_datos_de_pedido_textview();
            this.lpedido.setLayoutParams(mat);
            toggle.setDrawerIndicatorEnabled(false);
            pedido=true;
            this.ll_pedido.setVisibility(View.VISIBLE);
           // this.bt_contacto_usuario.setVisibility(View.VISIBLE);

            SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
          getImage(pedido.getString("id_usuario",""));
         //   startService(servicio_cargar_punto);

        }
        else if(ll_carrera) {
            //CLASE DE VEHICULO
            this.fl_map.setLayoutParams(completo);
            ll_perfil_usuario.setLayoutParams(mat);
            bt_chat.setVisibility(View.VISIBLE);

            int clase_vehiculo=1;
            SharedPreferences pedido2=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
            try{
                clase_vehiculo=Integer.parseInt(pedido2.getString("clase_vehiculo","1"));
            }catch (Exception e){
                clase_vehiculo=1;
            }

        if(clase_vehiculo==2){
            tv_clase_vehiculo.setText(getString(R.string.taxi_lujo));
            toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorLujo));
        }else if (clase_vehiculo==3){
            tv_clase_vehiculo.setText(getString(R.string.taxi_con_aire));
            toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAire));
        }else if(clase_vehiculo==4)
        {
            tv_clase_vehiculo.setText(getString(R.string.taxi_con_maletero));
            toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorMaletero));
        }else if(clase_vehiculo==5){
            tv_clase_vehiculo.setText(getString(R.string.taxi_delivery));
            toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPedido));
            bt_nueva_carrera.setText("VER CARRITO");
            bt_empresa.setVisibility(View.VISIBLE);
        }else if(clase_vehiculo==7){
            tv_clase_vehiculo.setText("Una Moto");
            toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPedido));
        }else if(clase_vehiculo==11)
        {
            tv_clase_vehiculo.setText(getString(R.string.taxi_con_parrilla));
            toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorMaletero));
        }else if(clase_vehiculo==15)
        {
            tv_clase_vehiculo.setText(getString(R.string.taxi_camioneta));
            toolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPedido));
        }




            try{
                mMap.clear();
            }catch (Exception e)
            {

            }
            cargar_datos_de_pedido_textview();
            this.ll_carrera.setLayoutParams(mat);
            toggle.setDrawerIndicatorEnabled(false);
            pedido=true;


            SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
            getImage(pedido.getString("id_usuario",""));
          this.ll_pedido.setVisibility(View.VISIBLE);
         //   this.bt_contacto_usuario.setVisibility(View.VISIBLE);
          //  startService(servicio_cargar_punto);

            SharedPreferences ped = getSharedPreferences("ultimo_pedido_conductor", MODE_PRIVATE);
            if(ped.getString("id_carrera","").equals("1") && clase_vehiculo==5)
            {
                marcar_ruta_carrera_primera();
            }else
            {
                try{
                    mMap.clear();
                }catch (Exception e)
                {

                }
            }

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
                            status.startResolutionForResult(Menu_taxi.this, PETICION_CONFIG_UBICACION);
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
        if (ActivityCompat.checkSelfPermission(Menu_taxi.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            //Ojo: estamos suponiendo que ya tenemos concedido el permiso.
            //Sería recomendable implementar la posible petición en caso de no tenerlo.

            Log.i(LOGTAG, "Inicio de recepción de ubicaciones");
            try {
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        apiClient, locRequest, Menu_taxi.this);
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

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                        PETICION_PERMISO_LOCALIZACION);
            }else{
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                        PETICION_PERMISO_LOCALIZACION);
            }

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
                marcar_ruta(marcado_sw);

            }

            SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);

            if((pedido.getString("id_carrera","0").equals("0")||pedido.getString("id_carrera","").equals("")) && m_pasajero!=null){
                servicio_ubicacion_pasajero_volley(getString(R.string.servidor) + "frmPedido.php?opcion=get_ubicacion_pedido",pedido.getString("id_pedido","0"));
            }




//diferencia de fechas
            long diferencia=0;
            Date fecha_actual=new Date();
            try {


                //obtienes la diferencia de las fechas
                diferencia = Math.abs(fecha_actual.getTime() - fecha_conexion.getTime());
            }catch (Exception ee)
            {
                diferencia=11000;
            }

            if(diferencia>10000)
            {
                fecha_conexion=new Date();
                servicio_volley_lista();
            }


//fin de diferencia de fechas


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

            pDialog = new ProgressDialog(Menu_taxi.this);
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
            pDialog = new ProgressDialog(Menu_taxi.this);
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


    private void getImage(String id)//
    {

        /*
        class GetImage extends AsyncTask<String,Void,Bitmap> {
            ImageView bmImage;


            public GetImage(ImageView bmImage) {
                this.bmImage = bmImage;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);


                Drawable dw = new BitmapDrawable(getResources(), bitmap);
                //se edita la imagen para ponerlo en circulo.

                if( bitmap==null)
                { dw = getResources().getDrawable(R.drawable.ic_perfil_negro);}

                imagen_circulo(dw,bmImage);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Bitmap doInBackground(String... strings) {
                String url = getString(R.string.servidor)+"frmTaxi.php?opcion=get_imagen_usuario&id_usuario="+strings[0];//hace consulta ala Bd para recurar la imagen
                Drawable d = getResources().getDrawable(R.drawable.ic_perfil_negro);
                Bitmap mIcon = drawableToBitmap(d);
                try {
                    InputStream in = new URL(url).openStream();
                    mIcon = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                }
                return mIcon;
            }
        }

        GetImage gi = new GetImage(im_perfil_usuario);
        gi.execute(id);
        */

        String  url=  getString(R.string.servidor)+"usuario/imagen/perfil/"+id+"_perfil.png";

        Picasso.with(this).load(url).into(target);



    }

    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            Drawable dw = new BitmapDrawable(getResources(), bitmap);
            //se edita la imagen para ponerlo en circulo.

            if( bitmap==null)
            { dw = getResources().getDrawable(R.drawable.ic_perfil_blanco);

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

    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
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

        obtener_direccion(latitud_fin,longitud_fin);
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(Menu_taxi.this);
        View promptView = layoutInflater.inflate(R.layout.input_text, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Menu_taxi.this);
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
        double latitud_inicio=0,longitud_inicio=0,altura_inicio=0;
        double distancia_i=0;

        String tiempo="",monto="";

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

            String token= SharedPrefManager.getInstance(this).getDeviceToken();

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


        obtener_direccion(latitud_fin,longitud_fin);
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(Menu_taxi.this);
        View promptView = layoutInflater.inflate(R.layout.input_text, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Menu_taxi.this);
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



    public void llamar_usuario(View v)
    {
        llamar_usuario_start();
    }
    public void llamar_usuario_start()
    {
        startActivity(new Intent(this,LLamar_usuario.class));
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
                    ActivityCompat.requestPermissions(Menu_taxi.this,
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
            ActivityCompat.requestPermissions(Menu_taxi.this,
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
                    ActivityCompat.requestPermissions(Menu_taxi.this,
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
            ActivityCompat.requestPermissions(Menu_taxi.this,
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
                    ActivityCompat.requestPermissions(Menu_taxi.this,
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
            ActivityCompat.requestPermissions(Menu_taxi.this,
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
            String url=getString(R.string.servidor) + "frm_version.php?opcion=valle_grande_conductor";
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
                pDialog = new ProgressDialog(Menu_taxi.this);
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

        Intent intent = new Intent(Menu_taxi.this, Servicio_enviar_audio.class);
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
                                dibujar_ruta(rutas);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                //  mensaje_error("Falla en tu conexión a Internet.");
                                dibujar_ruta(rutas);
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

    private void servicio_taxi_ruta_primera(String v_url) {
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
                                dibujar_ruta_carrera_primera(rutas);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                //  mensaje_error("Falla en tu conexión a Internet.");
                                dibujar_ruta_carrera_primera(rutas);
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
            tv_credito.setText(credito+ " ©");
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

                                    Items_solicitud adaptador = new Items_solicitud(Menu_taxi.this,cSolicitud);
                                    lv_lista.setAdapter(adaptador);

                                } else  {
                                    cSolicitud.clear();
                                    Items_solicitud adaptador = new Items_solicitud(Menu_taxi.this,cSolicitud);
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

    private void loadRewardedVideoAd() {

    }

    private void showRewardedVideo() {


    }

    private void servicio_volley_set_credito(String ci, String credito) {
        pDialog = new ProgressDialog(Menu_taxi.this);
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


    public  void aceptar_solicitud(String id_pedido){
        SharedPreferences perfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);

        servicio_aceptar_pedido(String.valueOf(id_pedido),perfil.getString("ci",""),perfil.getString("placa",""));
    }


    private void servicio_aceptar_pedido(String id_pedido, String ci, String placa) {


        try {

            pDialog = new ProgressDialog(Menu_taxi.this);
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





                                ///

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
        Intent startIntent = new Intent(Menu_taxi.this, Servicio_cargar_punto_google.class);
        startIntent.setAction(Constants.ACTION.START_ACTION);
        startService(startIntent);
    }

    public void estoy_ocupado(){
        Intent stopIntent = new Intent(Menu_taxi.this, Servicio_cargar_punto_google.class);
        stopIntent.setAction(Constants.ACTION.STOP_ACTION);
        startService(stopIntent);
    }





}
