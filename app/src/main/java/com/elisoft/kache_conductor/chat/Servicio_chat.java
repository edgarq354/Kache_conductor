package com.elisoft.kache_conductor.chat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.elisoft.kache_conductor.SqLite.AdminSQLiteOpenHelper;
import com.elisoft.kache_conductor.notificaciones.SharedPrefManager;
import com.elisoft.kache_conductor.Menu_taxi;
import com.elisoft.kache_conductor.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONObject;

import java.net.URISyntaxException;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class Servicio_chat extends Service {
    private Socket mSocket;
    public static final String TAG = Servicio_chat.class.getSimpleName();
    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "on created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "start command", Toast.LENGTH_SHORT).show();
        try {
            mSocket = IO.socket("http://64.227.10.80:5000/");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        SharedPreferences preferencias = getSharedPreferences("ultimo_pedido_conductor", Context.MODE_PRIVATE);
        SharedPreferences conductor = getSharedPreferences("perfil_conductor", MODE_PRIVATE);
        String id_conductor = conductor.getString("ci", "");
        String id_usuario=preferencias.getString("id_usuario","");


        mSocket.on("chat"+id_usuario+":"+id_conductor, onNewMessage);
        mSocket.connect();
        return START_STICKY;
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "run: ");
            Log.i(TAG, "run: " + args.length);
            JSONObject data = (JSONObject) args[0];

            String id;


            try {

                id = data.getString("uniqueId");

                String id_chat="";
                String id_conductor="";
                String id_usuario="";
                String titulo="";
                String mensaje="";
                String fecha="";
                String hora="";
                String tipo="";

                id_chat = data.getString("id_chat");
                id_conductor = data.getString("id_conductor");
                id_usuario = data.getString("id_usuario");
                titulo = data.getString("titulo");
                mensaje = data.getString("mensaje");
                fecha = data.getString("fecha");
                hora = data.getString("hora");
                tipo = data.getString("tipo");

                String yo="";
                if(id.equals(SharedPrefManager.getInstance(Servicio_chat.this).mostrarToken())==true) {
                    yo="0";
                }else{
                    yo="1";
                }

                guardar_mensaje_enviado(id_chat,
                        id_conductor,
                        id_usuario
                        ,titulo,
                        mensaje,
                        fecha,
                        hora,
                        "1",
                        yo,
                        tipo);


                if(id.equals(SharedPrefManager.getInstance(Servicio_chat.this).mostrarToken())==false) {
                    Log.i("MENSAJE", "Usuario: " + titulo + " mesnaje: " + mensaje + "  id:" + id);


                    Log.d(TAG, "call: new message ");

                    Intent chat = new Intent(getApplicationContext(), Chat.class);
                    chat.putExtra("id_conductor", data.getString("id_conductor"));
                    chat.putExtra("id_usuario", data.getString("id_usuario"));
                    chat.putExtra("titulo", titulo);
                    //sendGeneralNotification(getApplicationContext(), "1", username, message, null);
                    if(tipo.equals("AUDIO"))
                    {
                        mensaje="Audio";
                    }
                    notificacion(titulo, mensaje, chat,tipo);
                }


            } catch (Exception e) {
                return;
            }



        }
    };

    public void notificacion(String title, String message, Intent intent,String tipo) {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pintent = PendingIntent.getActivity(this,
                0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(false)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(pintent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert notificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        assert notificationManager != null;
        notificationManager.notify(0 /* Request Code */, mBuilder.build());

    }

    private void sendGeneralNotification(Context context, String uniqueId,
                                         String title, String contentText,
                                         @Nullable Class<?> resultClass) {

        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

         NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true);


        builder.setContentTitle(title);
        builder.setContentText(contentText);
        builder.setGroup("faskfjasfa");
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setStyle(new NotificationCompat.BigTextStyle()
                .setSummaryText(title)
                .setBigContentTitle(title)
                .bigText(contentText)
        );

        Intent requestsViewIntent = new Intent(context, Menu_taxi.class);
        requestsViewIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        requestsViewIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent requestsViewPending = PendingIntent.getActivity(context, Integer.valueOf(uniqueId), requestsViewIntent, 0);
        builder.setContentIntent(requestsViewPending);
        builder.setSmallIcon(R.mipmap.ic_movil_notificacion);

        builder.setShowWhen(true);
        Notification notification = builder.build();
        notificationManagerCompat.notify(Integer.valueOf(uniqueId), notification);
    }

    private Notification getNotification() {
        Notification notification;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setColor(getResources()
                .getColor(R.color.material_deep_teal_500))
                .setAutoCancel(true);

        notification = builder.build();
        notification.flags = Notification.FLAG_FOREGROUND_SERVICE | Notification.FLAG_AUTO_CANCEL;

        return notification;
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
            Log.d("registro Chat", e.toString());
        }
    }

}