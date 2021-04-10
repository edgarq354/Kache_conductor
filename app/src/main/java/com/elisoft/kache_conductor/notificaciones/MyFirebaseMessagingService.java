package com.elisoft.kache_conductor.notificaciones;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.elisoft.kache_conductor.Constants;
import com.elisoft.kache_conductor.Menu_taxi;
import com.elisoft.kache_conductor.Notificacion_mensaje;
import com.elisoft.kache_conductor.Notificacion_pedido_cancelado;
import com.elisoft.kache_conductor.R;
import com.elisoft.kache_conductor.SqLite.AdminSQLiteOpenHelper;
import com.elisoft.kache_conductor.chat.Chat;
import com.elisoft.kache_conductor.chat.Servicio_mensaje_recibido;
import com.elisoft.kache_conductor.chat.handy.Menu_Canal;
import com.elisoft.kache_conductor.chat.handy.Servicio_recibir_audio;
import com.elisoft.kache_conductor.historial_notificacion.Notificacion;
import com.elisoft.kache_conductor.servicio.Servicio_cargar_punto_google;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import okio.ByteString;


/**
 * Created by ROMAN on 24/11/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {

            //envio de ultima ubicacion del motista


             Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                sendPushNotification(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }

        }
    }

    private void sendPushNotification(JSONObject json) {
        // opcionalmente podemos mostrar el json en log
       // Log.e(TAG, "Notification JSON " + json.toString());

        try {
            // obtener los datos de json
            JSONObject data = json.getJSONObject("data");

            // análisis de datos json
            String title = data.getString("title");
            String message = data.getString("message");
            String cliente = data.getString("cliente");
            String id_pedido = data.getString("id_pedido");
            String nombre = data.getString("nombre");
            String latitud = data.getString("latitud");
            String longitud = data.getString("longitud");
            String tipo = data.getString("tipo");
            String fecha = data.getString("fecha");
            String hora = data.getString("hora");
            String indicacion = data.getString("indicacion");
            int clase_vehiculo=0;
            int tipo_pedido_empresa=0;
            try{
                clase_vehiculo = Integer.parseInt(data.getString("clase_vehiculo"));
                tipo_pedido_empresa = Integer.parseInt(data.getString("tipo_pedido_empresa"));
            }catch (Exception e){
                clase_vehiculo=0;
                tipo_pedido_empresa=0;
            }

            JSONArray pedido=null;
            if(data.getString("pedido").equals("")==false)
            {
                pedido=data.getJSONArray("pedido");
            }


            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());






switch (tipo)
{
    case "1":
        //usuario
        //se iniciar el servicio de obtencion de coordenadas deltaxi...

        break;
    case "2":
//SOLICITUDES DE MOIVIL
        if(get_estado()==1) {
            if(getSonido()==0){




            if(clase_vehiculo==5 )
            {

                Intent moto = new Intent(getApplicationContext(), Menu_taxi.class);
                Intent dialogIntent = new Intent(this, Menu_taxi.class);
                if(TienePedido() == true){
                     moto = new Intent(getApplicationContext(), Menu_taxi.class);
                     dialogIntent = new Intent(this, Menu_taxi.class);
                }else{
                    moto = new Intent(getApplicationContext(), Menu_taxi.class);
                     dialogIntent = new Intent(this, Menu_taxi.class);
                }
                // crear una intención para la notificación
                moto.putExtra("id_pedido", id_pedido);
                moto.putExtra("nombre", nombre);
                moto.putExtra("latitud", latitud);
                moto.putExtra("longitud", longitud);
                moto.putExtra("mensaje", message);
                moto.putExtra("indicacion", indicacion);
                moto.putExtra("clase_vehiculo", clase_vehiculo);
                moto.putExtra("tipo_pedido_empresa", tipo_pedido_empresa);


                dialogIntent.putExtra("id_pedido", id_pedido);
                dialogIntent.putExtra("nombre", nombre);
                dialogIntent.putExtra("latitud", latitud);
                dialogIntent.putExtra("longitud", longitud);
                dialogIntent.putExtra("mensaje", message);
                dialogIntent.putExtra("clase_vehiculo", clase_vehiculo);
                dialogIntent.putExtra("tipo_pedido_empresa", tipo_pedido_empresa);
                dialogIntent.putExtra("indicacion", indicacion);
                dialogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mNotificationManager.notificacion_delivery_activity(title, message, moto,clase_vehiculo);
                startActivity(dialogIntent);
            }
            else if (TienePedido() == false)
            {

                // crear una intención para la notificación
                //Intent moto = new Intent(getApplicationContext(), Notificacion_pedido_m.class);
                Intent moto = new Intent(getApplicationContext(), Menu_taxi.class);
                moto.putExtra("id_pedido", id_pedido);
                moto.putExtra("nombre", nombre);
                moto.putExtra("latitud", latitud);
                moto.putExtra("longitud", longitud);
                moto.putExtra("mensaje", message);
                moto.putExtra("indicacion", indicacion);
                moto.putExtra("clase_vehiculo", clase_vehiculo);
                moto.putExtra("tipo_pedido_empresa", tipo_pedido_empresa);


               // Intent dialogIntent = new Intent(this, Notificacion_pedido_m.class);
               // cargar notifi
                Intent dialogIntent = new Intent(this, Menu_taxi.class);
                dialogIntent.putExtra("id_pedido", id_pedido);
                dialogIntent.putExtra("nombre", nombre);
                dialogIntent.putExtra("latitud", latitud);
                dialogIntent.putExtra("longitud", longitud);
                dialogIntent.putExtra("mensaje", message);
                dialogIntent.putExtra("clase_vehiculo", clase_vehiculo);
                dialogIntent.putExtra("tipo_pedido_empresa", tipo_pedido_empresa);
                dialogIntent.putExtra("indicacion", indicacion);
                dialogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mNotificationManager.notificacion_pedir_taxi_activity(title, message, moto,clase_vehiculo);

                startActivity(dialogIntent);
            }


            }
        }


        break;
    case "3":
        //enviar notificacion sin acccion.
        break;
    case  "4":
        //notificacion oculpa para los motistas...(obtener ubicacion)
        break;
    case  "5":
        // crear una intención para la notificación

        break;
    case  "6":
    // crear una intención para la notificación
        Intent dialogo_notificacion = new Intent(this, Notificacion_mensaje.class);
        dialogo_notificacion.putExtra("mensaje",message);
        dialogo_notificacion.putExtra("titulo",title);
        dialogo_notificacion.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dialogo_notificacion);


        mNotificationManager.notificacion_con_activity(title, message, dialogo_notificacion);

        cargar_notificacion(title,message,cliente,id_pedido,nombre,latitud,longitud,tipo,fecha,hora,indicacion);

        break;

    case  "7":
    // crear una intención para la notificación
    SharedPreferences p = getSharedPreferences("ultimo_pedido_conductor", Context.MODE_PRIVATE);
    SharedPreferences.Editor edi=p.edit();
    edi.putString("id_pedido","");
    edi.commit();
        SharedPreferences datos_= getSharedPreferences("perfil_conductor", Context.MODE_PRIVATE);
       if(datos_.getString("ci","").equals("")==false)
        {
            Intent taxiIntent= new Intent(getApplicationContext(),Menu_taxi.class);
            mNotificationManager.notificacion_con_activity(title, message, taxiIntent);
        }

    break;

    case  "8":
        // notificacion para verificar la actualizacion nueva
        SharedPreferences act = getSharedPreferences("actualizacion_elitex", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit=act.edit();
        edit.putInt("version",Integer.parseInt(data.getString("version")));
        edit.putString("mensaje",data.getString("mensaje"));
        edit.commit();

        break;

    case  "9":
        // crear una intención para la notificación
        SharedPreferences inf= getSharedPreferences("informacion", Context.MODE_PRIVATE);
        SharedPreferences.Editor e=inf.edit();
        e.putString("informacion",data.getString("informacion"));
        e.commit();

        break;

    case  "10":
        //finalizar el pedido


        break;
    case  "11":
        //notificacion para cerrar sesion..


        break;
    case  "12":
        //pedido cancelado por el taxista para el pasaero..
        //PASAJERO

        SharedPreferences pedido_ultimo1 = getSharedPreferences("ultimo_pedido_conductor", MODE_PRIVATE);
        SharedPreferences.Editor editar1=pedido_ultimo1.edit();
        editar1.putString("nombre_taxi", "");
        editar1.putString("celular", "");
        editar1.putString("marca", "");
        editar1.putString("placa", "");
        editar1.putString("color", "");
        editar1.putString("id_pedido", "");
        editar1.putInt("notificacion_cerca", 0);
        editar1.putInt("notificacion_llego", 0);

        editar1.commit();
        //se vacia los puntos guardados de todos los pedido...
        vaciar_toda_la_base_de_datos();
        Intent usus1= new Intent(getApplicationContext(),Notificacion.class);
        mNotificationManager.notificacion_con_error_activity(title, message, usus1);

        cargar_notificacion(title,message,cliente,id_pedido,nombre,latitud,longitud,tipo,fecha,hora,indicacion);

        break;
    case  "13":
        //pedido cancelado por el pasajero para el taxista..
        // TAXISTA

        SharedPreferences p1 = getSharedPreferences("ultimo_pedido_conductor", Context.MODE_PRIVATE);
        SharedPreferences.Editor edi1=p1.edit();
        edi1.putString("id_pedido","");
        edi1.putInt("notificacion_cerca", 0);
        edi1.putInt("notificacion_llego", 0);
        edi1.commit();
        SharedPreferences datos_1= getSharedPreferences("perfil_conductor", Context.MODE_PRIVATE);
        if(datos_1.getString("ci","").equals("")==false)
        {
            //cambia el estado a activo. una ves que llego la notificacion de cancelacion...
            //habilitandose para otro pedido.
            SharedPreferences.Editor editor1=datos_1.edit();
            editor1.putString("estado","1");
            editor1.commit();



            Intent taxi1= new Intent(getApplicationContext(),Notificacion.class);
            mNotificationManager.notificacion_con_error_activity(title, message, taxi1);
        }
        Intent dialogo_cancelacion = new Intent(this, Notificacion_pedido_cancelado.class);
        dialogo_cancelacion.putExtra("mensaje",message);
        dialogo_cancelacion.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dialogo_cancelacion);

        break;

    case  "14":
        //el taxi esta cerca

        break;
    case  "15":
        //llego el taxi

        break;
    case "16":
        //NOTIFICACION DE PANICO.

        break;
    case "17":
        //RESERVA DE MOVIL ACEPTADO.
        break;
    case "18":
        //LEVANTAR SERVICIO.
        ejecutar_servicio_volley();
        break;
    case "19":
        //LEVANTAR SERVICIO  cambiando el estado a activo.
        SharedPreferences perfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);
        SharedPreferences.Editor editor=perfil.edit();
        editor.putString("estado","1");
        editor.commit();
        ejecutar_servicio_volley();
        //Intent servicio_cargar_punto1=new Intent(this,Servicio_cargar_punto.class);
        //startService(servicio_cargar_punto1);
        break;
    case "20":
        //para el SERVICIO.
        ejecutar_servicio_volley();
        break;
    case "21":
        //NOTIFICACION ELIMINAR DATOS DEL USUARIO
        eliminar_datos_share();
        Intent dialogo_cancelacion2 = new Intent(this, Notificacion_pedido_cancelado.class);
        dialogo_cancelacion2.putExtra("mensaje",message);
        dialogo_cancelacion2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dialogo_cancelacion2);
        break;

    case  "25":
        // crear una intención para la notificación

        /*Intent dialogo_notificacion1 = new Intent(this,Menu_taxi.class);
        dialogo_notificacion1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dialogo_notificacion1);
        */


        /*
        Intent servicio1=new Intent(this,Servicio_cargar_punto.class);
        startService(servicio1);
        */

        verificar_estado_servicio();

        /*
        Intent taxi1= new Intent(getApplicationContext(),Menu_taxi.class);
        mNotificationManager.notificacion_con_activity_menu(title, message, taxi1);
        */
        break;

    case "100":
        Intent serviceIntent = new Intent(this, Servicio_mensaje_recibido.class);
        serviceIntent.putExtra("id_chat", data.getString("id_chat"));
        serviceIntent.putExtra("id_conductor",data.getString("id_conductor"));
        serviceIntent.putExtra("id_usuario", data.getString("id_usuario"));
        serviceIntent.putExtra("titulo", title);
        serviceIntent.putExtra("mensaje",message);
        serviceIntent.putExtra("fecha", fecha);
        serviceIntent.putExtra("hora", hora);
        serviceIntent.putExtra("estado", data.getString("estado"));
        serviceIntent.putExtra("yo",data.getString("yo"));
        startService(serviceIntent);

        Intent chat= new Intent(getApplicationContext(),Chat.class);
        chat.putExtra("id_conductor",data.getString("id_conductor"));
        chat.putExtra("id_usuario",data.getString("id_usuario"));
        mNotificationManager.notificacion(title, message,chat);
        guardar_mensaje_enviado(data.getString("id_chat"),data.getString("id_conductor"),data.getString("id_usuario"),title,message,fecha,hora,data.getString("estado"),data.getString("yo"),data.getString("tipo_mensaje"));
