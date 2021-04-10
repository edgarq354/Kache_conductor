package com.elisoft.kache_conductor;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.elisoft.kache_conductor.preregistro.Verificar_numero;
import com.elisoft.kache_conductor.servicio.Servicio_cargar_punto_google;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class Iniciar_sesion extends AppCompatActivity implements View.OnClickListener {
    Button iniciar_sesion, iniciar_taxi;
    ProgressDialog pDialog;
    Suceso suceso;
    EditText et_usuario, et_contrasenia;
    TextView tv_olvidaste_tu_contrasenia, tv_condiciones;


    String imei = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_iniciar_sesion);




        if (verificar_login_taxi()) {

            startActivity(new Intent(this, Menu_taxi.class));
            finish();
        }


        iniciar_taxi = (Button) findViewById(R.id.iniciar_movil);
        et_usuario = (EditText) findViewById(R.id.et_usuario);
        et_contrasenia = (EditText) findViewById(R.id.et_contrasenia);
        tv_olvidaste_tu_contrasenia = (TextView) findViewById(R.id.tv_olvidates_tu_contrasenia);

        iniciar_taxi.setOnClickListener(this);
        tv_olvidaste_tu_contrasenia.setOnClickListener(this);
        verificar_todos_los_permisos();
        try{
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);



        }catch (Exception e)
        {}
        SharedPreferences datos_perfil = getSharedPreferences("perfil_conductor", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed=datos_perfil.edit();


        getSupportActionBar().hide();

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iniciar_movil) {

                String token = SharedPrefManager.getInstance(this).getDeviceToken();

                if (token != null || token == "") {

                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        imei = Settings.Secure.getString(
                                getContentResolver(),
                                Settings.Secure.ANDROID_ID);

                        servicio_iniciar_sesion(getString(R.string.servidor) + "frmTaxi.php?opcion=iniciar_sesion", et_usuario.getText().toString().trim(), et_contrasenia.getText().toString().trim(), token, imei);
                    } else {
                        if (ActivityCompat.checkSelfPermission(Iniciar_sesion.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            verificar_permiso_imei();
                        }else {
                            imei = telephonyManager.getDeviceId();

                            servicio_iniciar_sesion(getString(R.string.servidor) + "frmTaxi.php?opcion=iniciar_sesion", et_usuario.getText().toString().trim(), et_contrasenia.getText().toString().trim(), token, imei);
                        }
                    }


                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        verificar_permiso_imei();

                    } else {
                        imei = telephonyManager.getDeviceId();

                    }
                } else {
                    mensaje_error("No se a podido generar el Token. porfavor active sus datos de Red e instale Google Pay Service");
                }

        } else if (v.getId() == R.id.tv_olvidates_tu_contrasenia) {
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle(getString(R.string.app_name));
            dialogo1.setMessage("Por favor contactece con la Central de su Radio Movil.");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("OK", null);

            dialogo1.show();
        }
    }

    private void servicio_iniciar_sesion(String v_url, String usuario, String contrasenia, String token_usuario, String imei) {
        //para el progres Dialog
        pDialog = new ProgressDialog(Iniciar_sesion.this);
        pDialog.setTitle(getString(R.string.app_name));
        pDialog.setMessage("Autenticando ....");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        try {

            JSONObject jsonParam= new JSONObject();
            jsonParam.put("usuario", usuario);
            jsonParam.put("contrasenia", contrasenia);
            jsonParam.put("token", token_usuario);
            jsonParam.put("imei", imei);


            RequestQueue queue = Volley.newRequestQueue(this);


            JsonObjectRequest myRequest= new JsonObjectRequest(
                    Request.Method.POST,
                    v_url,
                    jsonParam,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject respuestaJSON) {

                            pDialog.cancel();//ocultamos proggress dialog

                            try {
                                suceso=new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));

                                if (suceso.getSuceso().equals("1")) {

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

                                    iniciar_sesion_taxi();
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
                    pDialog.cancel();//ocultamos proggress dialog
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

            pDialog.cancel();//ocultamos proggress dialog

            queue.add(myRequest);
        } catch (Exception e) {
        }

    }

    public void empresa(View v)
    {
        startActivity(new Intent(this, Verificar_numero.class));
    }


    public  void aplicacion_usuario(View v)
        {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.elisoft.gougo&hl=es");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        int per=0;
        switch (requestCode) {
            case 1: {
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

                if(per<grantResults.length){
                    finish();
                }else
                {

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }




    private void iniciar_sesion_taxi() {
        estoy_libre();
        startActivity(new Intent(getApplicationContext(), Menu_taxi.class));
        finish();
    }

    public void estoy_libre(){
        Intent startIntent = new Intent(this, Servicio_cargar_punto_google.class);
        startIntent.setAction(Constants.ACTION.START_ACTION);
        startService(startIntent);
    }


    private void error_sesion_taxi() {
        mensaje_error("Por favor verifique los datos que ingreso.\nSi los datos son Correctos contactese con el administrador porque su cuenta a sido Iniciada en otro Celular.");
    }


    public void mensaje(String mensaje) {
        Toast toast = Toast.makeText(this, mensaje, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    public void cargar_datos(String nombre, String apellido, String email, String celular, String id) {
        SharedPreferences usuario = getSharedPreferences("perfil_conductor", MODE_PRIVATE);
        SharedPreferences.Editor editar = usuario.edit();
        editar.putString("nombre", nombre);
        editar.putString("apellido", apellido);
        editar.putString("email", email);
        editar.putString("celular", celular);
        editar.putString("id_usuario", id);
        editar.putString("login_usuario", "1");
        editar.commit();
    }

    public boolean verificar_login_usuario() {
        SharedPreferences perfil = getSharedPreferences("perfil_conductor", MODE_PRIVATE);
        return (perfil.getString("login_usuario", "").equals("1"));

    }

    public boolean verificar_login_taxi() {
        SharedPreferences perfil = getSharedPreferences("perfil_conductor", MODE_PRIVATE);
        return (perfil.getString("login_taxi", "").equals("1"));

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
        editar.putString("imei", imei);
        editar.commit();

    }




    public void mensaje_error(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Importante");
        builder.setMessage(mensaje);
        builder.setPositiveButton("OK", null);
        builder.create();
        builder.show();
    }

    public void verificar_permiso_imei() {
        final String[] PERMISSIONS = {Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE};

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
            //YA LO CANCELE Y VOUELVO A PERDIR EL PERMISO.

            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Atención!");
            dialogo1.setMessage("Debes otorgar permisos de acceso al ID del Telefono por tema de Seguridad.");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Solicitar permiso", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    dialogo1.cancel();
                    ActivityCompat.requestPermissions(Iniciar_sesion.this,
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
            ActivityCompat.requestPermissions(Iniciar_sesion.this,
                    PERMISSIONS,
                    1);
        }
    }


    public void verificar_todos_los_permisos()
    {

        String[] SMS_PERMISSIONS1 = {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION };


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            SMS_PERMISSIONS1 = new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION};
        }



        ActivityCompat.requestPermissions(Iniciar_sesion.this,
                SMS_PERMISSIONS1,
                1);


    }





}
