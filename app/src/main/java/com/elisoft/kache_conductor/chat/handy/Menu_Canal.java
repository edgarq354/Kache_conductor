package com.elisoft.kache_conductor.chat.handy;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.elisoft.kache_conductor.Menu_taxi;
import com.elisoft.valle_grande_conductor.R;
import com.elisoft.kache_conductor.Suceso;
import com.zello.sdk.BluetoothAccessoryState;
import com.zello.sdk.BluetoothAccessoryType;
import com.zello.sdk.Contact;
import com.zello.sdk.ContactStatus;
import com.zello.sdk.ContactType;
import com.zello.sdk.Tab;
import com.zello.sdk.Zello;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import okio.ByteString;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class Menu_Canal extends AppCompatActivity implements View.OnClickListener ,com.zello.sdk.Events{


    private static final String LOG_TAG = "AudioRecordTest";
    public static final int RC_RECORD_AUDIO = 1000;
    public static String sRecordedFileName;
    private MediaRecorder mRecorder;
    MediaPlayer mp;


    Button  bt_escuchar,bt_conectar;
    ImageView im_reproducir;
    ImageView im_grabar;
    Button im_zello;

    Suceso suceso;
    boolean sw_grabacion=false;
    private MediaPlayer mPlayer;
    static List<byte[]> sLista = new ArrayList<>();
    private final Contact _selectedContact = new Contact();
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu__canal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        bt_escuchar=(Button)findViewById(R.id.bt_escuchar);
        bt_conectar=(Button)findViewById(R.id.bt_conectar);
        im_grabar=(ImageView)findViewById(R.id.im_grabar);
        im_reproducir=(ImageView)findViewById(R.id.im_reproducir);
        bt_escuchar.setOnClickListener(this);
        im_reproducir.setOnClickListener(this);
        bt_conectar.setOnClickListener(this);

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



        Zello.getInstance().subscribeToEvents(this);
        Zello.getInstance().configure("net.loudtalks", this);

        im_zello = ( Button) findViewById ( R .id.im_zello);

        im_zello.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent. ACTION_DOWN ) {

                    Zello.getInstance().beginMessage();

                } else if (event.getAction() == MotionEvent. ACTION_UP || event.getAction() == MotionEvent. ACTION_CANCEL ) {
                    Zello.getInstance().endMessage();
                }
                return false;
            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(this, Menu_taxi.class));
        return super.onSupportNavigateUp();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        Zello.getInstance().enterPowerSavingMode();
    }

    @Override
    protected void onStart() {
      //  ultimo_audio();
        super.onStart();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


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

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Zello.getInstance().unsubscribeFromEvents(this);
    }

    private void sendAudioBytes(FileChannel in) throws IOException {
        ByteBuffer buff = ByteBuffer.allocateDirect(32);

        String audio="";
        sLista.clear();
        while (in.read(buff) > 0) {

            buff.flip();
            String bytes = ByteString.of(buff).toString();
            //mSocket.send(bytes);

            try {
                String hexValue = bytes.substring(bytes.indexOf("hex=") + 4, bytes.length() - 1);

                audio+=hexValue;
                /*
                servicio_enviar_audio(String.valueOf(id_chat),
                        hexValue,
                        String.valueOf(numero),
                        getString(R.string.servidor) + "frmAudio.php?opcion=enviar_audio");
                */

                ByteString d = ByteString.decodeHex(hexValue);
                byte[] bytes1 = d.toByteArray();

                sLista.add(bytes1);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }

            buff.clear();
        }


        SharedPreferences prefe = getSharedPreferences("perfil_conductor", MODE_PRIVATE);

        Intent intent = new Intent(Menu_Canal.this, Servicio_enviar_audio.class);
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


    public void escuchar()
    {
        //escuchar audio
        playReceivedFile();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.bt_escuchar:
                ultimo_audio();
                break;
            case R.id.im_reproducir:
                startActivity(new Intent(this, Lista_audio_canal.class));
                break;
            case R.id.bt_conectar:
                connectChannel();
                break;
        }

    }
    private void connectChannel() {
        ContactType type = _selectedContact.getType();
        if (type == ContactType.CHANNEL || type == ContactType.GROUP || type == ContactType.CONVERSATION) {
            ContactStatus status = _selectedContact.getStatus();
            if (status == ContactStatus.OFFLINE) {
                Zello.getInstance().connectChannel(_selectedContact.getName());
            } else if (status == ContactStatus.AVAILABLE) {
                Zello.getInstance().disconnectChannel(_selectedContact.getName());
            }
        }
    }


    private void playReceivedFile() {
        File f = buildAudioFileFromReceivedBytes();

        playAudio(f);
    }

    @NonNull
    private File buildAudioFileFromReceivedBytes() {
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
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(this, Uri.parse(f.getPath()));
            mPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(LOG_TAG, "onClosing: dudation in millis: " + mPlayer.getDuration());

        mPlayer.start();
    }

    public void ultimo_audio()
    {
        //servicio de reproduccion de audio
        Intent intent = new Intent(getApplicationContext(), Servicio_recibir_audio.class);
        intent.putExtra("id_chat",0);
        intent.putExtra("canal","TAXI VALLE");
        intent.putExtra("url",getString(R.string.servidor) + "frmChat.php?opcion=get_ultimo_audio_canal");
        startService(intent);

/*
        servicio_ultimo_audio("TAXI VALLE",
                getString(R.string.servidor) + "frmChat.php?opcion=get_ultimo_audio_canal");
*/
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


            RequestQueue queue = Volley.newRequestQueue(this);

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
            sLista.clear();
            setRecordIcon(false);
            stopRecording();
            send();

        }
    }


    @Override
    public void onSelectedContactChanged() {

    }

    @Override
    public void onMessageStateChanged() {

    }

    @Override
    public void onAppStateChanged() {

    }

    @Override
    public void onLastContactsTabChanged(Tab tab) {

    }

    @Override
    public void onContactsChanged() {

    }

    @Override
    public void onAudioStateChanged() {

    }

    @Override
    public void onMicrophonePermissionNotGranted() {

    }

    @Override
    public void onBluetoothAccessoryStateChanged(BluetoothAccessoryType bluetoothAccessoryType, BluetoothAccessoryState bluetoothAccessoryState, String s, String s1) {

    }
}
