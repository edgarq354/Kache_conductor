package com.elisoft.kache_conductor.historial;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.elisoft.valle_grande_conductor.R;
import com.elisoft.kache_conductor.SqLite.AdminSQLiteOpenHelper;
import com.elisoft.kache_conductor.Suceso;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class Historial extends AppCompatActivity {

    ArrayList<CPedido_taxi> historial_enero = new ArrayList<CPedido_taxi>();
    ArrayList<CPedido_taxi> historial_febrero = new ArrayList<CPedido_taxi>();
    ArrayList<CPedido_taxi> historial_marzo = new ArrayList<CPedido_taxi>();
    ArrayList<CPedido_taxi> historial_abril = new ArrayList<CPedido_taxi>();
    ArrayList<CPedido_taxi> historial_mayo = new ArrayList<CPedido_taxi>();
    ArrayList<CPedido_taxi> historial_junio = new ArrayList<CPedido_taxi>();
    ArrayList<CPedido_taxi> historial_julio = new ArrayList<CPedido_taxi>();
    ArrayList<CPedido_taxi> historial_agosto = new ArrayList<CPedido_taxi>();
    ArrayList<CPedido_taxi> historial_septiembre = new ArrayList<CPedido_taxi>();
    ArrayList<CPedido_taxi> historial_octibre = new ArrayList<CPedido_taxi>();
    ArrayList<CPedido_taxi> historial_noviembre = new ArrayList<CPedido_taxi>();
    ArrayList<CPedido_taxi> historial_diciembre = new ArrayList<CPedido_taxi>();
    Suceso suceso;
    private ProgressDialog pDialog;
    LinearLayout pb_cargando;

    Enero enero=new Enero();
    Febrero febrero=new Febrero();
    Marzo marzo=new Marzo();
    Abril abril=new Abril();
    Mayo mayo=new Mayo();
    Junio junio=new Junio();
    Julio julio=new Julio();
    Agosto agosto=new Agosto();
    Septiembre septiembre=new Septiembre();
    Octubre octubre=new Octubre();
    Noviembre noviembre=new Noviembre();
    Diciembre diciembre=new Diciembre();

    int cantidad_de_servicios=0;



    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(mViewPager);

        Calendar calendarNow = Calendar.getInstance();
        int mes = calendarNow.get(Calendar.MONTH);
        int anio=calendarNow.get(Calendar.YEAR);
        tabLayout.getTabAt(mes).select();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pb_cargando=(LinearLayout) findViewById(R.id.pb_cargando);
        pb_cargando.setVisibility(View.INVISIBLE);

        if(mes>2){
            cargar_historial_en_la_lista_por_mes(mes);
            cargar_historial_en_la_lista_por_mes(mes-1);
        }
        cargar_historial_en_la_lista_por_mes(mes+1);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_historial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Calendar calendarNow = Calendar.getInstance();
            int anio=calendarNow.get(Calendar.YEAR);
            descargar_por_mes(position+1,anio);


            switch (position)
            {
                case 0:
                    enero.cargar_lista(historial_enero);
                    return enero;
                case 1:
                    febrero.cargar_lista(historial_febrero);
                    return febrero;
                case 2:
                    marzo.cargar_lista(historial_marzo);
                    return marzo;
                case 3:
                    abril.cargar_lista(historial_abril);
                    return abril;
                case 4:
                    mayo.cargar_lista(historial_mayo);
                    return mayo;
                case 5:
                    junio.cargar_lista(historial_junio);
                    return junio;
                case 6:
                    julio.cargar_lista(historial_julio);
                    return  julio;
                case 7:
                    agosto.cargar_lista(historial_agosto);
                    return agosto;
                case 8:
                    septiembre.cargar_lista(historial_septiembre);
                    return  septiembre;
                case 9:
                    octubre.cargar_lista(historial_octibre);
                    return  octubre;
                case 10:
                    noviembre.cargar_lista(historial_noviembre);
                    return noviembre;
                case 11:
                    diciembre.cargar_lista(historial_diciembre);
                    return  diciembre;
                default:
                    return  null;

            }
        }


        @Override
        public int getCount() {
            // Show 3 total pages.
            return 12;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Enero";
                case 1:
                    return "Febrero";
                case 2:
                    return "Marzo";
                case 3:
                    return "Abril";
                case 4:
                    return "Mayo";
                case 5:
                    return "Junio";
                case 6:
                    return "Julio";
                case 7:
                    return "Agosto";
                case 8:
                    return "Septiembre";
                case 9:
                    return "Octubre";
                case 10:
                    return "Noviembre";
                case 11:
                    return "Diciembre";
            }
            return null;
        }
    }

    public void actualizar()
    {
        SharedPreferences prefe=getSharedPreferences("perfil_conductor", Context.MODE_PRIVATE);
        String id=prefe.getString("ci","");
       Servicio hilo_pedido = new Servicio();
        String ip=getString(R.string.servidor);
        hilo_pedido.execute(ip+"frmPedido.php?opcion=lista_pedido_por_ci", "1",id);// parametro que recibe el doinbackground
        Log.i("Item", "actualizar!");
    }
    public void descargar_por_mes(int mes,int anio)
    {
        SharedPreferences prefe=getSharedPreferences("perfil_conductor", Context.MODE_PRIVATE);
        String id=prefe.getString("ci","");
        Servicio hilo_pedido = new Servicio();
        String ip=getString(R.string.servidor);
        hilo_pedido.execute(ip+"frmPedido.php?opcion=lista_pedido_por_ci_mes", "2",id,String.valueOf(mes),String.valueOf(anio));// parametro que recibe el doinbackground
        Log.i("Item", "actualizar!");
    }

    public class Servicio extends AsyncTask<String,Integer,String> {
        int mes=0;
        int anio=0;

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
                        // vacia los datos que estan registrados en nuestra base de datos SQLite..
                        vaciar_historial();
                        if (suceso.getSuceso().equals("1")) {
                            JSONArray usu=respuestaJSON.getJSONArray("historial");
                            for (int i=0;i<usu.length();i++)
                            {
                                int id_pedido=Integer.parseInt(usu.getJSONObject(i).getString("id"));
                                int id_usuario=Integer.parseInt(usu.getJSONObject(i).getString("id_usuario"));
                                int estado_pedido=Integer.parseInt(usu.getJSONObject(i).getString("estado"));
                                String indicacion=usu.getJSONObject(i).getString("direccion");
                                String fecha_pedido=usu.getJSONObject(i).getString("fecha_pedido");
                                double latitud=Double.parseDouble(usu.getJSONObject(i).getString("longitud"));
                                double longitud=Double.parseDouble(usu.getJSONObject(i).getString("longitud"));
                                String nombre=usu.getJSONObject(i).getString("nombre");
                                String apellido=usu.getJSONObject(i).getString("apellido");
                                String celular=usu.getJSONObject(i).getString("celular");
                                String descripcion=usu.getJSONObject(i).getString("detalle");
                                String monto_total=usu.getJSONObject(i).getString("monto_total");
                                int clase_vehiculo=Integer.parseInt(usu.getJSONObject(i).getString("clase_vehiculo"));
                                int calificacion_vehiculo=Integer.parseInt(usu.getJSONObject(i).getString("calificacion_vehiculo"));
                                int calificacion_conductor=Integer.parseInt(usu.getJSONObject(i).getString("calificacion_conductor"));


                                cargar_lista_en_historial( id_pedido,
                                        id_usuario,
                                        estado_pedido,
                                        fecha_pedido,
                                        nombre,
                                        apellido,
                                        celular,
                                        indicacion,
                                        descripcion,
                                        latitud,
                                        longitud,
                                        monto_total,
                                        clase_vehiculo,
                                        calificacion_conductor,
                                        calificacion_vehiculo);
                            }

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
            }else
            if (params[1] == "2") {
                try {
                    cantidad_de_servicios+=1;

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
                    jsonParam.put("mes", params[3]);
                    jsonParam.put("anio", params[4]);

                    mes=Integer.parseInt(params[3]);
                    anio=Integer.parseInt(params[4]);

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
                        mes=Integer.parseInt(respuestaJSON.getString("mes"));
                        anio=Integer.parseInt(respuestaJSON.getString("anio"));
                        // vacia los datos que estan registrados en nuestra base de datos SQLite..

                        vaciar_historial_por_fecha(mes,anio);
                        if (suceso.getSuceso().equals("1")) {
                            JSONArray usu=respuestaJSON.getJSONArray("historial");
                            for (int i=0;i<usu.length();i++)
                            {
                                int id_pedido=Integer.parseInt(usu.getJSONObject(i).getString("id"));
                                int id_usuario=Integer.parseInt(usu.getJSONObject(i).getString("id_usuario"));
                                int estado_pedido=Integer.parseInt(usu.getJSONObject(i).getString("estado"));
                                String indicacion=usu.getJSONObject(i).getString("direccion");
                                String fecha_pedido=usu.getJSONObject(i).getString("fecha_pedido");
                                double latitud=Double.parseDouble(usu.getJSONObject(i).getString("longitud"));
                                double longitud=Double.parseDouble(usu.getJSONObject(i).getString("longitud"));
                                String nombre=usu.getJSONObject(i).getString("nombre");
                                String apellido=usu.getJSONObject(i).getString("apellido");
                                String celular=usu.getJSONObject(i).getString("celular");
                                String descripcion=usu.getJSONObject(i).getString("detalle");
                                String monto_total=usu.getJSONObject(i).getString("monto_total");
                                int clase_vehiculo=Integer.parseInt(usu.getJSONObject(i).getString("clase_vehiculo"));
                                int calificacion_vehiculo=Integer.parseInt(usu.getJSONObject(i).getString("calificacion_vehiculo"));
                                int calificacion_conductor=Integer.parseInt(usu.getJSONObject(i).getString("calificacion_conductor"));


                                cargar_lista_en_historial( id_pedido,
                                        id_usuario,
                                        estado_pedido,
                                        fecha_pedido,
                                        nombre,
                                        apellido,
                                        celular,
                                        indicacion,
                                        descripcion,
                                        latitud,
                                        longitud,
                                        monto_total,
                                        clase_vehiculo,
                                        calificacion_conductor,
                                        calificacion_vehiculo);
                            }

                            devuelve="3";
                        } else  {
                            devuelve = "4";
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
            try {/*
                pDialog = new ProgressDialog(Historial.this);
                pDialog.setMessage("Descargando la lista de pedidos . . .");
                pDialog.setIndeterminate(true);
                pDialog.setCancelable(true);
                pDialog.show();
                */
                pb_cargando.setVisibility(View.VISIBLE);
            }catch (Exception e)
            {
                mensaje_error("Por favor actualice la aplicación.");
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
          //  pDialog.dismiss();//ocultamos proggress dialog

            Log.e("onPostExcute=", "" + s);

            if (s.equals("1")) {
                cargar_historial_en_la_lista();
            }
            else if(s.equals("2"))
            {
                cargar_historial_en_la_lista();
            }
            else if(s.equals("3"))
            {
                cantidad_de_servicios-=1;
                cargar_historial_en_la_lista_por_mes(mes);
            }
            else if(s.equals("4")){
                cargar_historial_en_la_lista_por_mes(mes);
                cantidad_de_servicios-=1;
            }
            else
            {
                cantidad_de_servicios-=1;
                mensaje_error("Error al conectar con el servidor.");
            }
            if(cantidad_de_servicios==0){
                pb_cargando.setVisibility(View.INVISIBLE);
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

    private void vaciar_historial()
    {
        try {
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, getString(R.string.nombre_sql), null, Integer.parseInt(getString(R.string.version_sql)));

            SQLiteDatabase db = admin.getWritableDatabase();
            db.execSQL("delete from pedido_taxi");
            db.close();
            Log.e("sqlite ", "vaciar historial pedido taxi");
        }catch (Exception e)
        {
            mensaje_error("Por favor actualice la aplicación.");
        }
    }
    private void vaciar_historial_por_fecha(int mes,int anio)
    {String s_mes="0";
        if(mes<10)
        {
            s_mes=s_mes+mes;
        }else{
            s_mes=""+anio;
        }

        try {
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, getString(R.string.nombre_sql), null, Integer.parseInt(getString(R.string.version_sql)));

            SQLiteDatabase db = admin.getWritableDatabase();
            db.execSQL("delete from pedido_taxi where strftime('%m', fecha_pedido)='"+s_mes+"'");
            db.close();
            Log.e("sqlite ", "vaciar historial pedido taxi");
        }catch (Exception e)
        {
            mensaje_error("Por favor actualice la aplicación.");
        }
    }

    public void mensaje_error(String mensaje)
    {
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle("Importante");
        dialogo1.setMessage(mensaje);
        dialogo1.setCancelable(false);
        dialogo1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {

            }
        });
        dialogo1.show();
    }
    private void cargar_historial_en_la_lista( )
    {
        cargar_historial_mes(historial_enero,"01");
        cargar_historial_mes(historial_febrero,"02");
        cargar_historial_mes(historial_marzo,"03");
        cargar_historial_mes(historial_abril,"04");
        cargar_historial_mes(historial_mayo,"05");
        cargar_historial_mes(historial_junio,"06");
        cargar_historial_mes(historial_julio,"07");
        cargar_historial_mes(historial_agosto,"08");
        cargar_historial_mes(historial_septiembre,"09");
        cargar_historial_mes(historial_octibre,"10");
        cargar_historial_mes(historial_noviembre,"11");
        cargar_historial_mes(historial_diciembre,"12");

        if(cantidad_de_servicios==0){
            pb_cargando.setVisibility(View.INVISIBLE);
        }
       //   pDialog.dismiss();
    }
    private void cargar_historial_en_la_lista_por_mes(int mes)
    {
        switch (mes)
        {
            case 1:
                cargar_historial_mes(historial_enero,"01");
                break;
            case 2:
                cargar_historial_mes(historial_febrero,"02");
                break;
            case 3:
                cargar_historial_mes(historial_marzo,"03");
                break;
            case 4:
                cargar_historial_mes(historial_abril,"04");
                break;
            case 5:
                cargar_historial_mes(historial_mayo,"05");
                break;
            case 6:
                cargar_historial_mes(historial_junio,"06");
                break;
            case 7:
                cargar_historial_mes(historial_julio,"07");
                break;
            case 8:
                cargar_historial_mes(historial_agosto,"08");
                break;
            case 9:
                cargar_historial_mes(historial_septiembre,"09");
                break;
            case 10:
                cargar_historial_mes(historial_octibre,"10");
                break;
            case 11:
                cargar_historial_mes(historial_noviembre,"11");
                break;
            case 12:
                cargar_historial_mes(historial_diciembre,"12");
                break;

        }
        try {
            if(cantidad_de_servicios==0) {
                pb_cargando.setVisibility(View.INVISIBLE);
            }
           // pDialog.dismiss();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void cargar_historial_mes(ArrayList<CPedido_taxi> lista,String mes )
    {
        lista.clear();

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, getString(R.string.nombre_sql), null, Integer.parseInt(getString(R.string.version_sql)));

        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor fila = bd.rawQuery("select * from pedido_taxi where strftime('%m', fecha_pedido) ='"+mes+"'", null);

        if (fila.moveToFirst()) {  //si ha devuelto 1 fila, vamos al primero (que es el unico)

            do {

                int id_pedido=Integer.parseInt(fila.getString(0));
                int id_taxi=Integer.parseInt(fila.getString(1));
                String fecha_pedido=fila.getString(2);
                double latitud=Double.parseDouble(fila.getString(3));
                double longitud=Double.parseDouble(fila.getString(4));
                String nombre=fila.getString(5);
                String apellido=fila.getString(6);
                String celular=fila.getString(7);
                String indicacion=fila.getString(8);
                String descripcion=fila.getString(9);
                int estado_pedido=Integer.parseInt(fila.getString(10));
                String monto_total=fila.getString(11);
                int clase_vehiculo= Integer.parseInt(fila.getString(12));
                int calificacion_conductor= Integer.parseInt(fila.getString(13));
                int calificacion_vehiculo= Integer.parseInt(fila.getString(14));

                CPedido_taxi hi =new CPedido_taxi(String.valueOf(id_pedido),
                        id_taxi,
                        estado_pedido,
                        fecha_pedido,
                        nombre,
                        apellido,
                        celular,
                        indicacion,
                        descripcion,
                        latitud,
                        longitud,
                        monto_total,
                        clase_vehiculo,
                        calificacion_conductor,
                        calificacion_vehiculo);
                lista.add(hi);
            } while(fila.moveToNext());

        } else
        {
            //NO HAY REGISTRO.
        }

        bd.close();
    }

    private void cargar_lista_en_historial( int id,
                                            int id_usuario,
                                            int estado_pedido,
                                            String fecha_pedido,
                                            String nombre,
                                            String apellido,
                                            String celular,
                                            String indicacion,
                                            String descripcion,
                                            double latitud,
                                            double longitud,
                                            String monto_total,
                                            int clase_vehiculo,
                                            int calificacion_conductor,
                                            int calificacion_vehiculo)
    {
try {
    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, getString(R.string.nombre_sql), null, Integer.parseInt(getString(R.string.version_sql)));

    SQLiteDatabase bd = admin.getWritableDatabase();
    ContentValues registro = new ContentValues();
    registro.put("id", String.valueOf(id));
    registro.put("id_usuario", String.valueOf(id_usuario));
    registro.put("estado_pedido", String.valueOf(estado_pedido));
    registro.put("fecha_pedido", fecha_pedido);
    registro.put("latitud", String.valueOf(latitud));
    registro.put("longitud", String.valueOf(longitud));
    registro.put("nombre", nombre);
    registro.put("apellido", apellido);
    registro.put("celular", celular);
    registro.put("indicacion", indicacion);
    registro.put("descripcion", descripcion);
    registro.put("monto_total", monto_total);
    registro.put("clase_vehiculo",String.valueOf(clase_vehiculo));
    registro.put("calificacion_conductor",String.valueOf(calificacion_conductor));
    registro.put("calificacion_vehiculo",String.valueOf(calificacion_vehiculo));
    bd.insert("pedido_taxi", null, registro);
    bd.close();
}catch (Exception e)
{
    e.printStackTrace();
}
    }

}