break;

    case "110":
        Intent serviceIntent1 = new Intent(this, Servicio_mensaje_recibido.class);
        serviceIntent1.putExtra("id_chat", data.getString("id_chat"));
        serviceIntent1.putExtra("id_conductor",data.getString("id_conductor"));
        serviceIntent1.putExtra("id_usuario", data.getString("id_usuario"));
        serviceIntent1.putExtra("titulo", title);
        serviceIntent1.putExtra("mensaje",message);
        serviceIntent1.putExtra("fecha", fecha);
        serviceIntent1.putExtra("hora", hora);
        serviceIntent1.putExtra("estado", data.getString("estado"));
        serviceIntent1.putExtra("yo",data.getString("yo"));

        guardar_mensaje_audio(
                data.getString("id_chat"),
                data.getString("id_conductor"),
                data.getString("id_administrador"),
                data.getString("id_usuario"),
                title,
                message,
                data.getString("tipo_mensaje"),
                data.getString("canal"),
                fecha,
                hora,
                data.getString("estado"),
                data.getString("yo"),
                "0"
        );

        startService(serviceIntent1);
        //servicio de reproduccion de audio
        Intent intent = new Intent(getApplicationContext(), Servicio_recibir_audio.class);
        intent.putExtra("id_chat",Integer.parseInt(data.getString("id_chat")));
        intent.putExtra("canal","TAXI VALLE");
        intent.putExtra("url",getString(R.string.servidor) + "frmChat.php?opcion=get_audio_canal_por_id_chat");
        startService(intent);

        Intent chat1= new Intent(getApplicationContext(), Menu_Canal.class);

        mNotificationManager.notificacion_sin_salto(title, message,chat1);

        startActivity(chat1);
        break;
   default:
       // crear una intención para la notificación
       Intent dialogo_no = new Intent(this, Notificacion_mensaje.class);
       dialogo_no.putExtra("mensaje",message);
       dialogo_no.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       startActivity(dialogo_no);

       break;
}





        } catch (JSONException e) {
           Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
           Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    public void guardar_mensaje_enviado(String id, String id_conductor,String id_usuario,String titulo,String mensaje,String fecha,String hora,String estado,String yo,String tipo) {
        try {
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, getString(R.string.nombre_sql), null, Integer.parseInt(getString(R.string.version_sql)));

            SQLiteDatabase bd = admin.getWritableDatabase();
            ContentValues registro = new ContentValues();
            registro.put("id", id);
            registro.put("id_conductor", id_conductor);
            registro.put("id_usuario", id_usuario);
            registro.put("fecha", fecha);
            registro.put("hora", hora);
            registro.put("mensaje", mensaje);
            registro.put("titulo", titulo);
            registro.put("estado", estado);
            registro.put("yo", yo);
            registro.put("tipo", tipo);
            bd.insert("chat", null, registro);
            bd.close();
        } catch (Exception e) {
            Log.w("ERROR registro chat", e.toString());
        }
    }

    public void guardar_mensaje_audio(
            String id,
            String id_conductor,
            String id_administrador,
            String id_usuario,
            String titulo,
            String mensaje,
            String tipo,
            String canal,
            String fecha,
            String hora,
            String estado,
            String yo,
            String visto
            ) {
        try {
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, getString(R.string.nombre_sql), null, Integer.parseInt(getString(R.string.version_sql)));

            SQLiteDatabase bd = admin.getWritableDatabase();
            ContentValues registro = new ContentValues();
            registro.put("id", id);
            registro.put("id_conductor", id_conductor);
            registro.put("id_administrador", id_administrador);
            registro.put("id_usuario", id_usuario);
            registro.put("fecha", fecha);
            registro.put("hora", hora);
            registro.put("mensaje", mensaje);
            registro.put("titulo", titulo);
            registro.put("tipo", tipo);
            registro.put("canal", canal);
            registro.put("titulo", titulo);
            registro.put("estado", estado);
            registro.put("yo", yo);
            registro.put("visto", visto);
            bd.insert("audio", null, registro);
            bd.close();
        } catch (Exception e) {
            Log.w("error al registro Chat", e.toString());
        }
    }



    private void eliminar_datos_share() {
        SharedPreferences datos_perfil = getSharedPreferences("perfil_conductor", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = datos_perfil.edit();
        editor.putString("nombre","");
        editor.putString("peterno","");
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

    private void cargar_notificacion(String title, String message, String cliente, String id_pedido, String nombre, String latitud, String longitud, String tipo, String fecha, String hora,String indicacion) {
       try {
           AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, getString(R.string.nombre_sql), null, Integer.parseInt(getString(R.string.version_sql)));

           SQLiteDatabase bd = admin.getWritableDatabase();
           ContentValues registro = new ContentValues();
           registro.put("titulo", title);
           registro.put("mensaje", message);
           registro.put("cliente", cliente);
           registro.put("nombre", nombre);
           registro.put("tipo", tipo);
           registro.put("fecha", fecha);
           registro.put("hora", hora);
           registro.put("latitud", latitud);
           registro.put("longitud", longitud);
           registro.put("id_pedido", id_pedido);
           registro.put("indicacion", indicacion);
           bd.insert("notificacion", null, registro);
           bd.close();
       }catch (Exception e)
       {
           Log.e("Notificacion",""+e);
       }

    }
    public void vaciar_toda_la_base_de_datos() {

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, getString(R.string.nombre_sql), null, Integer.parseInt(getString(R.string.version_sql)));

        SQLiteDatabase db = admin.getWritableDatabase();

        db.execSQL("delete from puntos_pedido");
        db.close();
        // Log.e("sqlite ", "vaciar todo");
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

    public  int getSonido()
    {
        SharedPreferences perfil=getSharedPreferences(getString(R.string.sonido),MODE_PRIVATE);
        return perfil.getInt("sonido",0);
    }


    public boolean TienePedido()
    {
        boolean sw=false;
        int id_pedido=0;
        try{
            SharedPreferences preferenciasPedido=getSharedPreferences("ultimo_pedido_conductor", Context.MODE_PRIVATE);
            id_pedido= Integer.parseInt(preferenciasPedido.getString("id_pedido","0"));
            if(id_pedido==0)
            {
                sw=false;
            }else
            {
                sw=true;
            }
        }catch (Exception e)
        {
            sw=false;
        }
        return sw;
    }
    public void reproducir_audio(String audio){


        File f = buildAudioFileFromReceivedBytes(audio);

        playAudio(f);
    }




    @NonNull
    private File buildAudioFileFromReceivedBytes(String audio) {
        List<byte[]> sLista = new ArrayList<>();
        ByteString d = ByteString.decodeHex(audio);
        byte[] bytes1 = d.toByteArray();

        sLista.add(bytes1);


        File f = new File(getCacheDir().getAbsolutePath() + "/received.3gp");
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        try {
            out = (new FileOutputStream(f));
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            for (byte[] b : sLista) {
                out.write(b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }

    private void playAudio(File f) {
        MediaPlayer mPlayer;
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(this, Uri.parse(f.getPath()));
            mPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("Audio handy", "onClosing: dudation in millis: " + mPlayer.getDuration());

        mPlayer.start();
    }



    public void verificar_estado_servicio(){
        SharedPreferences pedido=getSharedPreferences("ultimo_pedido_conductor",MODE_PRIVATE);
        int id_pedido=0;
        try{
            id_pedido=Integer.parseInt(pedido.getString("id_pedido","0"));
        }catch (Exception e){
            id_pedido=0;
        }
        if(id_pedido>0)
        {

            ejecutar_servicio_volley();
        }else
        {
            SharedPreferences  prefe = getSharedPreferences("perfil_conductor", Context.MODE_PRIVATE);

            if(prefe.getString("estado","0").equals("1"))
            {
                ejecutar_servicio_volley();
            }
        }

    }

    public void ejecutar_servicio_volley()
    {
        try{


            Intent SERVICIO2=new Intent(MyFirebaseMessagingService.this, Servicio_cargar_punto_google.class);
            SERVICIO2.setAction(Constants.ACTION.START_ACTION);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                startForegroundService(SERVICIO2);
            } else {
                startService(SERVICIO2);
            }
        }catch (Exception ee)
        {
            Toast.makeText(getApplicationContext(), ee.toString(), Toast.LENGTH_LONG).show();
        }
    }





}