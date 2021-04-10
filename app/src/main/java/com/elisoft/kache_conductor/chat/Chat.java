package com.elisoft.kache_conductor.chat;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.elisoft.kache_conductor.R;
import com.elisoft.kache_conductor.SqLite.AdminSQLiteOpenHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

public class Chat extends AppCompatActivity {


    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private ImageView buttonSend;

    ImageView im_perfil;
    TextView tv_nombre;
    private boolean side =true;
    SharedPreferences perfil;
    String id_usuario;

    public static final String mBroadcastStringAction = "com.truiton.broadcast.string";
    private IntentFilter mIntentFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonSend =  findViewById(R.id.send);

        listView = (ListView) findViewById(R.id.msgview);

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right);
        listView.setAdapter(chatArrayAdapter);

        chatText = (EditText) findViewById(R.id.msg);
        im_perfil = (ImageView) findViewById(R.id.im_perfil);
        tv_nombre = (TextView) findViewById(R.id.tv_nombre);


        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right);
        listView.setAdapter(chatArrayAdapter);


        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return enviar_mensaje();
                }
                return false;
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                enviar_mensaje();
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });

        try{
            Bundle bundle=getIntent().getExtras();
            id_usuario=  bundle.getString("id_usuario","");
            tv_nombre.setText(bundle.getString("titulo",""));
            getImage(String.valueOf(id_usuario));
        }catch (Exception e)
        {
            finish();
        }


        perfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);
         lista_contacto(id_usuario,perfil.getString("ci",""));

        //Intent filter para recibir datos del servicio.
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(mBroadcastStringAction);



        //
        SharedPreferences preferencias = getSharedPreferences("ultimo_pedido_conductor", Context.MODE_PRIVATE);
        Intent it_chat=new Intent(getApplicationContext(),Chat_pedido.class);
       // it_chat.putExtra("id_usuario",preferencias.getString("id_usuario",""));
        it_chat.putExtra("id_usuario",id_usuario);
        it_chat.putExtra("titulo",tv_nombre.getText().toString().trim());
        startActivity(it_chat);
        finish();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private boolean enviar_mensaje() {
        if(chatText.getText().toString().trim().length()>0){
        chatArrayAdapter.add(new CMensaje(side, chatText.getText().toString(),perfil.getString("nombre",""),now(),hora(),0, Integer.parseInt(id_usuario),Integer.parseInt(perfil.getString("ci","")),0,"TEXTO",0));
        }
        chatText.setText("");

        return true;
    }


    public static String now() {
        String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }
    public static String hora() {
        String DATE_FORMAT_NOW = "HH:mm:ss";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }
    public void guardar_mensaje_enviado(String id, String id_conductor, String id_usuario, String titulo, String mensaje, String fecha, String hora, String estado, String yo)
    {

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, getString(R.string.nombre_sql), null, Integer.parseInt(getString(R.string.version_sql)));

        SQLiteDatabase bd = admin.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("id", id);
        registro.put("id_conductor", id_conductor);
        registro.put("id_usuario", id_usuario);
        registro.put("fecha",fecha);
        registro.put("hora", hora);
        registro.put("mensaje", mensaje);
        registro.put("titulo",titulo);
        registro.put("estado",estado);
        registro.put("yo",yo);
        bd.insert("chat", null, registro);
        bd.close();

    }


    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(mBroadcastStringAction)) {

                chatArrayAdapter.add(new CMensaje(false, intent.getStringExtra("mensaje"),intent.getStringExtra("titulo"),intent.getStringExtra("fecha"),intent.getStringExtra("hora"),Integer.parseInt(intent.getStringExtra("estado")),Integer.parseInt(intent.getStringExtra("id_usuario")),Integer.parseInt(intent.getStringExtra("id_conductor")),Integer.parseInt(intent.getStringExtra("yo")),"TEXTO",0));

                Intent stopIntent = new Intent(Chat.this,
                        Servicio_mensaje_recibido.class);
                stopService(stopIntent);
            }
        }
    };

    @Override
    protected void onPause() {
        unregisterReceiver(mReceiver);
        super.onPause();
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
            { dw = getResources().getDrawable(R.drawable.ic_logo_app);}

            imagen_circulo(dw,im_perfil);
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
        try {
            imagen.setImageDrawable(roundedDrawable);
        }catch (Exception e)
        {

        }
    }


    public  void lista_contacto(String id_usuario, String id_conductor)
    {
        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right);



try {
    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, getString(R.string.nombre_sql), null, Integer.parseInt(getString(R.string.version_sql)));

    SQLiteDatabase bd = admin.getWritableDatabase();

    Cursor fila = bd.rawQuery("select id,id_conductor,id_usuario,titulo,mensaje,fecha,hora,estado,yo,tipo from chat where id_usuario=" + id_usuario + " and id_conductor=" + id_conductor + " order by id asc ", null);

    if (fila.moveToFirst()) {  //si ha devuelto 1 fila, vamos al primero (que es el unico)

        do {

            int id = Integer.parseInt(fila.getString(0));
            String id_cond = fila.getString(1);
            String id_usu = fila.getString(2);
            String titulo = fila.getString(3);
            String mensaje = fila.getString(4);
            String fecha = fila.getString(5);
            String hora = fila.getString(6);
            String estado = fila.getString(7);
            String yo = fila.getString(8);
            String tipo = fila.getString(9);

            boolean sw_left = false;
            if (yo.equals("0")) {
                sw_left = true;
                yo="0";
            }else{
                yo="1";
            }
            chatArrayAdapter.add(new CMensaje(sw_left, mensaje, titulo, fecha, hora, Integer.parseInt(estado), Integer.parseInt(id_usu), Integer.parseInt(id_cond),Integer.parseInt(yo),tipo,id));

        } while (fila.moveToNext());

    } else {

    }
    bd.close();

}catch (Exception e)
{
    e.printStackTrace();
}


        listView.setAdapter(chatArrayAdapter);

    }




}
