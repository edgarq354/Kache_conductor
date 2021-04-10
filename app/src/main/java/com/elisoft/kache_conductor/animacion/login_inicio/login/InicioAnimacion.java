package com.elisoft.kache_conductor.animacion.login_inicio.login;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.elisoft.kache_conductor.Constants;
import com.elisoft.kache_conductor.Menu_taxi;
import com.elisoft.kache_conductor.R;

import com.elisoft.kache_conductor.Suceso;
import com.elisoft.kache_conductor.animacion.login_inicio.login.login.LoginFragment;
import com.elisoft.kache_conductor.animacion.login_inicio.login.login.SignUpFragment;
import com.elisoft.kache_conductor.databinding.ActivityMainBinding;
import com.elisoft.kache_conductor.notificaciones.SharedPrefManager;
import com.elisoft.kache_conductor.servicio.Servicio_cargar_punto_google;
import static com.elisoft.kache_conductor.animacion.login_inicio.login.FlexibleFrameLayout.ORDER_LOGIN_STATE;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

public class InicioAnimacion extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private boolean isLogin = true;

    Button iniciar_sesion, iniciar_taxi;
    ProgressDialog pDialog;
    Suceso suceso;

    TextView tv_olvidaste_tu_contrasenia, tv_condiciones;


    String imei = "";
    SignUpFragment topSignUpFragment;
    LoginFragment topLoginFragment;

    AlertDialog alert2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

         topLoginFragment = new LoginFragment();
        topSignUpFragment = new SignUpFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_fragment, topLoginFragment)
                .replace(R.id.sign_up_fragment, topSignUpFragment)
                .commit();


        binding.loginFragment.setRotation(-90);

        binding.button.setOnSignUpListener(topSignUpFragment);
        binding.button.setOnLoginListener(topLoginFragment);

        binding.button.setOnButtonSwitched(isLogin -> {
            binding.getRoot()
                    .setBackgroundColor(ContextCompat.getColor(
                            this,
                            isLogin ? R.color.colorPrimary : R.color.secondPage));
        });

        binding.loginFragment.setVisibility(VISIBLE);

        if (verificar_login_taxi()) {
            startActivity(new Intent(this, Menu_taxi.class));
            finish();
        }


        aceptar_condiciones();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        binding.loginFragment.setPivotX(binding.loginFragment.getWidth() / 2);
        binding.loginFragment.setPivotY(binding.loginFragment.getHeight());
        binding.signUpFragment.setPivotX(binding.signUpFragment.getWidth() / 2);
        binding.signUpFragment.setPivotY(binding.signUpFragment.getHeight());
    }

    public void switchFragment(View v) {
        cambiar_vista();
    }

    public void cambiar_vista()
    {

        if (isLogin) {
            iniciar_sesion();
/*
            binding.loginFragment.setVisibility(VISIBLE);
            binding.loginFragment.animate().rotation(0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    binding.signUpFragment.setVisibility(INVISIBLE);
                    binding.signUpFragment.setRotation(90);
                    binding.wrapper.setDrawOrder(ORDER_LOGIN_STATE);
                }
            });

            Toast.makeText(this,"mensaje 1"+binding.signUpFragment.,Toast.LENGTH_LONG).show();
            /*
 */

        } else {
            /*
            binding.signUpFragment.setVisibility(VISIBLE);
            binding.signUpFragment.animate().rotation(0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    binding.loginFragment.setVisibility(INVISIBLE);
                    binding.loginFragment.setRotation(-90);
                    binding.wrapper.setDrawOrder(ORDER_SIGN_UP_STATE);
                }
            });

            Toast.makeText(this,"mensaje 2",Toast.LENGTH_LONG).show();

             */
            if (verificar_login_taxi()) {
                startActivity(new Intent(this, Menu_taxi.class));
                finish();
            }
           // startActivity(new Intent(this, Iniciar_sesion.class));
        }

       // isLogin = !isLogin;
       // binding.button.startAnimation();
    }

    public void iniciar_sesion()
    {

            String token = SharedPrefManager.getInstance(this).getDeviceToken();

            SharedPrefManager.getInstance(this).guardarToken();

            if (token != null || token == "") {

                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    imei = Settings.Secure.getString(
                            getContentResolver(),
                            Settings.Secure.ANDROID_ID);

                    SharedPreferences login = getSharedPreferences("login", MODE_PRIVATE);
                    servicio_iniciar_sesion(getString(R.string.servidor) + "frmTaxi.php?opcion=iniciar_sesion", login.getString("usuario", "").trim(), login.getString("contrasenia", "").trim(), token, imei);
                } else {
                    if (ActivityCompat.checkSelfPermission(InicioAnimacion.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        verificar_permiso_imei();
                    }else {
                        imei = telephonyManager.getDeviceId();

                        SharedPreferences login = getSharedPreferences("login", MODE_PRIVATE);
                        servicio_iniciar_sesion(getString(R.string.servidor) + "frmTaxi.php?opcion=iniciar_sesion", login.getString("usuario", "").trim(), login.getString("contrasenia", "").trim(), token, imei);
                    }
                }



            } else {
                mensaje_error("No se a podido generar el Token. porfavor active sus datos de Red e instale Google Pay Service");
             }
    }




    private void servicio_iniciar_sesion(String v_url, String usuario, String contrasenia, String token_usuario, String imei) {
        //para el progres Dialog
        pDialog = new ProgressDialog(InicioAnimacion.this);
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

                                    binding.loginFragment.setVisibility(VISIBLE);
                                    binding.loginFragment.animate().rotation(0).setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            binding.signUpFragment.setVisibility(INVISIBLE);
                                            binding.signUpFragment.setRotation(90);
                                            binding.wrapper.setDrawOrder(ORDER_LOGIN_STATE);
                                        }
                                    });

                                    isLogin = !isLogin;
                                    binding.button.startAnimation();
                                    //iniciar_sesion_taxi();
                                    estoy_libre();
                                } else  {

                                    binding.button.startAnimation();
                                    binding.button.startAnimation();

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
                    ActivityCompat.requestPermissions(InicioAnimacion.this,
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
            ActivityCompat.requestPermissions(InicioAnimacion.this,
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
        ActivityCompat.requestPermissions(InicioAnimacion.this,
                SMS_PERMISSIONS1,
                1);


    }



    public void  aceptar_condiciones()
    {


        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.aceptar_permiso_ubicacion, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setCancelable(false);


        final Button bt_aceptar_continuar= (Button) promptView.findViewById(R.id.bt_aceptar_continuar);
        final CheckBox cb_terminos_condiciones= (CheckBox) promptView.findViewById(R.id.cb_terminos_condiciones);

        bt_aceptar_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cb_terminos_condiciones.isChecked())
                {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    alert2.cancel();
                    verificar_todos_los_permisos();
                }else
                {
                    Toast.makeText(InicioAnimacion.this,"Aun no a aceptado los terminos y condiciones",Toast.LENGTH_SHORT).show();
                }

            }
        });

        // create an alert dialog
        alert2 = alertDialogBuilder.create();
        alert2.show();
    }




}
