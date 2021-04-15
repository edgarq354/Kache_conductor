package com.elisoft.kache_conductor.perfil;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.elisoft.kache_conductor.servicio.Servicio_descargar_imagen_perfil;
import com.elisoft.kache_conductor.Modificar_contrasenia;
import com.elisoft.kache_conductor.R;
import com.elisoft.kache_conductor.Suceso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

public class Perfil_Conductor extends AppCompatActivity implements View.OnClickListener {
        EditText nombre,apellido,celular,email,direccion,placa;
        ImageView perfil;
        TextView credito;
    RatingBar rb_conductor,rb_vehiculo;
        LinearLayout ll_placa;
        Switch estado;
        Suceso suceso;
        ProgressDialog pDialog;

private final int PHOTO_CODE = 200;
private final int SELECT_PICTURE = 300;
private String mPath;
private Uri path;


    Intent CropIntent;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil__conductor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    try {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }catch (Exception e)
    {

    }




        ll_placa=(LinearLayout)findViewById(R.id.ll_placa);
        nombre=(EditText)findViewById(R.id.nombre);
        nombre=(EditText)findViewById(R.id.nombre);
        apellido=(EditText)findViewById(R.id.apellido);
        celular=(EditText)findViewById(R.id.celular);
        placa=(EditText)findViewById(R.id.placa);
        email=(EditText)findViewById(R.id.email);
        direccion=(EditText)findViewById(R.id.direccion);
        perfil=(ImageView)findViewById(R.id.perfil);

    credito=(TextView)findViewById(R.id.credito);
    rb_conductor=(RatingBar) findViewById(R.id.rb_conductor);
    rb_vehiculo=(RatingBar) findViewById(R.id.rb_vehiculo);
        estado=(Switch)findViewById(R.id.sw_estado);

        estado.setOnClickListener(this);
        perfil.setOnClickListener(this);
        ll_placa.setOnClickListener(this);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cargar_datos();

        imagen_en_vista(perfil);


        SharedPreferences perfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);

        Servicio_moto hilo_moto = new Servicio_moto();
        hilo_moto.execute(getString(R.string.servidor) + "frmTaxi.php?opcion=get_credito", "2",perfil.getString("ci",""));// parametro que recibe

        Intent intent = new Intent(Perfil_Conductor.this, Servicio_descargar_imagen_perfil.class);
        intent.putExtra("id_conductor",perfil.getString("ci","0"));
        startService(intent);
        }

@Override
public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_perfil, menu);
        return true;
        }

@Override
public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.it_modificar_contrasenia) {
        startActivity(new Intent(this, Modificar_contrasenia.class));

        }

        return super.onOptionsItemSelected(item);
        }

@Override
public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
        }

@Override
public void onClick(View v) {
         if(v.getId()== R.id.sw_estado)
        {
        boolean b=estado.isChecked();
        Servicio_moto hilo_moto = new Servicio_moto();
        SharedPreferences prefe = getSharedPreferences("perfil_conductor", Context.MODE_PRIVATE);
        String id=prefe.getString("ci", "");
        if(b==true) {
        hilo_moto.execute(getString(R.string.servidor) + "frmTaxi.php?opcion=set_estado", "1",id,"1");// parametro que recibe
        }
        else
        {
        hilo_moto.execute(getString(R.string.servidor) + "frmTaxi.php?opcion=set_estado", "1",id,"0");// parametro que recibe
        }
        }
        else if(v.getId()== R.id.perfil)
        {
final CharSequence[] options={"Tomar foto","Elegir de galeria","Cancelar"};
final AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Elige una opcion");
        builder.setItems(options, new DialogInterface.OnClickListener(){

@Override
public void onClick(DialogInterface dialogInterface, int i) {
        if(options[i]=="Tomar foto")
        {
        openCamara();
        }else if(options[i]=="Elegir de galeria")
        {
        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent,"Seleccione app de imagen"),SELECT_PICTURE);

        }else if (options[i]=="Cancelar")
        {
        dialogInterface.dismiss();
        }
        }
        });
        builder.show();

        }else if(v.getId()==R.id.ll_placa){
             Intent empresa=new Intent(this,Datos_vehiculo.class);
             SharedPreferences perfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);
             empresa.putExtra("placa",perfil.getString("placa",""));
             startActivity(empresa);
         }
        }

public void cargar_datos()
        {
        SharedPreferences perfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);
        nombre.setText(perfil.getString("nombre",""));
        apellido.setText(perfil.getString("paterno","")+" "+perfil.getString("materno",""));
        celular.setText(perfil.getString("celular",""));
        email.setText(perfil.getString("email",""));
        direccion.setText(perfil.getString("direccion",""));
        placa.setText(perfil.getString("placa",""));
            credito.setText(perfil.getString("credito",""));
            rb_conductor.setRating(Integer.parseInt(perfil.getString("cal_conductor","0")));
            rb_vehiculo.setRating(Integer.parseInt(perfil.getString("cal_vehiculo","0")));
        if(perfil.getString("estado","0").equals("0"))
        {
        estado.setChecked(false);
        }
        else
        {
        estado.setChecked(true);
        }

        }

// comenzar el servicio con el motista....
public class Servicio extends AsyncTask<String,Integer,String> {


    @Override
    protected String doInBackground(String... params) {

        String cadena = params[0];
        URL url = null;  // url donde queremos obtener informacion
        String devuelve = "";


//Insertar nueva imagen de perfil en servico por parte del taxi..

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
                jsonParam.put("imagen", params[3]);
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return devuelve;
    }


    @Override
    protected void onPreExecute() {
        //para el progres Dialog
        pDialog = new ProgressDialog(Perfil_Conductor.this);
        pDialog.setTitle(getString(R.string.app_name));
        pDialog.setMessage("Insertando imagen de perfil. . .");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        pDialog.dismiss();//ocultamos proggress dialog
        // Log.e("onPostExcute=", "" + s);
        if(s.equals("3")||s.equals("1"))
        {
            mensaje(suceso.getMensaje());
        }
        else if(s.equals("2"))
        {
            mensaje(suceso.getMensaje());
            finish();
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


// comenzar el servicio con el motista....
public class Servicio_moto extends AsyncTask<String,Integer,String> {
    String s_placa="0";

    @Override
    protected String doInBackground(String... params) {

        String cadena = params[0];
        URL url = null;  // url donde queremos obtener informacion
        String devuelve = "";

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
                jsonParam.put("ci", params[2]);
                jsonParam.put("estado", params[3]);

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

                    suceso =new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));

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
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


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
                        editor.putString("placa",respuestaJSON.getString("placa"));
                        editor.commit();

                        s_placa=respuestaJSON.getString("placa");

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
        pDialog = new ProgressDialog(Perfil_Conductor.this);
        pDialog.setTitle(getString(R.string.app_name));
        pDialog.setMessage("Autenticando ....");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        pDialog.dismiss();//ocultamos proggress dialog
        // Log.e("respuesta del servidor=", "" + s);
        if(s.equals("1"))
        {


            Toast.makeText(getApplicationContext(),suceso.getMensaje(),Toast.LENGTH_SHORT).show();
        }else if(s.equals("2"))
        {
            estado.setChecked(!estado.isChecked());
            Toast.makeText(getApplicationContext(),suceso.getMensaje(),Toast.LENGTH_SHORT).show();
        }else if(s.equals("3"))
        {
            SharedPreferences perfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);
            credito.setText(perfil.getString("credito",""));
            if(s_placa.equals("0")==false){
                placa.setText(s_placa);
            }else{
                placa.setText("SIN MOVIl");
            }

            //  Toast.makeText(getApplicationContext(),suceso.getMensaje(),Toast.LENGTH_SHORT).show();
        }else if(s.equals("5"))
        {
            Toast.makeText(getApplicationContext(),suceso.getMensaje(),Toast.LENGTH_SHORT).show();
        }
        else
        {   estado.setChecked(!estado.isChecked());

            mensaje_error("No pudimos conectarnos al servidor.\nVuelve a intentarlo.");
        }
        SharedPreferences prefe=getSharedPreferences("perfil_conductor", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=prefe.edit();
        if(estado.isChecked())
        {
            editor.putString("estado","1");
        }
        else
        {
            editor.putString("estado","0");
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
    public void imagen_en_vista(ImageView imagen)
    { Drawable dw;
        SharedPreferences perfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);
        String mPath = Environment.getExternalStorageDirectory() + File.separator +getString(R.string.nombre_carpeta)+ "/Imagen"
                + File.separator +perfil.getString("ci","")+ "_perfil.jpg";


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

        imagen.setImageDrawable(roundedDrawable);
    }




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

    private void guardar_en_memoria(Bitmap bitmapImage)
    {
        File file=null;
        FileOutputStream fos = null;
        try {
            SharedPreferences perfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);
            String APP_DIRECTORY = getString(R.string.nombre_carpeta)+"/";//nombre de directorio
            String MEDIA_DIRECTORY = APP_DIRECTORY + "Imagen";//nombre de la carpeta
            file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
            File mypath=new File(file,perfil.getString("ci","")+"_perfil.jpg");//nombre del archivo imagen

            boolean isDirectoryCreated = file.exists();//pregunto si esxiste el directorio creado
            if(!isDirectoryCreated)
                isDirectoryCreated = file.mkdirs();

            if(isDirectoryCreated) {
                fos = new FileOutputStream(mypath);
                // Use the compress method on the BitMap object to write image to the OutputStream
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mensaje_error(String mensaje)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Importante");
        builder.setMessage(mensaje);
        builder.setPositiveButton("OK", null);
        builder.create();
        builder.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            //ubicacion de la imagen
            SharedPreferences sperfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);
            Bitmap img_cargar;
            String uploadImage;

            switch (requestCode){
                case PHOTO_CODE:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> Uri = " + uri);
                                }
                            });

                    //Convertir MPath a Bitmap
                    File newFile = new File(mPath);
                    path = Uri.fromFile(newFile);
                    CropImage(path);

                    break;
                case SELECT_PICTURE:
                    path = data.getData();
                    try {//convertir Uri a BitMap
                        CropImage(path);
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(this,"Error al seleccionar la imagen. ->"+e.toString(),Toast.LENGTH_LONG).show();
                    }
                    break;

                case 1:
                    //imagen recortada.
                    try {
                        Bundle bundle;
                        Bitmap tempBitmap=null;

                        try{

                           bundle=data.getExtras();
                           tempBitmap=bundle.getParcelable("data");


                        }catch (Exception eee){
                            tempBitmap=null;
                        }
                        //get the cropped bitmap
                        if(tempBitmap==null) {
                            Uri imageUri = data.getData();
                            tempBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        }
                        //imagen cuadrado
                        tempBitmap=imagen_cuadrado(tempBitmap);
                        guardar_en_memoria(tempBitmap);
                        //subir imagen de perfil al servidor...
                        img_cargar=ReducirImagen_b(tempBitmap,1000,1000);
                        Servicio hilo_ = new Servicio();
                        uploadImage = getStringImage(img_cargar);
                        hilo_.execute(getString(R.string.servidor) + "frmUsuario.php?opcion=insertar_imagen_taxi", "2",sperfil.getString("ci",""),uploadImage);// parametro que recibe el doinbackground
                        //Convert bitmap to drawable
                        Drawable dw1 = new BitmapDrawable(getResources(), tempBitmap);
                        //Conveertir Imagen a Circulo
                        imagen_circulo(dw1,perfil);
                        //guardar en SharePrefences
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(this,"Error al subir la imagen. ->"+e.toString(),Toast.LENGTH_LONG).show();
                    }
                    break;

            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("file_path", mPath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mPath = savedInstanceState.getString("file_path");
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100){
            if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permisos aceptados", Toast.LENGTH_SHORT).show();
                perfil.setEnabled(true);
            }
        }else{
            showExplanation();
        }
    }

    private void openCamara() {
        File file = new File(Environment.getExternalStorageDirectory(), getString(R.string.nombre_carpeta)+"/Imagen");
        boolean isDirectoryCreated = file.exists();

        if(!isDirectoryCreated)
            isDirectoryCreated = file.mkdirs();

        if(isDirectoryCreated){
            Long timestamp = System.currentTimeMillis() / 1000;
            String imageName = timestamp.toString() + ".jpg";

            SharedPreferences perfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);

            mPath = Environment.getExternalStorageDirectory() + File.separator + getString(R.string.nombre_carpeta)+"/Imagen"
                    + File.separator + perfil.getString("ci","")+"_perfil.jpg";

            File newFile = new File(mPath);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
            startActivityForResult(intent, PHOTO_CODE);
        }


    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Permisos denegados");
        builder.setMessage("Para usar las funciones de la app necesitas aceptar los permisos");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.show();
    }

    public Bitmap imagen_cuadrado(Bitmap originalBitmap)
    {
        if (originalBitmap.getWidth() > originalBitmap.getHeight()){
            originalBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getHeight(), originalBitmap.getHeight());
        }else if (originalBitmap.getWidth() < originalBitmap.getHeight()) {
            originalBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getWidth());
        }
        return originalBitmap;
    }


    public static Bitmap ReducirImagen_b( Bitmap BitmapOrg, int w, int h) {

        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;
        // calculamos el escalado de la imagen destino
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // para poder manipular la imagen
        // debemos crear una matriz
        Matrix matrix = new Matrix();
        // Cambiar el tamaño del mapa de bits
        matrix.postScale(scaleWidth, scaleHeight);
        // volvemos a crear la imagen con los nuevos valores
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width, height, matrix, true);

        return resizedBitmap;
    }

    /*Ahora que se tiene la imagen cargado en mapa de bits.
Vamos a convertir este mapa de bits a cadena de base64
este método es para convertir este mapa de bits a la cadena de base64*/
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void mensaje(String mensaje)
    {
        Toast toast =Toast.makeText(this,mensaje,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    private void CropImage(Uri uri) {

        try{
            CropIntent = new Intent("com.android.camera.action.CROP");
            CropIntent.setDataAndType(uri,"image/*");

            CropIntent.putExtra("crop","true");
            CropIntent.putExtra("outputX",5000);
            CropIntent.putExtra("outputY",5000);
            CropIntent.putExtra("aspectX",1);
            CropIntent.putExtra("aspectY",1);
            CropIntent.putExtra("scaleUpIfNeeded",true);
            CropIntent.putExtra("return-data",true);

            startActivityForResult(CropIntent,1);
        }
        catch (ActivityNotFoundException ex)
        {
            Toast.makeText(this,"error al recortar :"+ex.toString(),Toast.LENGTH_LONG).show();
        }

    }




    //PERMISOS....

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
                    ActivityCompat.requestPermissions(Perfil_Conductor.this,
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
            ActivityCompat.requestPermissions(Perfil_Conductor.this,
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
                    ActivityCompat.requestPermissions(Perfil_Conductor.this,
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
            ActivityCompat.requestPermissions(Perfil_Conductor.this,
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
                    ActivityCompat.requestPermissions(Perfil_Conductor.this,
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
            ActivityCompat.requestPermissions(Perfil_Conductor.this,
                    PERMISSIONS,
                    1);
        }
    }

    public void verificar_permiso_imei()
    {
        final String[] PERMISSIONS = { Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE };

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
            //YA LO CANCELE Y VOUELVO A PERDIR EL PERMISO.

            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Atención!");
            dialogo1.setMessage("Debes otorgar permisos de acceso al ID del Telefono por tema de Seguridad.");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Solicitar permiso", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    dialogo1.cancel();
                    ActivityCompat.requestPermissions(Perfil_Conductor.this,
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
            ActivityCompat.requestPermissions(Perfil_Conductor.this,
                    PERMISSIONS,
                    1);
        }
    }






}
